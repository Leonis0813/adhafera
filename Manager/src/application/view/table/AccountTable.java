package application.view.table;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import database.DBHandler;
import application.controller.IController;

public class AccountTable extends JTable implements MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	
	public static final int INCOME = 0;
	public static final int EXPENSE = 1;
	
	public static final int DATE = 0;
	public static final int CONTENT = 1;
	public static final int CATEGORY = 2;
	public static final int PRICE = 3;

	protected IController ctrl;

	protected int selectedRow;
	protected String[] before;
	protected String[] after;
	protected boolean isSelecting;
	
	protected TableRowSorter<DefaultTableModel> sorter;

	public AccountTable(DefaultTableModel tableModel, IController ctrl) {
		super(tableModel);
		this.ctrl = ctrl;

		for(int i=0;i<DBHandler.NUM_COLUMN;i++){
			tableModel.addColumn(DBHandler.COLUMN_JP[i]);
		}

		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		this.setColumnSelectionAllowed(true);
		this.setRowSelectionAllowed(true);
		
		sorter = new TableRowSorter<DefaultTableModel>(tableModel);
		this.setRowSorter(sorter);

		selectedRow = -1;
		before = new String[DBHandler.NUM_COLUMN];
		after = new String[DBHandler.NUM_COLUMN];
		isSelecting = false;
	}

	/*
	 *  条件のフォーマット
	 *   date: "[<更新前日付>]:[<更新後日付>]"
	 *   content: "[<更新前内容>]:[<更新後内容>]"
	 *   category: "[<更新前カテゴリ>]:[<更新後カテゴリ>]"
	 *   price: "[<更新前金額>]:[<更新後金額>]"
	 */

	protected String[] extractSetInfo(){
		ArrayList<String> setList = new ArrayList<String>();
		for(int i=0;i<after.length;i++){
			if(!after[i].equals(before[i])){
				if(i == AccountTable.PRICE){
					setList.add(DBHandler.COLUMN[i] + " = " + after[i]);
				}else{
					setList.add(DBHandler.COLUMN[i] + " = '" + after[i] + "'");
				}
			}
		}
		return (String[])setList.toArray();
	}
	
	protected String extractCondition(){
		String where = "";
		for(int i=0;i<before.length;i++){
			where += where.equals("") ? before[i] : " AND " + before[i];
		}
		return where;
	}
	
	public String extractDateInfo(){
		return before[0].toString() + ":" + after[0].toString();
	}

	public String extractContentInfo(){
		return before[1].toString() + ":" + after[1].toString();
	}

	public String extractCategoryInfo(){
		return before[2].toString() + ":" + after[2].toString();
	}

	public String extractPriceInfo(){
		return before[3].toString() + ":" + after[3].toString();
	}

	public void mouseClicked(MouseEvent arg0){
		if(!isSelecting){
			selectedRow = this.getSelectedRow();
			for(int i=0;i<DBHandler.NUM_COLUMN;i++){
				before[i] = this.getValueAt(selectedRow, i).toString();
			}
			isSelecting = true;
		}else{
			int currentRow = this.getSelectedRow();
			if(selectedRow != currentRow){
				for(int i=0;i<DBHandler.NUM_COLUMN;i++){
					this.setValueAt(before[i], selectedRow, i);
					before[i] = this.getValueAt(currentRow, i).toString();
				}
			}
			selectedRow = currentRow;
		}
	}

	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0){}
	public void mousePressed(MouseEvent arg0){}
	public void mouseReleased(MouseEvent arg0){}

	public void keyPressed(KeyEvent arg0){}
	public void keyReleased(KeyEvent arg0){}
	public void keyTyped(KeyEvent arg0){}
}
