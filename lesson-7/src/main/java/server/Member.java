package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Member {
    private String login;
    private String pass;
    private String nick;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isOnline;

    public Member(String login, String pass, String nick) {
        this.login = login;
        this.pass = pass;
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
