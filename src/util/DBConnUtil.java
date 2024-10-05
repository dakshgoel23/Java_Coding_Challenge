

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {

    public static Connection getConnection() {
        String connectionString = DBPropertyUtil.getPropertyString("resources/db.properties");//also can use in function parameter
        Connection connection = null;
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
        }
        catch(ClassNotFoundException e)
        {
        	e.printStackTrace();
        	System.out.println("Driver Loading Failed");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Not connected to Database");
        }
        return connection;
    }
}
