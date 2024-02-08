import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.PriorityBlockingQueue;

public class Server extends Thread {

    PriorityBlockingQueue<Message> pbq ;
    DatagramSocket socket;
    int[] ports;
    int idx;
    public Server(PriorityBlockingQueue<Message> pbq, int[] ports, int idx) 
    {
        this.pbq = pbq;
        this.ports = ports;
        this.idx = idx;
        try {
            socket = new DatagramSocket(ports[idx]);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(socket == null)
            return;
    }
    
    
}
