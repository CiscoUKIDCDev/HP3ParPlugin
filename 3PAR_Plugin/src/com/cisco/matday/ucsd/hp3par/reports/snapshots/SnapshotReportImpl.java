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
package com.cisco.matday.ucsd.hp3par.reports.snapshots;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implementation of tabular volume list
 *
 * @author Matt Day
 *
 */
public class SnapshotReportImpl implements TabularReportGeneratorIf {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SnapshotReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		final TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		final TabularReportInternalModel model = new TabularReportInternalModel();
		// Internal ID is hidden from normal view and is used by tasks later
		model.addTextColumn("Internal ID", "Internal ID", true);
		model.addTextColumn("ID", "ID");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Original Volume", "Original Volume");
		model.addTextColumn("Size GiB", "Size GiB");
		model.addTextColumn("Provisioning", "Provisioning");
		model.addTextColumn("User CPG", "User CPG");
		model.addTextColumn("Copy CPG", "Copy CPG");
		model.addTextColumn("Protection", "Protection");
		model.addTextColumn("Comment", "Comment");

		model.completedHeader();

		final HP3ParCredentials credentials = new HP3ParCredentials(context);
		// Get the list from the internal persistence list:
		final VolumeResponse list = HP3ParInventory.getVolumeResponse(credentials);
		// VolumeResponse list = new
		// HP3ParVolumeList(credentials).getVolumeList();

		HashMap<Integer, String> nameMap = new HashMap<>(list.getMembers().size());

		// Build IDs:
		for (VolumeResponseMember volume : list.getMembers()) {
			// Only want non-snapshots
			if (volume.getProvisioningType() == HP3ParConstants.PROVISION_SNAPSHOT) {
				continue;
			}
			nameMap.put(new Integer(volume.getId()), volume.getName());
		}

		for (final VolumeResponseMember volume : list.getMembers()) {
			// Don't show snapshots in this view - that's for the drilldown
			// report
			if (volume.getProvisioningType() != HP3ParConstants.PROVISION_SNAPSHOT) {
				continue;
			}

			// Internal ID, format:
			// accountName;volumeid@accountName@volumeName;cpgname;copycpgname
			// TODO This needs cleaning up and/or putting in its own library
			// (e.g. GetInternalId.volume())
			String copyCpg = (volume.getCopyCPG() == null) ? "" : volume.getCopyCPG();

			model.addTextValue(credentials.getAccountName() + ";" + volume.getId() + "@" + credentials.getAccountName()
					+ "@" + volume.getName() + ";" + volume.getUserCPG() + ";" + copyCpg);
			// Volume ID
			model.addTextValue(Integer.toString(volume.getId()));
			// Name of this volume
			model.addTextValue(volume.getName());

			// Snapshot name
			model.addTextValue(nameMap.get(new Integer(volume.getBaseId())));

			// Round off the size to gb with double precision
			final double volSize = (volume.getSizeMiB() / 1024d);
			model.addTextValue(Double.toString(volSize));

			model.addTextValue(volume.getProvisioningTypeAsText());

			model.addTextValue(volume.getUserCPG());

			model.addTextValue(volume.getCopyCPG());

			model.addTextValue(volume.isReadOnlyAsText());

			model.addTextValue(volume.getComment());

			model.completedRow();
		}
		model.updateReport(report);

		return report;
	}

}
