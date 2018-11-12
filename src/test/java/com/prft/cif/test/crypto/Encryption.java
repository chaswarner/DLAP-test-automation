package com.prft.cif.test.crypto;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * This class encrypt decrypt a given string.
 * <p></p>
 *
 * @author Anant Gowerdhan
 * @see Cipher
 * @see SecretKeySpec
 * @see MessageDigest
 * @since 06/13/2018
 */
public class Encryption {
    private final Logger LOGGER = Logger.getLogger(this.getClass());

    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String PBKDF_2_WITH_HMAC_SHA_512 = "PBKDF2WithHmacSHA512";
    private static final String AES = "AES";
    private static final String UTF_8 = "UTF-8";

    // Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
    private static final int ITERATION_COUNT = 40000;
    // Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
    private static final int KEY_LENGTH = 128;

    private final byte[] salt;

    public Encryption() {
        salt = PBKDF_2_WITH_HMAC_SHA_512.getBytes();
    }

    /**
     * @param password
     * @param salt
     * @param iterationCount
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_512);
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), AES);
    }

    /**
     * @param property
     * @param secreteKey
     * @return String: Encrypted string
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String property, String secreteKey) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeySpec key = createSecretKey(secreteKey.toCharArray(),
                salt, ITERATION_COUNT, KEY_LENGTH);
        Cipher pbeCipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    /**
     * @param bytes
     * @return
     */
    private String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * @param string
     * @param secreteKey
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public String decrypt(String string, String secreteKey) throws GeneralSecurityException, IOException {
        SecretKeySpec key = createSecretKey(secreteKey.toCharArray(),
                salt, ITERATION_COUNT, KEY_LENGTH);
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), UTF_8);
    }

    /**
     * @param property
     * @return
     * @throws IOException
     */
    private byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }

    public static void main(String arg[]) throws Exception {
        Encryption encryption = new Encryption();
        System.out.println(encryption.encrypt(arg[0], PBKDF_2_WITH_HMAC_SHA_512));
    }
}
