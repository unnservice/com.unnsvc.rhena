
package com.unnsvc.rhena.execution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class ExecutionFrame {

	private IEntryPoint incoming;
	private IRhenaModule module;
	private Set<IEntryPoint> outgoing;

	public ExecutionFrame(IRhenaModule module) {

		this.module = module;
		this.outgoing = new HashSet<IEntryPoint>();
	}

	public IEntryPoint getIncoming() {

		return incoming;
	}

	/**
	 * @param incomingType
	 */
	public void setIncoming(IEntryPoint incoming) {

		if (this.incoming == null) {
			this.incoming = incoming;
			return;
		}

		if (incoming.getExecutionType().greaterOrEqualTo(this.incoming.getExecutionType())) {
			this.incoming = incoming;
		}
	}

	public boolean isModuleIdentifier(ModuleIdentifier identifier) {

		if (this.getModule() == null && identifier == null) {

			return true;
		} else if (this.getModule() != null && this.getModule().getIdentifier().equals(identifier)) {

			return true;
		}

		return false;
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

	public void removeOutgoing(IEntryPoint entryPoint) {

		for (Iterator<IEntryPoint> iter = getOutgoing().iterator(); iter.hasNext();) {

			if (iter.next().equals(entryPoint)) {
				iter.remove();
			}
		}
	}

	@Override
	public String toString() {

		return "ExecutionFrame [incoming=" + incoming + ", module=" + (module == null ? null : module.getIdentifier()) + ", outgoing=" + outgoing + "]";
	}

	public boolean isFor(IRhenaModule source) {

		if (source == null && module == null) {
			return true;
		} else if (source != null && module != null) {
			if (source.getIdentifier().equals(module.getIdentifier())) {
				return true;
			}
		}
		return false;
	}

	public boolean isFor(EExecutionType incomingType) {

		if (incoming == null && incomingType == null) {
			return true;
		} else if (incoming != null && incomingType != null && incoming.equals(incomingType)) {
			return true;
		}
		return false;
	}

}
