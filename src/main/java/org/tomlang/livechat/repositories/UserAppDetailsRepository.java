package org.tomlang.livechat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.UserAppDetails;

@Repository
public interface UserAppDetailsRepository extends CrudRepository<UserAppDetails, Integer>{
    
    @Query(value = "select * from user_app_details where user_id = ?1 and app_details_id=?2", nativeQuery = true)
    public UserAppDetails getByUserIdAndAppDetail(Integer userId, Integer appdetailsId);
    
    @Query(value = "select * from user_app_details where user_id = ?1", nativeQuery = true)
    public List<UserAppDetails> getByUserIdAndAppDetail(Integer userId);
    
    @Query(value = "select * from user_app_details where app_details_id=?1 order by user_id", nativeQuery = true)
    public List<UserAppDetails> getByAppDetailId(Integer appdetailsId);
    
    @Query(value = "select * from user_app_details where user_id = ?1 order by last_pinged DESC limit 1", nativeQuery = true)
    public List<UserAppDetails> getUserLastPingedUserAppDetails(Integer userId);
    
    @Query(value = "select * from user_app_details where user_id = ?1 order by joined ASC limit 1", nativeQuery = true)
    public List<UserAppDetails> getUserFirstUserAppDetails(Integer userId);
    
    @Query(value="select * from user_app_details where invited_details_id =?1", nativeQuery = true)
    public UserAppDetails getByInvitedId(Integer id);
    
}
