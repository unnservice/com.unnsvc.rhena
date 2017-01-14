
package com.unnsvc.rhena.common.lifecycle;

import java.rmi.RemoteException;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface IProcessor extends ILifecycleProcessor {

	public void process(ICaller caller, IDependencies dependencies) throws RemoteException;

}
