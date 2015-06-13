package application.view.menu;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import application.controller.IController;

public abstract class MenuItem extends JMenuItem implements ActionListener{
	private static final long serialVersionUID = 1L;

	protected IController ctrl;

	protected MenuItem(String title, IController ctrl){
		super(title);

		this.ctrl = ctrl;
	}
}
