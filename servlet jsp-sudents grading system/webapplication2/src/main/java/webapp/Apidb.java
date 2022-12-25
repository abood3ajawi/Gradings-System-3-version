package webapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;

public class Apidb {
    Connection connection;
    Statement statement ;
    PreparedStatement pstmt ;

    public Apidb() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
         connection = DriverManager.getConnection("jdbc:mysql://localhost:3304/StudentsGradingSystem" ,"root", "password");
         statement = connection.createStatement();
         pstmt = null;
    }

    public boolean isValidStudent(String name , String password) throws SQLException {
        pstmt = connection.prepareStatement("select Name,Password from Students where Name=?");
        pstmt.setString(1, name);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        if(name.equals(resultSet.getString(1))&&password.equals(resultSet.getString(2)))
            return true;
        return false;

    }
    public boolean isValidAdmin(String name , String password) throws SQLException {
        pstmt = connection.prepareStatement("select Name,Password from Admin where Name=?");
        pstmt.setString(1, name);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        if(name.equals(resultSet.getString(1))&&password.equals(resultSet.getString(2)))
            return true;
        return false;

    }
    public void insertStudent(String studentName , String password) throws SQLException {
        pstmt = connection.prepareStatement("insert into Students values(?,?,?)");
        pstmt.setString(1, null);
        pstmt.setString(2, studentName );
        pstmt.setString(3, password);
        pstmt.executeUpdate();
    }
    public void insertGrades(String name, String Math, String programming, String English)throws SQLException, IOException {
        PreparedStatement pstmt1 = connection.prepareStatement("select name from Students where Name =?");
        pstmt1.setString(1, name);
        boolean check =  pstmt1.execute();
        if(check) {
            pstmt = connection.prepareStatement("insert into Grades values(?,?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, Math);
            pstmt.setString(3, programming);
            pstmt.setString(4, English);
            pstmt.executeUpdate();
        }
    }
    public String fetchMarks(String name)throws SQLException, IOException {
        pstmt = connection.prepareStatement("select * from Grades where Name=?");
        pstmt.setString(1, name);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        return "Math : "+resultSet.getString(2)+"/100 ----- "+"programming : "+resultSet.getString(3)+"/100 -----"+"English : "+resultSet.getString(4)+"/100";
    }


    public String fetchAnalytics(String subject)throws SQLException, IOException {
        pstmt = connection.prepareStatement("select MAX( "+subject+" ),MIN("+subject+"),AVG("+subject+") from Grades");
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        return "Highest mark in class: : "+resultSet.getString(1)+"/100 ----- "+"Lowest mark in class: : "+resultSet.getString(2)+"/100 -----"+"Average mark in class: : "+resultSet.getString(3)+"/100";
    }
}
