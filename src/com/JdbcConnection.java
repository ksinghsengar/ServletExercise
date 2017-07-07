package com;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@WebServlet(name = "JdbcConnection")
public class JdbcConnection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

         Connection connection;
        PreparedStatement preparedStatement;
         List<String> list = new ArrayList<>();
         String query;
         String username = null;
         String email = null;
         String pwd = null;
         String loginuser = null;
         String loginpassword = null;
         ServletContext servletContext = request.getServletContext();
         String user =   servletContext.getInitParameter("databaseUser");
         String password = servletContext.getInitParameter("databasePassword");
         String driver_class = servletContext.getInitParameter("databaseDriverClass");
         String url = servletContext.getInitParameter("databaseConnectionString");
       /* String uri = request.getRequestURI();
        String pageName = uri.substring(uri.lastIndexOf("/")+1);*/
         String page = (String) request.getAttribute("previous_page");

        System.out.println("Page: "+page);
        try {
            Class.forName(driver_class);
            connection = DriverManager.getConnection(url,user,password);
            if(page.equals("Register")){
                query = "insert into register(user,email,password) values(?,?,?)";
                username = request.getParameter("user");
                email =  request.getParameter("email");
                pwd = request.getParameter("regPassword");
                preparedStatement  = connection.prepareCall(query);
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,pwd);

                if(preparedStatement.executeUpdate()>0) {
                    request.setAttribute("message", "Registered Successfully");
                    out.println("Successful");
                }
                else {
                    request.setAttribute("message", "Registration Unsuccessful");
                    out.println("UnSuccessful");
                }
                connection.close();
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
                    requestDispatcher.forward(request,response);

                }

                else if(page.equals("Login")){
                loginuser = request.getParameter("user");
                loginpassword = request.getParameter("password");
                System.out.println("loginuser: "+loginuser+" password: "+loginpassword);
                query = "select count(*) from register where user=? and password=?;";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,loginuser);
                preparedStatement.setString(2,loginpassword);

                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("SQL: "+resultSet.getStatement().toString());
                if(resultSet.next()){
                    if(resultSet.getInt(1) > 0) {
                        query = "select content from blog where user=? ";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1,loginuser);
                        resultSet = preparedStatement.executeQuery();
                        while(resultSet.next()){
                             list.add( (String)resultSet.getString(1));
                        }
                        HttpSession session = request.getSession();
                        session.setAttribute("content",list);
                        System.out.println("list: "+list);
                        session.setAttribute("user",loginuser);
                        System.out.println("loginuser: "+loginuser);
                        connection.close();
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/blog.jsp");
                        requestDispatcher.forward(request, response);
                    }
                    else{
                        request.setAttribute("message","Username or Password is Incorrect");
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
                        requestDispatcher.forward(request,response);
                    }
                }
            }

           else if(page.equals("Blog")){
                HttpSession session = request.getSession(false);
                   String bloguser = (String) session.getAttribute("user");
                   String blogcontent =  request.getParameter("blog_content");
                System.out.println("user: "+bloguser+" content: "+blogcontent);

                query = "insert into blog (user,content) values (?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,bloguser);
                preparedStatement.setString(2,blogcontent);
                int rows = preparedStatement.executeUpdate();
                System.out.println("rows: "+rows);


                System.out.println(session.getAttribute("content"));
                if(rows > 0){
                    List<String> Addlist=(ArrayList<String>) session.getAttribute("content");
                    Addlist.add(blogcontent);
                    session.setAttribute("content",Addlist);
                    request.setAttribute("message","Blog saved successfully");
                    System.out.println("Successfully saved");

                }
                else {
                    request.setAttribute("message", "Error occured, blog not saved.");
                    System.out.println("error");
                }
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/blog.jsp");
                    requestDispatcher.forward(request,response);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
