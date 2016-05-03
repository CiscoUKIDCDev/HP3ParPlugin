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
package com.cisco.matday.ucsd.hp3par.reports.volume.drilldown;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.CreateVolumeSnapshotAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.DeleteVolumeAction;
import com.cisco.matday.ucsd.hp3par.reports.volume.actions.EditVolumeAction;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportWithActions;

/**
 * Tabular list of volumes with action buttons
 *
 * @author Matt Day
 *
 */
public class VolumeSnapshotReport extends CloupiaReportWithActions {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VolumeSnapshotReport.class);

	private CloupiaReportAction[] actions = {
			new CreateVolumeSnapshotAction(false, true), new EditVolumeAction(), new DeleteVolumeAction()
	};

	/**
	 * Unique identifier for this report
	 */
	private final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.tabular.VolumeSnapshotReport";
	/**
	 * User-friendly identifier for this report
	 */
	private final static String REPORT_LABEL = "Snapshots";

	/**
	 * Overridden default constructor which sets the management column (0)
	 */
	public VolumeSnapshotReport() {
		super();
		// IMPORTANT: this tells the framework which column of this report you
		// want to pass as the report context id
		// when there is a UI action being launched in this report
		this.setMgmtColumnIndex(0);
	}

	@Override
	public Class<VolumeSnapshotReportImpl> getImplementationClass() {
		return VolumeSnapshotReportImpl.class;
	}

	@Override
	public String getReportLabel() {
		return REPORT_LABEL;
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() {
		return true;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(HP3ParConstants.VOLUME_LIST_DRILLDOWN);

		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

	@Override
	public CloupiaReportAction[] getActions() {
		return this.actions;
	}

}
