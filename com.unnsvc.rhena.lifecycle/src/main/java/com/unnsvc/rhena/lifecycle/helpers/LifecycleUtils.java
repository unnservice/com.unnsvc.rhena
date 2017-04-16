
package com.unnsvc.rhena.lifecycle.helpers;

import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;

public class LifecycleUtils {

	/**
	 * To file name without extension
	 * 
	 * @param identifier
	 * @param type
	 * @return
	 */
	public static String toFileName(ModuleIdentifier identifier, EExecutionType type) {

		return identifier.getComponentName().toString() + "." + identifier.getModuleName().toString() + "-" + type.toString().toLowerCase() + "-"
				+ identifier.getVersion().toString();
	}
}
