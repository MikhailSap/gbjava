package server;

import server.dao.MemberDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseAuthService implements AuthService {

    public Member checkAuthData(String login, String pass) {
        return MemberDao.getMemberDao().getMemberData(login, pass);
//        Member member = new Member();
//        ResultSet response = DBService.getService().getMemberData(login, pass);
//        try {
//            response.next();
//            if (response.getRow() == 0)
//                return null;
//            member.setId_member(response.getInt("id_member"));
//            member.setNick(response.getString("nick"));
//        } catch (SQLException e ) {
//            e.printStackTrace();
//        }
//        return member;
    }
}
