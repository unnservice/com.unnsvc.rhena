
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(ICaller caller) throws RhenaException;

}
