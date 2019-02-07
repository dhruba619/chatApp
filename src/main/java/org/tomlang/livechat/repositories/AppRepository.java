package org.tomlang.livechat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.App;

@Repository
public interface AppRepository extends CrudRepository<App, Integer>{
    

    @Query(value = "select * from app where app_hashcode = ?1", nativeQuery = true)
    public App findByAppHashToken(String token);

}
