package application.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import database.DBHandler;
import application.controller.IController;

public class Dialog_Select extends Dialog {
	private static final long serialVersionUID = 1L;

	private final int WIDTH = 740;
	private final int HEIGHT = 295;

	/* データ選択用コンポーネント */
	private JRadioButton[] select;

	/* 日付用コンポーネント */
	private JTextField[] date_from, date_to;
	private JComboBox date_cb;

	/* 内容用コンポーネント */
	private JComboBox content_cb;
	private JTextField content_tf;

	/* カテゴリ用コンポーネント */
	private JComboBox category_cb;
	private String[] categories;

	/* 金額用コンポーネント */
	private JComboBox price_cb;
	private JTextField price_tf;

	/* テーブル用コンポーネント */
	private JRadioButton[] table;

	public Dialog_Select(){
		super("Select");
	}

	public void createContents(IController ctrl){
		super.setModal(true);
		super.ctrl = ctrl;

		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout());

		this.add( this.createSelectPanel() );
		this.add( this.createDatePanel() );
		this.add( this.createContentPanel() );
		this.add( this.createCategoryPanel() );
		this.add( this.createPricePanel() );
		this.add( this.createTablePanel() );
		this.add( this.createButtonPanel() );

		this.setResizable(false);
	}

	private JPanel createSelectPanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));
		panel.setLayout(new FlowLayout());

		panel.add(new JLabel("表示するデータを選択してください："));
		select = new JRadioButton[DBHandler.NUM_COLUMN];
		for(int i=0;i<DBHandler.NUM_COLUMN;i++){
			select[i] = new JRadioButton(DBHandler.COLUMN_JP[i]);
			panel.add(select[i]);
		}

		return panel;
	}

	private JPanel createDatePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("日付：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70,30));
		panel.add(label);

		date_from = new JTextField[DATE.length];
		for(int i=0;i<DATE.length;i++){
			date_from[i] = new JTextField();
			date_from[i].setPreferredSize(new Dimension(70, 30));
			panel.add(date_from[i]);
			panel.add(new JLabel(DATE[i]));
		}

		date_cb = new JComboBox(CONDITION_DATE);
		date_cb.setPreferredSize(new Dimension(85, 30));
		panel.add(date_cb);

		date_to = new JTextField[DATE.length];
		for(int i=0;i<DATE.length;i++){
			date_to[i] = new JTextField();
			date_to[i].setPreferredSize(new Dimension(70,30));
			panel.add(date_to[i]);
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

		content_tf = new JTextField(30);
		panel.add(content_tf);

		content_cb = new JComboBox(CONDITION_CONTENT);
		content_cb.setPreferredSize(new Dimension(120, 30));
		panel.add(content_cb);

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

		Object[] categories = ctrl.getCategory();
		category_cb = new JComboBox(categories);
		this.categories = new String[categories.length];
		for(int i=0;i<categories.length;i++){
			this.categories[i] = categories[i].toString();
		}
		category_cb.setPreferredSize(new Dimension(150, 30));
		panel.add(category_cb);

		return panel;
	}

	private JPanel createPricePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 35));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel label = new JLabel("金額：");
		label.setHorizontalAlignment(JLabel.RIGHT);
		label.setPreferredSize(new Dimension(70, 30));
		panel.add(label);

		price_tf = new JTextField(10);
		panel.add(price_tf);

		price_cb = new JComboBox(CONDITION_PRICE);
		price_cb.setPreferredSize(new Dimension(90, 30));
		panel.add(price_cb);

		return panel;
	}

	private JPanel createTablePanel(){
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, 30));

		panel.add(new JLabel("検索するテーブルを選択してください："));
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

	private String[] extractSelectedData(){
		ArrayList<String> attributeList = new ArrayList<String>();
		for(int i=0;i<this.select.length;i++){
			if(this.select[i].isSelected()){
				attributeList.add(DBHandler.COLUMN[i]);
			}
		}
		String[] attributes = new String[attributeList.size()];
		Iterator<String> it = attributeList.iterator();
		for(int i=0;it.hasNext();i++){
			attributes[i] = it.next().toString();
		}
		return attributeList.toArray(new String[attributeList.size()]);
	}

	private String[] extractTableInfo(){
		ArrayList<String> tableList = new ArrayList<String>();
		for(int i=0;i<this.table.length;i++){
			if(this.table[i].isSelected()){
				tableList.add(DBHandler.TABLE[i]);
			}
		}
		return tableList.toArray(new String[tableList.size()]);
	}

	private String extractCondition(){
		String where = "";
		where = this.extractCondition_Date();
		where += this.extractCondition_Content(where);
		where += this.extractCondition_Category(where);
		where += this.extractCondition_Price(where);
		return where;
	}
	
	private String extractCondition_Date(){
		if(date_from[0].getText().equals("") && date_from[1].getText().equals("") && date_from[2].getText().equals("")){
			return "";
		}else{
			switch (date_cb.getSelectedIndex()) {
			case Dialog.BETWEEN:{
				return "date BETWEEN " + this.format(date_from) + " AND " + this.format(date_to); 
			}
			case Dialog.BEFORE:{
				return "date < " + this.format(date_from);
			}
			case Dialog.AFTER:{
				return "date > " + this.format(date_from);
			}
			default : return "";
			}
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
	
	private String extractCondition_Content(String where){
		if(content_tf.getText().equals("")){
			return "";
		}else{
			String content;
			switch (content_cb.getSelectedIndex()) {
			case Dialog.SAME_CONTENT:{
				content = "content = '" + content_tf.getText() + "'";
				break;
			}
			case Dialog.INCLUDE:{
				content = "content LIKE '" + content_tf.getText() + "'";
				break;
			}
			case Dialog.NOT_INCLUDE:{
				content = "content NOT LIKE '" + content_tf.getText() + "'";
				break;
			}
			default : return "";
			}
			return where.equals("") ? content : " AND " + content;
		}
	}
	
	private String extractCondition_Category(String where){
		if(category_cb.getSelectedIndex()==0){
			return "";
		}else{
			String category = "category = '" + categories[category_cb.getSelectedIndex()] + "'";
			return where.equals("") ? category : " AND " + category;
		}
	}
	
	private String extractCondition_Price(String where){
		if(price_tf.getText().equals("")){
			return "";
		}else{
			String price;
			switch (price_cb.getSelectedIndex()) {
			case Dialog.SAME_PRICE:{
				price = "price = " + price_tf.getText();
				break;
			}
			case Dialog.UPPER:{
				price = "price >= " + price_tf.getText();
				break;
			}
			case Dialog.LOWER:{
				price = "price <= " + price_tf.getText();
				break;
			}
			default : return "";
			}
			return where.equals("") ? price : " AND " + price;
		}
	}

	public void actionPerformed(ActionEvent arg0){
		Object obj = arg0.getSource();
		if(obj==OKButton){
			if(inputChecker.checkBlank(select, table)){
				if(inputChecker.checkFormat(date_from, date_to, date_cb, content_tf, price_tf)){
					ctrl.controllSelect(this.extractSelectedData(), this.extractTableInfo(), this.extractCondition());
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