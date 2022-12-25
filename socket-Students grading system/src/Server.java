import java.io.*;
import java.net.*;
import java.sql.*;
public class Server {
    boolean admin ;
    boolean student;
    String studentName;
    String adminName;
    public boolean validAdmin( PreparedStatement pstmt, Connection connection,DataOutputStream out,String user,String password) throws SQLException, IOException {
        pstmt = connection.prepareStatement("select Name,Password from Admin where Name=?");
        pstmt.setString(1, user);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        if(user.equals(resultSet.getString(1))&&password.equals(resultSet.getString(2))) {
            System.out.println("valid");
            out.writeUTF("valid");
            adminName =user;
            return true;
        } else  {
            out.writeUTF("invalid");
            System.out.println("invalid");
            return false;
        }
    }
    public boolean validUser(PreparedStatement pstmt, Connection connection,DataOutputStream out,String user,String password) throws SQLException, IOException {
        pstmt = connection.prepareStatement("select Name,Password from Students where Name=?");
        pstmt.setString(1, user);
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
        if(user.equals(resultSet.getString(1))&&password.equals(resultSet.getString(2))) {
            System.out.println("valid");
            out.writeUTF("valid");
            studentName =user;
            return true;
        } else  {
            out.writeUTF("invalid");
            System.out.println("invalid");
            return false;
        }
    }
    public void registerStudent( PreparedStatement pstmt, Connection connection,DataOutputStream out,String newUser,String password)throws SQLException, IOException{
        pstmt = connection.prepareStatement("insert into Students values(?,?,?)");
        pstmt.setString(1, null);
        pstmt.setString(2, newUser );
        pstmt.setString(3, password);
        pstmt.executeUpdate();
        out.writeUTF(newUser+"inserted!");
        out.flush();
    }
    public void insertGrades( PreparedStatement pstmt, Connection connection,DataOutputStream out,String name,String Math,String programming,String English)throws SQLException, IOException{
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
            out.writeUTF(" marks inserted!");
            out.flush();
        }
        else
            out.writeUTF("there is no student with this name!");
    }
    public void start() throws ClassNotFoundException, SQLException, IOException{

        ServerSocket serverSocket=new ServerSocket(6565);
        Socket socket =serverSocket.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out=new DataOutputStream(socket.getOutputStream());
        BufferedReader readConsole=new BufferedReader(new InputStreamReader(System.in));

        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver loaded");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3304/StudentsGradingSystem" ,"root", "password");
        System.out.println("Database connected");
        Statement statement = connection.createStatement();
        PreparedStatement pstmt = null;
        boolean close=true;
        while (close) {
            String role = in.readUTF();
            if(role.equals("out")) {
                break;
            }
            String user = in.readUTF();
            String password = in.readUTF();


            if (role.equals("admin")) {
                admin = validAdmin(pstmt, connection, out, user, password);

                String option = in.readUTF();
                while (admin) {
                    if (option.equals("1")) {
                        String newUser = in.readUTF();
                        String pass = in.readUTF();
                        registerStudent(pstmt, connection, out, newUser, pass);
                    } else if (option.equals("2")) {
                        String name = in.readUTF();
                        String Math = in.readUTF();
                        String programming = in.readUTF();
                        String english = in.readUTF();
                        insertGrades(pstmt, connection, out, name, Math, programming, english);
                    } else if (option.equals("3")) {
                        admin = false;
                    }
                }

            } else if (role.equals("student")) {
                student = validUser(pstmt, connection, out, user, password);

                while (student) {
                    String option = in.readUTF();

                    if (option.equals("1")) {
                        String subject = in.readUTF();
                        // System.out.println(subject);
                        pstmt = connection.prepareStatement("select * from Grades where Name=?");
                        pstmt.setString(1, studentName);
                        ResultSet resultSet = pstmt.executeQuery();
                        resultSet.next();
                        out.writeUTF(resultSet.getString(subject));
                    } else if (option.equals("2")) {
                        String subject = in.readUTF();
                        // System.out.println(subject);
                        pstmt = connection.prepareStatement("select MAX(" + subject + "),MIN(" + subject + "),AVG(" + subject + ") from Grades");
                        // pstmt.setString(1, subject);
                        ResultSet resultSet = pstmt.executeQuery();
                        resultSet.next();
                        out.writeUTF(resultSet.getString(1));
                        out.writeUTF(resultSet.getString(2));
                        out.writeUTF(resultSet.getString(3));

                    } else if (option.equals("3")) {
                        student = false;
                    }
                }
            }

        }
        in.close();
        socket.close();
        connection.close();
        serverSocket.close();

    }
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Server server = new Server();
        server.start();

        /*
/*
*
*       Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver loaded");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3304/StudentsGradingSystem" ,"root", "password");
        System.out.println("Database connected");
        Statement statement = connection.createStatement();
        statement.executeUpdate("insert into Students values(null,'adham ramy','test2')");
        ResultSet resultSet = statement.executeQuery("select * from Students");
        while (resultSet.next())
            System.out.println(resultSet.getString(1) + "\t" +
                    resultSet.getString(2) );
        // Close the connection
        connection.close();
*
* */
        }
}

