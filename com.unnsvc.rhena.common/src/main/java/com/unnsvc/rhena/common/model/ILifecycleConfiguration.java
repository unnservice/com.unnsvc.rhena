
package com.unnsvc.rhena.common.model;

import java.io.Serializable;

public interface ILifecycleConfiguration extends Iterable<ILifecycleReference>, Serializable {

	public boolean isResolved();

	public void setName(String name);

	public String getName();

}
