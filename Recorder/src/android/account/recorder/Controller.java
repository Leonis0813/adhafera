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
		if(this.checkInputError(inputs)) {
			ac.create(inputs);
			this.writeToFile();
			ma.sendMessage("ï€ë∂ÇµÇ‹ÇµÇΩ");
			ma.updateView();
		}
	}
	
	public void getAccount(String item) {
		Account account = ac.read(item);
		ma.showAccount(account.toArray());
	}
	
	public void getAccount() {
		Iterator<Account> it = ac.read().iterator();
		while(it.hasNext()) {
			ma.showAccount(it.next().toArray());
		}
	}
	
	public void updateAccount(String[] inputs) {
		if(this.checkInputError(inputs)) {
			ac.update(inputs);
			this.writeToFile();
			ma.sendMessage("çXêVÇµÇ‹ÇµÇΩ");
			ma.switchView(MainActivity.EDIT);
			ma.updateView();
		}
	}
	
	public void deleteAccount(ArrayList<String> itemList) {
		Iterator<String> it = itemList.iterator();
		while(it.hasNext()) {
			ac.delete(it.next());
		}
		this.writeToFile();
		ma.updateView();
		ma.sendMessage("çÌèúÇµÇ‹ÇµÇΩ");
	}
	
	public void editAccount(String item) {
		ac.keepAccountInfo(item);
		ma.switchView(MainActivity.INPUT);
		ma.showAccount(ac.read(item).toArray());
		ma.changeState(InputView.UPDATE);
	}

	public void cancelInput() {
		ma.updateView();
	}
	
	public void cancelEdit() {
		ma.switchView(MainActivity.EDIT);
	}
	
	private void readFile() {
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
			Toast.makeText(ma, "â∆åvïÎèÓïÒÇÃì«Ç›çûÇ›Ç…é∏îsÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG).show();
		} finally {
			
		}
	}
	
	private void writeToFile() {
		try {
			out = ma.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
			Iterator<Account> it = ac.read().iterator();
			while(it.hasNext()){
				writer.println(it.next());
			}
			writer.close();
			out.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(ma, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(ma, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(ma, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		}
	}
	
	private boolean checkInputError(String[] inputs) {
		ArrayList<Integer> errors = ac.checkEmpty(inputs);
		if(!errors.isEmpty()) {
			ma.noteInputError("ëSÇƒÇÃçÄñ⁄Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢", errors);
			return false;
		}
		if(!ac.checkDate(inputs[Account.DATE])) {
			errors.add(Account.DATE-1);
			ma.noteInputError("ì˙ïtÇ™ïsê≥Ç≈Ç∑", errors);
			return false;
		}
		if(!ac.checkPrice(inputs[Account.PRICE])) {
			errors.add(Account.PRICE-1);
			ma.noteInputError("ã‡äzÇ™ïsê≥Ç≈Ç∑", errors);
			return false;
		}
		return true;
	}
}
