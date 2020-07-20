package com.puhui.email.config;

import com.puhui.email.util.AESUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description: security不做加密处理
 * @author: 杨利华
 * @date: 2020/7/11
 */
public class DefinitionSecurityPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        //不做任何加密处理
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //rawPassword是前端传过来的密码，encodedPassword是数据库中查到的密码
        String decryptPassword = AESUtil.decrypt(encodedPassword);
        if (rawPassword.toString().equals(decryptPassword)) {
            return true;
        }
        return false;
    }
}
