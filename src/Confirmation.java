


import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/api/confirmation")
public class Confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();


        ArrayList<Items> data = (ArrayList<Items>) session.getAttribute("previousmovies");
        int n=data.size();
        for(int i=0;i<n;i++) {
            int size=data.get(i).saleid.size();
            for(int j=0;j<size;j++){
                out.write(data.get(i).getTitle());
                out.write(",");
                out.write("1");
                out.write(",");
                out.write(data.get(i).saleid.get(j));
                out.write(",");
            }




        }
        out.close();
    }
}