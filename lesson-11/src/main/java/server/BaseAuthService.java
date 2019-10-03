package server;

import server.dao.MemberDao;

public class BaseAuthService implements AuthService {

    public Member checkAuthData(String login, String pass) {
        return MemberDao.getMemberDao().getMemberData(login, pass);
    }
}
