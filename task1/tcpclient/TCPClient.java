package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    public TCPClient() {}


    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        if(port <= 0)
            throw new IllegalArgumentException("Wrong Portnumber");
        int bufferSize = 10;
        try{
            Socket socket = new Socket(hostname, port);
            byte [] serverBytes = new byte[bufferSize];
            socket.getOutputStream().write(toServerBytes);
            ByteArrayOutputStream data = new ByteArrayOutputStream();

            int length = 0;
            while(length != -1)
            {
                length = socket.getInputStream().read(serverBytes);
                for(int i = 0; i < length; i++)
                data.write(serverBytes[i]);
            }
            data.close();
            socket.close();

            return data.toByteArray();
        }
        catch(Exception e){
            throw new IOException("couldnt execute" + e);
        }
    }
}

