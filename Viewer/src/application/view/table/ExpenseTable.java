package application.view.table;

import java.awt.event.KeyEvent;

import javax.swing.table.DefaultTableModel;

import database.DBHandler;
import application.controller.IController;

public class ExpenseTable extends AccountTable {
	private static final long serialVersionUID = 1L;

	public ExpenseTable(DefaultTableModel tableModel, IController ctrl){
		super(tableModel, ctrl);
	}
	
	public void keyReleased(KeyEvent arg0){
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
			for(int i=0;i<DBHandler.NUM_COLUMN;i++){
				after[i] = this.getValueAt(selectedRow, i).toString();
				if(after[i].equals("")){
					this.setValueAt(before[i], selectedRow, i);
					return;
				}
			}
			String[] tables = {"expense"};
			ctrl.controllUpdate(tables, this.extractSetInfo(), this.extractCondition());
			isSelecting = false;
		}
	}
}