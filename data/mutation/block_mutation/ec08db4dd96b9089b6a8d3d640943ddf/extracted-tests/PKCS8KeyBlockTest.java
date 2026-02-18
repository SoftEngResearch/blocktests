package org.apache.commons.ssl;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.commons.ssl.PKCS8Key.*;

public class PKCS8KeyBlockTest {

    @Test
    public void testLine665() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.1";
        int keySize = 128;
        String mode = "CBC";
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        assertEquals("ECB", mode);
    }

    @Test
    public void testLine666() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.2.1";
        int keySize = 128;
        String mode = "CBC";
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        assertEquals("CBC", mode);
    }

    @Test
    public void testLine667() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.23";
        int keySize = 192;
        String mode = "CBC";
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        assertEquals("OFB", mode);
    }

    @Test
    public void testLine668() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.44";
        int keySize = 256;
        String mode = "CBC";
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        assertEquals("CFB", mode);
    }

    @Test(expected = RuntimeException.class)
    public void testLine669() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.25";
        String mode = "CBC";
        int keySize = -1;
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        switch(finalDigit / 10) {
            case 0:
                keySize = 128;
                break;
            case 2:
                keySize = 192;
                break;
            case 4:
                keySize = 256;
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testLine670() throws Exception {
        String oid = "2.16.840.1.101.3.4.1.14";
        String mode = "CBC";
        int keySize = -1;
        int finalDigit;
        oid = oid.substring("2.16.840.1.101.3.4.1.".length());
        int x = oid.indexOf('.');
        if (x >= 0) {
            finalDigit = Integer.parseInt(oid.substring(0, x));
        } else {
            finalDigit = Integer.parseInt(oid);
        }
        switch(finalDigit % 10) {
            case 1:
                mode = "ECB";
                break;
            case 2:
                mode = "CBC";
                break;
            case 3:
                mode = "OFB";
                break;
            case 4:
                mode = "CFB";
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
        switch(finalDigit / 10) {
            case 0:
                keySize = 128;
                break;
            case 2:
                keySize = 192;
                break;
            case 4:
                keySize = 256;
                break;
            default:
                throw new RuntimeException("Unknown AES final digit: " + finalDigit);
        }
    }
}
