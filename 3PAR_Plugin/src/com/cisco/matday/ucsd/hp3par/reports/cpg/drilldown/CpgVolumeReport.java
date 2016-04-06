package com.cisco.matday.ucsd.hp3par.reports.cpg.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

/**
 * Provides a table for all the volumes in a CPG
 * 
 * @author Matt Day
 *
 */
public class CpgVolumeReport extends CloupiaReport {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CpgVolumeReport.class);

	/**
	 * Unique identifier for this report
	 */
	private final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.cpg.drilldown.CpgVolumeReport";
	/**
	 * User-friendly identifier for this report
	 */
	private final static String REPORT_LABEL = "Volumes";

	/**
	 * Overridden default constructor which sets the management column (0)
	 */
	public CpgVolumeReport() {
		super();
		// IMPORTANT: this tells the framework which column of this report you
		// want to pass as the report context id
		// when there is a UI action being launched in this report
		this.setMgmtColumnIndex(0);
	}

	@Override
	public Class<CpgVolumeReportImpl> getImplementationClass() {
		return CpgVolumeReportImpl.class;
	}

	@Override
	public String getReportLabel() {
		return REPORT_LABEL;
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() {
		return true;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.CPG_LIST_DRILLDOWN);

		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

}
