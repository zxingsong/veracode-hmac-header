import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public final class HmacRequestSigner {

    private static final String VERACODE_REQUEST_VERSION_STRING = "vcode_request_version_1";

    private static final String DATA_FORMAT = "id=%s&host=%s&url=%s&method=%s";

    private static final String HEADER_FORMAT = "%s id=%s,ts=%s,nonce=%s,sig=%s";

    private static final String VERACODE_HMAC_SHA_256 = "VERACODE-HMAC-SHA-256";

    private static final String HMAC_SHA_256 = "HmacSHA256";

    private static final String UTF_8 = "UTF-8";

    private static final SecureRandom secureRandom = new SecureRandom();



    public static String getVeracodeAuthorizationHeader(final String id, final String key, final URL url, final String httpMethod)
            throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
        final String urlPath = (url.getQuery() == null) ? url.getPath() : url.getPath().concat("?").concat(url.getQuery());
        final String data = String.format(DATA_FORMAT, id, url.getHost(), urlPath, httpMethod);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        final String nonce = DatatypeConverter.printHexBinary(generateRandomBytes(16)).toLowerCase(Locale.US);
        final String signature = getSignature(key, data, timestamp, nonce);
        return String.format(HEADER_FORMAT, VERACODE_HMAC_SHA_256, id, timestamp, nonce, signature);
    }


    private static String getSignature(final String key, final String data, final String timestamp, final String nonce)
            throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
        final byte[] keyBytes = DatatypeConverter.parseHexBinary(key);
        final byte[] nonceBytes = DatatypeConverter.parseHexBinary(nonce);
        final byte[] encryptedNonce = hmacSha256(nonceBytes, keyBytes);
        final byte[] encryptedTimestamp = hmacSha256(timestamp, encryptedNonce);
        final byte[] signingKey = hmacSha256(VERACODE_REQUEST_VERSION_STRING, encryptedTimestamp);
        final byte[] signature = hmacSha256(data, signingKey);
        return DatatypeConverter.printHexBinary(signature).toLowerCase(Locale.US);
    }

    private static byte[] hmacSha256(final String data, final byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        final Mac mac = Mac.getInstance(HMAC_SHA_256);
        mac.init(new SecretKeySpec(key, HMAC_SHA_256));
        return mac.doFinal(data.getBytes(UTF_8));
    }

    private static byte[] hmacSha256(final byte[] data, final byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac mac = Mac.getInstance(HMAC_SHA_256);
        mac.init(new SecretKeySpec(key, HMAC_SHA_256));
        return mac.doFinal(data);
    }

    private static byte[] generateRandomBytes(final int size) {
        final byte[] key = new byte[size];
        secureRandom.nextBytes(key);
        return key;
    }

}