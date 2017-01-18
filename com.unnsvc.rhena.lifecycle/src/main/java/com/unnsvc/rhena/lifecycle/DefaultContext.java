
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class DefaultContext extends AbstractDefaultContext {

	private static final long serialVersionUID = 1L;

	public DefaultContext() {

	}

	@Override
	public List<File> selectResources(EExecutionType type, String pattern) throws RhenaException {

		return selectResources(type, Pattern.compile(pattern));
	}

	private List<File> selectResources(EExecutionType type, Pattern pattern) throws RhenaException {

		List<File> selected = new ArrayList<File>();

		for (IResource res : getResources()) {
			if (res.getResourceType().equals(type)) {
				try {
					selectRecursive(new File(res.getBaseDirectory(), res.getRelativeSourcePath()), pattern, selected);
				} catch (IOException ioe) {
					throw new RhenaException(ioe.getMessage(), ioe);
				}
			}
		}

		return selected;
	}

	protected void selectRecursive(File srcPath, Pattern p, List<File> selected) throws IOException {

		if (srcPath.isFile()) {

			String pathStr = srcPath.getAbsoluteFile().getCanonicalPath();
			Matcher m = p.matcher(pathStr);
			if (m.find()) {
				selected.add(srcPath);
			}
		} else if (srcPath.isDirectory()) {

			for (File contained : srcPath.listFiles()) {
				selectRecursive(contained, p, selected);
			}
		}
	}

	@Override
	public File getOutputDirectory(IRhenaModule module) {

		File outputDirectory = new File(module.getLocation().getPath(), RhenaConstants.DEFAULT_OUTPUT_DIRECTORY_NAME);
		if (!outputDirectory.isDirectory()) {
			outputDirectory.mkdirs();
		}
		return outputDirectory;
	}

}
