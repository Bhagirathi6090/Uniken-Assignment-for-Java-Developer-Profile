package com.uniken.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateurl")
public class UpdateServlet extends HttpServlet {
	private static final String UPDATE_CORP_DETAILS = "UPDATE CORPORATE_DATABASE SET CORP_ID =? , ACC_NUMBER =? WHERE CORP_NAME =?";

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// get PrintWriter
		PrintWriter pw = res.getWriter();

		// set content type
		res.setContentType("text/html");

		// to check record updated or not
		int count = 0;

		// read form data
		String corpName = req.getParameter("corp_name");
		String corpId = req.getParameter("corp_id");
		int accNumber = Integer.parseInt(req.getParameter("corp_acc"));

		/**
		 * get ServletConfig object Dynamic assignment of Database connection details we
		 * can confiure this in web.xml file
		 */
		ServletContext sContext = getServletContext();
		String driver = sContext.getInitParameter("driver");
		String url = sContext.getInitParameter("url");
		String user = sContext.getInitParameter("dbuser");
		String password = sContext.getInitParameter("dbpwd");

		// load driver class
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		/**
		 * establishing the connection with oracle database and inserting values into
		 * the table
		 */
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(UPDATE_CORP_DETAILS);) {

			if (ps != null) {
				ps.setString(1, corpId);
				ps.setInt(2, accNumber);
				ps.setString(3, corpName);
			}

			// execute the query
			if (ps != null) {
				count = ps.executeUpdate();
			}

			if (count == 0) {
				pw.println("<h3 style='color:red;text-align:center'>Record Not Updated</h3>");
				pw.println("<br><h3 style='text-align:center'> <a href='update.html'>Update </a> </h3>");
			} else {
				pw.println("<h3 style='color:green;text-align:center'>Record Updated Successfully</h3>");
				pw.println("<br><h3 style='text-align:center'> <a href='update.html'>Update </a> </h3>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * if any error occurs during the process of submission we are displaying
			 * non-technical guiding message for enduser
			 */
			RequestDispatcher rd = req.getRequestDispatcher("errorServlet");
			rd.forward(req, res);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
