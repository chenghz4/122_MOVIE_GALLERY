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
import java.math.*;
import java.sql.Statement;
import java.util.ArrayList;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "StarsServlet", urlPatterns = "/api/stars")
public class StarsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public int page_global;
    public int total;
    public int per;
    // Create a dataSource which registered in web.xml
    //@Resource(name = "jdbc/moviedb")
   // private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String number = request.getParameter("num");
        String page_p="";
        String page_n="";

        if(page_global>1)  page_p=(page_global-1)+"";
        else  page_p=page_global+"";


        if(total==per)page_n=(page_global+1)+"";
        else page_n=page_global+"";



        String sort_r="a.rating desc";
        String sort_t="a.title desc";
        String sort_ra="a.rating asc";
        String sort_ta="a.title asc";

        PrintWriter out = response.getWriter();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number", number);
        jsonObject.addProperty("page_n", page_n);
        jsonObject.addProperty("page_p", page_p);
        jsonObject.addProperty("sort_r", sort_r);
        jsonObject.addProperty("sort_t", sort_t);
        jsonObject.addProperty("sort_ra", sort_ra);
        jsonObject.addProperty("sort_ta", sort_ta);
        out.write(jsonObject.toString());
        out.close();
   }







    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //response.setContentType("application/json"); // Response mime type
        String id = request.getParameter("id");
        String x="";
        String id_fix="";

        ArrayList<String> n=new ArrayList<String>();


        if(id!=null&&!id.isEmpty()&&!id.equals("")) {
            String m[] = id.split("\\s+");
            for (int i = 0; i < m.length; i++) id_fix = id_fix + "+" + m[i] + "* ";
            //m[i]
        }

        else {
            id_fix="";
            x="=0";
            id="";

        }
        String year = request.getParameter("year");
        String year_fix;

        if(year.equals(""))
            year_fix="%"+year+"%";
        else year_fix=year;

        String director = request.getParameter("director");
        String director_fix="%"+director+"%";
        String star = request.getParameter("star");
        String star_fix="%"+star+"%";
        String page=request.getParameter("page");
        String number=request.getParameter("number");
        int numberfix=20;
        int offset=0;


        if(!number.equals(null)&&!number.isEmpty()) {
            numberfix = Integer.parseInt(number);
            per = numberfix;
        }
        else{
            numberfix=20;
            per=numberfix;
        }
        if(!number.equals(null)&&!number.isEmpty()&&!page.equals(null)&&!page.isEmpty()){
            page_global = Integer.parseInt(page);
            offset = (Integer.parseInt(page) - 1) * numberfix;
        }
        else{
            page_global=1;
            offset=0;

        }
        String sort=request.getParameter("sort");
        String genres=request.getParameter("genres");

        String genres_fix;
        genres_fix=genres+"%";


        String letters=request.getParameter("letters");
        String letters_fix=letters+"%";
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            // Get a connection from dataSource
            Connection dbcon = ds.getConnection();
            String query =
                    "select distinct a.id, a.title, a.year, a.director, " +
                            "GROUP_CONCAT(distinct a.genre_name) as genre_name, a.rating,  " +
                            "GROUP_CONCAT(distinct s.name order by s.id) as star_name, " +
                            " GROUP_CONCAT(distinct s.id) as star_id " +
                            "from " +
                            "(select distinct m.id, m.title, m.year, m.director, " +
                            " GROUP_CONCAT(distinct g.name) as genre_name, r.rating " +
                            "from movies as m, ratings as r, genres as g, genres_in_movies as y " +
                            "where m.id=y.movieId and y.genreId=g.id and r.movieId=m.id " +
                            "and (MATCH (m.title) AGAINST (? IN BOOLEAN MODE)"+x+" or ed(m.title,?)<=2) " +
                            "and m.year like ? " +
                            "and m.director like ? " +
                            "and g.name like ? and m.title like ?  " +
                            "group by m.id " +
                            ") as a,  " +
                            " " +
                            "stars as s, stars_in_movies as x " +
                            "where a.id=x.movieId and x.starId=s.id and s.name like ?  " +
                            "group by a.id  " +
                            "order by  " +
                             sort  +
                            " limit ?, ? " ;


            String query1 =
                    "select distinct a.id, a.title, a.year, a.director, " +
                            "GROUP_CONCAT(distinct a.genre_name) as genre_name, a.rating,  " +
                            "GROUP_CONCAT(distinct s.name order by s.id) as star_name, " +
                            " GROUP_CONCAT(distinct s.id) as star_id " +
                            "from " +
                            "(select distinct m.id, m.title, m.year, m.director, " +
                            " GROUP_CONCAT(distinct g.name) as genre_name, r.rating " +
                            "from movies as m, ratings as r, genres as g, genres_in_movies as y " +
                            "where m.id=y.movieId and y.genreId=g.id and r.movieId=m.id " +
                            "group by m.id " +
                            "order by r.rating desc " +
                            "limit 20 " +
                            ") as a,  " +
                            " " +
                            "stars as s, stars_in_movies as x " +
                            "where a.id=x.movieId and x.starId=s.id  " +
                            "group by a.id " +
                            "order by a.rating desc " +
                            "limit 20 ";
            // Perform the query
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, id_fix);
            statement.setString(2,id);
            statement.setString(3,year_fix);
            statement.setString(4,director_fix);
            statement.setString(5,genres_fix);
            statement.setString(6,letters_fix);

            statement.setString(7,star_fix);

            statement.setInt(8,offset);
            statement.setInt(9,numberfix);

            ResultSet rs = statement.executeQuery();
            //





            JsonArray jsonArray = new JsonArray();
            total=0;
            // Iterate through each row of rs
            while (rs.next()) {

                total++;
                String movie_id = rs.getString("id");
                String movie_title = rs.getString("title");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String rate = rs.getString("rating");
                String star_id = rs.getString("star_id");
                String star_name = rs.getString("star_name");
                String genre_name=rs.getString("genre_name");

                // Create a JsonObject based on the data we retrieve from rs


                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("movie_id", movie_id);
                    jsonObject.addProperty("movie_title", movie_title);
                    jsonObject.addProperty("movie_year", movie_year);
                    jsonObject.addProperty("movie_director", movie_director);
                    jsonObject.addProperty("list_g", genre_name);
                    jsonObject.addProperty("list_s", star_name);
                    jsonObject.addProperty("s.id", star_id);
                    jsonObject.addProperty("rating", rate);

                    jsonArray.add(jsonObject);

                }



            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        out.close();

    }
}
