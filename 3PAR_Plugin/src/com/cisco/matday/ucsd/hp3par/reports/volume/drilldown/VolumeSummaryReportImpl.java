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
package com.cisco.matday.ucsd.hp3par.reports.volume.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParVolumeException;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;

/**
 * Implemenation of Overview table report
 *
 * @author Matt Day
 *
 */
public class VolumeSummaryReportImpl implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(VolumeSummaryReportImpl.class);

	private static final String VOL_INFO_TABLE = "Overview";

	private static final String[] GROUP_ORDER = {
			VOL_INFO_TABLE
	};

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

		String volName = null;
		// Split out hidden field in the format:
		// AccountName;id@AccountName@volumeName
		try {
			volName = context.getId().split(";")[1].split("@")[2];
		}
		catch (@SuppressWarnings("unused") Exception e) {
			logger.warn("Could not get ID from context ID: " + context.getId());
			throw new HP3ParVolumeException("Could not get ID from context");
		}

		// Get volume info:
		VolumeResponseMember volume = HP3ParInventory.getVolumeInfo(credentials, volName);

		String provType = null;
		if (volume.getProvisioningType() == HP3ParConstants.PROVISION_FULL) {
			provType = "Full";
		}
		else if (volume.getProvisioningType() == HP3ParConstants.PROVISION_THIN) {
			provType = "Thin";
		}
		else if (volume.getProvisioningType() == HP3ParConstants.PROVISION_SNAPSHOT) {
			provType = "Copy";
		}
		else {
			provType = "Unknown";
		}

		double total = volume.getSizeMiB() / 1024d;

		// Build the table
		model.addText("Volume Name", volName, VOL_INFO_TABLE);
		model.addText("Provisioning Type", provType, VOL_INFO_TABLE);
		model.addText("WWN", volume.getWwn(), VOL_INFO_TABLE);
		model.addText("User CPG", volume.getUserCPG(), VOL_INFO_TABLE);
		model.addText("Copy CPG", volume.getCopyCPG(), VOL_INFO_TABLE);
		model.addText("Comment", volume.getComment(), VOL_INFO_TABLE);
		model.addText("Size GiB", Double.toString(total), VOL_INFO_TABLE);
		model.addText("User Space GiB", Double.toString(volume.getUserSpace().getFreeMiB() / 1024d), VOL_INFO_TABLE);
		model.addText("Copy Space GiB", Double.toString(volume.getSnapshotSpace().getFreeMiB() / 1024d),
				VOL_INFO_TABLE);
		model.addText("Admin Space GiB", Double.toString(volume.getAdminSpace().getFreeMiB() / 1024d), VOL_INFO_TABLE);

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}

}
