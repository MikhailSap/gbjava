package server;

import java.util.ArrayList;
import java.util.List;

public class ChatMembers {
    List<Member> members = new ArrayList<>();

    public ChatMembers() {
        members.add(new Member("login1", "pass1", "nick1"));
        members.add(new Member("login2", "pass2", "nick2"));
        members.add(new Member("login3", "pass3", "nick3"));
    }

    public List<Member> getMembers() {
        return members;
    }

    public Member getMemberByNick(String nick) {
        for (Member member : members) {
            if (member.getNick().equals(nick))
                return member;
        }
        return null;
    }
}
