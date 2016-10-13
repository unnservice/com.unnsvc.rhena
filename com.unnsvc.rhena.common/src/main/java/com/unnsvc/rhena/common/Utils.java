
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class Utils {

	private static Logger log = LoggerFactory.getLogger(Utils.class);

	public static int stackTraceCount() {

		RhenaException re = new RhenaException(new Exception("no-op"));
		return re.getStackTrace().length;
	}

	public static boolean exists(URI uri) {

		String scheme = uri.getScheme();
		if (scheme.equals("http") || scheme.equals("https")) {
			return existsHttp(uri);
		} else {
			return existsOther(uri);
		}
	}

	private static boolean existsHttp(URI uri) {

		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean existsOther(URI uri) {

		try (InputStream is = uri.toURL().openStream()) {

			return true;

		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}

		return false;
	}

	public static URL toUrl(File file) throws RhenaException {

		try {
			return file.toURI().toURL();
		} catch (MalformedURLException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}
}
