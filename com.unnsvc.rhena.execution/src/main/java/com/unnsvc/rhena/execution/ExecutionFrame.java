
package com.unnsvc.rhena.execution;

import java.util.HashSet;
import java.util.Set;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionFrame {

	private Set<IEntryPoint> incoming;
	private IRhenaModule module;
	private Set<IEntryPoint> outgoing;

	public ExecutionFrame(IRhenaModule module) {

		this.incoming = new HashSet<IEntryPoint>();
		this.module = module;
		this.outgoing = new HashSet<IEntryPoint>();
	}

	public Set<IEntryPoint> getIncoming() {

		return incoming;
	}

	public void addIncoming(IEntryPoint incoming) {

		this.incoming.add(incoming);
	}

	public IRhenaModule getModule() {

		return module;
	}

	public Set<IEntryPoint> getOutgoing() {

		return outgoing;
	}

	public void addOutgoing(IEntryPoint outgoing) {

		this.outgoing.add(outgoing);
	}
}
