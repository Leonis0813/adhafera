package android.account.recorder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountController {
	private ArrayList<Account> accountList;
	private Account updatedAccount;
	
	public AccountController() {
		accountList = new ArrayList<Account>();
	}
	
	public void create(String[] inputs) {
		accountList.add(new Account(inputs));
	}

	public Account read(String item) {
		Iterator<Account> it = accountList.iterator();
		while(it.hasNext()) {
			Account account = it.next();
			if(account.toString().equals(item)) return account;
		}
		return null;
	}
	
	public ArrayList<Account> readAll() {
		return accountList;
	}
	
	public void update(String[] inputs) {
		updatedAccount.setType(inputs[Account.TYPE]);
		updatedAccount.setDate(inputs[Account.DATE]);
		updatedAccount.setContent(inputs[Account.CONTENT]);
		updatedAccount.setCategory(inputs[Account.CATEGORY]);
		updatedAccount.setPrice(Integer.parseInt(inputs[Account.PRICE]));
	}

	public void delete(String item) {
		Iterator<Account> it = accountList.iterator();
		for(int i=0;it.hasNext();i++) {
			Account account = it.next();
			if(account.toString().equals(item)) {
				accountList.remove(i);
				return;
			}
		}
	}
	
	public void keepAccountInfo(String item) {
		Iterator<Account> it = accountList.iterator();
		Account account = null;
		while(it.hasNext()) {
			account = it.next();
			if(account.toString().equals(item)) break;
		}
		updatedAccount = account;
	}
	
	public ArrayList<Integer> checkEmpty(String[] inputs) {
		ArrayList<Integer> emptyList = new ArrayList<Integer>();
		for(int i=1;i<inputs.length;i++) {
			if(inputs[i].equals("")) {
				emptyList.add(i-1);
			}
		}
		return emptyList;
	}
	
	public boolean checkDate(String date) {
		Pattern p = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
		Matcher m = p.matcher(date);
		if(!m.find()) {
			return false;
		}
		
		String[] token = date.split("-");
		int month = Integer.parseInt(token[1]);
		int day = Integer.parseInt(token[2]);
		if(month < 1 && 12 < month && day < 1 && 31 < day) {
			return false;
		}
		return true;
	}
	
	public boolean checkPrice(String price) {
		Pattern p = Pattern.compile("^[1-9][0-9]*$");
		Matcher m = p.matcher(price);
		return m.find();
	}
}
