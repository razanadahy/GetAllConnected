//package HTTPS;
//
//import io.netty.handler.codec.http.HttpRequest;
//import org.littleshoot.proxy.MitmManager;
//import org.littleshoot.proxy.impl.ProxyUtils;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLEngine;
//import javax.net.ssl.SSLSession;
//import java.security.KeyStore;
//import java.security.Security;
//
//public class CustomSslEngineSource implements MitmManager {
//    private final SSLContext sslContext;
//
//    public CustomSslEngineSource(KeyStore keyStore, String keyPassword) throws Exception {
//        // Ajouter BouncyCastle comme fournisseur de sécurité
//        Security.addProvider(new BouncyCastleProvider());
//
//        // Créer un contexte SSL à partir du keyStore et de la clé
//        this.sslContext = ProxyUtils.createServerSslContext(keyStore, keyPassword);
//    }
//
//    @Override
//    public SSLContext serverSslContext() {
//        return this.sslContext;
//    }
//
//    @Override
//    public SSLContext clientSslContext() {
//        // Renvoie un SSLContext qui permet d'accepter les connexions en toute confiance
//        return ProxyUtils.getTrustingSslContext();
//    }
//}
