import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class RicartAgrawala
{
    public static void main(String[] args) {
        AtomicBoolean waiting = new AtomicBoolean();
        AtomicBoolean inCS = new AtomicBoolean();
        AtomicLong csTimestamp = new AtomicLong();
        inCS.set(false);
        List<Message> deferred = Collections.synchronizedList(new ArrayList<>());
        int idx = Integer.parseInt(args[0]);
        int[] ports = {5001, 5002, 5003}; //
        
        Server server = new Server(deferred,inCS,waiting,csTimestamp,ports,idx);
        server.start();

        Client client = new Client(deferred,inCS,waiting,csTimestamp,ports,idx);
        client.start();
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }   
}
