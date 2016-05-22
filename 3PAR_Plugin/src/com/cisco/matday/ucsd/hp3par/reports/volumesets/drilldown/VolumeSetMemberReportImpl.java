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
package com.cisco.matday.ucsd.hp3par.reports.volumesets.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.sets.json.SetResponseMember;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implements a volume report list
 *
 * @author Matt Day
 *
 */
public class VolumeSetMemberReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VolumeSetMemberReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTextColumn("Internal ID", "Internal ID", true);
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Size GiB", "Size GiB");
		model.addTextColumn("Provisioning", "Provisioning");
		model.addTextColumn("User CPG", "User CPG");
		model.addTextColumn("Copy CPG", "Copy CPG");
		model.addTextColumn("Protection", "Protection");
		model.addTextColumn("Comment", "Comment");

		model.completedHeader();

		HP3ParCredentials credentials = new HP3ParCredentials(context);
		// accountName;volumeid@accountName@volumeName
		final String volumeSetId = context.getId().split(";")[1].split("@")[0];
		final String volumeSetName = context.getId().split(";")[1].split("@")[2];

		// VolumeResponse volumeList = HP3ParInventory.getVolumeResponse(new
		// HP3ParCredentials(context));
		SetResponseMember volumeSetList = HP3ParInventory.getVolumeSetInfo(credentials, volumeSetName);

		for (String volumeName : volumeSetList.getSetMembers()) {
			VolumeResponseMember volume = HP3ParInventory.getVolumeInfo(credentials, volumeName);

			// Don't show snapshots in this view - that's for the drilldown
			// report
			if (volume.getProvisioningType() == HP3ParConstants.PROVISION_SNAPSHOT) {
				continue;
			}

			final String internalId = credentials.getAccountName() + ";" + volume.getId() + "@"
					+ credentials.getAccountName() + "@" + volume.getName() + ";" + volumeSetId + "@"
					+ credentials.getAccountName() + "@" + volumeSetName;

			model.addTextValue(internalId);

			// Volume ID
			model.addTextValue(Integer.toString(volume.getId()));
			// Name of this volume
			model.addTextValue(volume.getName());

			// Round off the size to gb with double precision
			final double volSize = (volume.getSizeMiB() / 1024d);
			model.addTextValue(Double.toString(volSize));

			model.addTextValue(volume.getProvisioningTypeAsText());

			model.addTextValue(volume.getUserCPG());

			model.addTextValue(volume.getCopyCPG());

			model.addTextValue(volume.isReadOnlyAsText());

			model.addTextValue(volume.getComment());

			model.completedRow();

			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
