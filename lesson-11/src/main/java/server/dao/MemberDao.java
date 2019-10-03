package server.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import server.Member;
import server.util.HibernateSessionFactoryUtil;

import java.util.List;

public class MemberDao {
    private static MemberDao memberDao;

    private MemberDao() {
    }

    public static MemberDao getMemberDao() {
        if (memberDao == null)
            memberDao = new MemberDao();
        return memberDao;
    }

    public Member getMemberData(String login, String password) {
        NativeQuery nativeQuery = HibernateSessionFactoryUtil.getSessionFactory().openSession().createSQLQuery("SELECT * FROM members where login = '"+ login +"' AND password = '"+ password +"';");
        nativeQuery.addEntity(Member.class);
        List<Member> members = nativeQuery.list();
        if (members.get(0) != null) {
            return members.get(0);
        }
        return null;
    }

    public void update(Member member) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(member);
        tx1.commit();
        session.close();
    }
}
