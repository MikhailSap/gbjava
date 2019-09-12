import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String SERVER_IP = "localhost";
    private final int PORT = 5000;
    private Socket socket;
    private DataInputStream receive;
    private DataOutputStream send;
    private Scanner scanner = new Scanner(System.in);
    private Thread threadForSend;

    public Client() {
        try {
            connect();
            threadForSend.join();
            System.out.println("Client is closed");
            send.close();
            receive.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() throws IOException {
        socket = new Socket(SERVER_IP, PORT);
        receive = new DataInputStream(socket.getInputStream());
        send = new DataOutputStream(socket.getOutputStream());
        threadForSend = new Thread(() -> {
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
        threadForSend.start();
        receive();
    }

    private void receive() throws IOException{
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
        new Client();
    }
}
