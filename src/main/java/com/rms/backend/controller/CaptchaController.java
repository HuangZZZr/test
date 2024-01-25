package com.rms.backend.controller;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.rms.backend.common.ResponseResult;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description: 验证码
 * @author: huang
 * @date 2024年01月08日 19:32
 **/
@RestController
@RequestMapping("captcha")
public class CaptchaController {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    //验证码
    @GetMapping("getCaptcha")
    public ResponseResult getCaptcha(){
      //生成验证码
        LineCaptcha lineCaptcha = new LineCaptcha(110, 50, 4, 3);
        String code = lineCaptcha.getCode();
        //将验证阿门存储到redis中 key；uuid value： code
        String uuid = IdUtil.simpleUUID();
        //设置有效时间为3分钟
        stringRedisTemplate.opsForValue().set(uuid,code,3, TimeUnit.MINUTES);
        //把验证码以base64格式发送
        String imageBase64 = lineCaptcha.getImageBase64();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid",uuid);
        map.put("base64Img",imageBase64);
        return ResponseResult.success().data(map);
    }






}
