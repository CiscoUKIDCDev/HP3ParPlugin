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
package com.cisco.matday.ucsd.hp3par.reports.volume;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.CreateVolumeAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.CreateVolumeCopyAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.CreateVolumeSnapshotAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.DeleteVolumeAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.EditVolumeAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.GrowVolumeAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.drilldown.VolumeAllocationPieChart;
import com.cisco.matday.ucsd.hp3par.reports.volume.drilldown.VolumeSnapshotReport;
import com.cisco.matday.ucsd.hp3par.reports.volume.drilldown.VolumeSummaryReport;
import com.cisco.matday.ucsd.hp3par.reports.volume.drilldown.VolumeVlunReport;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;
import com.cloupia.service.cIM.inframgr.reports.simplified.actions.DrillDownAction;

/**
 * Tabular list of volumes with action buttons
 *
 * @author Matt Day
 *
 */
public class VolumeReport extends DrillableReportWithActions {

	/**
	 * Unique identifier for this report
	 */
	public final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.VolumeReport";
	private final static String REPORT_LABEL = "Volumes";

	// This MUST be defined ONCE!
	private CloupiaReport[] drillable = new CloupiaReport[] {
			new VolumeSummaryReport(), new VolumeVlunReport(), new VolumeSnapshotReport(),
			new VolumeAllocationPieChart()
	};

	private CloupiaReportAction[] actions = new CloupiaReportAction[] {
			new CreateVolumeAction(), new EditVolumeAction(), new DeleteVolumeAction(),
			new CreateVolumeSnapshotAction(), new CreateVolumeCopyAction(), new GrowVolumeAction(),
			new DrillDownAction(),
	};

	/**
	 * Overridden default constructor which sets the management column (0)
	 */
	public VolumeReport() {
		super();
		// This sets what column to use as the context ID for child drilldown
		// reports
		this.setMgmtColumnIndex(0);
		// This sets what to show in the GUI in the top
		this.setMgmtDisplayColumnIndex(2);
	}

	@Override
	public Class<VolumeReportImpl> getImplementationClass() {
		return VolumeReportImpl.class;
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		return this.drillable;
	}

	@Override
	public CloupiaReportAction[] getActions() {
		return this.actions;
	}

	@Override
	public String getReportLabel() {
		return VolumeReport.REPORT_LABEL;
	}

	@Override
	public String getReportName() {
		return VolumeReport.REPORT_NAME;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() {
		return false;
	}

	@Override
	public int getMenuID() {
		return 51;
	}

	@Override
	public int getContextLevel() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.VOLUME_LIST_DRILLDOWN);
		return context.getType();
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE);
		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

}
