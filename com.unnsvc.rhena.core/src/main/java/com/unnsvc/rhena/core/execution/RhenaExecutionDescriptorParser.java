
package com.unnsvc.rhena.core.execution;

import java.net.URI;
import java.util.Calendar;

import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaExecutionDescriptorParser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ExecutionDescriptor descriptor;

	public RhenaExecutionDescriptorParser(URI executionDescriptor) throws RhenaException {

		try {

			parse(executionDescriptor);
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	private void parse(URI executionDescriptorUri) throws Exception {

		Document document = parseAndValidateDocument(executionDescriptorUri);
		Node execution = document.getChildNodes().item(0);
		NodeList children = execution.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getLocalName().equals("artifact")) {

					String file = node.getAttributes().getNamedItem("file").getNodeValue();
					String sha1 = node.getAttributes().getNamedItem("sha1").getNodeName();
					Calendar date = DatatypeConverter.parseDateTime(node.getAttributes().getNamedItem("date").getNodeValue());
					descriptor = new ExecutionDescriptor(new ArtifactDescriptor(file, sha1, date));
					return;
				}
			}
		}
	}

	private Document parseAndValidateDocument(URI uri) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(uri.toString());

		// validate
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/execution.xsd"));

		try {
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (Exception ex) {
			log.error("Schema validation failed for : " + uri);
			throw new RhenaException("Schema validation error for: " + uri.toString(), ex);
		}

		return document;
	}

	public ExecutionDescriptor getDescriptor() {

		return null;
	}

}
