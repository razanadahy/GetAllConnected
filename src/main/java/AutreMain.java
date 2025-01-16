import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.namednumber.TcpPort;
import org.pcap4j.util.NifSelector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutreMain {
    public static void main(String[] args) {
        try {
            PcapNetworkInterface nif = new NifSelector().selectNetworkInterface();
            if (nif == null) {
                System.out.println("Aucune interface sélectionnée.");
                return;
            }

            System.out.println("Interface sélectionnée : " + nif.getName());

            PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);

            System.out.println("Capture en cours...");
            handle.loop(-1, (PacketListener) packet -> {
                if (packet.contains(TcpPacket.class)) {
                    TcpPacket tcpPacket = packet.get(TcpPacket.class);

                    // Filtrer les paquets HTTP (port 80)
                    if (tcpPacket.getHeader().getDstPort().equals(TcpPort.HTTP)) {
                        String payload = tcpPacket.getPayload() != null ? tcpPacket.getPayload().toString() : "";

                        // Rechercher les URLs dans le contenu HTTP
                        Pattern urlPattern = Pattern.compile("GET (.*?) HTTP");
                        Matcher matcher = urlPattern.matcher(payload);

                        while (matcher.find()) {
                            System.out.println("URL détectée : " + matcher.group(1));
                        }
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
