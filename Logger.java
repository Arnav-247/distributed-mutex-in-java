import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
    public static void log(String msg)
    {
        System.out.println(sdf.format(new Date(System.currentTimeMillis())) + " : "+msg);
    }
}
