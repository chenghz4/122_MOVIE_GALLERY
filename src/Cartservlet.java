import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below,
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "Cartservlet", urlPatterns = "/api/cart")
public class Cartservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String movieid=request.getParameter("id");
        PrintWriter out = response.getWriter();
        int flag=0;
        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String query ="select id, title, year,director " +
                    "from movies " +
                    "where id=? ";
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, movieid);
            ResultSet rs = statement.executeQuery();
            String movie_id = "";
            String movie_title ="";

            if(rs.next()){
                movie_id = rs.getString("id");
                movie_title = rs.getString("title");
            }

            ArrayList<Items> previousmovies = (ArrayList<Items>) session.getAttribute("previousmovies");

            if (previousmovies == null) {
                previousmovies = new ArrayList<>();
                Items item=new Items("Shopping Cart is Empty","1","");

                previousmovies.add(item);

                if(!movie_title.equals("")) {
                    Items item1=new Items(movie_title,"1",movie_id);
                    previousmovies.add(item1);

                }
                session.setAttribute("previousmovies", previousmovies);
            }

            else {
                int n=previousmovies.size();
                for(int i=0;i<n;i++){
                    String str=previousmovies.get(i).getTitle();
                    if(str.equals(movie_title))    flag=1;
                    String str1=previousmovies.get(i).getNumber();
                    if(str1.equals("0")) flag=1;
                }

                if(n>1) {
                    String q[];
                    q=new String[n-1];

                    for (int i = 2; i < 2 * n; i += 2) {

                        String string = i + "";
                        q[i/2-1]=request.getParameter(string);
                    }
                    for(int i=1;i<n;i++){
                        boolean flag1;
                        try {
                            Integer.parseInt(q[i-1]);
                            flag1=true;
                        } catch (NumberFormatException e) {
                            flag1= false;
                        }
                        if(q[i-1]!=""&&q[i-1]!=null&&Integer.parseInt(q[i-1])>=0&&flag1)
                            previousmovies.get(i).assignnumber(q[i-1]);
                    }
//
                    int counter=1;
                    boolean flag2=true;
                    while(flag2){
                        int size1=previousmovies.size();
                        if(previousmovies.get(counter).getNumber().equals("0")){
                            previousmovies.remove(counter);
                            size1=previousmovies.size();
                        }
                        else counter++;



                        if(counter==size1) flag2=false;


                    }
//
                }

                if(!movie_title.equals("")&&flag==0) {
                    synchronized (previousmovies) {

                        Items item1=new Items(movie_title,"1",movie_id);
                        previousmovies.add(item1);

                    }
                }
                session.setAttribute("previousmovies", previousmovies);
            }



            int n=previousmovies.size();
            for(int i=0;i<n;i++) {
                if(i==(n-1)) {
                    out.write(previousmovies.get(i).getTitle());
                    out.write(",");
                    out.write(previousmovies.get(i).getNumber());
                }
                else{
                    out.write(previousmovies.get(i).getTitle());
                    out.write(",");
                    out.write(previousmovies.get(i).getNumber());
                    out.write(",");



                }
            }

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


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();


        PrintWriter out = response.getWriter();

        try {
            // Get a connection from dataSource
            Connection dbcon = dataSource.getConnection();
            String query ="";
            PreparedStatement statement = dbcon.prepareStatement(query);
            ResultSet rs = statement.executeQuery();


            while(rs.next())
            {

            }









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
