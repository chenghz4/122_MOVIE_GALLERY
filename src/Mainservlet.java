import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation,
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "Mainservlet", urlPatterns = "/api/Main")
    public class Mainservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //@Resource(name = "jdbc/moviedb")


   // private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        PrintWriter out = response.getWriter();
        try {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            Connection dbcon = ds.getConnection();
            String query = "SELECT GROUP_CONCAT(distinct name) as genre_name FROM genres ";
            PreparedStatement statement = dbcon.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            JsonObject jsonObject = new JsonObject();


            // Iterate through each row of rs
           if (rs.next()) {
                String gname = rs.getString("genre_name");
                jsonObject.addProperty("gname", gname);

            }
            rs.close();
            statement.close();
            String searchtitle = request.getParameter("search");
            String searchyear = request.getParameter("search_year");
            String searchdirector = request.getParameter("search_director");
            String searchstar = request.getParameter("search_star");
            String genres = request.getParameter("genres");
            String letters = request.getParameter("letters");


            jsonObject.addProperty("title", searchtitle);
            jsonObject.addProperty("year", searchyear);
            jsonObject.addProperty("director", searchdirector);
            jsonObject.addProperty("star", searchstar);
            jsonObject.addProperty("genres", genres);
            jsonObject.addProperty("letters", letters);

            out.write(jsonObject.toString());


        }catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        }
        out.close();

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
            PrintWriter out = response.getWriter();
        try {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            // Get a connection from dataSource
            Connection dbcon = ds.getConnection();
            JsonArray jsonArray = new JsonArray();

            String movie_name=request.getParameter("moviename");
            if (movie_name == null || movie_name.trim().isEmpty()||movie_name.length()<3) {
                out.write(jsonArray.toString());
                return;
            }
            String ll="";


            String m[] = movie_name.split("\\s+");
            for (int i = 0; i < m.length; i++) ll = ll+"+" + m[i] + "* ";


            String query1="select * from movies where MATCH (title) AGAINST (? IN BOOLEAN MODE) " +
                    "or ed(title,?)<=1 " +
                    "limit 10";
            PreparedStatement statement1 = dbcon.prepareStatement(query1);
            statement1.setString(1, ll);
            statement1.setString(2, movie_name);

            ResultSet rs1 = statement1.executeQuery();
            while(rs1.next()){
                String movie_id = rs1.getString("id");
                String movie_title = rs1.getString("title");
                String movie_year = rs1.getString("year");
                String movie_director = rs1.getString("director");

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("value", movie_title);

                JsonObject additionalDataJsonObject = new JsonObject();
                additionalDataJsonObject.addProperty("movie_id", movie_id);
                additionalDataJsonObject.addProperty("movie_year", movie_year);
                additionalDataJsonObject.addProperty("movie_director", movie_director);

                jsonObject.add("data", additionalDataJsonObject);

                jsonArray.add(jsonObject);

            }
            out.write(jsonArray.toString());
            rs1.close();
            statement1.close();
            dbcon.close();



        } catch (Exception e) {
            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            response.setStatus(500);
        }

        out.close();
    }
}
