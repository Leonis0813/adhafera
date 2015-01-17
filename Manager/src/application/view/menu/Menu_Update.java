package application.view.menu;

import java.util.ArrayList;
import java.util.Iterator;

import application.controller.IController;

public class Menu_Update extends Menu {
	private static final long serialVersionUID = 1L;

	public Menu_Update(IController ctrl){
		super("更新");

		menuItemList = new ArrayList<MenuItem>();
		menuItemList.add(new MenuItem_Update("更新(update)", ctrl));
		menuItemList.add(new MenuItem_Insert("挿入(insert)", ctrl));
		menuItemList.add(new MenuItem_Delete("削除(delete)", ctrl));

		Iterator<MenuItem> it = menuItemList.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
	}

}
