import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Maekawa
{
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Message> clq = new ConcurrentLinkedQueue<>();
        AtomicBoolean waiting = new AtomicBoolean();
        int[] possiblePorts = {5000, 5001, 5002, 5003, 5004};
        int[] ports = new int[args.length];
        for (int i = 0; i< args.length; i++) {
            ports[i] = possiblePorts[Integer.parseInt(args[i])];
        }
        Server server = new Server(clq,waiting,ports,0);
        server.start();

        Client client = new Client(waiting,ports,0);
        client.start();
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }   
}
