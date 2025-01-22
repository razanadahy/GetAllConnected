//package HTTPS;
//
//
//import io.netty.channel.ChannelHandler;
//import io.netty.handler.codec.http.HttpRequest;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.littleshoot.proxy.HttpFiltersSourceAdapter;
//import org.littleshoot.proxy.HttpProxyServer;
//import org.littleshoot.proxy.HttpProxyServerBootstrap;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//import org.littleshoot.proxy.impl.ProxyUtils;
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpResponse;
//
//import java.security.Security;
//
//public class ProxyServer {
//    public static void main(String[] args) {
//        // Ajouter le fournisseur BouncyCastle pour le support HTTPS
//        Security.addProvider(new BouncyCastleProvider());
//
//        // Configurer et démarrer le proxy
//        HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer.bootstrap()
//                .withPort(8080) // Port du proxy
//                .withAllowLocalOnly(false) // Permet les connexions externes
//                .withFiltersSource(new HttpFiltersSourceAdapter() {
//                    @Override
//                    public io.netty.channel.ChannelHandlerAdapter filterRequest(HttpRequest originalRequest, ChannelHandler ctx) {
//                        return new io.netty.channel.ChannelHandlerAdapter() {
//                            @Override
//                            public void channelRead(io.netty.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
//                                if (msg instanceof HttpRequest) {
//                                    HttpRequest request = (HttpRequest) msg;
//                                    System.out.println("URL Capturée : " + request.uri());
//                                }
//                                super.channelRead(ctx, msg);
//                            }
//
//                            @Override
//                            public void channelReadComplete(io.netty.channel.ChannelHandlerContext ctx) throws Exception {
//                                ctx.flush();
//                            }
//                        };
//                    }
//                });
//
//        HttpProxyServer server = bootstrap.start();
//        System.out.println("Proxy démarré sur le port 8080. Configurez votre navigateur pour utiliser ce proxy.");
//    }
//}

//package HTTPS;
//
//import org.littleshoot.proxy.HttpFiltersAdapter;
//import org.littleshoot.proxy.HttpFiltersSourceAdapter;
//import org.littleshoot.proxy.HttpProxyServer;
//import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpRequest;
//import io.netty.handler.codec.http.HttpResponse;
//
//import java.security.KeyStore;
//
//public class ProxyServer {
//
//    public static void main(String[] args) {
//        try {
//            // Génération du certificat SSL via CertificateGenerator
//            KeyStore keyStore = CertificateGenerator.generateCertificate();
//            String keyPassword = "password"; // Mot de passe du KeyStore
//
//            // Création et configuration du serveur proxy
//            HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
//                    .withPort(8080) // Port du proxy
//                    .withManInTheMiddle(new CustomMitmManager(keyStore, keyPassword)) // Gestion HTTPS
//                    .withFiltersSource(new HttpFiltersSourceAdapter() {
//                        @Override
//                        public HttpFiltersAdapter filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
//                            return new HttpFiltersAdapter(originalRequest) {
//                                @Override
//                                public HttpResponse clientToProxyRequest(HttpObject httpObject) {
//                                    if (httpObject instanceof HttpRequest) {
//                                        HttpRequest request = (HttpRequest) httpObject;
//                                        System.out.println("URL Capturée : " + request.uri());
//                                    }
//                                    return null;
//                                }
//                            };
//                        }
//                    })
//                    .start();
//
//            System.out.println("Proxy HTTPS démarré sur le port 8080.");
//        } catch (Exception e) {
//            System.err.println("Erreur lors du démarrage du proxy : " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}

package HTTPS;

import io.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpObject;
import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
//import org.littleshoot.proxy.impl.DefaultMitmManager;
import java.security.KeyStore;

public class ProxyServer {

    public static void main(String[] args) throws Exception {
        KeyStore keyStore = CertificateGenerator.generateCertificate();
//        MitmManager mitmManager = new DefaultMitmManager(keyStore, "password");
        DefaultHttpProxyServer.bootstrap()
                .withPort(8080)
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    @Override
                    public HttpFiltersAdapter filterRequest(HttpRequest originalRequest) {
                        return new HttpFiltersAdapter(originalRequest) {
                            @Override
                            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                if (httpObject instanceof HttpRequest) {
                                    HttpRequest request = (HttpRequest) httpObject;
                                    System.out.println("URL capturée : " + request.getUri());
                                }
                                return super.clientToProxyRequest(httpObject);
                            }
                        };
                    }
                })
                .start();

        System.out.println("Proxy HTTP démarré sur le port 8080.");
    }
}