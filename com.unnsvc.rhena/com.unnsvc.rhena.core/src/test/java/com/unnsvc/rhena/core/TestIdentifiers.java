package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.Identifier;
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
	}

	@Test
	public void testQualifiedIdentifier() throws RhenaException {

		QualifiedIdentifier one = QualifiedIdentifier.valueOf("test.one:project:0.0.1");
		Assert.assertEquals("test.one:project:0.0.1", one.toString());

		QualifiedIdentifier two = QualifiedIdentifier.valueOf("project:0.0.1");
		Assert.assertEquals("project:0.0.1", two.toString());
		Assert.assertNull(two.getComponent());
	}

	@Test(expected = RhenaParserException.class)
	public void testInvalidQualifiedIdentifier() throws RhenaParserException {
		
		QualifiedIdentifier one = QualifiedIdentifier.valueOf("project");
		Assert.assertEquals("project", one.toString());
	}
}
