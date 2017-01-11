
package com.unnsvc.rhena.agent;

import java.security.Permission;

/**
 * Ended up being unnecessary but was intended for use in configuring a security
 * manager for RMI during lifecycle agent spawning. // if
 * (System.getSecurityManager() == null) { // System.setSecurityManager(new
 * PermitAllSecurityManager()); // }
 * 
 * 
 * @author noname
 *
 */
public class PermitAllSecurityManager extends SecurityManager {

	public PermitAllSecurityManager() {

	}

	public void checkPermission() {

	}

	public void checkPermission(Permission perm) {

	}

	public void checkPermission(Permission perm, Object context) {

	}
}
