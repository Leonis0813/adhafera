package application.view.table;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import application.controller.IController;

public class IncomePanel extends AccountPanel {
	private static final long serialVersionUID = 1L;

	public IncomePanel(IController ctrl){
		super();

		table = new IncomeTable(tableModel, ctrl);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(720, 700));
		this.add(scrollPane);
	}
}
