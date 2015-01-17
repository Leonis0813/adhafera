package application.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import database.DBHandler;
import database.Settlement;
import application.controller.IController;
import application.model.Account;
import application.view.dialog.Dialog;
import application.view.menu.MenuBar;
import application.view.table.AccountTable;
import application.view.table.ExpensePanel;
import application.view.table.IncomePanel;
import application.view.textField.NoticePanel;
import application.view.table.AccountPanel;


public class View extends JFrame implements WindowListener{
	private static final long serialVersionUID = 1L;

	private IController ctrl;

	private GridBagLayout layout;

	private JMenuBar menuBar;
	private AccountPanel[] accountPanels;
	private NoticePanel noticePanel;

	private Dialog dialog;

	public View(IController ctrl){
		super();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("AccountKeeper");
		this.getContentPane().setPreferredSize(new Dimension(1440, 760));
		this.setResizable(false);
		this.pack();

		layout = new GridBagLayout();
		this.setLayout(layout);

		menuBar = new MenuBar(ctrl);
		accountPanels = new AccountPanel[DBHandler.NUM_TABLE];
		this.createIncomePanel(layout, ctrl);
		this.createExpensePanel(layout, ctrl);
		this.createNoticePanel(layout, ctrl);

		this.setJMenuBar(menuBar);
		this.add(accountPanels[AccountTable.INCOME]);
		this.add(accountPanels[AccountTable.EXPENSE]);
		this.add(noticePanel);

		this.addWindowListener(this);

		this.ctrl = ctrl;

		this.setVisible(true);
	}

	private void createIncomePanel(GridBagLayout layout, IController ctrl){
		accountPanels[AccountTable.INCOME] = new IncomePanel(ctrl);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 720;
		constraints.gridheight = 700;
		constraints.weightx = 1.0d;
		constraints.weighty = 1.0d;
		layout.setConstraints(accountPanels[AccountTable.INCOME], constraints);
	}

	private void createExpensePanel(GridBagLayout layout, IController ctrl){
		accountPanels[AccountTable.EXPENSE] = new ExpensePanel(ctrl);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 720;
		constraints.gridy = 0;
		constraints.gridwidth = 720;
		constraints.gridheight = 700;
		constraints.weightx = 1.0d;
		constraints.weighty = 1.0d;
		layout.setConstraints(accountPanels[AccountTable.EXPENSE], constraints);
	}

	private void createNoticePanel(GridBagLayout layout, IController ctrl){
		noticePanel = new NoticePanel(ctrl);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 700;
		constraints.gridwidth = 1440;
		constraints.gridheight = 20;
		constraints.weightx = 1.0d;
		constraints.weighty = 1.0d;
		layout.setConstraints(noticePanel, constraints);
	}

	public void createDialog(Dialog dialog, IController ctrl){
		this.dialog = dialog;
		this.dialog.createContents(ctrl);
		this.dialog.setVisible(true);
	}

	public void clearTable(int table){
		accountPanels[table].clearTable();
	}
	
	public void update(ArrayList<Account> accountList){

	}

	public void updateTable(int table, Account account){
		accountPanels[table].addAccount(account);	
	}

	public void setSettlement(ArrayList<Settlement> settlementList){
		dialog.setSettlementList(settlementList);
	}

	public void notice(String note){
		noticePanel.showMessage(note);
	}

	public void windowActivated(WindowEvent arg0){}
	public void windowClosed(WindowEvent arg0){}

	public void windowClosing(WindowEvent arg0){
		ctrl.shutdownSystem();
	}

	public void windowDeactivated(WindowEvent arg0){}
	public void windowDeiconified(WindowEvent arg0){}
	public void windowIconified(WindowEvent arg0){}
	public void windowOpened(WindowEvent arg0){}
}
