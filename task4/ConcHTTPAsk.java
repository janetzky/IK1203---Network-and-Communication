
import java.net.*;
import java.io.*;

     //  http://localhost:8888/ask?hostname=time.nist.gov&port=13
     //  http://localhost:8888/ask?hostname=java.lab.ssvl.kth.se&port=13
     //  http://localhost:8888/ask?hostname=whois.iis.se&port=43&string=kth.se
     //  http://localhost:8888/ask?hostname=java.lab.ssvl.kth.se&port=7&string=kth.se&shutdown=true
     //  http://localhost:8888/ask?hostname=java.lab.ssvl.kth.se&port=9&string=kth.se&shutdown=true
     //  http://localhost:8888/ask?hostname=java.lab.ssvl.kth.se&port=19&string=kth.se&shutdown=true&limit=500
public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException {
        try{
            int port = Integer.parseInt(args[0]);
            ServerSocket socket = new ServerSocket(port);
            while(true){
                Socket connectionSocket = socket.accept();
                MyRunnable run = new MyRunnable(connectionSocket);
                new Thread(run).start();
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
