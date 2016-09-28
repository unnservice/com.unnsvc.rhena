package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.Identifier;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;
import com.unnsvc.rhena.core.identifier.Version;

public class TestIdentifiers {

	@Test
	public void testIdentifiers() throws RhenaException {

		Identifier one = Identifier.valueOf("com");
		Assert.assertEquals("com", one.toString());

		Identifier two = Identifier.valueOf("com.t-est");
		Assert.assertEquals("com.t-est", two.toString());

		Identifier three = Identifier.valueOf("com.test.three_");
		Assert.assertEquals("com.test.three_", three.toString());
	}

	@Test
	public void testVersions() throws RhenaException {

		Version one = Version.valueOf("0.0.1");
		Assert.assertEquals("0.0.1", one.toString());

		Version two = Version.valueOf("0.0.1-SNAPSHOT");
		Assert.assertEquals("0.0.1-SNAPSHOT", two.toString());
		
		Version three = Version.valueOf("0.1");
		Assert.assertEquals("0.1", three.toString());
		
		Assert.assertEquals(-1,Version.valueOf("0.0.1").compareTo(Version.valueOf("1.0.0")));
		Assert.assertEquals(1,Version.valueOf("0.1.0").compareTo(Version.valueOf("0.0.1")));
		
		Assert.assertEquals(0,Version.valueOf("0.0.0").compareTo(Version.valueOf("0.0.1")));
		Assert.assertEquals(0,Version.valueOf("0.0.1").compareTo(Version.valueOf("0.0.0")));
		
		Assert.assertEquals(-1,Version.valueOf("0.0.1").compareTo(Version.valueOf("0.0.1-SNAPSHOT")));
	}

	@Test
	public void testQualifiedIdentifier() throws RhenaException {

		QualifiedIdentifier one = new ProjectIdentifier("test.one:project:0.0.1");
		Assert.assertEquals("test.one:project:0.0.1", one.toString());

		QualifiedIdentifier three = new ComponentIdentifier("component:0.0.1");
		Assert.assertEquals("component:0.0.1", three.toString());
	}
}
