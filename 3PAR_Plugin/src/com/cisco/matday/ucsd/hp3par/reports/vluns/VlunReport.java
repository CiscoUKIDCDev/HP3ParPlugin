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
package com.cisco.matday.ucsd.hp3par.reports.vluns;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.reports.vluns.actions.CreateVlunAction;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;

/**
 * Tabular list of VLUNs with action buttons
 *
 * @author Matt Day
 *
 */
public class VlunReport extends DrillableReportWithActions {

	/**
	 * Unique identifier for this report
	 */
	public final static String REPORT_NAME = "com.cisco.matday.ucsd.hp3par.reports.vluns.VlunReport";
	private final static String REPORT_LABEL = "VLUNs";

	// This MUST be defined ONCE!
	private CloupiaReport[] drillable = new CloupiaReport[] {

	};

	private CloupiaReportAction[] actions = new CloupiaReportAction[] {
			new CreateVlunAction()
	};

	/**
	 * Overridden default constructor which sets the management column (0)
	 */
	public VlunReport() {
		super();
		// This sets what column to use as the context ID for child drilldown
		// reports
		this.setMgmtColumnIndex(0);
		// This sets what to show in the GUI in the top
		this.setMgmtDisplayColumnIndex(2);
	}

	@Override
	public Class<VlunReportImpl> getImplementationClass() {
		return VlunReportImpl.class;
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
		return VlunReport.REPORT_LABEL;
	}

	@Override
	public String getReportName() {
		return VlunReport.REPORT_NAME;
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
				.getContextByName(HP3ParConstants.VLUN_LIST_DRILLDOWN);
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
