package HTTPS;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertificateGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyStore generateCertificate() throws Exception {
        // Génération de la clé privée
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Génération du certificat auto-signé
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + (365 * 24 * 60 * 60 * 1000L)); // 1 an
        BigInteger serialNumber = new BigInteger(64, new SecureRandom());
        X509CertificateHolder certHolder = new JcaX509v3CertificateBuilder(
                new org.bouncycastle.asn1.x500.X500Name("CN=LittleProxy Root CA"),
                serialNumber,
                notBefore,
                notAfter,
                new org.bouncycastle.asn1.x500.X500Name("CN=LittleProxy Root CA"),
                keyPair.getPublic()
        ).build(new JcaContentSignerBuilder("SHA256WithRSA").setProvider("BC").build(keyPair.getPrivate()));

        X509Certificate rootCert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);

        // Création d'un KeyStore contenant la clé et le certificat
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry("root", keyPair.getPrivate(), "password".toCharArray(), new Certificate[]{rootCert});
        return keyStore;
    }
}
