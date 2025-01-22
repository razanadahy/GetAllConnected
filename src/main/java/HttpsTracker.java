import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

import java.util.Arrays;

public class HttpsTracker {
    public static void main(String[] args) throws PcapNativeException, NotOpenException, InterruptedException {

        PcapNetworkInterface nif = Pcaps.findAllDevs().get(3); // Sélectionnez l'interface réseau
        System.out.println("Interface sélectionnée : " + nif.getName());
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        handle.setFilter("", BpfProgram.BpfCompileMode.OPTIMIZE); // Filtre HTTPS

        System.out.println("Début de la capture des connexions HTTPS...");

        handle.loop(0, (PacketListener) packet -> {
            try {
                if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
                    if (tcpPacket != null && ipv4Packet != null) {
                        String srcIp = ipv4Packet.getHeader().getSrcAddr().getHostAddress();
                        String dstIp = ipv4Packet.getHeader().getDstAddr().getHostAddress();
                        int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();

                        System.out.println("Connexion HTTPS détectée : " + srcIp + " -> " + dstIp + ":" + dstPort);

                        // Optionnel : Ajouter l'analyse des données TLS si disponibles
                        if (tcpPacket.getPayload() != null) {
                            String payload = new String(tcpPacket.getPayload().getRawData());
                            System.out.println("Données TLS brutes : " + payload);
                            byte[] rawData = tcpPacket.getPayload().getRawData();
                            // Analyse basique pour extraire le SNI ou d'autres métadonnées.
                            System.out.println("Données TLS brutes : " + Arrays.toString(rawData));
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
