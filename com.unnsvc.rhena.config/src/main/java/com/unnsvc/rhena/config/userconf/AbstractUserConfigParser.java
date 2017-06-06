
package com.unnsvc.rhena.config.userconf;

import com.unnsvc.rhena.common.config.IUserConfigParser;

public abstract class AbstractUserConfigParser implements IUserConfigParser {
	
	protected static final String PLATFORM_NL = System.getProperty("line.separator");

	protected static String in(int indents) {

		char[] indent = new char[indents];
		for (int i = 0; i < indents; i++) {

			indent[i] = '\t';
		}
		return new String(indent);
	}
}
