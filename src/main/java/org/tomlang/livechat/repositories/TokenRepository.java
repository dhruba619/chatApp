package org.tomlang.livechat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tomlang.livechat.entities.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer>{

    @Query(value = "select * from token_store where token = ?1", nativeQuery = true)
    public Token findByToken(String token);
}
