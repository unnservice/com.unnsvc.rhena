package com.unnsvc.rhena.builder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;

public class TestIdentifiers {

	@Test
	public void test() throws Exception {
		
		String str = ":something";
		System.err.println("\"" + str.split(":")[0] + "\"");
	}
	
	@Test
	public void testVersion() throws Exception {
		
		String str = "0.1.1";
		
		Pattern p = Pattern.compile(Version.VERSION_PATTERN);
		Matcher m = p.matcher(str);
		Assert.assertTrue(m.matches());
	}
	
	@Test
	public void testModuleIdentifierEquivalende() throws Exception {
		
		Assert.assertEquals(new ModuleIdentifier("component1", "module1", "0.0.1"), new ModuleIdentifier("component1", "module1", "0.0.1"));
	}
}
