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
import java.sql.*;
import org.jasypt.util.password.StrongPasswordEncryptor;


/**
 * This class is declared as LoginServlet in web annotation,
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "DashLoginServlet", urlPatterns = "/api/dash_login")
public class Dashboard_login extends HttpServlet {
    private static final long serialVersionUID = 1L;
   // @Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String username = request.getParameter("username");
        String passwords = request.getParameter("password");
        PrintWriter out = response.getWriter();


        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
            // Verify reCAPTCHA
           try {
               request.getSession().setAttribute("counter", (int)1);
                RecaptchaVerifyUtils.verify(gRecaptchaResponse);
            } catch (Exception e) {
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
               responseJsonObject.addProperty("message", "Please do the recapcha verification");
               out.write(responseJsonObject.toString());
                out.close();
                return;
            }



        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");
            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            String email="";
            // Get a connection from dataSource
            Connection dbcon = ds.getConnection();
            String query1="select count(email) " +
                    "from employees " +
                    "where email=?  ";
            PreparedStatement statement1 = dbcon.prepareStatement(query1);
            statement1.setString(1, username);
            ResultSet rs1 = statement1.executeQuery();
            if(rs1.next()){
                email = rs1.getString("count(email)");
            }

            rs1.close();
            statement1.close();




            String query = "select password  " +
                    "from employees " +
                    "where email=?  " ;
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, username);
            //statement.setString(2, passwords);
            ResultSet rs = statement.executeQuery();
           /* String check = "";
            if(rs.next()){
                check = rs.getString("count(email)");
            }*/


            boolean success = false;
            if (rs.next()) {
                // get the encrypted password from the database
                String encryptedPassword = rs.getString("password");
                success = new StrongPasswordEncryptor().checkPassword(passwords, encryptedPassword);
            }



            rs.close();
            statement.close();
            dbcon.close();

            if (success==true) {
            //if (check.equals("1")) {
                request.getSession().setAttribute("employee_user", new User(username));

                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

                out.write(responseJsonObject.toString());
            }
            else {
                // Login fails
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");

                if (!email.equals("1")) {
                    responseJsonObject.addProperty("message", "User " + username + " does not exist");
                } else {
                    responseJsonObject.addProperty("message", "Incorrect Password");
                }


                out.write(responseJsonObject.toString());
            }
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


