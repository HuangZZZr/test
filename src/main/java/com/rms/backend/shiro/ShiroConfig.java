package com.rms.backend.shiro;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;

/**
 * @description: shiro配置类
 * @author: huang
 * @date 2024年01月08日 17:37
 **/
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        filters.put("kgc",new JWTFilter());
//        添加自定义的filter到shiro中
        shiroFilterFactoryBean.setFilters(filters);

        //        资源权限
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/captcha","anon");
        map.put("/property/login","anon");
        map.put("/alipay/pay","anon");
        map.put("/alipay/test","anon");
        map.put("/alipay/notify","anon");
        map.put("/**","kgc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(realm);
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;

    }
    @Bean
    public Realm realm(){
        CustomerRealm customerRealm = new CustomerRealm();
        return customerRealm;
    }

}
