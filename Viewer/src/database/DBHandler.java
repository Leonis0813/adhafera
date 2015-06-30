package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import log.LogWriter;
import database.attribute.Category;
import database.attribute.Content;
import database.attribute.Date;
import database.attribute.Price;
import application.model.Account;
import application.model.Expense;
import application.model.Income;
import application.view.table.AccountTable;

public class DBHandler {
	private static final String MySQLJDBCDriver = "org.gjt.mm.mysql.Driver";
	private static final String DBNAME = "account_test"; // Database Name
	private static final String USER = "root"; // user name for DB.
	private static final String PASS = "root"; // password for DB.
	private static final String JDBCDriver = MySQLJDBCDriver;
	private static final String DBURL = "jdbc:mysql://localhost:8889/" + DBNAME;

	public static final int NUM_TABLE = 2;
	public static final String[] TABLE = {"income", "expense"};
	public static final String[] TABLE_JP = {"収入", "支出"};
	
	public static final int NUM_COLUMN = 4;
	public static final String[] COLUMN = {"date", "content", "category", "price"};
	public static final String[] COLUMN_JP = {"日付", "内容", "カテゴリ", "金額"};

	private static Statement stmt = null;

	public DBHandler() {
		connectToDB( USER, PASS );
	}

	static Connection conn = null;

