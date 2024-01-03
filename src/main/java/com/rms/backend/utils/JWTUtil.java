package com.rms.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${secret}")
    private String secret;

    @Value("${time}")
    private Integer expireTime;

//    创建token
    public String creteToken(Map<String,Object> payload){
        //        头部
        HashMap<String, Object> header = new HashMap<>();
        header.put("alg","JS256");
        header.put("typ","jwt");

//        时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,expireTime);

        return JWT.create()
                .withHeader(header)
                .withClaim("userInfo",payload)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
    }

//    验证token
    public boolean verifyToken(String token){
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return true;
    }

//    解析token
    public Map<String,Object> getClaim(String token){
        return JWT.decode(token).getClaim("userInfo").asMap();
    }
}
