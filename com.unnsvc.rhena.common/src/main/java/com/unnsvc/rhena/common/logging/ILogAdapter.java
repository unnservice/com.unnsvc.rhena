package com.unnsvc.rhena.common.logging;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface ILogAdapter {

	public void info(ModuleIdentifier identifier, String message);

	public void warn(ModuleIdentifier identifier, String message);

	public void debug(ModuleIdentifier identifier, String message);

	public void error(ModuleIdentifier target, String message);

	public void debug(String message);

}
