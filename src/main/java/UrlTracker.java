import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

import java.util.List;

public class UrlTracker {
    public static void main(String[] args) throws PcapNativeException, NotOpenException, InterruptedException {
//        List<PcapNetworkInterface> allDevices = Pcaps.findAllDevs();
//        if (allDevices == null || allDevices.isEmpty()) {
//            System.out.println("Aucune interface réseau trouvée.");
//            return;
//        }
//
//        for (int i = 0; i < allDevices.size(); i++) {
//            System.out.println("[" + i + "] " + allDevices.get(i).getName());
//        }
//        PcapNetworkInterface nif = Pcaps.findAllDevs().get(0); // Choisissez la première interface
//
//        if (nif == null) {
//            System.out.println("Aucune interface réseau trouvée.");
//            return;
//        }
//
//        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
//
//        System.out.println("Monitoring network traffic...");
//        handle.loop(0, (PacketListener) packet -> {
//            System.out.println("on est ici...");//pourquoi ceci n'est pas afficher?
//            if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
//                String payload = packet.toString();
//                if (payload.contains("http://") || payload.contains("https://")) {
//                    System.out.println("URL trouvée : " + payload);
//                }
//            }
//        });
//
//        handle.close();

        PcapNetworkInterface nif = Pcaps.findAllDevs().get(3);

        System.out.println("Interface sélectionnée : " + nif.getName());
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        handle.setFilter("tcp port 80 or tcp port 443", BpfProgram.BpfCompileMode.OPTIMIZE);

        System.out.println("Début de la capture...");
//        handle.loop(0, (PacketListener) packet -> {
////            System.out.println("on est ici..."); // Vérifiez si cela s'affiche
//            System.out.println(packet.toString());
//            if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
//                String payload = packet.toString();
//                if (payload.contains("http://") || payload.contains("https://")) {
//                    System.out.println("URL trouvée : " + payload);
//                }
//            }
//        });
//
//        handle.close();
//        System.out.println("Capture terminée !");
        handle.loop(0, (PacketListener) packet -> {
            try {
                if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
                    if (tcpPacket != null && ipv4Packet != null) {
                        String srcIp = ipv4Packet.getHeader().getSrcAddr().getHostAddress();
                        String dstIp = ipv4Packet.getHeader().getDstAddr().getHostAddress();
                        int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();

                        // Vérifiez le port pour HTTP/HTTPS
                        if (dstPort == 80 || dstPort == 443) {
                            System.out.println("Connexion détectée : " + srcIp + " -> " + dstIp + ":" + dstPort);
                        }

                        // Extraction des URL HTTP
                        if (dstPort == 80 && tcpPacket.getPayload() != null) {
                            String payload = new String(tcpPacket.getPayload().getRawData());
                            if (payload.contains("Host:")) {
                                String[] lines = payload.split("\r\n");
                                for (String line : lines) {
                                    if (line.startsWith("Host:")) {
                                        String host = line.substring(6).trim();
                                        System.out.println("URL détectée : http://" + host);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'analyse du paquet : " + e.getMessage());
            }
        });

        handle.close();
        System.out.println("Capture terminée !");
    }
}
