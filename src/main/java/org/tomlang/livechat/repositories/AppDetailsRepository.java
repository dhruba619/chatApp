package org.tomlang.livechat.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.AppDetails;

@Repository
public interface AppDetailsRepository extends CrudRepository<AppDetails, Integer>{

}
