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

import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.account.HP3ParAccountDBStore;
import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.account.handler.HP3ParConnectionHandler;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParConvergedStackBuilder;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventory;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventoryItemHandler;
import com.cisco.matday.ucsd.hp3par.account.inventory.HP3ParInventoryListener;
import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.exceptions.HP3ParAccountException;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParAccountSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParCpgSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParDiskTypeSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParFCSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParHostAndHostSetSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParHostSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParHostSetSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParPortSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParRaidTypeSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVlunSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVolumeAndVolumeSetSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVolumeSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVolumeSetSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3PariSCSISelector;
import com.cisco.matday.ucsd.hp3par.reports.AccountReport;
import com.cisco.matday.ucsd.hp3par.reports.cpg.CPGReport;
import com.cisco.matday.ucsd.hp3par.reports.hosts.HostReport;
import com.cisco.matday.ucsd.hp3par.reports.hostsets.HostSetReport;
import com.cisco.matday.ucsd.hp3par.reports.paths.PathReport;
import com.cisco.matday.ucsd.hp3par.reports.polling.PollingReport;
import com.cisco.matday.ucsd.hp3par.reports.ports.PortListReport;
import com.cisco.matday.ucsd.hp3par.reports.vluns.VlunReport;
import com.cisco.matday.ucsd.hp3par.reports.volume.VolumeReport;
import com.cisco.matday.ucsd.hp3par.reports.volumesets.VolumeSetReport;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeCopyTask;
import com.cisco.matday.ucsd.hp3par.tasks.copy.CreateVolumeSnapshotTask;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.CreateCpgTask;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.DeleteCpgTask;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.EditCpgTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.AddFCWWNHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.AddiSCSIHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.CreateHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.DeleteHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.EditHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.RemoveFCWWNHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hosts.RemoveiSCSIHostTask;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.AddHostToHostSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.CreateHostSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.DeleteHostSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.EditHostSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.hostsets.RemoveHostFromHostSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.system.CollectInventoryTask;
import com.cisco.matday.ucsd.hp3par.tasks.vluns.CreateVlunTask;
import com.cisco.matday.ucsd.hp3par.tasks.vluns.DeleteVlunTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.CreateVolumeTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.DeleteVolumeTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.EditVolumeTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumes.GrowVolumeTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.AddVolumeToVolumeSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.CreateVolumeSetSnapshotTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.CreateVolumeSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.DeleteVolumeSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.EditVolumeSetTask;
import com.cisco.matday.ucsd.hp3par.tasks.volumesets.RemoveVolumeFromVolumeSetTask;
import com.cisco.matday.ucsd.hp3par.workflow.WorkflowInputTypeDeclaration;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.ConfigItemDef;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.InfraAccount;
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
		final CloupiaReport[] report = new CloupiaReport[] {
				new AccountReport(), new PollingReport(), new VolumeReport(), new VolumeSetReport(), new CPGReport(),
				new HostReport(), new HostSetReport(), new VlunReport(), new PortListReport(), new PathReport(),
		};
		return report;
	}

	// Return a list of API tasks supported
	@Override
	public AbstractTask[] getTasks() {
		logger.info("Adding tasks");
		final AbstractTask[] task = new AbstractTask[] {
				new CreateVolumeTask(), new DeleteVolumeTask(), new CreateVolumeSnapshotTask(),
				new CreateVolumeCopyTask(), new EditVolumeTask(), new CreateHostTask(), new DeleteHostTask(),
				new CreateVlunTask(), new DeleteVlunTask(), new CollectInventoryTask(), new AddFCWWNHostTask(),
				new RemoveFCWWNHostTask(), new AddiSCSIHostTask(), new RemoveiSCSIHostTask(), new EditHostTask(),
				new CreateHostSetTask(), new EditHostSetTask(), new DeleteHostSetTask(), new AddHostToHostSetTask(),
				new RemoveHostFromHostSetTask(), new CreateVolumeSetTask(), new EditVolumeSetTask(),
				new DeleteVolumeSetTask(), new AddVolumeToVolumeSetTask(), new RemoveVolumeFromVolumeSetTask(),
				new CreateVolumeSetSnapshotTask(), new GrowVolumeTask(), new CreateCpgTask(), new DeleteCpgTask(),
				new EditCpgTask()
		};
		return task;
	}

	@Override
	public void onStart(CustomFeatureRegistry cfr) {
		logger.info("HP 3PAR Plugin");
		try {
			// Register LOV inputs
			cfr.registerTabularField(HP3ParConstants.ACCOUNT_LIST_FORM_PROVIDER, HP3ParAccountSelector.class, "0", "0");
			cfr.registerTabularField(HP3ParConstants.CPG_LIST_FORM_PROVIDER, HP3ParCpgSelector.class, "0", "3");
			cfr.registerTabularField(HP3ParConstants.VOLUME_LIST_FORM_PROVIDER, HP3ParVolumeSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.PORT_LIST_FORM_PROVIDER, HP3ParPortSelector.class, "0", "1");
			cfr.registerTabularField(HP3ParConstants.HOST_LIST_FORM_PROVIDER, HP3ParHostSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.VLUN_LIST_FORM_PROVIDER, HP3ParVlunSelector.class, "0", "4");
			cfr.registerTabularField(HP3ParConstants.VOLUMESET_LIST_FORM_PROVIDER, HP3ParVolumeSetSelector.class, "0",
					"2");
			cfr.registerTabularField(HP3ParConstants.HOSTSET_LIST_FORM_PROVIDER, HP3ParHostSetSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_PROVIDER,
					HP3ParHostAndHostSetSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.VOLUME_AND_VOLUMESET_LIST_FORM_PROVIDER,
					HP3ParVolumeAndVolumeSetSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.ISCSI_LIST_FORM_PROVIDER, HP3PariSCSISelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.FCWWN_LIST_FORM_PROVIDER, HP3ParFCSelector.class, "0", "2");
			cfr.registerTabularField(HP3ParConstants.RAID_LIST_FORM_PROVIDER, HP3ParRaidTypeSelector.class, "0", "1");
			cfr.registerTabularField(HP3ParConstants.DISK_LIST_FORM_PROVIDER, HP3ParDiskTypeSelector.class, "0", "1");

			// Register drilldown reports
			ReportContextRegistry.getInstance().register(HP3ParConstants.VOLUME_LIST_DRILLDOWN,
					HP3ParConstants.VOLUME_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.CPG_LIST_DRILLDOWN,
					HP3ParConstants.CPG_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.HOST_LIST_DRILLDOWN,
					HP3ParConstants.HOST_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.VLUN_LIST_DRILLDOWN,
					HP3ParConstants.VLUN_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.PATH_LIST_DRILLDOWN,
					HP3ParConstants.PATH_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.VOLUMESET_LIST_DRILLDOWN,
					HP3ParConstants.VOLUMESET_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.HOSTSET_LIST_DRILLDOWN,
					HP3ParConstants.HOSTSET_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.POLLING_LIST_DRILLDOWN,
					HP3ParConstants.POLLING_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(HP3ParConstants.PORT_LIST_DRILLDOWN,
					HP3ParConstants.PORT_LIST_DRILLDOWN_LABEL);

			// Register workflow inputs
			WorkflowInputTypeDeclaration.registerWFInputs();

			// Register the account type drilldown
			ReportContextRegistry.getInstance().register(HP3ParConstants.INFRA_ACCOUNT_TYPE,
					HP3ParConstants.INFRA_ACCOUNT_LABEL);

			this.createAccountType();

			try {
				logger.info("Looping through accounts to kick off inventory");
				final ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
				final List<InfraAccount> objs = store.queryAll();
				for (final InfraAccount a : objs) {
					final PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
					// Important to check if the account type is null first
					if ((acc != null) && (acc.getAccountType() != null)
							&& (acc.getAccountType().equals(HP3ParConstants.INFRA_ACCOUNT_TYPE))) {
						final String accountName = acc.getAccountName();
						HP3ParInventory.init(new HP3ParCredentials(accountName));
					}

				}
			}
			catch (final Exception e) {
				logger.warn("Could not start initial inventory collection");
				logger.warn("3PAR inventory collection failed: " + e.getMessage());
				throw new HP3ParAccountException(e.getMessage());
			}

		}
		catch (final Exception e) {
			logger.error("Error loading HP 3PAR module.", e);
		}

	}

	// This method is deprecated, so return null
	@Override
	@Deprecated
	public CollectorFactory[] getCollectors() {
		return null;
	}

	// Create the plugin as an account in UCSD
	private void createAccountType() throws Exception {
		final AccountTypeEntry entry = new AccountTypeEntry();

		// This is mandatory, hold the information for device credential details
		entry.setCredentialClass(HP3ParAccountDBStore.class);

		// This is mandatory, type of the Account will be shown in GUI as drill
		// down box
		entry.setAccountType(HP3ParConstants.INFRA_ACCOUNT_TYPE);

		// This is mandatory, label of the Account
		entry.setAccountLabel(HP3ParConstants.INFRA_ACCOUNT_LABEL);

		// This is mandatory, specify the category of the account type ie.,
		// Network / Storage / //Compute
		entry.setCategory(InfraAccountTypes.CAT_STORAGE);

		// This is mandatory
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

		// Register for our own snowflake pod (3Par) and generic pods
		entry.setPodTypes(new String[] {
				HP3ParConstants.POD_TYPE, "GenericPod",
		});

		// This is mandatory, to test the connectivity of the new account. The
		// Handler should be of type PhysicalConnectivityTestHandler.
		entry.setTestConnectionHandler(new HP3ParConnectionHandler());

		// This is mandatory, we can implement inventory listener according to
		// the account Type , collect the inventory details.
		entry.setInventoryListener(new HP3ParInventoryListener());

		// This is mandatory , to show in the converged stack view
		entry.setConvergedStackComponentBuilder(new HP3ParConvergedStackBuilder());

		// This is required to show up the details of the stack view in the GUI
		// TODO: Add stack designer support (need a real 3PAR array on UCS to
		// really do this though!)
		// entry.setStackViewItemProvider(new FooStackViewProvider());

		try {
			// Adding inventory root
			this.registerInventoryObjects(entry);
			PhysicalAccountTypeManager.getInstance().addNewAccountType(entry);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("static-method")
	private void registerInventoryObjects(AccountTypeEntry hP3ParRecoverPointAccountEntry) {
		@SuppressWarnings("unused")
		final ConfigItemDef HP3ParRecoverPointStateInfo = hP3ParRecoverPointAccountEntry
				.createInventoryRoot("HP3Par.inventory.root", HP3ParInventoryItemHandler.class);
	}

}
