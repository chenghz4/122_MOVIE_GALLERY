import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.sql.Types;
import java.sql.CallableStatement;





@WebServlet(name = "DashBoardservlet", urlPatterns = "/api/write/dash")
public class DashBoard extends HttpServlet {
    private static final long serialVersionUID = 1L;
   // @Resource(name = "jdbc/moviedb")
   // private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession();
        String starname="";
        starname = request.getParameter("starname");
        String date_of_birth = request.getParameter("date_of_birth");
        PrintWriter out = response.getWriter();
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/WriteDB");
            Connection dbcon = ds.getConnection();

            if (!starname.equals("")) {

                String query4 = "select Max(id) " + "from stars where id like 'nm%'";
                PreparedStatement statement4 = dbcon.prepareStatement(query4);
                ResultSet rs4 = statement4.executeQuery();
                String offset="";
                if(rs4.next()){
                    offset = rs4.getString("Max(id)");
                }
                rs4.close();
                statement4.close();

                int a=Integer.parseInt(offset.substring(2))+1;
                String starid="nm"+a;
                String query3 = "INSERT INTO stars VALUES(?,?,?);";
                PreparedStatement statement3 = dbcon.prepareStatement(query3);
                statement3.setString(1, starid);
                statement3.setString(2, starname);


                if(date_of_birth.equals("")){
                    statement3.setString(3, null);

                }
                else {
                    int date = Integer.parseInt(date_of_birth);
                    statement3.setInt(3, date);
                }
                statement3.executeUpdate();
                statement3.close();

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            out.write(responseJsonObject.toString());
        }



            else{
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "starname cannot be empty");

            out.write(responseJsonObject.toString());

        }
        dbcon.close();
    }
        catch (Exception e){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            e.printStackTrace();
            // set reponse status to 500 (Internal Server Error)
            //
            response.setStatus(500);

        }
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String moviename="";
        moviename = request.getParameter("moviename");
        String year="";
        year = request.getParameter("year");
        String director="";
        director = request.getParameter("director");
        String genres="";
        genres = request.getParameter("genres");
        String starname="";
        starname = request.getParameter("starname");


        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/WriteDB");
            Connection dbcon = ds.getConnection();
            int check=0;
            if(!moviename.equals(null)&&!moviename.isEmpty()
                    &&!year.equals(null)&&!year.isEmpty()
                    && !director.equals(null)&&!director.isEmpty()
                    &&!genres.equals(null)&&!genres.isEmpty()
                    && !starname.equals(null)&&!starname.isEmpty()) {
                String query = "call addmovie (?,?,?,?,?,?)";
                CallableStatement statement = dbcon.prepareCall(query);
                statement.registerOutParameter(6, Types.INTEGER);
                statement.setString(1, year);
                statement.setString(2, moviename);
                statement.setString(3, director);
                statement.setString(4, starname);
                statement.setString(5, genres);
                statement.setInt(6, 0);


                statement.executeUpdate();
                check = statement.getInt(6);




                statement.close();
            }
            if (check==1) {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

                out.write(responseJsonObject.toString());
            }
            else if(check==2){
                // Login fails
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "The movie has already existed");
                out.write(responseJsonObject.toString());
            }
            else if(check==0){
                // Login fails
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", "information is not enough");
                out.write(responseJsonObject.toString());
            }


            dbcon.close();
        }
        catch (Exception e){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            e.printStackTrace();
            response.setStatus(500);

        }



    }





}
