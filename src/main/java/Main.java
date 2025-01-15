import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Liste les interfaces réseau disponibles
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("Aucune interface réseau trouvée.");
                return;
            }

            // Affiche les interfaces disponibles
            System.out.println("Interfaces réseau disponibles :");
            for (int i = 0; i < interfaces.size(); i++) {
                System.out.println(i + ": " + interfaces.get(i).getName() + " (" + interfaces.get(i).getDescription() + ")");
            }

            // Sélectionne la première interface
            PcapNetworkInterface nif = interfaces.get(0);
            System.out.println("Utilisation de l'interface : " + nif.getName());

            // Configure le handle pour capturer les paquets
            int snapshotLength = 65536; // Taille max d'un paquet
            int timeout = 10; // Timeout en ms
            PcapHandle handle = nif.openLive(snapshotLength, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

            System.out.println("Capture des paquets...");

            // Capture les paquets dans une boucle
            handle.loop(0, (PacketListener) packet -> {
                System.out.println("Paquet capturé !");
                parsePacket(packet);
            });

            // Ferme le handle après la capture
            handle.close();
        } catch (NotOpenException | InterruptedException | PcapNativeException e) {
            e.printStackTrace();
        }
    }

    private static void parsePacket(Packet packet) {
        // Affiche les informations de base du paquet
        System.out.println("Détails du paquet : " + packet);

        // Exemple : Analyse des paquets TCP
        if (packet.contains(TcpPacket.class)) {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            System.out.println("Paquet TCP détecté.");
            System.out.println("Port source : " + tcpPacket.getHeader().getSrcPort());
            System.out.println("Port destination : " + tcpPacket.getHeader().getDstPort());
        }

        // Exemple : Analyse des paquets UDP
        if (packet.contains(UdpPacket.class)) {
            UdpPacket udpPacket = packet.get(UdpPacket.class);
            System.out.println("Paquet UDP détecté.");
            System.out.println("Port source : " + udpPacket.getHeader().getSrcPort());
            System.out.println("Port destination : " + udpPacket.getHeader().getDstPort());
        }

        // TODO : Ajouter l'analyse HTTP/HTTPS et extraire les URLs
    }
}
