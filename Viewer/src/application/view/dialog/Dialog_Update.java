package application.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import database.DBHandler;
import application.controller.IController;

public class Dialog_Update extends Dialog {
	private static final long serialVersionUID = 1L;

	private final int WIDTH = 770;
	private final int HEIGHT = 300;
	private final int WIDTH_SUB = 380;
	private final int HEIGHT_SUB = 190;

	/* 更新条件用コンポーネント */
	/* 日付用コンポーネント */
	private JTextField[] date_before;
	/* 内容用コンポーネント */
	private JTextField content_before;
	/* カテゴリ用コンポーネント */
	private JComboBox category_before;
	private String[] categories;
	/* 金額用コンポーネント */
	private JTextField price_before;

	/* 更新後用コンポーネント */
	/* 日付用コンポーネント */
	private JTextField[] date_after;
	/* 内容用コンポーネント */
	private JTextField content_after;
	/* カテゴリ用コンポーネント */
	private JTextField category_after;
	/* 金額用コンポーネント */
	private JTextField price_after;

	/* テーブル用コンポーネント */
	private JRadioButton[] table;

	public Dialog_Update(){
		super("Update");
	}

	public void createContents(IController ctrl){
		super.setModal(true);
		super.ctrl = ctrl;
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.add( this.createBeforePanel() );
		this.add( this.createAfterPanel() );
		this.add( this.createTablePanel() );
		this.add( this.createButtonPanel() );

		this.setResizable(false);

		this.ctrl = ctrl;
	}

