package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/logout")
public class Logout extends HttpServlet {

    private Apidb service = new Apidb();

    public Logout() throws SQLException, ClassNotFoundException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String login =null;
        if (cookies != null) {
             login = cookies[1].getName();
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        if(login == null || login.equals("auth")) {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
        else
            request.getRequestDispatcher("/WEB-INF/views/loginStudent.jsp").forward(request, response);
    }



}