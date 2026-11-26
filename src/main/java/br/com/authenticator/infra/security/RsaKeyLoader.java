package br.com.authenticator.infra.security;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Carrega chaves RSA de arquivos PEM (formato PKCS#8 para privada, X.509 para pública).
 */
public class RsaKeyLoader {

    private RsaKeyLoader() {}

    public static RSAPrivateKey loadPrivateKey(Resource resource) {
        try {
            String pem = resource.getContentAsString(StandardCharsets.UTF_8);
            byte[] decoded = decodePem(pem, "PRIVATE KEY");
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to load RSA private key", e);
        }
    }

    public static RSAPublicKey loadPublicKey(Resource resource) {
        try {
            String pem = resource.getContentAsString(StandardCharsets.UTF_8);
            byte[] decoded = decodePem(pem, "PUBLIC KEY");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Failed to load RSA public key", e);
        }
    }

    private static byte[] decodePem(String pem, String type) {
        String stripped = pem
                .replace("-----BEGIN " + type + "-----", "")
                .replace("-----END " + type + "-----", "")
                .replaceAll("\\s+", "");
        return Base64.getDecoder().decode(stripped);
    }

}
