import java.util.concurrent.PriorityBlockingQueue;

public class Lamport
{
    public static void main(String[] args) {
        PriorityBlockingQueue<Message> pbq = new PriorityBlockingQueue<>();
        int idx = Integer.parseInt(args[0]);
        int[] ports = {5000,5001,5002};
        Server server = new Server(pbq,ports,idx);
        server.run();
        Client client = new Client(pbq,ports,idx);
        client.start();
    }   
}