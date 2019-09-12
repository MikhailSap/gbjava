
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private Socket connection;
    private DataInputStream receive;
    private DataOutputStream send;
    private Scanner scanner = new Scanner(System.in);
    private Thread thredForSend;

    public Server() {
        try {
            openConnect();
            thredForSend.join();
            System.out.println("Server is closed");
            send.close();
            receive.close();
            connection.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openConnect() throws IOException{
        serverSocket = new ServerSocket(PORT);
        connection = serverSocket.accept();
        receive = new DataInputStream(connection.getInputStream());
        send = new DataOutputStream(connection.getOutputStream());
        thredForSend = new Thread(() -> {
            String content;
            while (true) {
                content = scanner.nextLine();
                if (content.equals("quit")) {
                    try {
                        send.writeUTF(content);
                        System.out.println("send is closing..");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    send.writeUTF(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thredForSend.start();
        receive();
    }

    public void receive() throws IOException {
        String content;
        while (true) {
            content = receive.readUTF();
            if (content.equals("quit")) {
                System.out.println("receive is closing..");
                break;
            }
            System.out.println(content);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