	public void connectToDB(String user, String pass){
		try {
			Class.forName(JDBCDriver).newInstance();
			System.setProperty("jdbc.driver",JDBCDriver);

			if( user.isEmpty() && pass.isEmpty() ){
				conn = DriverManager.getConnection(DBURL);
			}else{
				Properties prop = new Properties();
				prop.put("user", user);
				prop.put("password", pass);
				conn = DriverManager.getConnection(DBURL,prop);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("データベース「" + DBNAME + "」に接続しました。");			// notice field内で表示させる
	}

	public void disconnectFromDB(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("データベース「" + DBNAME + "」との接続を停止しました。");	// notice field内で表示させる
	}

	public static ResultSet executeQuery(String sql) throws SQLException{
		conn.setReadOnly(true);
		stmt = conn.createStatement();
		System.out.println("Execute Query:" + sql);
		LogWriter.writeln("  Execute SQL: " + sql, true);
		return stmt.executeQuery(sql);
	}

	public static int executeUpdate(String sql) throws SQLException{
		conn.setReadOnly(false);
		stmt = conn.createStatement();
		System.out.println("Execute Update:" + sql);
		LogWriter.writeln("  Execute SQL: " + sql, true);
		return stmt.executeUpdate(sql);
	}

	public Object[] getCategory(){
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("");
		try {
			ResultSet rs = executeQuery("SELECT DISTINCT category FROM income;");
			while(rs.next()){
		       	categories.add(rs.getString("category"));
			}
			rs = executeQuery("SELECT DISTINCT category FROM expense;");
			while(rs.next()){
		       	categories.add(rs.getString("category"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories.toArray();
	}

	/*
	 * 入力チェックをここで行わせるべきか検討
	 */
	public String check(String info){
		String[] str = info.split(",");
		if(str.length!=4){
			return "NG:element";
		}else{
			String[] str2 = str[0].split("-");
			if(str2.length!=3){
				return "NG:date";
			}else if(str[1].length()>100 || str[1].equals("")){
				return "NG:content";
			}else if(str[2].length()>100 || str[2].equals("")){
				return "NG:category";
			}else{
				Pattern p = Pattern.compile("^[0-9]*$");
		        Matcher m = p.matcher(str[3]);
				if(!m.find() || Integer.parseInt(str[3])<0){
					return "NG:price";
				}
			}
		}
		return "OK";
	}

	public ArrayList<Account> select(String[] attributes, String[] tables, String condition){
		ArrayList<Account> accountList = new ArrayList<Account>();
		
		for(int i=0;i<tables.length;i++){
			ArrayList<Account> tmp = this.select(attributes, tables[i], condition);
			for(int j=0;j<tmp.size();j++){
				accountList.add(tmp.get(j));
			}
			tmp.clear();
		}
		
		return accountList;
	}
	
	public ArrayList<Account> select(String[] attributes, String table, String condition){
		String sql =  "SELECT ";
		for(int i=0;i<attributes.length;i++){
			sql += i==0 ? attributes[i] : ","+attributes[i];
		}
		sql += " FROM " + table;
		sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;
		sql += " ORDER BY date";

		ArrayList<Account> accountList = new ArrayList<Account>();
		try {
			ResultSet rs = executeQuery(sql);
			LogWriter.writeln("    >> Select[", false);
			Account account = null;
			while(rs.next()){
				if(table.equals("income")){
					account = new Income();
				} else if(table.equals("expense")){
					account = new Expense();
				}
				for(int i=0;i<attributes.length;i++){
					if(attributes[i].equals("date")){
						account.setDate(new Date(rs.getDate("date")));
					}else if(attributes[i].equals("content")){
						account.setContent(new Content(rs.getString("content")));
					}else if(attributes[i].equals("category")){
						account.setCategory(new Category(rs.getString("category")));
					}else if(attributes[i].equals("price")){
						account.setPrice(new Price(rs.getInt("price")));
					}
				}
				accountList.add(account);
				LogWriter.writeln("              " + account.getDateString() + ", " + account.getContent().toString() +
						", " + account.getCategory().toString() + ", " + account.getPriceString(), false);
			}
			LogWriter.writeln("             ]", false);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return accountList;
	}

	public void update(String table, String[] set, String condition){
		try{
			conn.setReadOnly(true);
			stmt = conn.createStatement();
			String sql = "SELECT * FROM " + table;
			sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;
			ResultSet rs_before = stmt.executeQuery(sql);
			stmt.close();
			
			sql = "UPDATE " + table + " SET ";
			for(int i=0;i<set.length;i++){
				sql += i==0 ? set[i] : ","+set[i];
			}
			sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;

			int result = executeUpdate(sql);
			System.out.println("更新件数：" + result + "件");
			stmt.close();
			
			conn.setReadOnly(true);
			stmt = conn.createStatement();
			sql = "SELECT * FROM " + table;
			sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;
			ResultSet rs_after = stmt.executeQuery(sql);
			stmt.close();
			
			LogWriter.writeln("    >> Update[", false);
			while(rs_before.next() && rs_after.next()){
				LogWriter.write("{" + rs_before.getDate(COLUMN[AccountTable.DATE]) + " " + rs_after.getDate(COLUMN[AccountTable.DATE]) + "}, ", false);
				LogWriter.write("{" + rs_before.getString(COLUMN[AccountTable.CONTENT]) + " " + rs_after.getDate(COLUMN[AccountTable.CONTENT]) + "}, ", false);
				LogWriter.write("{" + rs_before.getString(COLUMN[AccountTable.CATEGORY]) + " " + rs_after.getDate(COLUMN[AccountTable.CATEGORY]) + "}, ", false);				
				LogWriter.writeln("{" + rs_before.getInt(COLUMN[AccountTable.PRICE]) + " " + rs_after.getInt(COLUMN[AccountTable.PRICE]) + "}", false);
			}
			LogWriter.writeln("             ]", false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Account insert(String table, String[] values) throws SQLException{
		String sql = "SELECT MAX(id) AS NUM_RECORD FROM " + table + ";";
		ResultSet rs = executeQuery(sql);
		rs.next();
		int num = rs.getInt("NUM_RECORD")+1;

		sql = "INSERT INTO " + table + " VALUES(" + num + ",'" + values[AccountTable.DATE] + "', '" + values[AccountTable.CONTENT] +
				"', '" + values[AccountTable.CATEGORY] + "', '" + values[AccountTable.PRICE] + "');";
		int result = executeUpdate(sql);
		System.out.println("更新件数：" + result + "件");
		stmt.close();
		LogWriter.writeln("    >> Insert[", false);
		LogWriter.writeln("               " + values[AccountTable.DATE] + ", " + values[AccountTable.CONTENT] +
				", " + values[AccountTable.CATEGORY] + ", " + values[AccountTable.PRICE], false);
		LogWriter.writeln("             ]", false);
		
		if(table.equals("income")){
			return new Income(new Date(values[AccountTable.DATE]), new Content(values[AccountTable.CONTENT]),
					new Category(values[AccountTable.CATEGORY]), new Price(Integer.parseInt(values[AccountTable.PRICE])));			
		}else if(table.equals("expense")){
			return new Expense(new Date(values[AccountTable.DATE]), new Content(values[AccountTable.CONTENT]),
					new Category(values[AccountTable.CATEGORY]), new Price(Integer.parseInt(values[AccountTable.PRICE])));
		}else{
			return null;
		}
	}

	public void delete(String table, String condition){
		try {
			String sql = "SELECT * FROM " + table;
			conn.setReadOnly(true);
			stmt = conn.createStatement();
			sql = "SELECT * FROM " + table;
			sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;
			ResultSet rs = stmt.executeQuery(sql);
			stmt.close();
		
			sql = "DELETE FROM " + table;
			sql += condition.equals("") ? " WHERE 1" : " WHERE " + condition;

			int result = executeUpdate(sql);
			LogWriter.writeln("    >> Delete[", false);
			while(rs.next()){
				LogWriter.writeln("              " + rs.getDate(COLUMN[AccountTable.DATE]) + ", " + rs.getString(COLUMN[AccountTable.CONTENT]) +
						", " + rs.getString(COLUMN[AccountTable.CATEGORY]) + ", " + rs.getInt(COLUMN[AccountTable.PRICE]), false);
			}
			LogWriter.writeln("             ]", false);
			System.out.println("更新件数：" + result + "件");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Settlement> getSettlement(String interval){
		String sql = "SELECT date AS day, MONTH(date) AS month, YEAR(date) AS year, sum(price) AS sum " +
		"FROM ( SELECT date, price FROM income UNION ALL SELECT date, -1*price FROM expense ) AS ACCOUNT " +
		"GROUP BY " + interval + " ORDER BY " + interval + ";";
		ResultSet rs;
		ArrayList<Settlement> settlementList = new ArrayList<Settlement>();
		try {
			rs = executeQuery(sql);
			while(rs.next()){
				settlementList.add(new Settlement(interval, rs.getInt("year"), rs.getInt("month"),
						rs.getDate("day").toString(), rs.getInt("sum")));
			}
			rs.close();
			stmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return settlementList;
	}
}
