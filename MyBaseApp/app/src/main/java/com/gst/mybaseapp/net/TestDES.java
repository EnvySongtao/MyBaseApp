package com.gst.mybaseapp.net;

import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TestDES {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    public static final String DEFAULT_VALUER = "12345678";

    /**
     * DES算法，加密
     *
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes());
    }

    /**
     * DES算法，加密
     *
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     */
    public static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(DEFAULT_VALUER.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);
            String s = Base64.encodeToString(bytes, 0);
            return s;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * DES算法，解密
     *
     * @param data
     *            待解密字符串
     * @param key
     *            解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    public static byte[] decode(String key, byte[] data) throws Exception {
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(DEFAULT_VALUER.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 获取编码后的值
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String decodeValue(String key, String data) throws Exception {
        byte[] datas;
        String value = null;

        datas = decode(key, Base64.decode(data, 0));

        value = new String(datas);
        if (value.equals("")) {
            throw new Exception();
        }
        return value;
    }

    public static void main(String[] args)throws Exception{
        String encodeStr=encode("123456123456", "{NAME:'1',NIHAO:'2'}");
        System.out.println("密文为："+encodeStr);
        String decodeStr=decodeValue("123456123456",encodeStr);
        System.out.println("解密后的明文为："+decodeStr);
    }



    /**
     * AES加密ܹ的处理方法
     *
     * @param pwd      加密密钥
     * @param strEncpt 即将被加密的字符串
     */
    public static String aesEncrypt(String pwd, String strEncpt) throws Exception {
        try {
            byte[] bytIn = strEncpt.getBytes("UTF8");
            SecretKeySpec skeySpec = new SecretKeySpec(pwd.getBytes("UTF8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] bytOut = cipher.doFinal(bytIn);
            String ecrOut = new BASE64Encoder().encode(bytOut);
            return ecrOut;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * AES解密处理方法
     *
     * @param pwd 解密密钥
     * @param
     **/
    public static String aesDencrypt(String pwd, String strDencpt) {
        byte[] bytIn;
        String ecrOut = "";
        try {
            bytIn = new BASE64Decoder().decodeBuffer(strDencpt);
            SecretKeySpec skeySpec = new SecretKeySpec(pwd.getBytes("UTF8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] bytOut = cipher.doFinal(bytIn);
            ecrOut = new String(bytOut, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ecrOut;
    }

    /**
     * AES加密ܹ的处理方法
     *
     * @param pwd      加密密钥
     * @param strEncpt 即将被加密的字符串
     * @param ivKey    使用CBC模式，需要一个向量iv，可增加加密算法的强度
     */
    public static String aesEncrypt(String pwd, String strEncpt, String ivKey)
            throws Exception {
        try {
            byte[] bytIn = strEncpt.getBytes("UTF8");
            SecretKeySpec skeySpec = new SecretKeySpec(pwd.getBytes("UTF8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                    iv);
            byte[] bytOut = cipher.doFinal(bytIn);
            String ecrOut = new BASE64Encoder().encode(bytOut);//此处使用BAES64做转码功能，同时能起到2次加密的作用。
            return ecrOut;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * AES解密处理方法
     *
     * @param pwd   解密密钥
     * @param
     * @param ivKey 使用CBC模式，需要一个向量iv，可增加加密算法的强度
     **/
    public static String aesDencrypt(String pwd, String strDencpt, String ivKey) {
        byte[] bytIn;
        String ecrOut = "";
        try {
            bytIn = new BASE64Decoder().decodeBuffer(strDencpt);
            SecretKeySpec skeySpec = new SecretKeySpec(pwd.getBytes("UTF8"),
                    "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] bytOut = cipher.doFinal(bytIn);
            ecrOut = new String(bytOut, "UTF8");
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ecrOut;
    }

}
