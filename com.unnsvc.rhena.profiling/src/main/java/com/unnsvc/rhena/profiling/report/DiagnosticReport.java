
package com.unnsvc.rhena.profiling.report;

public class DiagnosticReport implements IDiagnosticReport {

	private static final long serialVersionUID = 1L;

	private int totalLoadedClasses;

	public DiagnosticReport(int totalLoadedClasses) {

		this.totalLoadedClasses = totalLoadedClasses;
	}

	@Override
	public int getTotalLoadedClasses() {

		return totalLoadedClasses;
	}

}
