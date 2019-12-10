package com.wm.v1userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author weimin
 * @ClassName JwtTokenUtils
 * @Description TODO
 * @date 2019/11/14 11:27
 */
@Data
@AllArgsConstructor
public class JwtTokenUtils {
    private String id;
    private String subject;
    private Long ttl;
    private String secretKey;

    public JwtTokenUtils(String secretKey){
        this.secretKey = secretKey;
    }

    public String createJwtToken(){
        JwtBuilder builder = Jwts.builder()
                .setId(id).setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ttl))
                .signWith(SignatureAlgorithm.HS256,secretKey);
        return builder.compact();
    }

    public Claims getBody(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
    }

}
