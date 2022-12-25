import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {
    boolean admin =false;
    boolean student =false;
    String studentName;
    String adminName;

    public void signInAdmin(DataInputStream in ,DataOutputStream  out,BufferedReader readConsole) throws IOException {
        System.out.println("Admin Name : ");
        String user = readConsole.readLine();
        System.out.println("Password : ");
        String password = readConsole.readLine();
        out.writeUTF("admin");
        out.writeUTF(user);
        out.writeUTF(password);
        String valid = in.readUTF();
        if(valid.equals("valid")) {
            admin = true;
            student=false;
            adminName = user ;
        }
    }
    public void signInUser(DataInputStream in ,DataOutputStream  out ,BufferedReader readConsole) throws IOException {
        System.out.println("Student Name : ");
        String user = readConsole.readLine();
        System.out.println("Password : ");
        String password = readConsole.readLine();
        out.writeUTF("student");
        out.writeUTF(user);
        out.writeUTF(password);
        String valid = in.readUTF();
        if(valid.equals("valid")) {
            studentName =user;
            student = true;
            admin = false;
        }

    }
    public void registerStudent(BufferedReader readConsole,DataInputStream in, DataOutputStream out)throws SQLException, IOException{
        out.writeUTF("1");
        out.flush();
        System.out.println("Name : ");
        String name =  readConsole.readLine();
        System.out.println("password : ");
        String password =  readConsole.readLine();
        out.writeUTF(name);
        out.writeUTF(password);
        out.flush();
        System.out.println(in.readUTF());
    }
    public void insertGrades(BufferedReader readConsole,DataInputStream in, DataOutputStream out)throws SQLException, IOException{
        out.writeUTF("2");
        out.flush();
        System.out.println("Name : ");
        String name =  readConsole.readLine();
        System.out.println("english mark : ");
        String english =  readConsole.readLine();
        System.out.println("programing mark : ");
        String programing =  readConsole.readLine();
        System.out.println("data mark :");
        String data =  readConsole.readLine();

        out.writeUTF(name);
        out.writeUTF(english);
        out.writeUTF(programing);
        out.writeUTF(data);
        out.flush();
        System.out.println(in.readUTF());
    }
    public void start(){
        try {
            Socket socket = new Socket("localhost", 6565);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            BufferedReader readConsole=new BufferedReader(new InputStreamReader(System.in));
            boolean close =true;
            while (close) {
                System.out.println("welcome to System Grading school");
                System.out.println("1 : Sign as admin ");
                System.out.println("2 : Sign as student ");
                System.out.println("3 : close ");

                String signIn = readConsole.readLine();
                if (signIn.equals("1")) {
                    signInAdmin(in, out, readConsole);
                } else if (signIn.equals("2")) {
                    signInUser(in, out, readConsole);
                }
                else if(signIn.equals("3")) {
                    out.writeUTF("close");
                    close = false;
                }
                while (admin) {
                    System.out.println("Hi " + adminName);
                    System.out.println("1 : register student ");
                    System.out.println("2 : insert grades ");
                    System.out.println("3 : sign out ");
                    String option = readConsole.readLine();
                    if (option.equals("1")) {
                        registerStudent(readConsole, in, out);
                    } else if (option.equals("2")) {
                        insertGrades(readConsole, in, out);
                    } else if (option.equals("3")) {
                        admin = false;
                    }
                }
                while (student) {
                    System.out.println("Hi " + studentName);
                    System.out.println("1 : marks  ");
                    System.out.println("2 : marks analytics");
                    System.out.println("3 : sign out ");
                    String option = readConsole.readLine();
                    if (option.equals("1")) {
                        out.writeUTF("1");
                        out.flush();
                        System.out.println("1 : English ");
                        System.out.println("2 : Math");
                        System.out.println("3 : Programing ");
                        String subject = readConsole.readLine();
                        if (subject.equals("1")) {
                            out.writeUTF("English");
                            out.flush();
                        } else if (subject.equals("2")) {
                            out.writeUTF("Math");
                            out.flush();
                        }
                        if (subject.equals("3")) {
                            out.writeUTF("programming");
                            out.flush();
                        }
                        System.out.println(in.readUTF() + "/100");
                    } else if (option.equals("2")) {
                        out.writeUTF("2");
                        System.out.println("1 : English ");
                        System.out.println("2 : Math");
                        System.out.println("3 : Programing ");
                        String subject = readConsole.readLine();
                        if (subject.equals("1")) {
                            out.writeUTF("English");
                            out.flush();
                        } else if (subject.equals("2")) {
                            out.writeUTF("Math");
                            out.flush();
                        }
                        if (subject.equals("3")) {
                            out.writeUTF("programming");
                            out.flush();
                        }
                        System.out.println("Highest mark in class: " + in.readUTF());
                        System.out.println("Lowest mark in class: " + in.readUTF());
                        System.out.println("Average mark in class: " + in.readUTF());
                    } else if (option.equals("3")) {
                        student = false;
                    }
                }
            }
            in.close();
            socket.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}