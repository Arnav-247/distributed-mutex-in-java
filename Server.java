import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class Server extends Thread {

    PriorityBlockingQueue<Message> pbq ;
    DatagramSocket socket;
    int[] ports;
    int idx;
    boolean running;

    byte[] buf = new byte[16];
    // boolean[] replyCheck;
    HashMap<Integer, Boolean> replyCheck;
    public Server(PriorityBlockingQueue<Message> pbq, int[] ports, int idx) 
    {
        running = false;
        this.pbq = pbq;
        this.ports = ports;
        this.idx = idx;
        replyCheck = new HashMap<>();
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
        running = true;
        while(running)
        {
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(dp);
                Message m = new Message(buf);
                if(m.type == MessageType.REQUEST)
                {
                    pbq.add(m);
                    //TODO replyback
                }
                else if(m.type == MessageType.RELEASE)
                {
                    pbq.remove(m);
                }
                else if(m.type == MessageType.REPLY)
                {
                    replyCheck.putIfAbsent(m.type, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                buf = null;
            }
            
            
        }
    }
    
    
}
