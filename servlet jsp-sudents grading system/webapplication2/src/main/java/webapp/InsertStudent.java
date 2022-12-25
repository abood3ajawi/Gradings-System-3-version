package webapp;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/insertStudent")
public class InsertStudent extends HttpServlet {
    private Apidb service = new Apidb();

    public InsertStudent() throws SQLException, ClassNotFoundException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();

        if( cookies !=null  && cookies.length > 1 ){
            System.out.println(cookies[1].getValue());
            String auth=cookies[1].getValue();
            System.out.println(auth);
            System.out.println(auth.indexOf("/"));
            int split = auth.indexOf("/");
            String name = auth.substring(0,split);
            System.out.println(name);
            String password = auth.substring(split+1,auth.length());
            System.out.println(password);
            try {
                if(service.isValidAdmin(name,password)){
                    response.addCookie(cookies[0]);
                    request.setAttribute("name", name);
                    request.getRequestDispatcher("/WEB-INF/views/insertStudent.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

            }
            catch (SQLException | ServletException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String studentName = request.getParameter("studentName");
        String password = request.getParameter("password");

        try {
            service.insertStudent(studentName,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("Message", "inserted!");
        request.getRequestDispatcher("/WEB-INF/views/insertStudent.jsp").forward(request, response);
    }

}

