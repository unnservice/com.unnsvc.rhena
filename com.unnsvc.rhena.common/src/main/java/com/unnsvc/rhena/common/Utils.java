
package com.unnsvc.rhena.common;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;

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
			log.trace(e.getMessage(), e);
			return false;
		}
	}

	private static boolean existsOther(URI uri) {

		try (InputStream is = uri.toURL().openStream()) {

			return true;

		} catch (Exception ex) {

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

	public static Document newEmptyDocument() throws RhenaException {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			return document;
		} catch (ParserConfigurationException pce) {

			throw new RhenaException(pce.getMessage(), pce);
		}
	}

	public static EExecutionType valueOf(String executionType) throws RhenaException {

		switch (executionType) {
			case "model":
				return EExecutionType.MODEL;
			case "framework":
				return EExecutionType.FRAMEWORK;
			case "deliverable":
				return EExecutionType.DELIVERABLE;
			case "test":
				return EExecutionType.TEST;
			case "integration":
				return EExecutionType.INTEGRATION;
			case "prototype":
				return EExecutionType.PROTOTYPE;
			default:
				throw new RhenaException("Unknown execution type: " + executionType);
		}
	}

	public static URI toUri(String uriString) throws RhenaException {

		try {
			return new URI(uriString).normalize();
		} catch (URISyntaxException use) {
			throw new RhenaException(use.getMessage(), use);
		}
	}
}
