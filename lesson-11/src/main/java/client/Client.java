package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class Client {
    private final String SERVER_IP = "localhost";
    private final int PORT = 5000;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    JTextArea incoming;
    private Thread threadForRead;
    private boolean isConnect;
    private boolean isAuth = true;
    private String nick;
    private String historyFile;
    private int countLoadLines;


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
                if (strFromServer.equals("quit"))
                    break;
                if (strFromServer.contains("/authok")) {
                    nick = strFromServer.substring(0, strFromServer.indexOf(" "));
                    historyFile = nick + ".mgs";
                    isConnect = true;
                    isAuth = false;
                    break;
                } else if (strFromServer.equals("/timeout")) {
                    incoming.append("you have been kicked from server by timeout" + "\n");
                    out.writeUTF(strFromServer);
                    isAuth = false;
                    break;
                }
                else
                    incoming.append(strFromServer + "\n");
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
                    if (isConnect || isAuth)
                    out.writeUTF(outgoing.getText());
                    outgoing.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mainPanel.add(scrollPane);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        threadForRead = new Thread(() -> {
            authentication();
            loadMessageHistory();
            String message;
            while (true)
            try {
                if (!isConnect) {
                    closeConnection();
                    break;
                }
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
                //if (isConnect)
                try {
                    out.writeUTF("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    saveMessageHistory();
                    closeConnection();
                }
                System.exit(0);
            }
        });

        frame.add(BorderLayout.CENTER, mainPanel);
        frame.setVisible(true);
    }

    private void saveMessageHistory() {
        Path pathTmp = null;
        if (historyFile == null)
            return;
        Path pathHistory = Paths.get(historyFile);
        try {
            pathTmp = Files.createTempFile("Messages-", ".tmp");
            if (!Files.exists(pathHistory))
                Files.createFile(pathHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileChannel source = new FileInputStream(historyFile).getChannel();
             FileChannel dest = new FileOutputStream(pathTmp.toFile().getName()).getChannel()) {
            dest.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new FileWriter(historyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stack<String> messages = new Stack<>();
        String[] lines = incoming.getText().split("\n");
        for (int i = countLoadLines; i < lines.length; i++)
            messages.add(lines[i]);
        try (BufferedReader reader = new BufferedReader(new FileReader(pathTmp.toFile().getName()));
             BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile,true))) {
            Iterator iterator = messages.iterator();
            while (iterator.hasNext())
                writer.write(messages.pop()+"\n");

            reader.lines().forEach(s -> {
                try {
                    writer.write(s+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void loadMessageHistory() {
        int countMessagesForLoad = 100;
        incoming.setText("");
        Path pathHistory = Paths.get(historyFile);
        Stack<String> messages = new Stack<>();
        if (Files.exists(pathHistory)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pathHistory.toFile()));) {
                for (int i = 0; i < countMessagesForLoad; i++) {
                    if (reader.ready())
                        messages.add(reader.readLine());
                    else break;
                }
                countLoadLines = messages.size();
                Iterator iterator = messages.iterator();
                while (iterator.hasNext()) {
                    incoming.append(messages.pop()+"\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
