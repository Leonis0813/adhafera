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
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;
import application.view.table.AccountTable;

public class MainActivity extends ActionBarActivity {
	private InputView inputView;
	private SelectView selectView;
	public static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    
    public static final int INPUT = 0;
    public static final int EDIT = 1;
    public static final int DELETE = 2;
    public static final int NUM_OPERATION = 3;
    private int currentState;
    
    public static final String FILE_NAME = "accounts.txt";
    private OutputStream out;
	private PrintWriter writer;
	private InputStream in;
	private BufferedReader reader;
	
    private HashMap<Integer, String> accountMap;
    private int selected;
    
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inputView = new InputView(this);
		selectView = new SelectView(this);
		currentState = INPUT;
		setContentView(inputView);
		
		accountMap = new HashMap<Integer, String>();
		
		readAccount();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, INPUT, 0, R.string.menu_input);
		menu.add(0, EDIT, 0, R.string.menu_edit);
	    menu.add(0, DELETE, 0, R.string.menu_delete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int operation = item.getItemId();
		switch(operation){
		case INPUT:{
			inputView.setup();
			currentState = INPUT;
			setContentView(inputView);
			return true;
		}
		case EDIT:{
			selectView.setup(accountMap, EDIT);
			currentState = EDIT;
			setContentView(selectView);
			return true;
		}
		case DELETE:{
			selectView.setup(accountMap, DELETE);
			currentState = DELETE;
			setContentView(selectView);
			return true;
		}
		default: return false;
		}
	}
	
	private void readAccount() {
		try {
			in = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
	    	String lineBuffer;
	        for(int i=0;(lineBuffer = reader.readLine()) != null;i++){
	            accountMap.put(i, lineBuffer);
	        }
	        reader.close();
	        in.close();
	    } catch (FileNotFoundException e) {
	    	
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			Toast.makeText(this, "â∆åvïÎèÓïÒÇÃì«Ç›çûÇ›Ç…é∏îsÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG).show();
		} finally {
			
		}
	}
	
	private void writeAccount() {
		try {	
			out = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
			Iterator<Integer> it = accountMap.keySet().iterator();
			while(it.hasNext()){
				writer.println(accountMap.get(it.next()));
			}
			writer.close();
			out.close();
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(this, "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ(" + e.toString() + ")", Toast.LENGTH_LONG).show();
		}
	}
	
	public String addAccount(String[] inputs) {
		if(hasEmpty(inputs)){
			return "ëSÇƒÇÃçÄñ⁄Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢";
		}else if(!correctFormat(inputs)){
			return "ê≥ÇµÇ¢ÉtÉHÅ[É}ÉbÉgÇ≈ì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢";
		}
		if(currentState == INPUT){
			accountMap.put(accountMap.size(), format(inputs));
			writeAccount();
			inputView.setup();
			return "ï€ë∂ÇµÇ‹ÇµÇΩ";
		}else if(currentState == EDIT){
			accountMap.put(selected, format(inputs));
			writeAccount();
			selectView.setup(accountMap, EDIT);
			setContentView(selectView);
			return "ïœçXÇµÇ‹ÇµÇΩ";
		}else{
			return "ï€ë∂Ç…é∏îsÇµÇ‹ÇµÇΩ";
		}
	}

	private String format(String[] inputs) {
		String account = "";
		for(int i=0;i<inputs.length;i++){
			account += i==0 ? inputs[i] : ", " + inputs[i];
		}
		return account;
	}
	
	private boolean hasEmpty(String[] inputs) {
		boolean hasBlank = false;
		for(int i=1;i<inputs.length;i++){
			if(inputs[i].equals("")){
				inputView.noticeError(i-1);
				hasBlank = true;
			}
		}
		return hasBlank;
	}
	
	private boolean correctFormat(String[] inputs) {
		boolean date = dateIsCorrect(inputs[AccountTable.DATE+1]);
		boolean price = priceIsCorrect(inputs[AccountTable.PRICE+1]);
		return date && price;
	}
	
	private boolean dateIsCorrect(String date) {
		Pattern p = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
		Matcher m = p.matcher(date);
		if(!m.find()){
			inputView.noticeError(AccountTable.DATE);
			return false;
		}
		
		String[] token = date.split("-");
		int month = Integer.parseInt(token[1]);
		int day = Integer.parseInt(token[2]);
		if(month < 1 && 12 < month && day < 1 && 31 < day){
			inputView.noticeError(AccountTable.DATE);
			return false;
		}
		return true;
	}
	
	private boolean priceIsCorrect(String price) {
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(price);
		if(!m.find()){
			inputView.noticeError(AccountTable.PRICE);
			return false;
		}
		return true;
	}

	public void showAccount(String value, int key){
		selected = key;
		String[] columns = value.split(", ");
		inputView.setup(columns);
		setContentView(inputView);
	}
	
	public void deleteAccount(int id){
		accountMap.remove(id);
		Toast.makeText(this, "çÌèúÇµÇ‹ÇµÇΩ", Toast.LENGTH_LONG).show();
		selectView.setup(accountMap, DELETE);
		writeAccount();
	}
}
