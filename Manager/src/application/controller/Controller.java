package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import log.LogWriter;
import database.DBHandler;
import database.Settlement;
import application.model.Account;
import application.model.Model;
import application.view.View;
import application.view.dialog.Dialog_Delete;
import application.view.dialog.Dialog_Insert;
import application.view.dialog.Dialog_Select;
import application.view.dialog.Dialog_Settlement;
import application.view.dialog.Dialog_Update;
import application.view.table.AccountTable;

public class Controller implements IController{
	public static void main(String[] args){
		new Controller();
	}

	private View view;
	private Model model;

	private Controller(){
		if(!LogWriter.openFile("DB_Operation.log")){
			System.out.println("ログファイル読み込み中にエラーが発生しました。");
			this.shutdownSystem();
		}
		LogWriter.writeln("START application", true);
		model = new Model();
		view = new View(this);
		this.initialize();
	}

	public void initialize(){
		this.controllSelect(DBHandler.COLUMN, DBHandler.TABLE, "");
	}

	/*
	 *  終了操作
	 *   Vからの通知を受けると，Mにデータベースとの接続を切断するように指示．
	 */

	public void shutdownSystem(){
		model.disconnectFromDB();
		LogWriter.writeln("END application", true);
		LogWriter.closeFile();
		System.exit(0);
	}

	public void createDialog(String operation){
		if(operation.equals("select")){
			view.createDialog(new Dialog_Select(), this);
		}else if(operation.equals("update")){
			view.createDialog(new Dialog_Update(), this);
		}else if(operation.equals("insert")){
			view.createDialog(new Dialog_Insert(), this);
		}else if(operation.equals("delete")){
			view.createDialog(new Dialog_Delete(), this);
		}
	}

	public Object[] getCategory(){
		return model.getCategory();
	}

	/*
	 *  選択操作
	 *   Mに条件を渡して選択結果を返すように指示．返ってきた結果をVに表示するように指示．
	 */

	/*
	 *    ダイアログから条件を受け取り，Mに渡す．返ってきた結果をVに表示するように指示．
	 */
	public void controllSelect(String[] attributes, String[] tables, String condition){
		int table = -1;
		for(int i=0;i<tables.length;i++){
			if(tables[i].equals("income")){
				table = AccountTable.INCOME;
			}else if(tables[i].equals("expense")){
				table = AccountTable.EXPENSE;
			}else{
				System.out.println("ERROR: 不正なテーブルへのアクセス");
				this.shutdownSystem();
			}
			view.clearTable(table);
			ArrayList<Account> accountList = model.select(attributes, tables[i], condition);
			Iterator<Account> it = accountList.iterator();
			while(it.hasNext()){
				Account account = it.next();
				view.updateTable(table, account);
			}
		}
	}

	/*
	 *  更新操作
	 *   Mに条件と値を渡して更新結果を返すように指示．返ってきたデータベースをVに表示するように指示．
	 */

	/*
	 *    メニューから通知を受け取り，Vにダイアログを作成させる．
	 */
	public void controllUpdate(String[] tables, String[] set, String condition){
		for(int i=0;i<tables.length;i++){
			model.update(tables[i], set, condition);
			this.controllSelect(DBHandler.COLUMN, DBHandler.TABLE, "");
		}
	}

	/*
	 *  挿入操作
	 *   Mに値を渡して挿入結果を返すように指示．返ってきたデータベースをVに表示するように指示．
	 */

	public void controllInsert(String table, String[] values) throws SQLException{
		Account account = model.insert(table, values);
		view.notice("データベースに登録されました。");
		if(table.equals("income")){
			view.updateTable(AccountTable.INCOME, account);	
		}else if(table.equals("expense")){
			view.updateTable(AccountTable.EXPENSE, account);
		}
	}

	/*
	 *  削除操作
	 *   Mに条件を渡して削除結果を返すように指示．返ってきたデータベースをVに表示するように指示．
	 */

	public void controllDelete(String[] tables, String condition){
		for(int i=0;i<tables.length;i++){
			model.delete(tables[i], condition);
		}
		this.controllSelect(DBHandler.COLUMN, DBHandler.TABLE, "");
	}

	/*
	 *  収支計算操作
	 *   Vからの通知を受けると，Mに収支を計算するように指示．また，返ってきた収支リストをVに渡す．
	 */

	public void controllSettlement(String interval){
		ArrayList<Settlement> settlementList = model.getSettlement(interval);
		view.createDialog(new Dialog_Settlement(settlementList), this);
	}
}