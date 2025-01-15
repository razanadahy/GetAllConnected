import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

public class NetworkSniffer {

    public static void main(String[] args) {
        try {
            // Liste des interfaces réseau disponibles
            System.out.println("Listing available network devices...");
            for (PcapNetworkInterface device : Pcaps.findAllDevs()) {
                System.out.println(device.getName() + " - " + device.getDescription());
            }

            // Choisir la première interface réseau
            PcapNetworkInterface nif = Pcaps.findAllDevs().get(0);
            if (nif == null) {
                System.err.println("No network interface found.");
                return;
            }

            System.out.println("Using network interface: " + nif.getName());

            // Ouvrir le handle pour capturer les paquets
            PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);

            System.out.println("Listening for packets...");

            // Capture et affichage des paquets
            PacketListener listener = packet -> System.out.println(packet);
            handle.loop(-1, listener); // -1 pour capturer en continu

            handle.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
