package com.cisco.matday.ucsd.hp3par.reports.drilldown;

import com.cisco.matday.ucsd.hp3par.reports.tabular.CPGReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;

@SuppressWarnings("javadoc")
public class VolumeDetails extends DrillableReportWithActions {

	@Override
	public CloupiaReport[] getDrilldownReports() {
		return new CloupiaReport[] {
				new CPGReport(),
		};
	}

	@Override
	public CloupiaReportAction[] getActions() {
		// No actions for now
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getImplementationClass() {
		// As we're paginated, no need to return anything here...
		return null;
	}

	@Override
	public String getReportLabel() {
		// TODO Auto-generated method stub
		return "Volume Details";
	}

	@Override
	public String getReportName() {
		// TODO Auto-generated method stub
		return "Volume Details";
	}

	@Override
	public boolean isEasyReport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLeafReport() {
		// TODO Auto-generated method stub
		return false;
	}

}
