package application.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import database.DBHandler;
import application.controller.IController;
import application.view.table.AccountTable;

public class Dialog_Insert extends Dialog {
	private static final long serialVersionUID = 1L;

	private final int WIDTH = 620;
	private final int HEIGHT = 265;

	private JTextField[] date_tf;

	private JTextField content, category, price;
	private JRadioButton[] table_rb;
	private ButtonGroup group;
	
	private JButton nextButton;

	public Dialog_Insert(){
		super("Insert");
	}

	public void createContents(IController ctrl){
		super.setModal(true);
		super.ctrl = ctrl;

		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout());

		this.add( this.createDatePanel() );
		this.add( this.createContentPanel() );
		this.add( this.createCategoryPanel() );
		this.add( this.createPricePanel() );
		this.add( this.createTablePanel() );
		this.add( this.createButtonPanel() );

		this.setResizable(false);

		this.ctrl = ctrl;
	}

	private JPanel createDatePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("日付：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70,30));
		panel.add(label);

		date_tf = new JTextField[DATE.length];
		for(int i=0;i<DATE.length;i++){
			date_tf[i] = new JTextField();
			date_tf[i].setPreferredSize(new Dimension(70, 30));
			panel.add(date_tf[i]);
			panel.add(new JLabel(DATE[i]));
		}

		return panel;
	}

	private JPanel createContentPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("内容：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70,30));
		panel.add(label);

		content = new JTextField(30);
		content.setPreferredSize(new Dimension(445, 30));
		panel.add(content);

		return panel;
	}

	private JPanel createCategoryPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("カテゴリ：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70,30));
		panel.add(label);

		category = new JTextField();
		category.setPreferredSize(new Dimension(150, 30));
		panel.add(category);

		return panel;
	}

	private JPanel createPricePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("金額：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70,30));
		panel.add(label);

		price = new JTextField(10);
		panel.add(price);

		return panel;
	}

	private JPanel createTablePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		panel.add(new JLabel("挿入するテーブルを選択してください："));
		table_rb = new JRadioButton[DBHandler.NUM_TABLE];
		group = new ButtonGroup();
		for(int i=0;i<DBHandler.NUM_TABLE;i++){
			table_rb[i] = new JRadioButton(DBHandler.TABLE_JP[i]);
			group.add(table_rb[i]);
			panel.add(table_rb[i]);
		}

		return panel;
	}

	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		nextButton = new JButton("NEXT");
		OKButton = new JButton("OK");
		cancelButton = new JButton("CANCEL");
		nextButton.addActionListener(this);
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
		panel.add(nextButton);
		panel.add(OKButton);
		panel.add(cancelButton);

		return panel;
	}

	public void actionPerformed(ActionEvent arg0){
		Object obj = arg0.getSource();
		if(obj == OKButton || obj == nextButton){
			if(inputChecker.checkBlank(table_rb, date_tf, content, category, price)){
				if(inputChecker.checkFormat(date_tf, content, category, price)){
					String[] values = new String[DBHandler.NUM_COLUMN];
					values[AccountTable.DATE] = format(date_tf);
					values[AccountTable.CONTENT] = content.getText();
					values[AccountTable.CATEGORY] = category.getText();
					values[AccountTable.PRICE] = price.getText();
					String table = "";
					if(table_rb[0].isSelected()){
						table = "income";
					}else if(table_rb[1].isSelected()){
						table = "expense";
					}
					try {
						ctrl.controllInsert(table, values);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(this, "入力内容が正しくありません");
					return;
				}
			}else{
				JOptionPane.showMessageDialog(this, "必要な情報が入力されていません");
				return;
			}
		}
		if(obj!=nextButton){
			this.dispose();
		}else{
			for(int i=0;i<date_tf.length;i++){
				date_tf[i].setText("");
			}
			content.setText("");
			category.setText("");
			price.setText("");
		}
	}

	private String format(JTextField[] date){
		String date_str = date[0].getText() + "-";
		if(Integer.parseInt(date[1].getText()) < 10){
			date_str += "0";
		}
		date_str += date[1].getText() + "-";
		if(Integer.parseInt(date[2].getText()) < 10){
			date_str += "0";
		}
		date_str += date[2].getText();
		return date_str;
	}
}