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
package com.cisco.matday.ucsd.hp3par.reports.hostsets.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParHostSetException;
import com.cisco.matday.ucsd.hp3par.rest.hostsets.json.HostSetResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;

/**
 * Implementation for a host summary
 *
 * @author Matt Day
 *
 */
public class HostSetSummaryReportImpl implements TabularReportGeneratorIf {
	private static Logger logger = Logger.getLogger(HostSetSummaryReportImpl.class);

	private static final String HOSTSET_INFO_TABLE = "Overview";

	private static final String[] GROUP_ORDER = {
			HOSTSET_INFO_TABLE
	};

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

		String hostSetName = null;
		// Split out hidden field in the format:
		// AccountName;id@AccountName@volumeName
		try {
			hostSetName = context.getId().split(";")[1].split("@")[2];
		}
		catch (Exception e) {
			logger.warn("Could not get ID from context ID: " + context.getId());
			throw new HP3ParHostSetException("Could not get ID from context" + e.getMessage());
		}

		// Get volume info:
		HostSetResponseMember hostSet = HP3ParInventory.getHostSetInfo(credentials, hostSetName);

		// Build the table
		model.addText("Set Name", hostSetName, HOSTSET_INFO_TABLE);
		model.addNumber("ID", hostSet.getId(), HOSTSET_INFO_TABLE);
		// Descriptors
		model.addText("Members", Integer.toString(hostSet.getSetMembers().length), HOSTSET_INFO_TABLE);

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}

}
