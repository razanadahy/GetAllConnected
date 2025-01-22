package HTTPS;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContextBuilder;
import org.littleshoot.proxy.*;
import org.littleshoot.proxy.extras.SelfSignedSslEngineSource;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;

public class HttpsUrlInterceptor {
    public static void main(String[] args) {
        // Ajoute BouncyCastle comme fournisseur de sécurité
        Security.addProvider(new BouncyCastleProvider());

        // Configure le proxy LittleProxy
        HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
                .withPort(8080)
                .withManInTheMiddle(new BouncyCastleMitmManager())
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        return new HttpFiltersAdapter(originalRequest) {
                            @Override
                            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                if (httpObject instanceof HttpRequest) {
                                    HttpRequest request = (HttpRequest) httpObject;
                                    System.out.println("Intercepted URL: " + request.getUri());
                                }
                                return null;
                            }
                        };
                    }
                })
                .start();

        System.out.println("Proxy started on port 8080...");
    }

    // Classe interne pour gérer le MITM avec BouncyCastle
    static class BouncyCastleMitmManager implements MitmManager {
        /*private final KeyPair keyPair;
        private final X509Certificate certificate;

        public BouncyCastleMitmManager() {
            try {
                // Génère une paire de clés RSA
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                this.keyPair = keyPairGenerator.generateKeyPair();

                // Génère un certificat auto-signé
                this.certificate = SelfSignedCertificateGenerator.generateCertificate("CN=Proxy, O=MyOrg", keyPair);
            } catch (Exception e) {
                throw new RuntimeException("Error generating keys or certificate", e);
            }
        }

        @Override
        public ClientSslEngineSource getClientSslEngineSource() {
            return new SelfSignedSslEngineSource(keyPair, certificate);
        }

        @Override
        public ServerSslEngineSource getServerSslEngineSource() {
            return new SelfSignedSslEngineSource(keyPair, certificate);
        }

        @Override
        public SSLEngine serverSslEngine(String s, int i) {
            return null;
        }

        @Override
        public SSLEngine serverSslEngine() {
            return null;
        }

        @Override
        public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession sslSession) {
            return null;
        }*/
        private final KeyPair keyPair;
        private final X509Certificate certificate;

        public BouncyCastleMitmManager() {
            try {
                // Génère une paire de clés RSA
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                this.keyPair = keyPairGenerator.generateKeyPair();

                // Génère un certificat auto-signé
                this.certificate = SelfSignedCertificateGenerator.generateCertificate("CN=Proxy, O=MyOrg", keyPair);
            } catch (Exception e) {
                throw new RuntimeException("Error generating keys or certificate", e);
            }
        }

        @Override
        public SSLEngine serverSslEngine(String peerHost, int peerPort) {
            try {
                //return SslContextBuilder.forServer(keyPair.getPrivate(), certificate).build().newEngine(new ,peerHost,peerPort);
                return SslContextBuilder.forServer(keyPair.getPrivate(), certificate).build().newEngine(ByteBufAllocator.DEFAULT);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create server SSLEngine", e);
            }
        }

        @Override
        public SSLEngine serverSslEngine() {
            return serverSslEngine(null, -1);
        }

        @Override
        public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession sslSession) {
            try {
                return SslContextBuilder.forClient().build().newEngine(ByteBufAllocator.DEFAULT);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create client SSLEngine", e);
            }
        }

    }
}
