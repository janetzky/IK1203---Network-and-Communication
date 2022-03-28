import tcpclient.TCPClient;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyRunnable implements Runnable {
    Socket clientSocket;

    public MyRunnable(Socket socket){
        this.clientSocket = socket;
    }

        private static int BUFFERSIZE = 1024;

        public void run() {

            //ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

            String HTTP200 = "HTTP/1.1 200 OK\r\n\r\n";
            String HTTP404 = "HTTP/1.1 404 Not Found\r\n";
            String HTTP400 = "HTTP/1.1 400 Bad Request\r\n";

                // Kopplar upp socketen
                //Socket clientSocket = serverSocket.accept();

            try {
                InputStream input = clientSocket.getInputStream();
            //byte[] fromClientBytes = "".getBytes(StandardCharsets.UTF_8);
                byte[] fromClientBuffer = new byte[BUFFERSIZE];

                ByteArrayOutputStream data = new ByteArrayOutputStream();
                OutputStream output = clientSocket.getOutputStream();
                int fromClientLength = input.read(fromClientBuffer);

                while (fromClientLength != -1) {
                    data.write(fromClientBuffer, 0, fromClientLength);
                    if(new String(fromClientBuffer).contains("HTTP/1.1"))
                        break;
                }

                String serverOutput = new String(data.toByteArray());


                String hostname = null;
                String toServer = "";
                Integer limit = null;
                Integer port = 0;
                Integer timeout = 0;
                boolean shutdown = false;
                boolean correct_http = false;


                if(!serverOutput.contains("/favicon.ico")) {
                    String[] extraction = serverOutput.split("[?&= ]");
                    for (int i = 0; i < extraction.length; i++) {
                        if (extraction[i].equals("hostname")) {
                            hostname = extraction[++i];
                        } if (extraction[i].equals("port")) {
                            port = Integer.parseInt(extraction[++i]);
                        } if (extraction[i].equals("string")) {
                            toServer = extraction[++i];
                        } if (extraction[i].equals("limit")) {
                            limit = Integer.parseInt(extraction[++i]);
                        } if (extraction[i].equals("timeout")) {
                            timeout = Integer.parseInt(extraction[++i]);
                        } if (extraction[i].equals("shutdown")) {
                            shutdown = (extraction[++i]).equals("true");
                        } if (extraction[i].contains("HTTP/1.1")) {
                            correct_http = true;
                        }
                    }

                    //
                    if(extraction[0].equals("GET") && extraction[1].equals("/ask") && hostname != null && port != 0 && correct_http){
                        try {

                            TCPClient tcpClient = new tcpclient.TCPClient(shutdown, timeout, limit);
                            byte[] toServerBytes = toServer.getBytes(StandardCharsets.UTF_8);
                            byte[] serverBytes  = tcpClient.askServer(hostname, port, toServerBytes);
                            String test1 = new String(serverBytes);
                            //System.out.printf("%s:%d says:\n%s", hostname, port, test1);

                            output.write(HTTP200.getBytes(StandardCharsets.UTF_8));
                            output.write(test1.getBytes(StandardCharsets.UTF_8));

                        } catch(IOException ex) {
                            // Sending error message that the site cant be found
                            output.write(HTTP404.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                    else{
                        // Sending error message 400 for bad request.
                        output.write(HTTP400.getBytes(StandardCharsets.UTF_8));
                    }
                    output.flush();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

  // private HTTPAsk httpAsk;

  // public void run() {
  //     try {
  //         httpAsk.run();
  //     } catch (IOException e) {
  //         e.printStackTrace();
  //     }
  // }

