package application.view.menu;

import java.util.ArrayList;
import java.util.Iterator;

import application.controller.IController;

public class Menu_Select extends Menu {
	private static final long serialVersionUID = 1L;

	public Menu_Select(IController ctrl){
		super("選択");
		menuItemList = new ArrayList<MenuItem>();
		menuItemList.add(new MenuItem_Select("選択(select)", ctrl));

		Iterator<MenuItem> it = menuItemList.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
	}
}
