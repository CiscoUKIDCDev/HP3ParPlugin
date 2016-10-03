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
package com.cisco.matday.ucsd.hp3par.reports.hosts.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParHostException;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseDescriptors;
import com.cisco.matday.ucsd.hp3par.rest.hosts.json.HostResponseMember;
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
public class HostSummaryReportImpl implements TabularReportGeneratorIf {
	private static Logger logger = Logger.getLogger(HostSummaryReportImpl.class);

	private static final String HOST_INFO_TABLE = "Overview";

	private static final String[] GROUP_ORDER = {
			HOST_INFO_TABLE
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

		String hostName = null;
		// Split out hidden field in the format:
		// AccountName;id@AccountName@volumeName
		try {
			hostName = context.getId().split(";")[1].split("@")[2];
		}
		catch (Exception e) {
			logger.warn("Could not get ID from context ID: " + context.getId());
			throw new HP3ParHostException("Could not get ID from context" + e.getMessage());
		}

		// Get volume info:
		HostResponseMember host = HP3ParInventory.getHostInfo(credentials, hostName);

		// Descriptors is optional so may return null, if so initialise with
		// defaults:
		HostResponseDescriptors desc = host.getDescriptors();
		if (desc == null) {
			desc = new HostResponseDescriptors();
		}

		int paths = host.getFCPaths().size() + host.getiSCSIPaths().size();

		// Build the table
		model.addText("Host Name", hostName, HOST_INFO_TABLE);
		model.addNumber("ID", host.getId(), HOST_INFO_TABLE);
		model.addText("Persona", HostResponseMember.getPersona(host.getPersona()), HOST_INFO_TABLE);
		model.addNumber("Paths", paths, HOST_INFO_TABLE);
		// Descriptors
		model.addText("Location", desc.getLocation(), HOST_INFO_TABLE);
		model.addText("IP Address", desc.getIPAddr(), HOST_INFO_TABLE);
		model.addText("Operating System", desc.getOs(), HOST_INFO_TABLE);
		model.addText("Model", desc.getModel(), HOST_INFO_TABLE);
		model.addText("Contact", desc.getContact(), HOST_INFO_TABLE);
		model.addText("Comment", desc.getComment(), HOST_INFO_TABLE);

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}

}
