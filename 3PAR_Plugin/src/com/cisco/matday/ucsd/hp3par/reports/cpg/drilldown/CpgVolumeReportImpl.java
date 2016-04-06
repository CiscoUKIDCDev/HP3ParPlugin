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

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.reports.volume.VolumeReportImpl;
import com.cisco.matday.ucsd.hp3par.rest.volumes.HP3ParVolumeList;
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
			logger.warn("Could not get ID from context ID: " + context.getId());
		}

		HP3ParVolumeList list = new HP3ParVolumeList(credentials);

		for (Iterator<VolumeResponseMember> i = list.getVolume().getMembers().iterator(); i.hasNext();) {
			VolumeResponseMember volume = i.next();
			// Only interested in volumes on this CPG (user or copy)
			if ((!volume.getUserCPG().equals(cpgName)) && (!volume.getCopyCPG().equals(cpgName))) {
				continue;
			}
			int provisioning = volume.getProvisioningType();
			String provType = null;

			if (provisioning == HP3ParConstants.PROVISION_FULL) {
				provType = "Full";
			}
			else if (provisioning == HP3ParConstants.PROVISION_THIN) {
				provType = "Thin";
			}
			else if (provisioning == HP3ParConstants.PROVISION_SNAPSHOT) {
				provType = "Copy";
			}
			else {
				provType = "Unknown";
			}

			// Internal ID, format:
			// accountName;volumeid@accountName@volumeName
			model.addTextValue(credentials.getAccountName() + ";" + volume.getId() + "@" + credentials.getAccountName()
					+ "@" + volume.getName());
			// Volume ID
			model.addTextValue(Integer.toString(volume.getId()));
			// Name of this volume
			model.addTextValue(volume.getName());

			// Round off the size to gb with double precision
			Double volSize = (double) (volume.getSizeMiB() / 1024d);
			model.addTextValue(volSize.toString());

			model.addTextValue(provType);

			model.addTextValue(volume.getUserCPG());

			model.addTextValue(volume.getCopyCPG());

			model.addTextValue(volume.getComment());

			model.completedRow();
		}
		model.updateReport(report);

		return report;
	}

}
