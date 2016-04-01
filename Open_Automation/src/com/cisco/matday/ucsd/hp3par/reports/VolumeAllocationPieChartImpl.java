package com.cisco.matday.ucsd.hp3par.reports;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMembers;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;

public class VolumeAllocationPieChartImpl implements SnapshotReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UsagePieChartImpl.class);

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {

		SnapshotReport report = new SnapshotReport();
		report.setContext(context);

		report.setReportName(reportEntry.getReportLabel());

		report.setNumericalData(true);

		report.setDisplayAsPie(true);

		report.setPrecision(0);

		HP3ParCredentials credentials = new HP3ParCredentials(context);
		VolumeResponse volumes = new HP3ParVolumeList(credentials).getVolume();
		
		double adminSpace = 0;
		double userSpace = 0;
		double snapSpace = 0;
		
		for (Iterator<VolumeResponseMembers> i = volumes.getMembers().iterator(); i.hasNext();) {
			VolumeResponseMembers volume = i.next();
			adminSpace += volume.getAdminSpace().getUsedMiB();
			userSpace += volume.getUserSpace().getUsedMiB();
			snapSpace += volume.getSnapshotSpace().getUsedMiB();
		}
		
		ReportNameValuePair[] rnv = new ReportNameValuePair[3];
		rnv[0] = new ReportNameValuePair("User Space (GiB)", (userSpace / 1024d));
		rnv[1] = new ReportNameValuePair("Snapshot Space (GiB)", (snapSpace / 1024d));
		rnv[2] = new ReportNameValuePair("Admin Space (GiB)", (adminSpace / 1024d));
		SnapshotReportCategory cat = new SnapshotReportCategory();
		
		cat.setCategoryName("Virtual Volume Allocation");
		cat.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cat
		});

		return report;

	}

}
