package org.tomlang.livechat.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.tomlang.livechat.entities.UserAppInvitedDetails;

public interface UserAppDetailsInvitedRepository extends CrudRepository<UserAppInvitedDetails, Integer> {
    
    @Query(value = "select * from user_app_invited_detail where email = ?1", nativeQuery = true)
    public List<UserAppInvitedDetails> getInvitedDetailsByEmail (String email);
    
    @Query(value = "select * from user_app_invited_detail where invitation_code = ?1", nativeQuery = true)
    public UserAppInvitedDetails getInvitedDetailsByCode (String code);
    
    @Transactional
    @Modifying
    @Query(value = "delete from user_app_invited_detail where invitation_code = ?1", nativeQuery = true)
    public void deleteByCode(String code);
      
    @Transactional
    @Modifying
    @Query(value = "delete from user_app_invited_detail where email = ?1", nativeQuery = true)
    public void deleteByEmail(String email);
    
    
    
    
    

}
