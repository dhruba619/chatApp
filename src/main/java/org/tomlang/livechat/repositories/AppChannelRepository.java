package org.tomlang.livechat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tomlang.livechat.entities.AppChannel;

public interface AppChannelRepository extends CrudRepository<AppChannel, Integer>{
    @Query(value = "select * from app_channel where app_id = ?1 and is_default_channel=true", nativeQuery = true)
    public AppChannel findDefaultChannel(Integer appId);
    
    @Query(value = "select * from app_channel where app_id = ?1 ORDER BY id DESC", nativeQuery = true)
    public List<AppChannel> findAllForApp(Integer appId);
}
