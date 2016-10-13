
package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface ILifecycleProcessor {

	public void configure(IRhenaModule module, Document configuration);

}
