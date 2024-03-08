import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Lamport
{
    public static void main(String[] args) {
        PriorityBlockingQueue<Message> pbq = new PriorityBlockingQueue<>();
        AtomicBoolean waiting = new AtomicBoolean();
        int idx = Integer.parseInt(args[0]);
        int[] ports = {5001, 5002, 5003}; //
        
        Server server = new Server(pbq,waiting,ports,idx);
        server.start();

        Client client = new Client(pbq,waiting,ports,idx);
        client.start();
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }   
}
