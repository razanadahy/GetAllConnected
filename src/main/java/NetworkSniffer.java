import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.util.NifSelector;

import java.io.EOFException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkSniffer {

//    public static void main(String[] args) {
//        try {
//            // Liste des interfaces réseau disponibles
//            System.out.println("Listing available network devices...");
//            for (PcapNetworkInterface device : Pcaps.findAllDevs()) {
//                System.out.println(device.getName() + " - " + device.getDescription());
//            }
//
//            // Choisir la première interface réseau
//            PcapNetworkInterface nif = Pcaps.findAllDevs().get(0);
//            if (nif == null) {
//                System.err.println("No network interface found.");
//                return;
//            }
//
//            System.out.println("Using network interface: " + nif.getName());
//
//            // Ouvrir le handle pour capturer les paquets
//            PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
//
//            System.out.println("Listening for packets...");
//
//            // Capture et affichage des paquets
//            PacketListener listener = packet -> System.out.println(packet);
//            handle.loop(-1, listener); // -1 pour capturer en continu
//
//            handle.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        try {
            // Sélectionne une interface réseau
            PcapNetworkInterface nif = new NifSelector().selectNetworkInterface();
            if (nif == null) {
                System.out.println("Aucune interface sélectionnée.");
                return;
            }

            System.out.println("Interface sélectionnée : " + nif.getName());

            // Ouvre un handle pour l'écoute
            int snapLen = 65536; // Longueur maximale des paquets capturés
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS; // Capture en mode promiscuous
            int timeout = 10; // Timeout en millisecondes
            PcapHandle handle = nif.openLive(snapLen, mode, timeout);

            System.out.println("Capture en cours...");

            // Capture les paquets en boucle
            handle.loop(-1, (PacketListener) packet -> {
                // Vérifie si c'est un paquet TCP
                if (packet.contains(TcpPacket.class)) {
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);
                    String payload = tcpPacket.getPayload() != null ? tcpPacket.getPayload().toString() : "";

                    // Recherche les URLs dans le contenu
                    Pattern urlPattern = Pattern.compile("http[s]?://[\\w.-]+(:\\d+)?(/[\\w./-]*)?");
                    Matcher matcher = urlPattern.matcher(payload);

                    while (matcher.find()) {
                        System.out.println("URL détectée : " + matcher.group());
                    }
                }
            });

        } catch (PcapNativeException | EOFException | InterruptedException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (IOException | NotOpenException e) {
            e.printStackTrace();
        }
    }
}
