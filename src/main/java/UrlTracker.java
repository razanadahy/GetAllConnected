//import org.pcap4j.core.*;
//import org.pcap4j.packet.IpV4Packet;
//import org.pcap4j.packet.TcpPacket;
//
//
//public class UrlTracker {
//    public static void main(String[] args) throws PcapNativeException, NotOpenException, InterruptedException {
//
//        PcapNetworkInterface nif = Pcaps.findAllDevs().get(3);
//
//        System.out.println("Interface sélectionnée : " + nif.getName());
//        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
//        handle.setFilter("tcp port 80 or tcp port 443", BpfProgram.BpfCompileMode.OPTIMIZE);
//
//        System.out.println("Début de la capture...");
//
//        handle.loop(0, (PacketListener) packet -> {
//            try {
//                if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
//                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
//                    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
//                    if (tcpPacket != null && ipv4Packet != null) {
//                        String srcIp = ipv4Packet.getHeader().getSrcAddr().getHostAddress();
//                        String dstIp = ipv4Packet.getHeader().getDstAddr().getHostAddress();
//                        int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();
//
//                        if (dstPort == 80 || dstPort == 443) {
//                            System.out.println("Connexion détectée : " + srcIp + " -> " + dstIp + ":" + dstPort);
//                        }
//
//                        if (dstPort == 80 && tcpPacket.getPayload() != null) {
//                            String payload = new String(tcpPacket.getPayload().getRawData());
//                            if (payload.contains("Host:")) {
//                                String[] lines = payload.split("\r\n");
//                                for (String line : lines) {
//                                    if (line.startsWith("Host:")) {
//                                        String host = line.substring(6).trim();
//                                        System.out.println("URL détectée : http://" + host);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                System.err.println("Erreur lors de l'analyse du paquet : " + e.getMessage());
//            }
//        });
//
//        handle.close();
//        System.out.println("Capture terminée !");
//    }
//}
import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

public class UrlTracker {
    public static void main(String[] args) throws PcapNativeException, NotOpenException, InterruptedException {

        PcapNetworkInterface nif = Pcaps.findAllDevs().get(3); // Sélectionnez l'interface réseau
        System.out.println("Interface sélectionnée : " + nif.getName());
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        handle.setFilter("", BpfProgram.BpfCompileMode.OPTIMIZE); // Filtre pour HTTP uniquement

        System.out.println("Début de la capture...");

        handle.loop(0, (PacketListener) packet -> {

            try {
                if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
                    if (tcpPacket != null && ipv4Packet != null) {
//                        String srcIp = ipv4Packet.getHeader().getSrcAddr().getHostAddress();
//                        String dstIp = ipv4Packet.getHeader().getDstAddr().getHostAddress();
////
//                        int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();

                        if ( tcpPacket.getPayload() != null) {
                            String payload = new String(tcpPacket.getPayload().getRawData());
                            // Analyse du contenu HTTP
                            if (payload.startsWith("GET") || payload.startsWith("POST")) {
                                String[] lines = payload.split("\r\n");
                                String method = lines[0].split(" ")[0]; // La méthode HTTP (GET, POST, etc.)
                                String path = lines[0].split(" ")[1];  // Le chemin demandé

                                // Recherche du champ Host dans les en-têtes
                                String host = "";
                                for (String line : lines) {
                                    if (line.startsWith("Host:")) {
                                        host = line.substring(6).trim();
                                        break;
                                    }
                                }

                                if (!host.isEmpty()) {
                                    System.out.println("URL détectée : " + method + " http://" + host + path);
                                }
                            }
                        }else{
//                            System.out.println("else....!tcpPacket");
                        }
                    }
                }else{
//                    System.out.println("else....!IpV4Packet");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'analyse du paquet : " + e.getMessage());
            }
        });

        handle.close();
        System.out.println("Capture terminée !");
    }
}
