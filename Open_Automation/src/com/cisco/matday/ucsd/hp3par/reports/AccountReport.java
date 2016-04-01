package com.cisco.matday.ucsd.hp3par.reports;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.service.cIM.inframgr.collector.impl.GenericInfraAccountReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

public class AccountReport extends GenericInfraAccountReport {

	private static final String NAME = "HP 3PAR";

	//SUPER IMPORTANT MAKE SURE THIS IS ONLY INSTANTIATED ONCE!!!!
	//this is the best way to declare what reports can be drilled down to from the dummy account mgmt report
	private CloupiaReport[] ddReports = new CloupiaReport[] {
			new UsagePieChart(),
			new VolumeAllocationPieChart(),
			new OverviewTable(),
	};

	public AccountReport() {
		super(NAME, HP3ParConstants.INFRA_ACCOUNT_MAGIC_NUMBER, InfraAccountTypes.CAT_STORAGE);
		//you'll need to provide the a name for the report which will be shown in the UI
		//the account type you used when creating your collector
		//the category type you used when creating your collector
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		//Warning: again make sure you DO NOT create new instances of this report, when your reports
		//are registered the framework uses whatever you pass in, so if you pass new instances each
		//time it won't recognize them and probably will not display them in the UI!!!
		return ddReports;
	}

}