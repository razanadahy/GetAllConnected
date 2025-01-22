package HTTPS;

import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class SelfSignedCertificateGenerator {

    public static X509Certificate generateCertificate(String dn, KeyPair pair) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);

        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                new org.bouncycastle.asn1.x500.X500Name(dn),
                BigInteger.valueOf(now),
                startDate,
                new Date(now + 365 * 24 * 60 * 60 * 1000L),
                new org.bouncycastle.asn1.x500.X500Name(dn),
                pair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(pair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(builder.build(signer));
    }
}

