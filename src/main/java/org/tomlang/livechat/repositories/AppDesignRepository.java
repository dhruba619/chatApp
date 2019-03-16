package org.tomlang.livechat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tomlang.livechat.entities.AppDesign;

public interface AppDesignRepository extends CrudRepository<AppDesign, Integer> {

    @Query(value = "select * from app_design where app_id = ?1", nativeQuery = true)
    AppDesign findByAppId(Integer id);

}
