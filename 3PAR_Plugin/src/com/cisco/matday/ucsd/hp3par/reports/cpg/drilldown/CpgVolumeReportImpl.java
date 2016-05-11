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
package com.cisco.matday.ucsd.hp3par.reports.cpg.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParAccountException;
import com.cisco.matday.ucsd.hp3par.reports.volume.VolumeReportImpl;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponse;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Builds a list of volumes for a specified CPG
 *
 * @author Matt Day
 *
 */
public class CpgVolumeReportImpl implements TabularReportGeneratorIf {

	private static Logger logger = Logger.getLogger(VolumeReportImpl.class);

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		// Internal ID is hidden from normal view and is used by tasks later
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

		String cpgName = null;
		// Split out hidden field in the format:
		// AccountName;id@AccountName@volumeName
		try {
			cpgName = context.getId().split(";")[1].split("@")[2];
		}
		catch (Exception e) {
			logger.warn("Could not get ID from context ID: " + context.getId() + " " + e.getMessage());
			throw new HP3ParAccountException("Could not get ID from context: " + context.getId());
		}

		VolumeResponse list = HP3ParInventory.getVolumeResponse(credentials);

		for (VolumeResponseMember volume : list.getMembers()) {
			// Only interested in volumes on this CPG (user or copy)
			if ((!cpgName.equals(volume.getUserCPG())) && (!cpgName.equals(volume.getCopyCPG()))) {
				continue;
			}

			// Internal ID, format:
			// accountName;volumeid@accountName@volumeName;cpgname;copycpgname
			// TODO This needs cleaning up and/or putting in its own library
			// (e.g. GetInternalId.volume())
			model.addTextValue(credentials.getAccountName() + ";" + volume.getId() + "@" + credentials.getAccountName()
					+ "@" + volume.getName() + ";" + volume.getUserCPG() + ";" + volume.getCopyCPG());
			// Volume ID
			model.addTextValue(Integer.toString(volume.getId()));
			// Name of this volume
			model.addTextValue(volume.getName());

			// Round off the size to gb with double precision
			double volSize = volume.getSizeMiB() / 1024d;
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
