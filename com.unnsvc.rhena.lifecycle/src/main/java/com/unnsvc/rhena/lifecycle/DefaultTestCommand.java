
package com.unnsvc.rhena.lifecycle;

import java.rmi.RemoteException;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.ICommand;
import com.unnsvc.rhena.common.visitors.IDependencies;

public class DefaultTestCommand implements ICommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void process(ICaller caller, IDependencies dependencies) throws RemoteException {

		if (caller.getExecutionType().equals(EExecutionType.TEST)) {

			System.err.println("Executing tests on " + caller.getIdentifier() + " ...");
		}
	}

	@Override
	public void configure(ICaller caller, Document configuration) throws RhenaException {

	}

}
