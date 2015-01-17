package application.view.table;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import database.attribute.Category;
import database.attribute.Content;
import database.attribute.Date;
import database.attribute.Price;
import application.model.Account;

public class AccountPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	protected DefaultTableModel tableModel;
	protected AccountTable table;
	protected JScrollPane scrollPane;

	public AccountPanel() {
		super();

		this.setPreferredSize(new Dimension(720, 700));
		tableModel = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex){
				switch (columnIndex){
				case AccountTable.DATE:
					return Date.class;
				case AccountTable.CONTENT:
					return Content.class;
				case AccountTable.CATEGORY:
					return Category.class;
				case AccountTable.PRICE:
					return Price.class;
				default:return null;
				}
			}
		};
	}

	public void addAccount(Account account){
		Object[] record = { account.getDate(), account.getContent(), account.getCategory(), account.getPrice() };
		tableModel.addRow(record);
	}

	public void clearTable(){
		tableModel.setRowCount(0);
	}
}