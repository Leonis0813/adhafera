package application.view.table;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import application.controller.IController;

public class ExpensePanel extends AccountPanel {
	private static final long serialVersionUID = 1L;

	public ExpensePanel(IController ctrl){
		super();

		table = new ExpenseTable(tableModel, ctrl);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(720, 700));
		this.add(scrollPane);
	}
}
