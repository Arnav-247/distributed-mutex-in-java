import java.nio.ByteBuffer;

class MessageType {
    static final int REQUEST = 0;
    static final int REPLY = 1;
    static final int RELEASE = 2;
}

public class Message implements Comparable<Message>{
    long timestamp;
    int type = MessageType.RELEASE;
    int port;

    
    public Message(long timestamp, int type, int port) {
        this.timestamp = timestamp;
        this.type = type;
        this.port = port;
    }
    public Message(byte[] buf)
    {
        ByteBuffer bb = ByteBuffer.wrap(buf);
        this.timestamp = bb.getLong();
        this.type = bb.getInt();
        this.port = bb.getInt();
    }

    @Override
    public int compareTo(Message o) {
        return (this.timestamp - o.timestamp > 0) ? 1 : -1;
    }
    
    public byte[] toBytes()
    {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putLong(timestamp);
        buf.putInt(type);
        buf.putInt(port);
        return buf.array();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Message)
        {
            Message m = (Message)obj;
            if (m.port == this.port)
                return true;
        }
        return false;
    }

    

}
