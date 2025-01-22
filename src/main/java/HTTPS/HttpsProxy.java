//package HTTPS;
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpRequest;
//import org.littleshoot.proxy.HttpFiltersAdapter;
//import org.littleshoot.proxy.HttpFiltersSourceAdapter;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//import org.littleshoot.proxy.impl.BouncyCastleSslEngineSource;
//
//import java.security.KeyStore;
//
//public class HttpsProxy {
//
//    public static void main(String[] args) throws Exception {
//        // Générer le certificat racine
//        KeyStore keyStore = CertificateGenerator.generateCertificate();
//
//        // Démarrer le proxy
//        DefaultHttpProxyServer.bootstrap()
//                .withPort(8888) // Port du proxy
//                .withManInTheMiddle(new BouncyCastleSslEngineSource(keyStore, "password")) // SSL interception
//                .withFiltersSource(new HttpFiltersSourceAdapter() {
//                    @Override
//                    public HttpFiltersAdapter filterRequest(HttpRequest originalRequest) {
//                        return new HttpFiltersAdapter(originalRequest) {
//                            @Override
//                            public io.netty.handler.codec.http.HttpResponse clientToProxyRequest(
//                                    HttpObject httpObject) {
//                                System.out.println("Requête interceptée : " + originalRequest.uri());
//                                return null;
//                            }
//                        };
//                    }
//                })
//                .start();
//
//        System.out.println("Proxy démarré sur le port 8888. Configurez votre navigateur pour utiliser ce proxy.");
//    }
//}
