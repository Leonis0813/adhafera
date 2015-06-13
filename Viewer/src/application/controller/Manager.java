package application.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBHandler;
import database.Settlement;
import application.model.Account;
import application.view.Receptor;

public class Manager {

	private DBHandler dbHandler;
	private Receptor receptor;

	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		new Manager();
	}

	private Manager() throws IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		dbHandler = new DBHandler();
		receptor = new Receptor();

		String input = "";
		while(true){
			input = receptor.receiveUserInput();
			if(input.equals("1")){
				String table = receptor.receiveTableInfo();
				if(table.equals("income") || table.equals("expense")){
					String info = receptor.receiveAccountInfo();
					String result = dbHandler.check(info);
					if(result.equals("OK")){
						dbHandler.insert(table, info.split(","));
						receptor.noticeRegister();
					}else{
						receptor.noticeError(result);
					}
				}
			}else if(input.equals("2")){
				ArrayList<Account> accountList = dbHandler.select(DBHandler.COLUMN, DBHandler.TABLE, "");
				receptor.showAccount(accountList);
				accountList.clear();
			}else if(input.equals("3")){
				String interval = receptor.receiveProcessingMethod();
				if(interval.equals("year") || interval.equals("month") || interval.equals("day")){
					ArrayList<Settlement> settlementList = dbHandler.getSettlement(interval);
					receptor.showSettlement(settlementList);
					settlementList.clear();
				}
			}else if(input.equals("0")){
				dbHandler.disconnectFromDB();
				break;
			}
		}
	}
}
