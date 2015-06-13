package application.view.menu;

import java.util.ArrayList;
import java.util.Iterator;

import application.controller.IController;

public class Menu_Settlement extends Menu {
	private static final long serialVersionUID = 1L;

	public Menu_Settlement(IController ctrl){
		super("収支計算");

		menuItemList = new ArrayList<MenuItem>();
		menuItemList.add(new MenuItem_AnnualSettlement("年次", ctrl));
		menuItemList.add(new MenuItem_MonthlySettlement("月次", ctrl));
		menuItemList.add(new MenuItem_DailySettlement("日次", ctrl));

		Iterator<MenuItem> it = menuItemList.iterator();
		while(it.hasNext()){
			this.add(it.next());
		}
	}

}
