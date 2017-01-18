
package com.unnsvc.rhena.common.execution;

import java.io.Serializable;
import java.net.URL;

public interface IResult extends Serializable {

	public String getName();

	public URL getResultUrl();

}
