package application.view.textField;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import application.controller.IController;

public class NoticePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;

	private IController ctrl;

	private JTextField textField;

	public NoticePanel(IController ctrl){
		super();
		this.setPreferredSize(new Dimension(1440, 20));
		this.setLayout(new GridLayout(1,1));

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(1440, 20));
		textField.addActionListener(this);

		this.add(textField);

		this.ctrl = ctrl;
	}

	public void actionPerformed(ActionEvent arg0){

	}

	public void showMessage(String note){
		textField.setText(note);
	}

}
