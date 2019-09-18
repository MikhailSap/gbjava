package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private final int PORT = 5000;
    BaseAuthService authService = new BaseAuthService();
    private ConcurrentLinkedQueue<Member> online;


    public void go() {
        online = new ConcurrentLinkedQueue<>();
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
                    member.getOut().writeUTF("private from " + from.getNick() + ":" + message);
                    from.getOut().writeUTF("private to " + nick + ":" + message);
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
        boolean authOk;


        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                if (!authentication()) {
                    return;
                }
                readMessages();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (member != null) {
                    member.setOnline(false);
                    removeFromOnline(member);
                }
                closeConnection();
            }
        }

        private boolean authentication() throws IOException{
            new Thread(() -> {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!authOk)
                sendForAuth(out, "/timeout");
            }).start();
            sendForAuth(out, "enter your login and pass with space");
            String authData;
            while (true) {
                authData = in.readUTF();
                if (authData.equals("/timeout")) {
                    break;
                }
                String[] parts = authData.split("\\s");
                member = authService.checkAuthData(parts[0], parts[1]);
                if (member != null) {
                    if (member.isOnline()) {
                        sendForAuth(out, "is busy");
                        continue;
                    }
                    member.setIn(in);
                    member.setOut(out);
                    member.setOnline(true);
                    addToOnline(member);
                    sendForAuth(out, "/authok");
                    sendPublicMessage("is connected", member);
                    authOk = true;
                    return true;
                } else {
                    sendForAuth(out, "login or password incorrect");
                }
            }
            return false;
        }

        private void readMessages() throws IOException{
            while (true) {
                    String content = in.readUTF();
                    if (content.equals("/end")) {
                        sendPublicMessage("is disconnected", member);
                        sendRequestForDisconnect(member);
                        member.setOnline(false);
                       break;
                    }
                    if (content.startsWith("/w")) {
                        String[] parts = content.split("\\s");
                        String nick = parts[1];
                        int prefix = "/w ".length() + nick.length();
                        String message = content.substring(prefix);
                        sendPrivateMessage(nick, message, member);
                    }
                    else
                        sendPublicMessage(content, member);
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
