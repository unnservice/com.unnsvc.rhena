
package com.unnsvc.rhena.core.execution;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ArtifactDescriptor;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifact;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.execution.PackagedArtifact;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RhenaArtifactsDescriptorParser {

	private ModuleIdentifier identifier;
	private EExecutionType type;
	private URI baseUri;

	/**
	 * 
	 * @param identifier
	 * @param type
	 * @param executionDescriptor
	 * @throws RhenaException
	 */
	public RhenaArtifactsDescriptorParser(ModuleIdentifier identifier, EExecutionType type, URI baseUri) throws RhenaException {

		this.identifier = identifier;
		this.type = type;
		this.baseUri = baseUri;
		try {

			URI descriptor = new URI(baseUri.toString() + "/" + RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME).normalize();
			parse(descriptor);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private List<IArtifactDescriptor> artifactDescriptors = new ArrayList<IArtifactDescriptor>();
	private Calendar date;

	private void parse(URI executionDescriptorUri) throws Exception {

		Document document = parseAndValidateDocument(executionDescriptorUri);
		Node execution = document.getChildNodes().item(0);
		NodeList children = execution.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getLocalName().equals("meta")) {

					String dateValue = node.getAttributes().getNamedItem("date").getNodeValue();
					date = DatatypeConverter.parseDateTime(dateValue);
				} else if (node.getLocalName().equals("artifact")) {

					String classifier = node.getAttributes().getNamedItem("classifier").getNodeValue();
					
					Node primaryNode = Utils.getChildNode(node, "primary");
					String artifactName = primaryNode.getAttributes().getNamedItem("name").getNodeValue();
					String sha1 = primaryNode.getAttributes().getNamedItem("sha1").getNodeValue();
					URI location = new URI(baseUri.toString() + "/" + artifactName).normalize();
					IArtifact primary = new PackagedArtifact(artifactName, location.toURL(), sha1);
					
					Node sourcesNode = Utils.getChildNode(node, "sources");
					artifactName = sourcesNode.getAttributes().getNamedItem("name").getNodeValue();
					sha1 = sourcesNode.getAttributes().getNamedItem("sha1").getNodeValue();
					location = new URI(baseUri.toString() + "/" + artifactName).normalize();
					IArtifact sources = new PackagedArtifact(artifactName, location.toURL(), sha1);

					artifactDescriptors.add(new ArtifactDescriptor(classifier, primary, sources));

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
		Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/artifacts.xsd"));

		try {
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (Exception ex) {
			throw new RhenaException("Schema validation error for: " + uri.toString(), ex);
		}

		return document;
	}

	public IRhenaExecution getExecution() {

		return new RemoteExecution(identifier, type, artifactDescriptors, date);
	}

}
