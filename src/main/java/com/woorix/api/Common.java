package com.woorix.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Common {
    public static String Hmac(String secret, String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        final String hmacSHA512 = "HmacSHA512";
        final Mac hasher = Mac.getInstance(hmacSHA512);
        hasher.init(new SecretKeySpec(secret.getBytes(), hmacSHA512));
        final byte[] hash = hasher.doFinal(payload.getBytes());

//            String resultBase = DatatypeConverter.printBase64Binary(hash); // to base64
        return DatatypeConverter.printHexBinary(hash).toLowerCase(); // to hex
    }
}
