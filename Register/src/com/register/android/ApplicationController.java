package com.register.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;

public class ApplicationController {
	public static final int INPUT_SIZE = 5;
	private Context context;
	private InputChecker inputChecker;
	private HTTPClient httpClient;
	
	public ApplicationController(Context context) {
		this.context = context;
		
		inputChecker = new InputChecker();
		httpClient = new HTTPClient();
	}
	
	public void registAccount(String[] inputs) {
		ArrayList<Integer> ids = inputChecker.checkEmpty(inputs);
		if(!ids.isEmpty()) {
			Iterator<Integer> it = ids.iterator();
			String errorMessage = RegistrationView.LABELS[it.next()];
			while(it.hasNext()) {
				errorMessage += "," + RegistrationView.LABELS[it.next()];
			}
			errorMessage += "Ç™ì¸óÕÇ≥ÇÍÇƒÇ¢Ç‹ÇπÇÒ";
			((MainActivity)context).noticeError(errorMessage, ids);
			return;
		}
		if(inputChecker.checkDate(inputs[0])) {
			ids.add(0);
		}
		if(inputChecker.checkPrice(inputs[3])) {
			ids.add(3);
		}
		if(!ids.isEmpty()) {
			Iterator<Integer> it = ids.iterator();
			String errorMessage = RegistrationView.LABELS[it.next()];
			while(it.hasNext()) {
				errorMessage += "," + RegistrationView.LABELS[it.next()];
			}
			errorMessage += "Ç™ïsê≥Ç≈Ç∑";
			((MainActivity)context).noticeError(errorMessage, ids);
			return;
		}
		HashMap<String, Object> result = httpClient.sendRequest(inputs);
		if(result.get("status_code").equals("201")) {
			((MainActivity)context).noticeResult("â∆åvïÎÇ™ìoò^Ç≥ÇÍÇ‹ÇµÇΩ");
		} else if(result.get("status_code").equals("400")) {
			((MainActivity)context).noticeResult("â∆åvïÎÇÃìoò^Ç…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
}
