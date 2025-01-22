//package HTTPS;
//
//import io.netty.handler.codec.http.HttpObject;
//import org.littleshoot.proxy.HttpFiltersAdapter;
//import org.littleshoot.proxy.HttpFiltersSourceAdapter;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//import org.littleshoot.proxy.impl.ProxyUtils;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.Security;
//import java.util.logging.Logger;
//
//public class HttpHttpsInterceptor {
//    private static final Logger logger = Logger.getLogger(HttpHttpsInterceptor.class.getName());
//    public static void main(String[] args) {
//        try {
//            // Add BouncyCastle as a Security Provider
//            Security.addProvider(new BouncyCastleProvider());
//
//            // Generate a key pair for the HTTPS interceptor
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(2048);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//
//            // Start the proxy server
//            DefaultHttpProxyServer.bootstrap()
//                    .withPort(8080) // The port for the proxy server
//                    .withManInTheMiddle(new SelfSignedMitmManager(keyPair))
//                    .withFiltersSource(new HttpFiltersSourceAdapter() {
//                        @Override
//                        public HttpFiltersAdapter filterRequest(HttpObject originalRequest) {
//                            return new HttpFiltersAdapter(originalRequest) {
//                                @Override
//                                public HttpObject proxyToServerRequest(HttpObject httpObject) {
//                                    logger.info("Request: " + httpObject.toString());
//                                    return super.proxyToServerRequest(httpObject);
//                                }
//
//                                @Override
//                                public HttpObject serverToProxyResponse(HttpObject httpObject) {
//                                    logger.info("Response: " + httpObject.toString());
//                                    return super.serverToProxyResponse(httpObject);
//                                }
//                            };
//                        }
//                    })
//                    .start();
//
//            System.out.println("Proxy server started on port 8080. Configure your browser to use this proxy.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
