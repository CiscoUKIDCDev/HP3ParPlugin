package com.cisco.matday.ucsd.hp3par.reports;

import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

public class CPGBarChartReport extends CloupiaNonTabularReport {

	private static final String NAME = "com.cisco.matday.ucsd.hp3par.reports.CPGBarChartReport";
	private static final String LABEL = "CPG usage by volume";

	/**
	 * @return BarChartReport implementation class type
	 */
	@Override
	public Class<CPGBarChartReportImpl> getImplementationClass() {
		return CPGBarChartReportImpl.class;
	}

	/**
	 * @return Report label
	 */
	@Override
	public String getReportLabel() {
		return LABEL;
	}

	/**
	 * @return report name
	 */
	@Override
	public String getReportName() {
		return NAME;
	}

	/**
	 * @return true only if the report implementation followed POJO approach
	 */
	@Override
	public boolean isEasyReport() {
		return false;
	}

	/**
	 * @return true if the report does not have any drillDown report
	 */
	@Override
	public boolean isLeafReport() {
		return true;
	}

	/**
	 * @return report type like tabular report,snapshot report ,summary report
	 *         etc.
	 */
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SNAPSHOT;
	}

	/**
	 * @return report hint
	 */
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_BARCHART;
	}

	/**
	 * @return true if you want this chart to show up in a summary report
	 */
	@Override
	public boolean showInSummary() {
		return true;
	}

}
