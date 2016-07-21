/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
package com.cisco.matday.ucsd.hp3par.reports.graphs;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponse;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;

/**
 * Implements the CPG bar chart
 *
 * @author Matt Day
 *
 */
public class PathPieChartReport implements SnapshotReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PathPieChartReport.class);

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {

		SnapshotReport report = new SnapshotReport();
		report.setContext(context);

		report.setReportName(reportEntry.getReportLabel());

		report.setNumericalData(true);

		report.setDisplayAsPie(true);

		report.setPrecision(0);

		final HP3ParCredentials credentials = new HP3ParCredentials(context);

		HostResponse hostList = HP3ParInventory.getHostResponse(credentials);

		int iscsi = 0;
		int fc = 0;

		for (HostResponseMember host : hostList.getMembers()) {
			fc += host.getFCPaths().size();
			iscsi += host.getiSCSIPaths().size();
		}

		ReportNameValuePair[] rnv = new ReportNameValuePair[2];
		rnv[0] = new ReportNameValuePair("Fibre Channel", fc);
		rnv[1] = new ReportNameValuePair("iSCSI", iscsi);

		SnapshotReportCategory cat = new SnapshotReportCategory();

		cat.setCategoryName("Path Type");
		cat.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cat
		});

		return report;
	}

}
