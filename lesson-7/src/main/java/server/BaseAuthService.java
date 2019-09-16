package server;

import java.util.List;

public class BaseAuthService implements AuthService{
    private List<Member> members = new ChatMembers().getMembers();

    @Override
    public Member checkAuthData(String login, String pass) {
        for (Member member : members) {
            if (member.getLogin().equals(login) && member.getPass().equals(pass))
                return member;
        }
        return null;
    }
}
