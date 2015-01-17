package application.view.menu;

import java.awt.event.ActionEvent;

import application.controller.IController;

public class MenuItem_AnnualSettlement extends MenuItem {
	private static final long serialVersionUID = 1L;

	public MenuItem_AnnualSettlement(String title, IController ctrl){
		super(title, ctrl);
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		ctrl.controllSettlement("year");
	}

}
