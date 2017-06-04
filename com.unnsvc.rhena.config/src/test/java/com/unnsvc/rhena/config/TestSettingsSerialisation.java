
package com.unnsvc.rhena.config;

import java.net.URL;

import org.junit.Test;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.config.settings.RhenaSettingsMarshall_old;

public class TestSettingsSerialisation {

	@Test
	public void testSerialisation() throws Exception {

		URL location = TestSettingsSerialisation.class.getResource("/TEST-INF/settings.xml");
		IRhenaConfiguration config = RhenaSettingsMarshall_old.loadSettings(location);
	}
}
