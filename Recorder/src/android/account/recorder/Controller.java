package android.account.recorder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.widget.Toast;

public class Controller {
	private MainActivity ma;
	private AccountController ac;
	
	private static final String FILE_NAME = "accounts.txt";
    private OutputStream out;
	private PrintWriter writer;
	private InputStream in;
	private BufferedReader reader;
	
	public Controller(MainActivity ma, AccountController ac) {
		this.ma = ma;
		this.ac = ac;
		this.readFile();
	}
	
	public void createAccount(String[] inputs) {
		ArrayList<Integer> errors = ac.checkEmpty(inputs);
		if(!errors.isEmpty()) {
			ma.noteInputError("全ての項目を入力してください", errors);
			return;
		}
		if(!ac.checkDate(inputs[Account.DATE])) {
			errors.add(Account.DATE-1);
			ma.noteInputError("日付が不正です", errors);
			return;
		}
		if(!ac.checkPrice(inputs[Account.PRICE])) {
			errors.add(Account.PRICE-1);
			ma.noteInputError("金額が不正です", errors);
			return;
		}
		ac.create(inputs);
		this.writeToFile();
		ma.sendMessage("保存しました");
		ma.updateView();
	}
	
	public void getAccount(String item) {
		Account account = ac.read(item);
		ma.showAccount(account.toArray());
	}
	
	public void getAllAccount() {
		Iterator<Account> it = ac.readAll().iterator();
		while(it.hasNext()) {
			ma.showAccount(it.next().toArray());
		}
	}
	
	public void updateAccount(String[] inputs) {
		ArrayList<Integer> errors = ac.checkEmpty(inputs);
		if(!errors.isEmpty()) {
			ma.noteInputError("全ての項目を入力してください", errors);
			return;
		}
		if(!ac.checkDate(inputs[Account.DATE])) {
			errors.add(Account.DATE-1);
			ma.noteInputError("日付が不正です", errors);
			return;
		}
		if(!ac.checkPrice(inputs[Account.PRICE])) {
			errors.add(Account.PRICE-1);
			ma.noteInputError("金額が不正です", errors);
			return;
		}
		ac.update(inputs);
		this.writeToFile();
		ma.sendMessage("更新しました");
		ma.switchView(MainActivity.EDIT);
		ma.updateView();
	}
	
	public void deleteAccount(ArrayList<String> item) {
		Iterator<String> it = item.iterator();
		while(it.hasNext()) {
			ac.delete(it.next());
		}
		this.writeToFile();
		ma.updateView();
		ma.sendMessage("削除しました");
	}
	
	public void editAccount(String item) {
		ac.keepAccountInfo(item);
		ma.switchView(MainActivity.INPUT);
		ma.showAccount(ac.read(item).toArray());
		ma.changeState(InputView.UPDATE);
	}

	public void readFile() {
		try {
			in = ma.openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	    	String lineBuffer;
	        while((lineBuffer = reader.readLine()) != null) {
	        	ac.create(lineBuffer.split(", "));
	        }
	        reader.close();
	        in.close();
	    } catch (FileNotFoundException e) {
	    	
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			Toast.makeText(ma, "家計簿情報の読み込みに失敗しました", Toast.LENGTH_LONG).show();
		} finally {
			
		}
	}
	
	public void writeToFile() {
		try {
			out = ma.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
			Iterator<Account> it = ac.readAll().iterator();
			while(it.hasNext()){
				writer.println(it.next());
			}
			writer.close();
			out.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(ma, "保存に失敗しました(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(ma, "保存に失敗しました(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ma, "保存に失敗しました(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		}
	}
}
