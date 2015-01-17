package application.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBHandler;


public class Model extends DBHandler {
	
	public Model() {
		super();
	}
	
	public boolean haveAccount(String table, String[] columns) {
		try{
			String sql = "SELECT COUNT(*) as count FROM " + table + " WHERE date = '" + columns[0]+ "' AND content = '" + columns[1] + "' AND"
					+ " category = '"+ columns[2] + "' AND price = '" + columns[3] + "'";
			ResultSet rs = DBHandler.executeQuery(sql);
			
			return rs.getInt("count") > 0;
		}catch (SQLException e) {
			return false;
		}
	}
}
