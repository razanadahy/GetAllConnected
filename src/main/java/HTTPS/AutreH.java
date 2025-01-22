package HTTPS;

public class AutreH {
//    package HTTPS;
//
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpRequest;
//import org.littleshoot.proxy.DefaultHttpProxyServer;
//import org.littleshoot.proxy.HttpFiltersAdapter;
//import org.littleshoot.proxy.HttpFiltersSourceAdapter;
//import org.littleshoot.proxy.MitmManager;
//import org.littleshoot.proxy.impl.DefaultMitmManager;
//
//import java.security.KeyStore;
//
//    public class HttpsProxy {
//
//        public static void main(String[] args) {
//            try {
//                // Générer un certificat racine
//                KeyStore keyStore = CertificateGenerator.generateCertificate();
//
//                // Configurer le MitmManager avec le certificat racine
//                MitmManager mitmManager = new DefaultMitmManager(keyStore, "password");
//
//                // Démarrer le proxy HTTPS
//                DefaultHttpProxyServer.bootstrap()
//                        .withPort(8080)
//                        .withManInTheMiddle(mitmManager) // Interception HTTPS
//                        .withFiltersSource(new HttpFiltersSourceAdapter() {
//                            @Override
//                            public HttpFiltersAdapter filterRequest(HttpRequest originalRequest) {
//                                return new HttpFiltersAdapter(originalRequest) {
//                                    @Override
//                                    public HttpObject clientToProxyRequest(HttpObject httpObject) {
//                                        if (httpObject instanceof HttpRequest) {
//                                            HttpRequest request = (HttpRequest) httpObject;
//                                            System.out.println("URL interceptée : " + request.uri());
//                                        }
//                                        return super.clientToProxyRequest(httpObject);
//                                    }
//                                };
//                            }
//                        })
//                        .start();
//
//                System.out.println("Proxy HTTPS démarré sur le port 8080.");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
