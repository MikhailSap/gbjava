package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final int PORT = 5000;
    BaseAuthService authService = new BaseAuthService();
    private List<Member> online;


    public void go() {
        online = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket connection = serverSocket.accept();
                new ClientHandler(connection).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendForAuth(DataOutputStream out, String answer) {
        try {
            out.writeUTF(answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendPublicMessage(String message, Member from) {
        for (Member member : online) {
            try {
                member.getOut().writeUTF(from.getNick() + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private synchronized void sendPrivateMessage(String nick, String message, Member from) {
        for (Member member : online) {
            if (member.getNick().equals(nick))
                try {
                    member.getOut().writeUTF("Private from " + from.getNick() + ": " + message);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        try {
            from.getOut().writeUTF(nick + " is not online");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized void sendRequestForDisconnect(Member member) {
        try {
            member.getOut().writeUTF("quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addToOnline(Member member) {
        online.add(member);
    }

    private synchronized void removeFromOnline(Member member) {
        online.remove(member);
    }

    private class ClientHandler extends Thread {
        Socket socket;
        DataInputStream in;
        DataOutputStream out;
        Member member;


        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                if (!authentication()) {
                    closeConnection();
                    return;
                }
                readMessages();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                member.setOnline(false);
                removeFromOnline(member);
                closeConnection();
            }
        }

        private boolean authentication() throws IOException{
            String authData;
            while (true) {
                authData = in.readUTF();
                String[] parts = authData.split("\\s");
                member = authService.checkAuthData(parts[0], parts[1]);
                if (member != null) {
                    if (member.isOnline()) {
                        sendForAuth(out, "is busy" + "\n");
                        continue;
                    }
                    member.setIn(in);
                    member.setOut(out);
                    member.setOnline(true);
                    addToOnline(member);
                    sendForAuth(out, "/authok");
                    sendPublicMessage("is connected", member);
                    return true;
                } else {
                    sendForAuth(out, "login or password incorrect");
                }
            }
        }

        private void readMessages() throws IOException{
            while (true) {
                    String message = in.readUTF();
                    if (message.equals("/end")) {
                        sendPublicMessage("is disconnected", member);
                        sendRequestForDisconnect(member);
                        member.setOnline(false);
                       break;
                    }
                    if (message.startsWith("/w")) {
                        String[] parts = message.split("\\s");
                        sendPrivateMessage(parts[1], parts[2], member);
                    }
                    else
                        sendPublicMessage(message, member);
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
    }
}
