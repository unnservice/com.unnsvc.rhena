
package com.unnsvc.rhena.core.events;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;

public class ModuleAddRemoveEvent implements IContextEvent {

	private ModuleIdentifier identifier;
	private EAddRemove addRemove;

	public ModuleAddRemoveEvent(ModuleIdentifier identifier, EAddRemove addRemove) {

		this.identifier = identifier;
		this.addRemove = addRemove;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public EAddRemove getAddRemove() {

		return addRemove;
	}

	public static enum EAddRemove {

		ADDED, REMOVED
	}
}
