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
package com.cisco.matday.ucsd.hp3par;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccount;
import com.cisco.matday.ucsd.hp3par.account.handler.HP3ParConnectionHandler;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParConvergedStackBuilder;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventoryItemHandler;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventoryListener;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParAccountSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParCpgSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVolumeSelector;
import com.cisco.matday.ucsd.hp3par.reports.AccountReport;
import com.cisco.matday.ucsd.hp3par.reports.cpg.CPGReport;
import com.cisco.matday.ucsd.hp3par.reports.volume.VolumeReport;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeCopyTask;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeSnapshotTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.CreateVolumeTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.DeleteVolumeTask;
import com.cisco.matday.ucsd.hp3par.workflow.WorkflowInputTypeDeclaration;
import com.cloupia.lib.connector.ConfigItemDef;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.AbstractCloupiaModule;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.CustomFeatureRegistry;
import com.cloupia.service.cIM.inframgr.collector.controller.CollectorFactory;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

/**
 * UCS Director HP 3PAR storage module
 * 
 * @author matt
 *
 */
public class HP3ParModule extends AbstractCloupiaModule {

	private static Logger logger = Logger.getLogger(HP3ParModule.class);

	@Override
	public CloupiaReport[] getReports() {
		logger.info("Adding reports");
		CloupiaReport[] report = new CloupiaReport[] {
				new AccountReport(), new VolumeReport(), new CPGReport(),
		};
		return report;
	}

	// Return a list of API tasks supported
	public AbstractTask[] getTasks() {
		logger.info("Adding tasks");
		AbstractTask[] task = new AbstractTask[] {
				new CreateVolumeTask(), new DeleteVolumeTask(), new CreateVolumeSnapshotTask(),
				new CreateVolumeCopyTask(),
		};
		// task[1] = new HP3ParDeleteVolume();
		return task;
		// return null;
	}

	@Override
	public void onStart(CustomFeatureRegistry cfr) {
		logger.info("HP 3PAR Plugin");
		try {

			logger.info("Registering tabular list of values: " + HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER);
			cfr.registerTabularField(HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER, HP3ParAccountSelector.class, "0", "0");

			logger.info("Registering tabular list of values: " + HP3ParConstants.CPG_LIST_FORM_PROVIDER);
			cfr.registerTabularField(HP3ParConstants.CPG_LIST_FORM_PROVIDER, HP3ParCpgSelector.class, "0", "3");

			logger.info("Registering tabular list of values: " + HP3ParConstants.VOLUME_LIST_FORM_PROVIDER);
			cfr.registerTabularField(HP3ParConstants.VOLUME_LIST_FORM_PROVIDER, HP3ParVolumeSelector.class, "0", "2");

			logger.info("Registering drilldown reports");
			ReportContextRegistry.getInstance().register(HP3ParConstants.VOLUME_LIST_DRILLDOWN,
					HP3ParConstants.VOLUME_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.CPG_LIST_DRILLDOWN,
					HP3ParConstants.CPG_LIST_DRILLDOWN_LABEL);

			logger.info("Registering workflow inputs");
			WorkflowInputTypeDeclaration.registerWFInputs();
			// InputTypeDeclaration.registerWFInputs();

			logger.info("Registering as " + HP3ParConstants.INFRA_ACCOUNT_TYPE);
			ReportContextRegistry.getInstance().register(HP3ParConstants.INFRA_ACCOUNT_TYPE,
					HP3ParConstants.INFRA_ACCOUNT_LABEL);

			// support for new Account Type
			logger.info("Adding account...");
			createAccountType();

		}
		catch (Exception e) {
			logger.error("Error loading HP 3PAR module.", e);
		}

	}

	// This method is deprecated, so return null
	public CollectorFactory[] getCollectors() {
		return null;
	}

	// Create the plugin as an account in UCSD
	private void createAccountType() {
		logger.debug("Creating AccountTypeEntry");
		AccountTypeEntry entry = new AccountTypeEntry();

		logger.debug("Setting credenital class to HP3ParAccount.class");
		// This is mandatory, hold the information for device credential details
		entry.setCredentialClass(HP3ParAccount.class);

		logger.debug("Setting account type to " + HP3ParConstants.INFRA_ACCOUNT_TYPE);
		// This is mandatory, type of the Account will be shown in GUI as drill
		// down box
		entry.setAccountType(HP3ParConstants.INFRA_ACCOUNT_TYPE);

		logger.debug("Setting label to " + HP3ParConstants.INFRA_ACCOUNT_LABEL);
		// This is mandatory, label of the Account
		entry.setAccountLabel(HP3ParConstants.INFRA_ACCOUNT_LABEL);

		logger.debug("Setting category to " + InfraAccountTypes.CAT_STORAGE);
		// This is mandatory, specify the category of the account type ie.,
		// Network / Storage / //Compute
		entry.setCategory(InfraAccountTypes.CAT_STORAGE);

		// This is mandatory
		logger.debug("Setting context type");
		entry.setContextType(
				ReportContextRegistry.getInstance().getContextByName(HP3ParConstants.INFRA_ACCOUNT_TYPE).getType());

		// This is mandatory, on which accounts either physical or virtual
		// account , new account //type belong to.
		entry.setAccountClass(AccountTypeEntry.PHYSICAL_ACCOUNT);

		// Optional , prefix of the task
		entry.setInventoryTaskPrefix(HP3ParConstants.TASK_PREFIX);

		// Optional. Group inventory system tasks under this folder.
		// By default it is grouped under General Tasks
		entry.setWorkflowTaskCategory(HP3ParConstants.WORKFLOW_CATEGORY);

		// Optional , collect the inventory frequency, whenever required you can
		// change the
		// inventory collection frequency, in mins.
		entry.setInventoryFrequencyInMins(15);

		// Register for our own snowflake pod (3Par), Generic pods and also
		// FlexPods
		entry.setPodTypes(new String[] {
				HP3ParConstants.POD_TYPE, "GenericPod", "FlexPod",
		});

		// This is optional, dependents on the need of session for collecting
		// the inventory
		// entry.setConnectorSessionFactory(new FooSessionFactory());

		// This is mandatory, to test the connectivity of the new account. The
		// Handler should be of type PhysicalConnectivityTestHandler.
		entry.setTestConnectionHandler(new HP3ParConnectionHandler());

		// This is mandatory, we can implement inventory listener according to
		// the account Type , collect the inventory details.
		entry.setInventoryListener(new HP3ParInventoryListener());

		// This is mandatory , to show in the converged stack view
		entry.setConvergedStackComponentBuilder(new HP3ParConvergedStackBuilder());

		// This is required to show up the details of the stack view in the GUI
		// entry.setStackViewItemProvider(new FooStackViewProvider());

		// This is required credential.If the Credential Policy support is
		// required for this Account type then this is mandatory, can implement
		// credential check against the policyname.
		// entry.setCredentialParser(new FooAccountCredentialParser());

		try {
			// Adding inventory root
			registerInventoryObjects(entry);
			PhysicalAccountTypeManager.getInstance().addNewAccountType(entry);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void registerInventoryObjects(AccountTypeEntry hP3ParRecoverPointAccountEntry) {
		@SuppressWarnings("unused")
		ConfigItemDef HP3ParRecoverPointStateInfo = hP3ParRecoverPointAccountEntry
				.createInventoryRoot("HP3Par.inventory.root", HP3ParInventoryItemHandler.class);
	}

}
