package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class webget extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String endpoint = req.getParameter("endpoint");
		String startpoint = req.getParameter("startpoint");
		String scenictype = req.getParameter("scenictype");
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		//File file = new generatezmdpfile().generatezmdp(startpoint);
		
		out.println("<html><head><title>result</title></head>");
		
		out.println("<body>endpiont : " + endpoint + "<br>");
		out.println("startpoint : " + startpoint + "<br>");
		out.println("scenictype :" + scenictype + "</body></html>");
		
		
		out.flush();
		
	}
	
//	public void generatezmdp(){
//		
//		
//		
//	}
	
	
	


}
