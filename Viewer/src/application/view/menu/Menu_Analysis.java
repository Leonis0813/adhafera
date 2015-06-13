package application.view.menu;

import application.controller.IController;

public class Menu_Analysis extends Menu {
	private static final long serialVersionUID = 1L;

	public Menu_Analysis(IController ctrl){
		super("解析");
		super.add(new Menu_Settlement(ctrl));
	}
}
