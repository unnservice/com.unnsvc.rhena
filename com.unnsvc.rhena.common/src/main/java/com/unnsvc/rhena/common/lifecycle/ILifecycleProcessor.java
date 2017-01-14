
package com.unnsvc.rhena.common.lifecycle;

import java.io.Serializable;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface ILifecycleProcessor extends Serializable {

	public void configure(ICaller caller, Document configuration) throws RhenaException;

}
