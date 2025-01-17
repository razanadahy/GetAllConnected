import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutreQueAutre {
    public static void main(String[] args) {
        try {
            // Liste des interfaces réseau disponibles
            PcapNetworkInterface device = Pcaps.findAllDevs().get(0); // Choisissez la première interface

            if (device == null) {
                System.out.println("Aucune interface réseau trouvée.");
                return;
            }

            System.out.println("Capturing on: " + device.getName());

            int snapLen = 65536; // Taille maximale d'un paquet à capturer
            PcapHandle handle = device.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 50);

            handle.loop(10, (PacketListener) packet -> { // Capture 10 paquets pour l'exemple
                if (packet.contains(IpV4Packet.class)) {
                    String payload = packet.getPayload().toString();
                    // Regex pour extraire les URLs
                    Pattern pattern = Pattern.compile("https?://[\\w/._-]+");
                    Matcher matcher = pattern.matcher(payload);

                    while (matcher.find()) {
                        System.out.println("URL détectée: " + matcher.group());
                    }
                }
            });

            handle.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
