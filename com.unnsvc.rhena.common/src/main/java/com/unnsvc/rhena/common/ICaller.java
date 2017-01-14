
package com.unnsvc.rhena.common;

import java.io.Serializable;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * The caller is passed through the execution chain and into the lifecycle
 * 
 * @author noname
 *
 */
public interface ICaller extends Serializable {

	public ModuleIdentifier getIdentifier();

	public EExecutionType getExecutionType();

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

}
