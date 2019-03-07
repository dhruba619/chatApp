package org.tomlang.livechat.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tomlang.livechat.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {
    
    @Value("${app.jwt.keystore}")
    private String keystore;
    
    @Value("${app.jwt.keystore.password}")
    private String keystorePss;
    
    @Value("${app.jwt.key.password}")
    private String keypass;
    
    @Value("${app.jwt.key.alias}")
    private String alias;
    
    @Value("${app.jwt.ttl}")
    private String jwtTTL;
    
    
      
    public static final String TOKEN_ISSUER = "LIVE-CHAT-APP";
    public  String createAccessToken(User user) {
        
        
        
      //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
     
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
       
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(String.valueOf(user.getId()))
                                    .setIssuedAt(now)
                                    .setSubject(user.getEmail())
                                    .setIssuer(TOKEN_ISSUER)
                                    .signWith(signatureAlgorithm, getKeyPair().getPrivate());
     
        //if it has been specified, let's add the expiration
        
        long ttlMillis = Long.parseLong(jwtTTL);
        if (ttlMillis >= 0) {
        long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
     
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    
    public  Integer getUserIdFromJwt(String jwt) {
        Claims claims = Jwts.parser()         
            .setSigningKey(getKeyPair().getPrivate())
            .parseClaimsJws(jwt).getBody();
        return Integer.valueOf(claims.getId());
    }
    
    public Claims parseToken(String jwt) {
        return Jwts.parser()         
        .setSigningKey(getKeyPair().getPrivate())
        .parseClaimsJws(jwt).getBody();
    }
    
    private KeyPair getKeyPair()  {
        FileInputStream is;
        try {
            is = new FileInputStream(keystore);
        

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, keystorePss.toCharArray());

        

        Key key = keystore.getKey(alias, keypass.toCharArray());
        if (key instanceof PrivateKey) {
          // Get certificate of public key
          Certificate cert = keystore.getCertificate(alias);

          // Get public key
          PublicKey publicKey = cert.getPublicKey();

          // Return a key pair
          return new KeyPair(publicKey, (PrivateKey) key);
        }
        
        } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
