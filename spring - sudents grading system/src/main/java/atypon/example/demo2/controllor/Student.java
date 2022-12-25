package atypon.example.demo2.controllor;

import atypon.example.demo2.model.APIdb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class Student {
    private APIdb service = new APIdb();

    public Student() throws SQLException, ClassNotFoundException {
    }
    @GetMapping("/login-student")
    public void doGetLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                if( service.isValidStudent(name,password)){
                    response.addCookie(cookies[0]);
                    request.setAttribute("name", name);
                    request.getRequestDispatcher("/WEB-INF/jsp/studenthome.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);
    }



    @PostMapping("/login-student")
    protected void doPostLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        System.out.println("im here");
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        boolean isValidUser = false;
        try {
            isValidUser = service.isValidStudent(name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (isValidUser) {
            HttpSession session=request.getSession();
            String cookies = name+"/"+ password;
            Cookie c = new Cookie("authS", cookies);
            response.addCookie(c);
            request.setAttribute("name", name);
            request.getRequestDispatcher("/WEB-INF/jsp/studenthome.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid Credentials!!");
            request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);
        }
    }

    @GetMapping("/Marks")
    public void getMarks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String Marks = service.fetchMarks(name);
                    request.setAttribute("Marks", Marks);
                    request.getRequestDispatcher("/WEB-INF/jsp/Marks.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);

    }





    @GetMapping("/MarksAnalytics")
    public void getMarksAnalytics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    request.getRequestDispatcher("/WEB-INF/jsp/MarksAnalytics.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);

            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);
    }
    @PostMapping("/MarksAnalytics")
    public void postMarksAnalytics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subject = request.getParameter("subject");
        String analytics = null;
        try {
            analytics = service.fetchAnalytics(subject);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("subject",subject+"--------");
        request.setAttribute("Marks",analytics);
        request.getRequestDispatcher("/WEB-INF/jsp/MarksAnalytics.jsp").forward(request, response);

    }



    @GetMapping("/logout-student")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/loginStudent.jsp").forward(request, response);
    }



}