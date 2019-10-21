package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.dao.MemberDao;

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
    private static final Logger LOGGER = LogManager.getLogger();


    public void go() {
        online = new ConcurrentLinkedQueue<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server Started.");
            while (true) {
                Socket connection = serverSocket.accept();
                LOGGER.info("New connection.");
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
            LOGGER.error(e);
        }
    }

    private synchronized void sendPublicMessage(String message, Member from) {
        for (Member member : online) {
            try {
                member.getOut().writeUTF(from.getNick() + ": " + message);
            } catch (IOException e) {
                LOGGER.error(e);
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
                    LOGGER.error(e);
                }
        }
        try {
            from.getOut().writeUTF(nick + " is not online");
        } catch (IOException e) {
            LOGGER.error(e);
        }

    }

    private synchronized void sendRequestForDisconnect(Member member) {
        try {
            member.getOut().writeUTF("quit");
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private synchronized void changeNick(String newNick, Member member) {
        LOGGER.info(member.getNick() + " change nick to " + newNick);
        member.setNick(newNick);
        MemberDao.getMemberDao().update(member);
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
            Thread authTime = new Thread(() -> {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    return;
                }
                if (!authOk) {
                    sendForAuth(out, "/timeout");
                    LOGGER.warn("Timeout from user.");
                }
            });
            authTime.start();
            sendForAuth(out, "enter your login and pass with space");
            String authData;
            while (true) {
                authData = in.readUTF();
                if (authData.equals("/timeout"))
                    break;
                if (authData.equals("/end")) {
                    member = new Member();
                    member.setOut(out);
                    sendRequestForDisconnect(member);
                    authTime.interrupt();
                    LOGGER.warn("Disconnect from user.");
                    break;
                }
                String[] parts = authData.split("\\s");
                member = authService.checkAuthData(parts[0], parts[1]);
                if (member != null) {
                    if (member.isOnline()) {
                        sendForAuth(out, "is busy");
                        LOGGER.error("User is busy.");
                        continue;
                    }
                    member.setIn(in);
                    member.setOut(out);
                    member.setOnline(true);
                    addToOnline(member);
                    sendForAuth(out, member.getNick() + " /authok");
                    sendPublicMessage("is connected", member);
                    LOGGER.info(member.getNick() + " is connected.");
                    authOk = true;
                    return true;
                } else {
                    sendForAuth(out, "login or password incorrect");
                    LOGGER.error("Login or password incorrect, has been provided: login - " + parts[0] + " password - " + parts[1]);
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
                        LOGGER.info(member.getNick() + " is disconnected.");
                       break;
                    }
                    if (content.startsWith("/rn")) {
                        String[] parts = content.split("\\s");
                        String newNick = parts[1];
                        sendPublicMessage("changed nick to " + newNick, member);
                        changeNick(newNick, member);
                    }
                    else if (content.startsWith("/w")) {
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
                LOGGER.info("Connection is closed.");
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
