package tech.shefoo.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ManagedBean(name = "databaseConnectionBean")
@ApplicationScoped
public class DatabaseConnectionBean {

    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            // Initialize the database connection
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL JDBC driver
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/club", "donatello", "d0n4t3ll0");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @PreDestroy
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
