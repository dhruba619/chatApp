package org.tomlang.livechat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.UserAppDetails;

@Repository
public interface UserAppDetailsRepository extends CrudRepository<UserAppDetails, Integer>{
    
    @Query(value = "select * from user_app_details where user_id = ?1 and app_details_id=?2", nativeQuery = true)
    public UserAppDetails getByUserIdAndAppDetail(Integer userId, Integer appdetailsId);
    

}
