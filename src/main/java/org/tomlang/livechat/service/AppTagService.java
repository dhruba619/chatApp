package org.tomlang.livechat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tomlang.livechat.entities.App;
import org.tomlang.livechat.entities.AppTag;
import org.tomlang.livechat.exceptions.LiveChatException;
import org.tomlang.livechat.json.AppTagJson;
import org.tomlang.livechat.repositories.AppRepository;
import org.tomlang.livechat.repositories.AppTagRepository;

@Service
public class AppTagService {

    @Autowired
    AppTagRepository appTagRepository;

    @Autowired
    AppRepository appRepository;

    @Autowired
    UserService userService;

    public AppTagJson createTag(String authToken, String appHash, AppTagJson request) {

        App app = appRepository.findByAppHashToken(appHash);
        AppTag tag = new AppTag();
        tag.setAppId(app.getId());
        tag.setColor(request.getColor());
        tag.setName(request.getName());
        tag.setTarget(request.getTarget());

        AppTag entity = appTagRepository.save(tag);
        request.setId(entity.getId());
        return request;

    }

    public AppTagJson updateTag(String authToken, String appHash, AppTagJson request) throws LiveChatException {
        App app = appRepository.findByAppHashToken(appHash);
        Optional<AppTag> tagOptional = appTagRepository.findById(request.getId());
        if (tagOptional.isPresent()) {
            AppTag tag = tagOptional.get();
            tag.setAppId(app.getId());
            tag.setColor(request.getColor());
            tag.setName(request.getName());
            tag.setTarget(request.getTarget());
            appTagRepository.save(tag);
            return request;
        } else {
            throw new LiveChatException("No Tag found to update", HttpStatus.BAD_REQUEST);
        }

    }

    public AppTagJson deleteTag(String authToken, String appHash, AppTagJson request) throws LiveChatException {
        Optional<AppTag> tagOptional = appTagRepository.findById(request.getId());
        if (tagOptional.isPresent()) {
            AppTag tag = tagOptional.get();
            appTagRepository.delete(tag);
            return request;
        } else {
            throw new LiveChatException("No Tag found to update", HttpStatus.BAD_REQUEST);
        }
    }

    public List<AppTagJson> getTags(String authToken, String appHash) {
        App app = appRepository.findByAppHashToken(appHash);
        List<AppTag> tags = appTagRepository.findByAppId(app.getId());
        List<AppTagJson> appTagJsons = new ArrayList<>();
        for (AppTag tag : tags) {
            AppTagJson json = new AppTagJson();
            json.setColor(tag.getColor());
            json.setName(tag.getName());
            json.setTarget(tag.getTarget());
            json.setId(tag.getId());

            appTagJsons.add(json);
        }
        return appTagJsons;
    }

}
