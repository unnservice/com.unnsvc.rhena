
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.Serializable;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface ILifecycleProcessor extends Serializable {

	public void configure(IRhenaModule module, Document configuration) throws RhenaException;

}
