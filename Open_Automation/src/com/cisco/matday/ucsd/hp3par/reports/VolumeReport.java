package com.cisco.matday.ucsd.hp3par.reports;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;

public class VolumeReport extends CloupiaReport {
	public final static String REPORT_NAME = "HP3ParVolumes";
	public final static String REPORT_LABEL = "Volumes";
	
	public VolumeReport () {
		super();
		//IMPORTANT: this tells the framework which column of this report you want to pass as the report context id
		//when there is a UI action being launched in this report
		this.setMgmtColumnIndex(1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getImplementationClass() {
		return VolumeReportImpl.class;
	}
	
	public CloupiaReportAction[] getActions() {
		return null;
	}
	
	@Override
	public String getReportLabel() {
		return VolumeReport.REPORT_LABEL;
	}

	@Override
	public String getReportName() {
		return VolumeReport.REPORT_NAME;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() { 
		return false;
	}
	
	public int getMenuID() {
		return 51; // NimbleConstants.DUMMY_MENU_1;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		//i'm using an autogenerated report context (which I registered in NimbleModule), as mentioned in documentation
		//the type may vary depending on deployments, so the safest way to retrieve the auto generated type value
		//is to use the getContextByName api!
		DynReportContext context = ReportContextRegistry.getInstance().getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE);
		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());
		
		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;
		
		return rules;
	}

}
