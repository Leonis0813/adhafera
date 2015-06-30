package application.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import database.Settlement;
import application.controller.IController;

public class Dialog_Settlement extends Dialog {
	private static final long serialVersionUID = 1L;

	private final int WIDTH = 400;
	private final int HEIGHT = 500;

	private DefaultTableModel tableModel;
	private JTable settlementTable;
	private JScrollPane scrollPane;

	private static final String[] columnNames = {"日付", "金額"};

	private ArrayList<Settlement> settlementList;

	public Dialog_Settlement(ArrayList<Settlement> settlementList){
		super("Settlement");
		this.settlementList = settlementList;
	}

	public void createContents(IController ctrl){
		super.setModal(true);
		super.ctrl = ctrl;

		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));

		this.add( this.createSettlementTable() );
		this.add( this.createButtonPanel() );
		this.displaySettlement();

		this.setResizable(false);
	}

	private JScrollPane createSettlementTable(){
		tableModel = new DefaultTableModel();
		settlementTable = new JTable(tableModel);
		for(int i=0;i<columnNames.length;i++){
			tableModel.addColumn(columnNames[i]);
		}
		scrollPane = new JScrollPane(settlementTable);
		scrollPane.setPreferredSize(new Dimension(WIDTH-50, HEIGHT-80));

		return scrollPane;
	}

	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		OKButton = new JButton("OK");
		OKButton.addActionListener(this);
		panel.add(OKButton);

		return panel;
	}

	public void setSettlementList(ArrayList<Settlement> settlementList){
		this.settlementList = settlementList;
	}

	private void displaySettlement(){
		Iterator<Settlement> it = settlementList.iterator();
		while(it.hasNext()){
			Settlement s = it.next();
			String[] record = {s.getDate().toString(), s.getPrice().toString()};
			tableModel.addRow(record);
		}
	}

	public void actionPerformed(ActionEvent arg0){
		JButton button = (JButton) arg0.getSource();
		if(button==OKButton){
			this.settlementList.clear();
			this.dispose();
		}
	}

}
