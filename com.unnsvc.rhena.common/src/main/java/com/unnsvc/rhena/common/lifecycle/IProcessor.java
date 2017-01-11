
package com.unnsvc.rhena.common.lifecycle;

import java.rmi.RemoteException;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface IProcessor extends ILifecycleProcessor {

	public void process(IRhenaModule module, EExecutionType type, IDependencies dependencies) throws RemoteException;

}
