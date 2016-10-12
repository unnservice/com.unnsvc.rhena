
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IBoundedModelVisitor extends IModelVisitor {

	/**
	 * This is called once when starting to visit tree
	 * 
	 * @param module
	 * @return
	 */
	public void startTree(IRhenaModule module);

	
	/**
	 * This is called once when ending tree visit
	 * @param module
	 * @return
	 */
	public void endTree(IRhenaModule module);
}
