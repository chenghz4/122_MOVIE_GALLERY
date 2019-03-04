import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "Singlemovie", urlPatterns = "/api/single-movie")
public class Singlemovie extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();

            // Construct a query with parameter represented by "?"
            String query =
                    "select distinct a.id, a.title, a.year, a.director, " +
                            "GROUP_CONCAT(distinct a.genre_name) as genre_name, a.rating,  " +
                            "GROUP_CONCAT(distinct s.name order by s.id) as star_name,  " +
                            "GROUP_CONCAT(distinct s.id) as star_id " +
                            "from " +
                            "(select distinct m.id, m.title, m.year, m.director, " +
                            " GROUP_CONCAT(distinct g.name) as genre_name, r.rating " +
                            "from movies as m, ratings as r, genres as g, genres_in_movies as y " +
                            "where m.id=y.movieId and y.genreId=g.id and r.movieId=m.id " +
                            "group by m.id " +
                            "order by r.rating desc " +
                            ") as a,  " +
                            " " +
                            "stars as s, stars_in_movies as x " +
                            "where a.id=x.movieId and x.starId=s.id and x.movieId=?  " +
                            "group by a.id " +
                            "order by a.rating desc ";


            // Declare our statement
            PreparedStatement statement = dbcon.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String movie_title = rs.getString("title");
                String movie_id=rs.getString("id");
                String movie_year = rs.getString("year");
                String movie_director = rs.getString("director");
                String rate = rs.getString("rating");
                String star_id = rs.getString("star_id");
                String star_name = rs.getString("star_name");
                String genre_name=rs.getString("genre_name");

                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_title", movie_title);
                jsonObject.addProperty("movie_year", movie_year);
                jsonObject.addProperty("movie_director", movie_director);
                jsonObject.addProperty("list_g", genre_name);
                jsonObject.addProperty("list_s", star_name);
                jsonObject.addProperty("s.id", star_id);
                jsonObject.addProperty("rating",rate);
                jsonObject.addProperty("movie_id",movie_id);
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