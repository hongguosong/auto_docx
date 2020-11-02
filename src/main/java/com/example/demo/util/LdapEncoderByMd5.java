package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/4/23 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class LdapEncoderByMd5 {
    /**
     * ldap md5加密
     * @param psw
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encode(String psw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        String md5psw=base64en.encode(md5.digest(psw.getBytes("utf-8")));
        return "{MD5}"+ md5psw;
    }

    /**
     * sha1加密
     * @param str
     * @return
     */
    public static String getSha1(String str) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Openldap 产生SSHA密码的算法
     * 效果等同于 slappasswd -h {ssha} -s password
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateSSHAPwd(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final int SALT_LENGTH = 4;
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes("utf-8"));
        crypt.update(salt);
        byte[] hash = crypt.digest();

        byte[] hashPlusSalt = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
        System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

        return new StringBuilder().append("{SSHA}")
                .append(new String(Base64.encodeBase64(hashPlusSalt), Charset.forName("utf-8")))
                .toString();
    }

    public static PasswordEncoder getBCrypt(){
        String idForEncode = "CRYPT";
        Map<String,PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
// * encoders.put("noop", NoOpPasswordEncoder.getInstance());
// * encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
// * encoders.put("scrypt", new SCryptPasswordEncoder());
// * encoders.put("sha256", new StandardPasswordEncoder());
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        return passwordEncoder;
    }
}
