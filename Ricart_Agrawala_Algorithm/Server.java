import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class Server extends Thread {

    AtomicBoolean waiting, inCS;
    AtomicLong csTimestamp;
    List<Message> deferred;

    DatagramSocket socket;
    InetAddress addr;
    int[] ports;
    int idx;
    
    boolean running;
    byte[] buf = new byte[16];
    HashMap<Integer, Boolean> replyCheck;
    

    Message req;
    public Server(List<Message> deferred, AtomicBoolean inCS, AtomicBoolean waiting, AtomicLong csTimestamp, int[] ports, int idx) 
    {
        this.deferred = deferred;
        this.inCS = inCS;
        this.waiting = waiting;
        this.csTimestamp = csTimestamp;
        this.ports = ports;
        this.idx = idx;
        running = false;
        req = new Message(System.currentTimeMillis(), MessageType.REQUEST, ports[idx]);
        replyCheck = new HashMap<>();
        
        try {
            addr = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } try {
            socket = new DatagramSocket(ports[idx]);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(socket == null)
            return;
        Logger.log("Server is running....");
        running = true;
        while(running)
        {
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(dp);
                Message recvMessage = new Message(buf);
                // Logger.log("Received: "+recvMessage.toString());
                if(recvMessage.type == MessageType.REQUEST)
                {
                    // Logger.log("Q: " + pbq.toString());
                    if((!waiting.get() && !inCS.get()) || (waiting.get() && recvMessage.timestamp < csTimestamp.get()))
                        sendReply(recvMessage.port);
                    else
                    {
                        deferred.add(recvMessage);
                    }
                }
                else if(recvMessage.type == MessageType.REPLY)
                {
                    replyCheck.put(recvMessage.port, true);
                    updateWaiting();
                }
            } catch (IOException e) {
                e.printStackTrace();
                buf = null;
            }
            
            
        }
        socket.close();
    }

    private void sendReply(int replyPort) {
        Message m = new Message(System.currentTimeMillis(), MessageType.REPLY, ports[idx]);
        byte[] mbytes = m.toBytes();
        DatagramPacket dp = new DatagramPacket(mbytes, mbytes.length,addr,replyPort);
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    void updateWaiting()
    {
        if(!waiting.get())
        {
            return;
        }
        for(int i = 0; i<ports.length; i++)        
        {
            if (ports[i] == ports[idx])
                continue;
            if(replyCheck.containsKey(ports[i]) && replyCheck.get(ports[i]) != true || !replyCheck.containsKey(ports[i]) )
            {
                waiting.set(true);
                return;
            }
        }
        waiting.set(false);
        inCS.set(true);
        replyCheck.clear();
    }
    
    
}
