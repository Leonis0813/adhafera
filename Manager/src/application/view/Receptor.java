package application.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import database.Settlement;
import application.model.Account;

public class Receptor {

	private BufferedReader stdReader;

	public Receptor(){
		stdReader = new BufferedReader(new InputStreamReader(System.in));
	}

	public String receiveUserInput() throws IOException{
		String input = "";
		while(true){
			System.out.print("選択して数値を入力してください。1:家計簿追加 2:家計簿表示 3:収支計算 0:終了：");
			input = stdReader.readLine();
			if(input.equals("1")){
				System.out.println("データベースに家計簿を追加します。");
				return "1";
			}else if(input.equals("2")){
				System.out.println("登録されている家計簿を表示します。");
				return "2";
			}else if(input.equals("3")){
				System.out.println("家計簿から収支を計算します。");
				return "3";
			}else if(input.equals("0")){
				System.out.println("終了します。");
				return "0";
			}else{
				System.out.println("!!!!! エラー：指定された数字を入力してください !!!!!");
			}
		}
	}

	public String receiveTableInfo() throws IOException{
		String input = "none";
		while(true){
			System.out.print("選択して数値を入力してください。1:収入 2:支出 0:終了：");
			input = stdReader.readLine();
			if(input.equals("1")){
				System.out.println("収入情報を追加します。");
				return "income";
			}else if(input.equals("2")){
				System.out.println("支出情報を追加します。");
				return "expense";
			}else if(input.equals("0")){
				System.out.println("家計簿の追加を中止します。");
				return input;
			}else{
				System.out.println("!!!!! エラー：指定された数字を入力してください !!!!!");
			}
		}
	}

	public String receiveAccountInfo() throws IOException{
		System.out.println("家計簿情報を入力してください（入力形式：日付,内容,カテゴリ,金額）");
		System.out.println("日付：yyyy/mm/ddの形式で記入してください");
		System.out.println("内容：100文字以内で記入してください");
		System.out.println("カテゴリ：100文字以内で記入してください。複数ある場合は+でつなげてください（例：食費+交通費）");
		System.out.println("金額：正の数を入力してください");
		System.out.print(">>");
		return stdReader.readLine();
	}

	public void noticeRegister(){
		System.out.println("家計簿情報を登録しました。");
	}

	public void noticeError(String result){
		if(result.equals("NG:date")){
			System.out.println("!!!!! エラー：日付が適切に入力されていません !!!!!");
		}else if(result.equals("NG:content")){
			System.out.println("!!!!! エラー：内容が適切に入力されていません !!!!!");
		}else if(result.equals("NG:category")){
			System.out.println("!!!!! エラー：カテゴリが適切に入力されていません !!!!!");
		}else if(result.equals("NG:price")){
			System.out.println("!!!!! エラー：金額が適切に入力されていません !!!!!");
		}else if(result.equals("NG:element")){
			System.out.println("!!!!! エラー：必要な家計簿情報が不足しています !!!!!");
		}
	}

	public void showAccount(ArrayList<Account> accountList){
		Iterator<Account> it = accountList.iterator();
		while(it.hasNext()){
			Account account = it.next();
			System.out.println(account.getDate()+ " " + account.getContent() + " " + account.getCategory() + " " + account.getPrice());
		}
	}

	public String receiveProcessingMethod() throws IOException{
		String input = "none";
		while(true){
			System.out.print("選択して数値を入力してください。1:年ごと 2:月ごと 3:日ごと 0:中止：");
			input = stdReader.readLine();
			if(input.equals("1")){
				System.out.println("年ごとに収支を計算します。");
				return "year";
			}else if(input.equals("2")){
				System.out.println("月ごとに収支を計算します。");
				return "month";
			}else if(input.equals("3")){
				System.out.println("日ごとに収支を計算します。");
				return "day";
			}else if(input.equals("0")){
				System.out.println("収支計算を中止します。");
				return input;
			}else{
				System.out.println("!!!!! エラー：指定された数字を入力してください !!!!!");
			}
		}
	}

	public void showSettlement(ArrayList<Settlement> settlementList){
		for(Iterator<Settlement> it = settlementList.iterator();it.hasNext();){
			Settlement settlement = it.next();
			System.out.println(settlement.getDate()+" -> "+settlement.getPrice()+"円");
		}
	}

	public String receiveWhatToEdit() throws IOException{
		String input = "none";
		while(true){
			System.out.print("選択して数値を入力してください。1:レコードの値の変更 2:カラムの値の変更 3:データベースの構造を変更 0:中止：");
			input = stdReader.readLine();
			if(input.equals("1")){
				return "record";
			}else if(input.equals("2")){
				return "column";
			}else if(input.equals("3")){
				return "database";
			}else if(input.equals("0")){
				System.out.println("家計簿の編集を中止します。");
				return input;
			}else{
				System.out.println("!!!!! エラー：指定された数字を入力してください !!!!!");
			}
		}
	}

}
