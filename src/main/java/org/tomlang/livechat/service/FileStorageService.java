package org.tomlang.livechat.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tomlang.livechat.exceptions.LiveChatException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    
    public FileStorageService() throws LiveChatException {
        this.fileStorageLocation = Paths.get("./resources/avatars/128x128")
            .toAbsolutePath()
            .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new LiveChatException("Could not create the directory where the uploaded files will be stored.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String storeFile(MultipartFile file) throws LiveChatException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new LiveChatException("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }

            if (!(fileName.toLowerCase()
                .endsWith("jpg")
                || fileName.toLowerCase()
                    .endsWith("png"))) {
                throw new LiveChatException("Sorry! Invalid File name (Must be .jpg or .png) " + fileName, HttpStatus.BAD_REQUEST);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            String fileNameHashed = createImageHash()+"."+fileName.substring(fileName.length()-3, fileName.length());
            Path targetLocation = this.fileStorageLocation.resolve(fileNameHashed);
            //Check image size
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image.getHeight()>128 || image.getWidth()>128) {
                throw new LiveChatException("Invalid image size: "+image.getHeight() +" X "+image.getWidth(), HttpStatus.BAD_REQUEST);
            }
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileNameHashed;
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new LiveChatException("Could not store file " + fileName + ". Please try again!-", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource loadFileAsResource(String fileName) throws LiveChatException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName)
                .normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new LiveChatException("File not found " + fileName, HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage());
            throw new LiveChatException("File not found " + fileName, HttpStatus.NOT_FOUND);
        }

    }

    private String createImageHash() throws LiveChatException {
        try {
            String input = UUID.randomUUID()
                .toString();
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            hashtext = hashtext.substring(0, 8);
            return hashtext;
        } catch (Exception e) {
            throw new LiveChatException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
