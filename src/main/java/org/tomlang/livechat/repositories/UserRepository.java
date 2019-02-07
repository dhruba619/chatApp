package org.tomlang.livechat.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
    
    @Query(value = "select u from User u where u.email is ?1")
    public Optional<User> findByEmailAddress(String emailAddress);
    
    @Query(value = "select u from User u where u.confirmEmailHash is ?1")
    public User findByEmailHash(String hash);

}
