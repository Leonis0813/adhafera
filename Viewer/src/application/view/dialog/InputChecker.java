package application.view.dialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class InputChecker {
	private String operation;
	private static final int[] maxDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	public InputChecker(String operation) {
		this.operation = operation;
	}

	/* ------------------------- ここから ------------------------- */
	/*
	 * 最低必要な情報が入力されているかをチェックする
	 * SELECT:表示するデータ(JRadioButton)，選択するテーブル(JRadioButton)
	 * UPDATE:更新するテーブル(JRadioButton)，更新後の値のうち少なくとも１つの属性(JTextField)
	 * INSERT:選択するテーブル(JRadioButton)，全ての属性(JTextField)
	 * DELETE:削除するテーブル(JRadioButton)
	 */
	public boolean checkBlank(JRadioButton[] table){
		if(!isBlank(table)){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkBlank(JRadioButton[] select, JRadioButton[] table){
		if( !isBlank(select) && !isBlank(table) ){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkBlank(JRadioButton[] table, JTextField[] date, JTextField content, JTextField category, JTextField price){
		if(operation.equals("Update")){
			if( !isBlank(table) && ( !isBlank(date) || !isBlank(content) || !isBlank(category) || !isBlank(price) ) ){
				return true;
			}else{
				return false;
			}
		}else if(operation.equals("Insert")){
			if( !isBlank(table) && !isBlank(date) && !isBlank(content) && !isBlank(category) && !isBlank(price) ) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	private boolean isBlank(JRadioButton[] radioButtons){
		for(int i=0;i<radioButtons.length;i++){
			if(radioButtons[i].isSelected()){
				return false;
			}
		}
		return true;
	}

	private boolean isBlank(JTextField[] textFields){
		for(int i=0;i<textFields.length;i++){
			if(!isBlank(textFields[i])){
				return false;
			}
		}
		return true;
	}

	private boolean isBlank(JTextField textField){
		if(textField.getText().equals("")){
			return true;
		}else{
			return false;
		}
	}

	/* ------------------------- ここまで ------------------------- */

	/* ------------------------- ここから ------------------------- */
	/*
	 * 入力のフォーマットが正しいかをチェックする
	 * SELECT:日付:数字以外が入力されていないか&年月日が正常値であるか&期間を条件とする場合両方に入力されているか，内容:100文字以内であるか，カテゴリ:100文字以内であるか，金額:数字以外が入力されていないか&正常値であるか
	 * UPDATE:日付:数字以外が入力されていないか&年月日が正常値であるか，内容:100文字以内であるか，カテゴリ:100文字以内であるか，金額:数字以外が入力されていないか&正常値であるか
	 * INSERT:日付:数字以外が入力されていないか&年月日が正常値であるか，内容:100文字以内であるか，カテゴリ:100文字以内であるか，金額:数字以外が入力されていないか&正常値であるか
	 * DELETE:日付:数字以外が入力されていないか&年月日が正常値であるか&期間を条件とする場合両方に入力されているか，内容:100文字以内であるか，金額:数字以外が入力されていないか&正常値であるか
	 */
	public boolean checkFormat(JTextField[] date1, JTextField[] date2, JComboBox period, JTextField content, JTextField price){
		return ( dateIsCorrect(date1, date2, period.getSelectedIndex()) && contentIsCorrect(content) && priceIsCorrect(price) );
	}

	public boolean checkFormat(JTextField[] date_before, JTextField content_before, JTextField price_before,
			JTextField[] date_after, JTextField content_after, JTextField price_after){
		return ( dateIsCorrect(date_before) && contentIsCorrect(content_before) && priceIsCorrect(price_before) &&
				dateIsCorrect(date_after) && contentIsCorrect(content_after) && priceIsCorrect(price_after) );
	}

	public boolean checkFormat(JTextField[] date, JTextField content, JTextField category, JTextField price){
		return ( dateIsCorrect(date) && contentIsCorrect(content) && categoryIsCorrect(category) && priceIsCorrect(price) );
	}

	private boolean dateIsCorrect(JTextField[] date1, JTextField[] date2, int index){
		if(dateIsEmpty(date1) && dateIsEmpty(date2)){
			return true;
		}else{
			if(index==0 && !dateIsCorrect(date2)){
				return false;
			}
			if(dateIsCorrect(date1)){
				return true;
			}else{
				return false;
			}
		}
	}

	private boolean dateIsCorrect(JTextField[] date){
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m;
		for(int i=0;i<date.length;i++){
			m = p.matcher(date[i].getText());
			if(!m.find() || date[i].getText().equals("")){
				return false;
			}
		}
		int month = Integer.parseInt(date[1].getText());
		int day = Integer.parseInt(date[2].getText());
		if( 1 <= month && month <= 12 && 1 <= day && day <= maxDays[month-1] ){
			return true;
		}else{
			return false;
		}
	}

	private boolean dateIsEmpty(JTextField[] date){
		for(int i=0;i<date.length;i++){
			if(!date[i].getText().equals("")){
				return false;
			}
		}
		return true;
	}

	private boolean contentIsCorrect(JTextField content){
		String str = content.getText();
		return ( str.equals("") || (!str.equals("") && str.length()<=100) );
	}

	private boolean categoryIsCorrect(JTextField category){
		String str = category.getText();
		return ( str.equals("") || (!str.equals("") && str.length()<=100) );
	}

	private boolean priceIsCorrect(JTextField price){
		String str = price.getText();
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(str);
		return ( str.equals("") || (!str.equals("") && m.find()) );
	}
}