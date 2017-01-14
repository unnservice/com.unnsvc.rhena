
package com.unnsvc.rhena.common;

import java.io.Serializable;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;

public interface ICaller extends Serializable {

	public ModuleIdentifier getIdentifier();

	public EExecutionType getExecutionType();

	public IEntryPoint getEntryPoint();

}
