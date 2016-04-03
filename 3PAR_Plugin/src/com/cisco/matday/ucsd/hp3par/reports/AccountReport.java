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
package com.cisco.matday.ucsd.hp3par.reports;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.service.cIM.inframgr.collector.impl.GenericInfraAccountReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

public class AccountReport extends GenericInfraAccountReport {

	//SUPER IMPORTANT MAKE SURE THIS IS ONLY INSTANTIATED ONCE!!!!
	//this is the best way to declare what reports can be drilled down to from the dummy account mgmt report
	private CloupiaReport[] ddReports = new CloupiaReport[] {
			new OverviewTable(),
			new UsagePieChart(),
			new VolumeAllocationPieChart(),
			new CPGBarChartReport(),
	};

	public AccountReport() {
		super(HP3ParConstants.INFRA_ACCOUNT_NAME, HP3ParConstants.INFRA_ACCOUNT_MAGIC_NUMBER, InfraAccountTypes.CAT_STORAGE);
		//you'll need to provide the a name for the report which will be shown in the UI
		//the account type you used when creating your collector
		//the category type you used when creating your collector
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		//Warning: again make sure you DO NOT create new instances of this report, when your reports
		//are registered the framework uses whatever you pass in, so if you pass new instances each
		//time it won't recognize them and probably will not display them in the UI!!!
		return ddReports;
	}

}