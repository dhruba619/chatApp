package org.tomlang.livechat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tomlang.livechat.entities.AppTag;

public interface AppTagRepository extends CrudRepository<AppTag, Integer> {

    @Query(value = "select * from app_tag where app_id = ?1", nativeQuery = true)
    List<AppTag> findByAppId(Integer id);

}