	private JPanel createBeforePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH_SUB, HEIGHT_SUB));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		panel.add(new JLabel("更新条件"));

		// 日付部分作成
		JPanel datePanel = new JPanel();
		datePanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		datePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel dateLabel = new JLabel("日付：");
		dateLabel.setHorizontalAlignment(JLabel.RIGHT);
		dateLabel.setPreferredSize(new Dimension(70, 30));
		datePanel.add(dateLabel);

		date_before = new JTextField[DATE.length];
		for(int i=0;i<DATE.length;i++){
			date_before[i] = new JTextField("");
			date_before[i].setPreferredSize(new Dimension(70, 30));
			datePanel.add(date_before[i]);
			datePanel.add(new JLabel(DATE[i]));
		}
		panel.add(datePanel);

		// 内容部分作成
		JPanel contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel contentLabel = new JLabel("内容：");
		contentLabel.setHorizontalAlignment(JLabel.RIGHT);
		contentLabel.setPreferredSize(new Dimension(70,35));
		contentPanel.add(contentLabel);
		content_before = new JTextField(10);
		contentPanel.add(content_before);
		panel.add(contentPanel);

		// カテゴリ部分作成
		JPanel categoryPanel = new JPanel();
		categoryPanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel categoryLabel = new JLabel("カテゴリ：");
		categoryLabel.setHorizontalAlignment(JLabel.RIGHT);
		categoryLabel.setPreferredSize(new Dimension(70,35));
		categoryPanel.add(categoryLabel);

		Object[] categories = ctrl.getCategory();
		category_before = new JComboBox(categories);
		this.categories = new String[categories.length];
		for(int i=0;i<categories.length;i++){
			this.categories[i] = categories[i].toString();
		}

		category_before.setPreferredSize(new Dimension(150, 35));
		categoryPanel.add(category_before);
		panel.add(categoryPanel);

		// 金額部分作成
		JPanel pricePanel = new JPanel();
		pricePanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		pricePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel priceLabel = new JLabel("金額：");
		priceLabel.setHorizontalAlignment(JLabel.RIGHT);
		priceLabel.setPreferredSize(new Dimension(70,35));
		pricePanel.add(priceLabel);

		price_before = new JTextField(10);
		pricePanel.add(price_before);
		panel.add(pricePanel);

		return panel;
	}

	private JPanel createAfterPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH_SUB, HEIGHT_SUB));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		panel.add(new JLabel("更新後"));

		// 日付部分作成
		JPanel datePanel = new JPanel();
		datePanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		datePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel dateLabel = new JLabel("日付：");
		dateLabel.setHorizontalAlignment(JLabel.RIGHT);
		dateLabel.setPreferredSize(new Dimension(70, 30));
		datePanel.add(dateLabel);

		date_after = new JTextField[DATE.length];
		for(int i=0;i<DATE.length;i++){
			date_after[i] = new JTextField("");
			date_after[i].setPreferredSize(new Dimension(70, 30));
			datePanel.add(date_after[i]);
			datePanel.add(new JLabel(DATE[i]));
		}
		panel.add(datePanel);

		// 内容部分作成
		JPanel contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel contentLabel = new JLabel("内容：");
		contentLabel.setHorizontalAlignment(JLabel.RIGHT);
		contentLabel.setPreferredSize(new Dimension(70,35));
		contentPanel.add(contentLabel);
		content_after = new JTextField(10);
		contentPanel.add(content_after);
		panel.add(contentPanel);

		// カテゴリ部分作成
		JPanel categoryPanel = new JPanel();
		categoryPanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel categoryLabel = new JLabel("カテゴリ：");
		categoryLabel.setHorizontalAlignment(JLabel.RIGHT);
		categoryLabel.setPreferredSize(new Dimension(70,35));
		categoryPanel.add(categoryLabel);

		category_after = new JTextField(10);
		categoryPanel.add(category_after);
		panel.add(categoryPanel);

		// 金額部分作成
		JPanel pricePanel = new JPanel();
		pricePanel.setPreferredSize(new Dimension(WIDTH_SUB, 35));
		pricePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel priceLabel = new JLabel("金額：");
		priceLabel.setHorizontalAlignment(JLabel.RIGHT);
		priceLabel.setPreferredSize(new Dimension(70,35));
		pricePanel.add(priceLabel);

		price_after = new JTextField(10);
		pricePanel.add(price_after);
		panel.add(pricePanel);

		return panel;
	}

	private JPanel createTablePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		panel.add(new JLabel("更新するテーブルを選択してください："));
		table = new JRadioButton[DBHandler.NUM_TABLE];
		for(int i=0;i<DBHandler.NUM_TABLE;i++){
			table[i] = new JRadioButton(DBHandler.TABLE_JP[i]);
			panel.add(table[i]);
		}

		return panel;
	}

	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		OKButton = new JButton("OK");
		cancelButton = new JButton("CANCEL");
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
		panel.add(OKButton);
		panel.add(cancelButton);

		return panel;
	}

	private String[] extractTableInfo(){
		ArrayList<String> tableList = new ArrayList<String>();
		for(int i=0;i<this.table.length;i++){
			if(this.table[i].isSelected()){
				tableList.add(DBHandler.TABLE[i]);
			}
		}
		return (String[])tableList.toArray();
	}
	
	private String[] extractSetInfo(){
		ArrayList<String> setList = new ArrayList<String>();
		
		if(!date_after[0].getText().equals("") && !date_after[1].getText().equals("") && !date_after[2].getText().equals("")){
			setList.add("date = '" + format(date_after) + "'");
		}
		if(!content_after.getText().equals("")){
			setList.add("content = '" + content_after.getText() + "'");
		}
		if(!category_after.getText().equals("")){
			setList.add("category = '" + category_after.getText() + "'");
		}
		if(!price_after.getText().equals("")){
			setList.add("price = " + price_after.getText());
		}
		
		return (String[])setList.toArray();
	}
	
	private String extractCondition(){
		String where = "";
		where += this.extractCondition_Date();
		where += where.equals("") ? this.extractCondition_Content() : " AND " + this.extractCondition_Content();
		where += where.equals("") ? this.extractCondition_Category() : " AND " + this.extractCondition_Category();
		where += where.equals("") ? this.extractCondition_Price() : " AND " + this.extractCondition_Price();
		return where;
	}
	
	private String extractCondition_Date(){
		if(date_before[0].getText().equals("") && date_before[1].getText().equals("") && date_before[2].getText().equals("")){
			return "";
		}else{
			return "date = " + this.format(date_before);
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
	
	private String extractCondition_Content(){
		if(content_before.getText().equals("")){
			return "";
		}else{
			return "content = '" + content_before.getText() + "'";
		}
	}
	
	private String extractCondition_Category(){
		if(category_before.getSelectedIndex()==0){
			return "";
		}else{
			return "category = '" + categories[category_before.getSelectedIndex()] + "'";
		}
	}
	
	private String extractCondition_Price(){
		if(price_before.getText().equals("")){
			return "";
		}else{
			return "price = " + price_before.getText();
		}
	}

	public void actionPerformed(ActionEvent arg0){
		Object obj = arg0.getSource();
		if(obj == OKButton){
			if(inputChecker.checkBlank(table, date_after, content_after, category_after, price_after)){
				if(inputChecker.checkFormat(date_before, content_before, price_before, date_after, content_after, price_after)){
					ctrl.controllUpdate(this.extractTableInfo(), this.extractSetInfo(), this.extractCondition());
				}else{
					JOptionPane.showMessageDialog(this, "入力内容が正しくありません");
					return;
				}
			}else{
				JOptionPane.showMessageDialog(this, "必要な情報が入力されていません");
				return;
			}
		}
		this.dispose();
	}
}