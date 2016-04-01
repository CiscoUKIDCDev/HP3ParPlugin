package com.cisco.matday.ucsd.hp3par.reports;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

public class VolumeAllocationPieChart  extends CloupiaNonTabularReport {

	// Unique report name here.
	public final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.VolumeAllocationPieChart";

	private static final String REPORT_LABEL = "Virtual Volume Allocation";

	// Returns implementation class
	@Override
	public Class<VolumeAllocationPieChartImpl> getImplementationClass() {
		return VolumeAllocationPieChartImpl.class;
	}

	// Returns report type for pie chart as shown below
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SNAPSHOT;
	}

	@Override
	public String getReportLabel() {
		return REPORT_LABEL;
	}

	@Override
	public boolean isLeafReport() {
		return true;
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	// Forcing this report into the Physical->Storage part of the GUI.
	@Override
	public int getMenuID() {
		return 51;
	}

	/**
	 * @return true if you want this chart to show up in a summary report
	 */
	@Override
	public boolean showInSummary() {
		return true;
	}

	// Returns report hint for pie chart as shown below
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_PIECHART;
	}

	@Override
	public ContextMapRule[] getMapRules() {

		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE);

		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

}