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
import java.math.*;
import java.sql.Statement;
import java.util.ArrayList;

@WebServlet(name = "MetaServlet", urlPatterns = "/api/Meta")

public class MetaServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;
	
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();
		
		JsonArray jsonArray = new JsonArray();
		try {
			Connection dbcon = dataSource.getConnection();
			ArrayList<String> tables = new ArrayList<>();
			tables.add("explain movies");
			tables.add("explain stars");
			tables.add("explain stars_in_movies");
			tables.add("explain genres");
			tables.add("explain genres_in_movies");
			tables.add("explain ratings");
			tables.add("explain sales");
			tables.add("explain customers");
			tables.add("explain creditcards");
			tables.add("explain employees");
			
			
			Statement statement = dbcon.createStatement();
			ResultSet rs = null;
			
			for(int i=0; i<tables.size(); i++) {
				String query1 = tables.get(i);
				
				//PreparedStatement statement = dbcon.prepareStatement(query1);
				//statement.setString(1,one);
				//out.write("this is for testing");
				rs = statement.executeQuery(query1);
				
				while(rs.next()) {
					String field = rs.getString(1);
					String type = rs.getString(2);
					String isnull = rs.getString(3);
					String key = rs.getString(4);
					String isdefault = rs.getString(5);
					
					JsonObject jsonObject = new JsonObject();
					
					jsonObject.addProperty("tablename", tables.get(i));
					jsonObject.addProperty("field", field);
	                jsonObject.addProperty("type", type);
	                jsonObject.addProperty("isnull", isnull);
	                jsonObject.addProperty("key", key);
	                jsonObject.addProperty("isdefault", isdefault);
	                
	                jsonArray.add(jsonObject);
				}
				
			}
			
			// write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
