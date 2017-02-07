
package com.unnsvc.rhena.cmdline;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.cmdline.flags.ArgumentException;
import com.unnsvc.rhena.cmdline.flags.ArgumentParser;

public class TestArgumentParser {

	@Test(expected = ArgumentException.class)
	public void testArgumentNoFlag() throws ArgumentException {

		ArgumentParser ae = new ArgumentParser("--something".split("\\s+"));
	}

	@Test
	public void testArgument() throws ArgumentException {

		ArgumentParser ap = new ArgumentParser("--workspace path -w path --workspace another".split("\\s+"));
		Assert.assertEquals(3, ap.getFlag("workspace", "w").size());
	}
}
