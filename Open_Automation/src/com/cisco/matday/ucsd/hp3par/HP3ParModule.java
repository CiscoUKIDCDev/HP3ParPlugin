package com.cisco.matday.ucsd.hp3par;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.reports.HP3ParVolumeReport;
import com.cisco.matday.ucsd.hp3par.tasks.HP3ParCreateVolume;
import com.cisco.matday.ucsd.hp3par.tasks.HP3ParDeleteVolume;
import com.cloupia.service.cIM.inframgr.AbstractCloupiaModule;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.CustomFeatureRegistry;
import com.cloupia.service.cIM.inframgr.collector.controller.CollectorFactory;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;


public class HP3ParModule extends AbstractCloupiaModule {
	
	private static Logger logger = Logger.getLogger(HP3ParModule.class);


	@Override
	public CloupiaReport[] getReports() {
		CloupiaReport[] report = new CloupiaReport[1];
		report[0] = new HP3ParVolumeReport();
		return report;
	}

	// Return a list of API tasks supported
	public AbstractTask[] getTasks() {
		AbstractTask[] task = new AbstractTask[1];
		task[0] = new HP3ParCreateVolume();
//		task[1] = new HP3ParDeleteVolume();
		return task;
	}

	@Override
	public void onStart(CustomFeatureRegistry arg0) {
		logger.info("HP 3PAR Plugin");
		
	}

	// This method is deprecated, so return null
	public CollectorFactory[] getCollectors() {
		return null;
	}
	
	// Create the plugin as an account in UCSD
	private void createAccountType() {
		return;
	}

}
