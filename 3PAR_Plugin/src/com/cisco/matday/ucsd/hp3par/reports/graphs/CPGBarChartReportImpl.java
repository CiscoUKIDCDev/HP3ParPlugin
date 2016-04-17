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
package com.cisco.matday.ucsd.hp3par.reports.graphs;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponse;
import com.cisco.matday.ucsd.hp3par.rest.cpg.json.CPGResponseMember;
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
public class CPGBarChartReportImpl implements SnapshotReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CPGBarChartReportImpl.class);

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {
		SnapshotReport report = new SnapshotReport();
		report.setContext(context);
		report.setReportName(reportEntry.getReportLabel());
		report.setNumericalData(true);
		report.setValueAxisName("Volumes");
		report.setPrecision(0);

		CPGResponse cpgList = HP3ParInventory.getCPGResponse(new HP3ParCredentials(context).getAccountName());

		ReportNameValuePair[] rnv = new ReportNameValuePair[cpgList.getTotal()];
		int j = 0;
		for (CPGResponseMember cpg : cpgList.getMembers()) {
			String name = cpg.getName();
			int vol = cpg.getNumTPVVs() + cpg.getNumFPVVs();
			if (name == null) {
				name = "-";
			}
			rnv[j++] = new ReportNameValuePair(name, vol);
		}
		SnapshotReportCategory cpgs = new SnapshotReportCategory();
		cpgs.setCategoryName("CPGs");
		cpgs.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cpgs
		});

		return report;
	}

}
