/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.matday.ucsd.hp3par.reports;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cisco.matday.ucsd.hp3par.rest.system.json.SystemResponse;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;

public class OverviewTableImpl implements TabularReportGeneratorIf, SnapshotReportGeneratorIf {

	private static final String SYS_INFO_TABLE = "Overview";
	// private static final String TABLE_TWO = "Dummy Table Two";

	private static final String[] GROUP_ORDER = {
			SYS_INFO_TABLE
	};

	private String getAccountName(ReportContext context) {
		String contextId = context.getId();
		String accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}
		return accountName;
	}

	/**
	 * This method returns implemented tabular report,and also perform cleanup
	 * process and updating report.
	 * 
	 * @param reportEntry
	 *            This parameter contains Object of ReportRegistryEntry class
	 *            which is used to register newly created report
	 * @param context
	 *            This parameter contains context of the report
	 * @return report
	 */
	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();
		report.setContext(context);
		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());

		// showing how to add two tables to your summary panel
		// the tables in summary panel are always two column tables
		SummaryReportInternalModel model = new SummaryReportInternalModel();

		HP3ParCredentials credentials = new HP3ParCredentials(context);
		SystemResponse systemInfo = new HP3ParSystem(credentials).getSystem();

		// Build the table
		model.addText("Account Name", getAccountName(context), SYS_INFO_TABLE);
		model.addText("IP Address", systemInfo.getIPv4Addr(), SYS_INFO_TABLE);
		model.addText("System Name", systemInfo.getName(), SYS_INFO_TABLE);
		model.addText("Version", systemInfo.getSystemVersion(), SYS_INFO_TABLE);
		model.addText("Serial Number", systemInfo.getSerialNumber(), SYS_INFO_TABLE);
		model.addText("Total Nodes", Short.toString(systemInfo.getTotalNodes()), SYS_INFO_TABLE);
		model.addText("Total Capacity (GiB)", Double.toString(systemInfo.getTotalCapacityMiB() / 1024d));
		// model.addText("Pod Name", "table one property two", SYS_INFO_TABLE);

		/*
		 * model.addText("table two key one", "table two property one",
		 * DUMMY_TABLE_TWO); model.addText("table two key two",
		 * "table two property two", DUMMY_TABLE_TWO);
		 */

		// you'll notice the charts that show in summary panels aren't mentioned
		// here
		// that's because you'll need to specify in the chart report whether or
		// not the
		// chart should be displayed in the summary panel or not, look in the
		// BarChartReport
		// example for more detail

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		SnapshotReport report = new SnapshotReport();
		report.setContext(context);

		report.setReportName(reportEntry.getReportLabel());

		report.setNumericalData(true);

		report.setDisplayAsPie(true);

		report.setPrecision(0);

		HP3ParCredentials credentials = new HP3ParCredentials(context);
		HP3ParSystem systemInfo = new HP3ParSystem(credentials);
		systemInfo.getSystem();
		double free = systemInfo.getSystem().getFreeCapacityMiB();
		double allocated = systemInfo.getSystem().getAllocatedCapacityMiB();
		double failed = systemInfo.getSystem().getFailedCapacityMiB();

		ReportNameValuePair[] rnv = new ReportNameValuePair[3];
		rnv[0] = new ReportNameValuePair("Free GiB", (free / 1024d));
		rnv[1] = new ReportNameValuePair("Allocated GiB", (allocated / 1024d));
		rnv[2] = new ReportNameValuePair("Failed GiB", (failed / 1024d));

		SnapshotReportCategory cat = new SnapshotReportCategory();

		cat.setCategoryName("Usage");
		cat.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cat
		});

		return report;
	}
}
