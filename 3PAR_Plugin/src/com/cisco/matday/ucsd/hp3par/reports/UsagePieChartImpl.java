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

import org.apache.log4j.Logger;

import com.cloupia.model.cIM.ReportNameValuePair;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.system.HP3ParSystem;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;

public class UsagePieChartImpl implements SnapshotReportGeneratorIf {

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

		/*
		 * // Get array model and OS version level. GetArrayInventory gai = new
		 * GetArrayInventory(username, password, deviceIp);
		 * 
		 * // Get array inventory. ArrayDataObject ado = gai.getInventory();
		 * 
		 * List<Long> volUsageBytesList = ado.getVolUsageBytes(); List<Long>
		 * snapUsageBytesList = ado.getSnapUsageBytes(); List<Long>
		 * usableCapacityBytesList = ado.getUsableCapacityBytes();
		 * 
		 * Long volUsageBytes = (long) 0; Long snapUsageBytes = (long) 0; Long
		 * usableCapacityBytes = (long) 0;
		 * 
		 * if (usableCapacityBytesList.size() > 0) {
		 * 
		 * volUsageBytes = volUsageBytesList.get(0); snapUsageBytes =
		 * snapUsageBytesList.get(0); usableCapacityBytes =
		 * (usableCapacityBytesList.get(0) - (volUsageBytes + snapUsageBytes));
		 * 
		 * }
		 * 
		 * // creation of report name value pair goes ReportNameValuePair[] rnv
		 * = new ReportNameValuePair[3];
		 * 
		 * rnv[0] = new ReportNameValuePair("Volume Bytes", volUsageBytes);
		 * rnv[1] = new ReportNameValuePair("Snapshot Bytes", snapUsageBytes);
		 * rnv[2] = new ReportNameValuePair("Available Bytes",
		 * usableCapacityBytes);
		 * 
		 * // setting of report category goes SnapshotReportCategory cat = new
		 * SnapshotReportCategory();
		 * 
		 * cat.setCategoryName(""); cat.setNameValuePairs(rnv);
		 * 
		 * report.setCategories(new SnapshotReportCategory[] { cat });
		 */

	}

}
