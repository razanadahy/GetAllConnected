//package HTTPS;
//
//import org.littleshoot.proxy.mitm.CertificateAndKeySource;
//import org.littleshoot.proxy.impl.ManInTheMiddle;
//import org.littleshoot.proxy.mitm.SelfSignedCertificateGenerator;
//import org.littleshoot.proxy.MitmManager;
//import java.security.KeyStore;
//
//public class CustomMitmManager extends ManInTheMiddle {
//    public CustomMitmManager(KeyStore keyStore, String keyPassword) {
//        super(new CertificateAndKeySource(
//                keyStore,
//                keyPassword,
//                new SelfSignedCertificateGenerator())
//        );
//    }
//}
