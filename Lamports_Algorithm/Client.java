import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    
    PriorityBlockingQueue<Message> pbq;
    AtomicBoolean waiting;

    DatagramSocket socket;
    InetAddress addr;
    int[] ports;
    int idx;
    public Client(PriorityBlockingQueue<Message> pbq, AtomicBoolean waiting, int[] ports, int idx) {
        this.pbq = pbq;
        this.waiting = waiting;
        this.ports = ports;
        this.idx = idx;
        try {
            socket = new DatagramSocket();
            addr = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            addr = null;
            e.printStackTrace();
        } catch (SocketException e) {
            socket = null;
            e.printStackTrace();
        }
    }

    void start()
    {
        Logger.log("Client is running");
        try {
            Thread.sleep(5000 + (new Random()).nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestCS();
        //execute cs when can
        try {
            Thread.sleep(1500 + (new Random()).nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //release
        releaseCS();
        //idle
    }

    private void releaseCS() {
        Logger.log("Releasing CS");
        Message releaseMsg = new Message(System.currentTimeMillis(), MessageType.RELEASE, ports[idx]);
        pbq.remove(releaseMsg);
        byte[] mbytes = releaseMsg.toBytes();
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
    }

    void requestCS()
    {
        Logger.log( "Requesting CS...");
        waiting.set(true);
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
        
            while(waiting.get())
            {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        
        Logger.log("In CS...");

        
    }
    
}
