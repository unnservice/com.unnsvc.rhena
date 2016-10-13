
package com.unnsvc.rhena.lifecycle.resource;

import java.io.File;

import com.unnsvc.rhena.common.model.lifecycle.IResource;

/**
 * @TODO implement include/exclude filters
 * @author noname
 *
 */
public class Resource implements IResource {

	private File source;
	private File target;
	private boolean staged;

	public Resource(File source, File target) {

		this(source, target, false);
	}

	public Resource(File source, File target, boolean staged) {

		this.source = new File(source.toURI().normalize().getPath());
		this.target = new File(target.toURI().normalize().getPath());
		this.staged = true;
	}

	@Override
	public File getSource() {

		return source;
	}

	@Override
	public File getTarget() {

		return target;
	}

	@Override
	public boolean isStaged() {

		return staged;
	}

	@Override
	public String toString() {

		return "Resource [source=" + source + ", target=" + target + ", staged=" + staged + "]";
	}
}
