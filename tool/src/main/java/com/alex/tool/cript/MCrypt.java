package com.alex.tool.cript;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MCrypt {

    private String iv;
    
    private IvParameterSpec ivspec;
    
    private SecretKeySpec keyspec;
    
    private Cipher cipher;

    private String SecretKey ;

    public MCrypt(String sKey, String iv) {
    	
        this.iv = iv;
        
        this.SecretKey = sKey;
        
        ivspec = new IvParameterSpec(iv.getBytes());
        
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException e) {
        }
    }

    public String encrypt(String text) {
    	
        if (text == null || text.length() == 0)
            throw new RuntimeException("Empty string");
        
        byte[] encrypted = null;
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            
            encrypted = cipher.doFinal(padString(text));
        } catch (Exception e) {
            throw new RuntimeException("[encrypt] " + e.getMessage());
        }
        
        return Base64.encodeBase64URLSafeString(encrypted);
    }
    
    public String encrypt(byte[] bin) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (bin == null || bin.length < 10)
            throw new RuntimeException("byte[] < 10");
        
        byte[] encrypted = null;
        
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        
        encrypted = cipher.doFinal(bin);
        
        return Base64.encodeBase64URLSafeString(encrypted);
    }

    public String encryptURLSafe(String text) throws Exception {
    	
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        
        byte[] encrypted = null;
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            
            encrypted = cipher.doFinal(padString(text));
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        
        return Base64.encodeBase64URLSafeString(encrypted);
    }
    
    public String encryptTWURLSafe(String text) throws Exception {
    	
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        
        byte[] encrypted = null;
        text = Base64.encodeBase64String(text.getBytes());
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            
            encrypted = cipher.doFinal(padString(text));
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        
        return Base64.encodeBase64URLSafeString(encrypted);
    }

    public byte[] decrypt(String code) throws Exception {
    	
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            
            decrypted = cipher.doFinal(Base64.decodeBase64(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        
        return decrypted;
    }
    
    public byte[] decryptTW(String code) throws Exception {
    	
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            
            decrypted = cipher.doFinal(Base64.decodeBase64(code));
            decrypted = Base64.decodeBase64(decrypted);
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        
        return decrypted;
    }
    
    private static byte[] padString(String source) {
    	byte[] ori = source.getBytes();
    	int m = ori.length % 16;	// 求餘數
    	byte[] dest = new byte[16*(ori.length/16) + (m>0 ? 16 : 0)];
    	System.arraycopy(ori, 0, dest, 0, ori.length);
    	for(int i=ori.length ; i<dest.length;i++){
    		dest[i] = ' ';
    	}
    	return dest;
    }
    
}