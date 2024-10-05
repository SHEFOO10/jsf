package tech.shefoo.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import tech.shefoo.DatabaseConfig;
import tech.shefoo.utils.PasswordUtils;

public class LoginDAO {

    public static boolean validate(String user, String password) {
        Connection con = null;
        PreparedStatement ps = null;

        String adminUser = null;
        String adminPwd = null;

        // Load admin credentials from the properties file
        Properties properties = new Properties();
        try (InputStream input = LoginDAO.class.getClassLoader().getResourceAsStream("admin-config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find admin-config.properties");
                return false;
            }
            properties.load(input);
            adminUser = properties.getProperty("admin.username");
            adminPwd = properties.getProperty("admin.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Check if the user is admin
        if (user.equals(adminUser)) {
            // Compare the hashed passwords
            if (PasswordUtils.verifyPassword(password, adminPwd)) {
                return true; // Valid admin credentials
            } else {
                return false; // Invalid admin credentials
            }
        }

        // Proceed to check in the database for regular users
        try {
            con = DatabaseConfig.getDataSource().getConnection();
            ps = con.prepareStatement("SELECT uname, password FROM Users WHERE uname = ? AND password = ?");
            ps.setString(1, user);
            ps.setString(2, PasswordUtils.hashPassword(password)); // Assuming passwords are stored hashed

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // result found, valid inputs
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Login error -->" + ex.getMessage());
            return false;
        } finally {
            // Close resources
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (con != null) try { con.close(); } catch (SQLException ignored) {}
        }

        return false; // Invalid user or password
    }
}
