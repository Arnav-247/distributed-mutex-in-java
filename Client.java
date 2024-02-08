import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;

public class Client {
    
    PriorityBlockingQueue<Message> pbq;
    DatagramSocket socket;
    boolean waiting = false;
    InetAddress addr;
    int[] ports;
    int idx;
    public Client(PriorityBlockingQueue<Message> pbq, int[] ports, int idx) {
        this.pbq = pbq;
        this.ports = ports;
        this.idx = idx;
        try {
            addr = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            addr = null;
            e.printStackTrace();
        }
    }

    void start()
    {
        //wait random
        //request cs
        //execute cs when can
        //release
        //idle
    }

    void requestCS()
    {
        waiting = true;
        Message m = new Message(System.currentTimeMillis(), MessageType.REQUEST, ports[idx]);
        byte[] mbytes = m.toBytes();
        pbq.add(m);
        for(int i = 0; i< ports.length; i++)
        {
            if(i == idx)
                continue;
            DatagramPacket dp = new DatagramPacket( mbytes,mbytes.length,addr,ports[i]);
            try {
                socket.send(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
        while(waiting)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
