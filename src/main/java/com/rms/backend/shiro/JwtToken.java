package com.rms.backend.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description: JwtToken
 * @author: huang
 * @date 2024年01月10日 19:36
 **/

public class JwtToken implements AuthenticationToken {
       private String token;

    public JwtToken(String token) {
        this.token=token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
