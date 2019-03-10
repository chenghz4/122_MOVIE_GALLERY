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
import java.util.Calendar;

/**
 * This class is declared as LoginServlet in web annotation,
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "Payservlet", urlPatterns = "/api/pay")
public class Payservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
   // @Resource(name = "jdbc/moviedb")


    //private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession();
        String cardnumber = request.getParameter("cardnumber");
        String lastname = request.getParameter("lastname");
        String firstname = request.getParameter("firstname");
        String expiration = request.getParameter("date");
        String check="";
        String card="";
        PrintWriter out = response.getWriter();


        try {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            // Get a connection from dataSource
            Connection dbcon = ds.getConnection();

            String query1="select count(id) " +
                    "from creditcards " +
                    "where id=?  and firstName=? and lastName=? and expiration=? ";
            PreparedStatement statement1 = dbcon.prepareStatement(query1);
            statement1.setString(1, cardnumber);
            statement1.setString(2, firstname);
            statement1.setString(3, lastname);
            statement1.setString(4, expiration);
            ResultSet rs1 = statement1.executeQuery();
            if(rs1.next()){
                check = rs1.getString("count(id)");
            }
            rs1.close();
            statement1.close();
//


            String query = "select count(id) " +
                    "from creditcards " +
                    "where id=?  " ;
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, cardnumber);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                card = rs.getString("count(id)");
            }
            rs.close();
            statement.close();

//
            User user=(User) session.getAttribute("user");
            String usern=user.getUsername();
            String query2 ="select id " +
                    "from customers " +
                    "where email=? ";

            PreparedStatement statement2 = dbcon.prepareStatement(query2);
            statement2.setString(1, usern);
            ResultSet rs2 = statement2.executeQuery();
            String customerid="";
            if(rs2.next()){
                 customerid= rs2.getString("id");
            }
            rs2.close();
            statement2.close();

//

//




            if (!check.equals("0")) {
                ArrayList<Items> data = (ArrayList<Items>) session.getAttribute("previousmovies");
                int n=data.size();




                if(n>1&&!customerid.equals("")) {

                    for(int i=1;i<n;i++) {
                        Calendar cal=Calendar.getInstance();


                        String query3 = "INSERT INTO sales VALUES(?,?,?, ?);";
                        String movieid="";
                        movieid=data.get(i).getId();
                        String movienumber=data.get(i).getNumber();
                        if(!movieid.equals("")&&!movienumber.equals("")) {
                            int size=Integer.parseInt(movienumber);
                            for(int j=0;j<size;j++) {



                                String query4 = "select count(id) " +
                                        "from sales ";
                                PreparedStatement statement4 = dbcon.prepareStatement(query4);
                                ResultSet rs4 = statement4.executeQuery();
                                String offset="";
                                if(rs4.next()){
                                    offset = rs4.getString("count(id)");
                                }
                                rs4.close();
                                statement4.close();



                                int a=Integer.parseInt(offset)+6;
                                data.get(i).assignsaleid(a+"",j);
                                Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                                PreparedStatement statement3 = dbcon.prepareStatement(query3);
                                statement3.setInt(1, a);
                                statement3.setInt(2, Integer.parseInt(customerid));
                                statement3.setString(3, movieid);
                                statement3.setDate(4, date);


                                statement3.executeUpdate();
                                statement3.close();
                            }
                        }
                    }
                }



                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

                out.write(responseJsonObject.toString());
            }
            else {
                // Login fails
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");

                if (card.equals("0")) {
                    responseJsonObject.addProperty("message", "cardnumber " + cardnumber + " doesn't exist");
                } else {
                    responseJsonObject.addProperty("message", "card information doesnt match");
                }


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
            response.setStatus(500);

        }
    }
}
