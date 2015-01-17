package application.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;

import database.Settlement;
import application.controller.IController;


public abstract class Dialog extends JDialog implements ActionListener, WindowListener{
	private static final long serialVersionUID = 1L;

	protected static final String[] DATE = {"年", "月", "日"};
	protected static final int YEAR = 0;
	protected static final int MONTH = 1;
	protected static final int DAY = 2;
	
	protected static final String[] CONDITION_DATE = {"〜", "以前", "以降"};
	protected static final int BETWEEN = 0;
	protected static final int BEFORE = 1;
	protected static final int AFTER = 2;
	
	protected static final String[] CONDITION_CONTENT = {"に一致", "を含む", "を含まない"};
	protected static final int SAME_CONTENT = 0;
	protected static final int INCLUDE = 1;
	protected static final int NOT_INCLUDE = 2;
	
	protected static final String[] CONDITION_PRICE = {"に一致", "以上", "以下"};
	protected static final int SAME_PRICE = 0;
	protected static final int UPPER = 1;
	protected static final int LOWER = 2;
	
	protected IController ctrl;
	protected InputChecker inputChecker;

	protected JButton OKButton, cancelButton;

	public Dialog(String title){
		super.setTitle(title);
		this.addWindowListener(this);

		inputChecker = new InputChecker(title);
	}

	abstract public void createContents(IController ctrl);
	
	public void setSettlementList(ArrayList<Settlement> settlementList){}

	abstract public void actionPerformed(ActionEvent arg0);

	public void windowActivated(WindowEvent arg0){}
	public void windowClosed(WindowEvent arg0){}
	public void windowClosing(WindowEvent arg0){
		this.dispose();
	}
	public void windowDeactivated(WindowEvent arg0){}
	public void windowDeiconified(WindowEvent arg0){}
	public void windowIconified(WindowEvent arg0){}
	public void windowOpened(WindowEvent arg0){}
}
