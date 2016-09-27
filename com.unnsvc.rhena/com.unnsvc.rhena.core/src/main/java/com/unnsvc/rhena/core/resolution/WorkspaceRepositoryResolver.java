package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.model.RhenaComponentDescriptor;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.parsers.ComponentParser;

public class WorkspaceRepositoryResolver implements IResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File location;

	public WorkspaceRepositoryResolver(File location) {

		this.location = location.getAbsoluteFile();
	}
	
	public WorkspaceRepositoryResolver(String location) {
		
		this(new File(location));
	}

	@Override
	public ResolutionResult resolveProject(ResolutionContext context, ProjectIdentifier projectIdentifier) throws ResolverException {

		File componentLocation = new File(location, projectIdentifier.getComponent().toString());
		File componentDescriptorLocation = new File(componentLocation, "component.xml");
		if (componentDescriptorLocation.exists() && componentDescriptorLocation.isFile()) {
			RhenaComponentDescriptor componentDescriptor = resolveComponentDescriptor(context, componentDescriptorLocation, projectIdentifier.getComponent().toString());
			RhenaProject project = componentDescriptor.getProject(projectIdentifier.getProject().toString());
			if (project == null || project.getVersion().matches(projectIdentifier.getVersion())) {
				return new ResolutionFailure("Component exists in repository but not project in component");
			}
			return new ProjectResolutionResult(project);
		} else {
			return new ResolutionFailure("Does not exist in repository");
		}
	}

	@Override
	public ResolutionResult resolveComponent(ResolutionContext context, ComponentIdentifier componentIdentifier) throws ResolverException {

		File componentLocation = new File(location, componentIdentifier.toString());
		File componentDescriptorLocation = new File(componentLocation, "component.xml");
		if (componentDescriptorLocation.exists() && componentDescriptorLocation.isFile()) {
			RhenaComponentDescriptor componentDescriptor = resolveComponentDescriptor(context, componentDescriptorLocation, componentIdentifier.toString());
			return new ComponentResolutionResult(componentDescriptor);
		} else {
			return new ResolutionFailure("Does not exist in repository");
		}
	}

	/**
	 * @param context
	 * @param componentIdentifier
	 * @return Null if component does not exist in repository
	 * @throws ResolverException
	 *             on any error
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private RhenaComponentDescriptor resolveComponentDescriptor(ResolutionContext context, File componentDescriptorLocation, String componentName) throws ResolverException {

		try {

			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder builder = fact.newDocumentBuilder();
			Document document = builder.parse(componentDescriptorLocation);
			
			ComponentParser parser = new ComponentParser(context, document, componentName);
			RhenaComponentDescriptor componentDescriptor = parser.getComponentDescriptor();
			
			return componentDescriptor;
		} catch (Exception ex) {
			log.debug(ex.getMessage(), ex);
			throw new ResolverException(ex);
		}
	}

	@Override
	public URI getLocation() {

		return location.toURI();
	}

}
