package com.cisco.matday.ucsd.hp3par.reports;

import org.apache.log4j.Logger;

import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

public class CPGBarChartReport extends CloupiaNonTabularReport {

	private static final String NAME = "com.cisco.matday.ucsd.hp3par.reports.CPGBarChartReport";
	private static final String LABEL = "CPG usage by volume";
	private static Logger logger = Logger.getLogger(CPGBarChartReportImpl.class);

	/**
	 * @return BarChartReport implementation class type
	 */
	@Override
	public Class<CPGBarChartReportImpl> getImplementationClass() {
		logger.info("Asked for bar chart implementation class");
		return CPGBarChartReportImpl.class;
	}

	/**
	 * @return Report label
	 */
	@Override
	public String getReportLabel() {
		logger.info("Asked for bar chart label");
		return LABEL;
	}

	// Forcing this report into the Physical->Storage part of the GUI.
	@Override
	public int getMenuID() {
		logger.info("Asked for bar chart menu id");
		return 51;
	}

	/**
	 * @return report name
	 */
	@Override
	public String getReportName() {
		logger.info("Asked for bar chart name");
		return NAME;
	}

	/**
	 * @return true only if the report implementation followed POJO approach
	 */
	@Override
	public boolean isEasyReport() {
		logger.info("Asked for bar chart easy report");
		return false;
	}

	/**
	 * @return true if the report does not have any drillDown report
	 */
	@Override
	public boolean isLeafReport() {
		logger.info("Asked for bar chart leaf report");
		return true;
	}

	/**
	 * @return report type like tabular report,snapshot report ,summary report
	 *         etc.
	 */
	@Override
	public int getReportType() {
		logger.info("Asked for bar chart type");
		return ReportDefinition.REPORT_TYPE_SNAPSHOT;
	}

	/**
	 * @return report hint
	 */
	@Override
	public int getReportHint() {
		logger.info("Asked for bar chart hint");
		return ReportDefinition.REPORT_HINT_BARCHART;
	}

	/**
	 * @return true if you want this chart to show up in a summary report
	 */
	@Override
	public boolean showInSummary() {
		logger.info("Asked for bar chart show in summary");
		return true;
	}

}
