package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class Utils {

	
	public static int stackTraceCount() {
		
		RhenaException re = new RhenaException(new Exception("no-op"));
		return re.getStackTrace().length;
	}
}
