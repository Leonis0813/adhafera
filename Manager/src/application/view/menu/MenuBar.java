package application.view.menu;


import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JMenuBar;

import application.controller.IController;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private ArrayList<Menu> menuList;

	public MenuBar(IController ctrl){
		super();

		menuList = new ArrayList<Menu>();
		menuList.add(new Menu_File(ctrl));
		menuList.add(new Menu_Select(ctrl));
		menuList.add(new Menu_Update(ctrl));
		menuList.add(new Menu_Analysis(ctrl));

		Iterator<Menu> it = menuList.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
	}

}
