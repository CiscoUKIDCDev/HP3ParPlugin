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
package com.cisco.matday.ucsd.hp3par.workflow;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
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
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputTypeRegistry;
import com.cloupia.service.cIM.inframgr.forms.wizard.TabularFieldRegistry;

/**
 * Sets up the workflow input types (e.g. account/volume/cpg selection lists)
 *
 * @author Matt Day
 *
 */
public class WorkflowInputTypeDeclaration {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkflowInputTypeDeclaration.class);

	/**
	 * Empty default constructor
	 */
	public WorkflowInputTypeDeclaration() {
	}

	/**
	 * This method is used to register Workflow Input Types.
	 *
	 */
	public static void registerWFInputs() {
		registerAccountPicker();
		registerCpgList();
		registerVolumeList();
		registerHostList();
		registerVlunList();
		registerPortList();
		registerVolumeSetList();
		registerHostSetList();
		registeriSCSIList();
		registerfCWWNList();
		registerHostAndHostSetList();
		registerVolumeAndVolumeSetList();
		registerRaidList();
		registerDiskList();

	}

	/**
	 * This method is used to register Workflow input of type tabular.
	 *
	 */

	private static void registerAccountPicker() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME, HP3ParConstants.ACCOUNT_LIST_FORM_LABEL,
				FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HP3ParConstants.ACCOUNT_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.ACCOUNT_LIST_FORM_NAME,
				HP3ParAccountSelector.class, "0", "0");
	}

	private static void registerCpgList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.CPG_LIST_FORM_TABLE_NAME,
				HP3ParConstants.CPG_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.CPG_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.CPG_LIST_FORM_NAME,
				HP3ParCpgSelector.class, "0", "3");
	}

	private static void registerVolumeList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME, HP3ParConstants.VOLUME_LIST_FORM_LABEL,
				FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HP3ParConstants.VOLUME_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.VOLUME_LIST_FORM_NAME,
				HP3ParVolumeSelector.class, "0", "2");
	}

	private static void registerPortList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.PORT_LIST_FORM_TABLE_NAME,
				HP3ParConstants.PORT_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.PORT_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.PORT_LIST_FORM_NAME,
				HP3ParPortSelector.class, "0", "1");
	}

	private static void registerHostList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.HOST_LIST_FORM_TABLE_NAME,
				HP3ParConstants.HOST_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.HOST_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.HOST_LIST_FORM_NAME,
				HP3ParHostSelector.class, "0", "2");
	}

	private static void registerVlunList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.VLUN_LIST_FORM_TABLE_NAME,
				HP3ParConstants.VLUN_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.VLUN_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.VLUN_LIST_FORM_NAME,
				HP3ParVlunSelector.class, "0", "2");
	}

	private static void registerVolumeSetList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.VOLUMESET_LIST_FORM_TABLE_NAME, HP3ParConstants.VOLUMESET_LIST_FORM_LABEL,
				FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HP3ParConstants.VOLUMESET_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.VOLUMESET_LIST_FORM_NAME,
				HP3ParVolumeSetSelector.class, "0", "2");
	}

	private static void registerHostSetList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.HOSTSET_LIST_FORM_TABLE_NAME, HP3ParConstants.HOSTSET_LIST_FORM_LABEL,
				FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HP3ParConstants.HOSTSET_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.HOSTSET_LIST_FORM_NAME,
				HP3ParHostSetSelector.class, "0", "2");
	}

	private static void registeriSCSIList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.ISCSI_LIST_FORM_TABLE_NAME,
				HP3ParConstants.ISCSI_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.ISCSI_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.ISCSI_LIST_FORM_NAME,
				HP3PariSCSISelector.class, "0", "2");
	}

	private static void registerHostAndHostSetList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_TABLE_NAME, HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_LABEL,
				FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.HOST_AND_HOSTSET_LIST_FORM_NAME,
				HP3ParHostAndHostSetSelector.class, "0", "2");
	}

	private static void registerVolumeAndVolumeSetList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(
				HP3ParConstants.VOLUME_AND_VOLUMESET_LIST_FORM_TABLE_NAME,
				HP3ParConstants.VOLUME_AND_VOLUMESET_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.VOLUME_AND_VOLUMESET_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.VOLUME_AND_VOLUMESET_LIST_FORM_NAME,
				HP3ParVolumeAndVolumeSetSelector.class, "0", "2");
	}

	private static void registerfCWWNList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.FCWWN_LIST_FORM_TABLE_NAME,
				HP3ParConstants.FCWWN_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.FCWWN_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.FCWWN_LIST_FORM_NAME,
				HP3ParFCSelector.class, "0", "2");
	}

	private static void registerRaidList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.RAID_LIST_FORM_TABLE_NAME,
				HP3ParConstants.RAID_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.RAID_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.RAID_LIST_FORM_NAME,
				HP3ParRaidTypeSelector.class, "0", "1");
	}

	private static void registerDiskList() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.DISK_LIST_FORM_TABLE_NAME,
				HP3ParConstants.DISK_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				HP3ParConstants.DISK_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.DISK_LIST_FORM_NAME,
				HP3ParDiskTypeSelector.class, "0", "1");
	}
}
