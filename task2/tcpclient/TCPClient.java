package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    int SIZE = 1024;
    private final boolean shutdown;
    private final Integer timeout;
    private final Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(hostname, port);

            if(timeout != null){
                socket.connect(socketAddress, timeout);
                socket.setSoTimeout(timeout);
            }
            else{
                socket.connect(socketAddress);
            }

            ByteArrayOutputStream data = new ByteArrayOutputStream();
            socket.getOutputStream().write(toServerBytes);
        try{

            if(shutdown){socket.shutdownOutput(); }

            byte [] serverBytes = new byte[SIZE];
            int length = 0;

            if(limit != null){
                serverBytes = new byte[limit];
                while((socket.getInputStream().read(serverBytes, 0, limit)) !=-1){
                    data.write(serverBytes, 0, limit);
                    if(data.size() == limit){break;}}

            }
            else{
            while ((length = socket.getInputStream().read(serverBytes)) != -1) {
                    data.write(serverBytes, 0, length);
            }}

            data.close();
            socket.close();
        }
        catch(IOException e){System.out.println("" + e);}
        return data.toByteArray(); }
}

