package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/login-admin")
public class LoginServlet extends HttpServlet {

	private Apidb service = new Apidb();

	public LoginServlet() throws SQLException, ClassNotFoundException {
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
			if( service.isValidAdmin(name,password)){
				response.addCookie(cookies[0]);
				request.setAttribute("name", name);
				request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
			}
			else
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		}
		else
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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
			request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
		} else {
			request.setAttribute("errorMessage", "Invalid Credentials!!");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
	}

}