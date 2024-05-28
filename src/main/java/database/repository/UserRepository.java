package database.repository;

import database.config.DatabaseConfiguration;
import database.service.AuditService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserRepository {


    private static UserRepository instance;
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private UserRepository(){};

    AuditService auditService = AuditService.getInstance();

    public int login() {
        int tries = 4;

        while(tries > 0)
        {
            Scanner sc = new Scanner(System.in);

            System.out.println("Introduceti username-ul: ");
            String username = sc.nextLine();
            System.out.println("Introduceti password-ul: ");
            String password = sc.nextLine();

            int id = checkAccount(username, password);

            if (id >= 0) {
                auditService.writeToFile("log.csv", "S-a autentificat " + username);

                return id;
            }
            else
                tries--;

            if (tries > 0)
                System.out.println("User si parola incorecte, mai incercati o data.");
            else {
                System.out.println("deja de 4 ori vere");
                System.exit(4);
            }
        }

        return -1;
    }

    public Boolean signup()
    {
        Scanner sc = new Scanner(System.in);

        int tries = 4;

        while(tries > 0){
            tries++;

            System.out.println("Introduceti username-ul: ");
            String username = sc.nextLine();

            if (checkUser(username)){
                System.out.println("Userul exista deja. Alegeti altul.");
                continue;
            }

            System.out.println("Introduceti parola: ");
            String password = sc.nextLine();

            insertUser(username, password);

            System.out.println("Login reusit.");

            // audit creat cont
            auditService.writeToFile("log.csv", "A fost creat contul " + username + ":" + password);
            return true;
        }
        System.out.println("Ati depasit numarul de 4 incercari.");
        return false;
    }


    public void insertUser(String name, String password) {
        String sql = "insert into user (name, pass) values (?, ?)";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ps.execute();

            // nu uita audit

            System.out.println("Am adaugat userul " + name + " cu parola " + password );
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int checkAccount(String name, String password) {
        String sql = "select * from user where name = ? and pass = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("id");
            else
                return -1;

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean checkUser(String name){
        String sql = "select * from user where name = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return true;
            else
                return false;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void changePassById(int id, String password)
    {
        String sql = "update user set pass = ? where id = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, String.valueOf(id));
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0)
                System.out.println("Password changed to " + password);

            auditService.writeToFile("log.csv", "Userul " + id + " si-a schimbat parola.");


        }catch(SQLException e)
        {
            e.printStackTrace();
        }


    }

    public void changePassword(String name, String password) {
        String sql = "update user set pass = ? where name = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, name);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Parola schimbata cu succes.");
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String getNameById(int id) {
        String sql = "select name from user where id = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getString("name");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

}
