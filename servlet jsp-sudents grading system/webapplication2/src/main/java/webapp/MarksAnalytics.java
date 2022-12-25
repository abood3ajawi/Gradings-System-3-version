package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/MarksAnalytics")
public class MarksAnalytics extends HttpServlet {

    private Apidb service = new Apidb();

    public MarksAnalytics() throws SQLException, ClassNotFoundException {
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
                if(service.isValidStudent(name,password)){
                    // response.addCookie(cookies[0]);
                    request.getRequestDispatcher("/WEB-INF/views/MarksAnalytics.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/views/loginStudent.jsp").forward(request, response);

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/views/loginStudent.jsp").forward(request, response);

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            String subject = request.getParameter("subject");
        String analytics = null;
        try {
            analytics = service.fetchAnalytics(subject);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("subject",subject+"--------");
            request.setAttribute("Marks",analytics);
            request.getRequestDispatcher("/WEB-INF/views/MarksAnalytics.jsp").forward(request, response);

    }

}