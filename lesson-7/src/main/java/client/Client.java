package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String SERVER_IP = "localhost";
    private final int PORT = 5000;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    JTextArea incoming;
    private Thread threadForRead;
    private boolean isConnect;


    public Client() {
        try {
            connect();
            go();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException{
        socket = new Socket(SERVER_IP, PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void authentication() {
        String strFromServer;
        try {
            while (true) {
                strFromServer = in.readUTF();
                if (strFromServer.equals("/authok")) {
                    isConnect = true;
                    break;
                }
                incoming.append(strFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void go() {
        JFrame frame = new JFrame("Chat Client");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 400;
        int height = 350;
        frame.setSize(width, height);
        int startX = dimension.width / 2 - width / 2;
        int startY = dimension.height / 2 - height / 2;
        frame.setLocation(startX, startY);
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 25);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(incoming);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JTextField outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                try {
                    out.writeUTF(outgoing.getText());
                    outgoing.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MouseListener mouseListener = sendButton.getMouseListeners()[0];
        sendButton.removeMouseListener(mouseListener);
        mainPanel.add(scrollPane);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        threadForRead = new Thread(() -> {
            authentication();
            String message;
            while (true)
            try {
                message = in.readUTF();
                if (message.equals("quit")) {
                    closeConnection();
                    isConnect = false;
                    break;
                }
                incoming.append(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        threadForRead.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                super.windowClosing(event);
                if (isConnect)
                try {
                    out.writeUTF("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                closeConnection();
                System.exit(0);
            }
        });

        frame.add(BorderLayout.CENTER, mainPanel);
        frame.setVisible(true);
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Client();
    }
}
