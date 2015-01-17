package application.view.menu;

import java.util.ArrayList;

import javax.swing.JMenu;

public abstract class Menu extends JMenu{
	private static final long serialVersionUID = 1L;

	protected ArrayList<MenuItem> menuItemList;

	protected Menu(String title){
		super(title);
	}
}
