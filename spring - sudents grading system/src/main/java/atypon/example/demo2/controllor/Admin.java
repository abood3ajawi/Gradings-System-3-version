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
public class Admin {
    private APIdb service = new APIdb();

    public Admin() throws SQLException, ClassNotFoundException {
    }
    @GetMapping("/login-admin")
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
                if( service.isValidAdmin(name,password)){
                    response.addCookie(cookies[0]);
                    request.setAttribute("name", name);
                    request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

            }
            catch (SQLException | ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
    @PostMapping("/login-admin")
    protected void doPostLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        boolean isValidUser = false;
        try {
            isValidUser = service.isValidAdmin(name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (isValidUser) {
            HttpSession session=request.getSession();
            String cookies = name+"/"+ password;
            Cookie c = new Cookie("auth", cookies);
            response.addCookie(c);
            request.setAttribute("name", name);
            request.getRequestDispatcher("/WEB-INF/jsp/welcome.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid Credentials!!");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }

    @GetMapping("/insertStudent")
    public void getInsertStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    request.getRequestDispatcher("/WEB-INF/jsp/insertStudent.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

            }
            catch (SQLException | ServletException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
    @PostMapping("/insertStudent")
    public void postInsertStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentName = request.getParameter("studentName");
        String password = request.getParameter("password");

        try {
            service.insertStudent(studentName,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("Message", "inserted!");
        request.getRequestDispatcher("/WEB-INF/jsp/insertStudent.jsp").forward(request, response);
    }




    @GetMapping("/insertGrades")
    public void getGradesStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    request.getRequestDispatcher("/WEB-INF/jsp/insertGrades.jsp").forward(request, response);
                }
                else
                    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

            }
            catch (SQLException | ServletException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
    @PostMapping("/insertGrades")
    public void postGradesStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentName = request.getParameter("studentName");
        String Math =request.getParameter("Math");
        String programming =request.getParameter("programming");
        String English =request.getParameter("English");

        try {
            service.insertGrades(studentName,Math,programming,English);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("Message", "inserted!");
        request.getRequestDispatcher("/WEB-INF/jsp/insertGrades.jsp").forward(request, response);
    }


        @GetMapping("/logout")
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