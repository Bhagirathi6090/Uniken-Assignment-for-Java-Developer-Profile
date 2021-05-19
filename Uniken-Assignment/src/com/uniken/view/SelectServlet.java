package com.uniken.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewurl")
public class SelectServlet extends HttpServlet {
	private static final String GET_CORP_DETAILS = "SELECT CORP_NAME,CORP_ID,ACC_NUMBER FROM CORPORATE_DATABASE WHERE CORP_NAME=?";

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// get PrintWriter
		PrintWriter pw = res.getWriter();

		// set content type
		res.setContentType("text/html");

		// read form data
		String corpName = req.getParameter("corp_name");

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
				PreparedStatement ps = con.prepareStatement(GET_CORP_DETAILS);) {

			// set values to query param
			if (ps != null) {
				ps.setString(1, corpName);
			}

			// execute the query
			ResultSet rs = ps.executeQuery();

			// process the ResultSet object
			if (rs.next()) {
				pw.println("<h1 style='color:blue;text-align:center'> " + " Corporate Details are </h1>");
				pw.println("<div style='color:cyan;text-align:center'>");
				pw.println("<br> <b> Corporate name::" + rs.getString(1) + "</b><br>");
				pw.println("<br> <b> Corporate ID::" + rs.getString(2) + "</b><br>");
				pw.println("<br> <b> Account Number::" + rs.getInt(3) + "</b><br>");
				pw.println("<br><h3> <a href='view_record.html'>View </a> </h3>");
				pw.println("</div>");
			} else {
				pw.println("<h1 style='color:red;text-align:center'>Record not found </h1>");
				pw.println("<br><h3 style='text-align:center'> <a href='view_record.html'>View </a> </h3>");
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
