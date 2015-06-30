package application.view.menu;

import java.util.ArrayList;
import java.util.Iterator;

import application.controller.IController;

public class Menu_File extends Menu {
	private static final long serialVersionUID = 1L;

	public Menu_File(IController ctrl){
		super("メニュー");
		menuItemList = new ArrayList<MenuItem>();
		menuItemList.add(new MenuItem_Quit("終了", ctrl));

		Iterator<MenuItem> it = menuItemList.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
	}

}
