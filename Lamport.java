import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

public class Lamport
{
    public static void main(String[] args) {
        PriorityBlockingQueue<Message> pbq = new PriorityBlockingQueue<>();
        int idx = Integer.parseInt(args[0]);
        int[] ports = {5001, 5002, 5003};
        int port = ports[idx];
        int[] ports2 = new int[ports.length-1];
        for(int i = 0, j = 0; i<ports.length; i++)
        {
            if(port == ports[i])
                continue;
            ports2[j] = ports[i];
            j++;
        }   
        
        Server server = new Server(pbq,ports,idx);
        server.run();
        Client client = new Client(pbq,ports,idx);
        client.start();
    }   
}