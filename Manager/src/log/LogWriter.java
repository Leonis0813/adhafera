package log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;

public class LogWriter {
	private static File file = null;
	private static PrintWriter printWriter = null;
	
	private static Calendar calendar = Calendar.getInstance();
	private static String[] DAY_OF_THE_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
	
	public static boolean openFile(String filePath){
		file = new File(filePath);
		try{
			if(!file.exists()){
				printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
				System.out.println("ログファイルを新規作成します。");
				return  true;
			}else if(file.isFile() && file.canWrite()){
				printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
				System.out.println("既存のログファイルに追記します。");
				return true;
			}
		}catch(IOException e){}
		return false;
	}
	
	public static void write(String log, boolean appendTime){
		if(printWriter!=null){
			if(appendTime){
				log += " at (" + getDate() + ")";
			}
			printWriter.print(log);
		}else{
			System.out.println("ファイルを指定してください。");
		}
	}
	
	public static void writeln(String log, boolean appendTime){
		if(printWriter!=null){
			if(appendTime){
				log += " at (" + getDate() + ")";
			}
			printWriter.println(log);
		}else{
			System.out.println("ファイルを指定してください。");
		}
	}
	
	private static String getDate(){
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return year + "-" + month + "-" + day + "(" + DAY_OF_THE_WEEK[dayOfTheWeek-1] + ") " + hour + ":" + minute + ":" + second;
	}
	
	public static boolean closeFile(){
		printWriter.close();
		return true;
	}
}