package org.tomlang.livechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppDesign;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppDesignJson;
import org.tomlang.livechat.repositories.AppDesignRepository;
import org.tomlang.livechat.repositories.AppRepository;

@Service
public class AppDesignService {

    @Autowired
    AppDesignRepository appDesignRepository;

    @Autowired
    AppRepository appRepository;

    @Value("${app.max.wallpaper.count}")
    Integer maxWallpaper;

    public AppDesignJson createDesign(String appHash, AppDesignJson json) throws LiveChatException {
        App app = appRepository.findByAppHashToken(appHash);
        AppDesign design = new AppDesign();
        design.setAppId(app.getId());
        design.setCustomCss(json.getCustomCss());
        design.setThemeColor(json.getThemeColor());
        if (json.getWallpaper() > maxWallpaper) {
            throw new LiveChatException("Exceeds max walpaper count", HttpStatus.BAD_REQUEST);
        }
        design.setWallpaper(json.getWallpaper());
        appDesignRepository.save(design);
        return json;

    }

    public AppDesignJson updateDesign(String appHash, AppDesignJson json) throws LiveChatException {
        App app = appRepository.findByAppHashToken(appHash);
        AppDesign design = appDesignRepository.findByAppId(app.getId());
        if (null != design) {
            design.setAppId(app.getId());
            design.setCustomCss(json.getCustomCss());
            design.setThemeColor(json.getThemeColor());
            if (json.getWallpaper() > maxWallpaper) {
                throw new LiveChatException("Exceeds max walpaper count", HttpStatus.BAD_REQUEST);
            }
            design.setWallpaper(json.getWallpaper());
            appDesignRepository.save(design);
            return json;
        } else {
            throw new LiveChatException("No design data found to update", HttpStatus.BAD_REQUEST);
        }
    }

    public AppDesignJson getDesign(String appHashCode) throws LiveChatException {
        App app = appRepository.findByAppHashToken(appHashCode);
        AppDesign design = appDesignRepository.findByAppId(app.getId());

        if (null != design) {
            AppDesignJson json = new AppDesignJson();
            json.setCustomCss(design.getCustomCss());
            json.setThemeColor(design.getThemeColor());
            json.setWallpaper(design.getWallpaper());
            return json;
        } else {
            throw new LiveChatException("No design data found to update", HttpStatus.BAD_REQUEST);
        }

    }

}
