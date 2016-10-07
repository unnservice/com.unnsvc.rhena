package com.unnsvc.rhena.ng;

import com.unnsvc.rhena.builder.exceptions.RhenaException;

public class Utils {

	
	public static int stackTraceCount() {
		
		RhenaException re = new RhenaException(new Exception("no-op"));
		return re.getStackTrace().length;
	}
}
