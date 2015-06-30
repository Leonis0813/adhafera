package application.controller;

import java.sql.SQLException;

public interface IController {

	public void shutdownSystem();

	public void createDialog(String operation);

	public Object[] getCategory();

	public void controllSelect(String[] attributes, String[] tables, String condition);

	public void controllUpdate(String[] tables, String[] set, String condition);

	public void controllInsert(String table, String[] values) throws SQLException;

	public void controllDelete(String[] tables, String condition);

	public void controllSettlement(String interval);
}
