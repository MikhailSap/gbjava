package server;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChatMembers {
    ConcurrentLinkedQueue<Member> members = new ConcurrentLinkedQueue<>();

    public ChatMembers() {
        members.add(new Member("login1", "pass1", "nick1"));
        members.add(new Member("login2", "pass2", "nick2"));
        members.add(new Member("login3", "pass3", "nick3"));
    }

    public ConcurrentLinkedQueue<Member> getMembers() {
        return members;
    }
}
