package server;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BaseAuthService implements AuthService{
    private ConcurrentLinkedQueue<Member> members = new ChatMembers().getMembers();

    @Override
    public Member checkAuthData(String login, String pass) {
        for (Member member : members) {
            if (member.getLogin().equals(login) && member.getPass().equals(pass))
                return member;
        }
        return null;
    }
}
