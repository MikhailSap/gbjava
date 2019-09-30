package server;

import java.sql.*;

public class DBService{
    private static DBService service;

    private DBService() {
    }

    public static DBService getService() {
        if (service == null)
            service = new DBService();
        return service;
    }

    private Connection getConnection() {
        String url = "jdbc:postgresql://127.0.0.1:5432/gbchat";
        String name = "mikhail";
        String password = "123";
        Connection connection =null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet getMemberData(String login, String pass) {
        ResultSet result = null;
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM members where login = ? AND password = ?;");
            statement.setString(1, login);
            statement.setString(2, pass);
            result = statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateNick(String newNick, int id) {
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE members SET nick = ? where id_member = ?;");
            statement.setString(1, newNick);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
