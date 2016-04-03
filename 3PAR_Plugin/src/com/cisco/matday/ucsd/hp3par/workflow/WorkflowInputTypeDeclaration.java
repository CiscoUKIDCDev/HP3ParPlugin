package com.cisco.matday.ucsd.hp3par.workflow;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.constants.HP3ParConstants;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParAccountSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParCpgSelector;
import com.cisco.matday.ucsd.hp3par.inputs.HP3ParVolumeSelector;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputTypeRegistry;
import com.cloupia.service.cIM.inframgr.forms.wizard.TabularFieldRegistry;

public class WorkflowInputTypeDeclaration {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkflowInputTypeDeclaration.class);

	public WorkflowInputTypeDeclaration() {
	}

	/**
	 * This method is used to register Workflow Input Types.
	 * 
	 * @return void This returns sum of numA and numB.
	 */
	public static void registerWFInputs() {
		registerAccountPicker();
		registerCpgList();
		registerVolumeList();
	}

	/**
	 * This method is used to register Workflow input of type tabular.
	 * 
	 * @return void
	 */

	private static void registerAccountPicker() {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.ACCOUNT_LIST_FORM_TABLE_NAME,
				"HP 3PAR Account", FormFieldDefinition.FIELD_TYPE_TABULAR, HP3ParConstants.ACCOUNT_LIST_FORM_NAME));

		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.ACCOUNT_LIST_FORM_NAME,
				HP3ParAccountSelector.class, "0", "2");
	}
	
	private static void registerCpgList () {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.CPG_LIST_FORM_TABLE_NAME,
				"HP 3PAR CPG List", FormFieldDefinition.FIELD_TYPE_TABULAR, HP3ParConstants.CPG_LIST_FORM_NAME));

		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.CPG_LIST_FORM_NAME,
				HP3ParCpgSelector.class, "0", "2");
	}
	
	private static void registerVolumeList () {
		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration(HP3ParConstants.VOLUME_LIST_FORM_TABLE_NAME,
				"HP 3PAR Volume List", FormFieldDefinition.FIELD_TYPE_TABULAR, HP3ParConstants.VOLUME_LIST_FORM_NAME));

		TabularFieldRegistry.getInstance().registerTabularField(HP3ParConstants.VOLUME_LIST_FORM_NAME,
				HP3ParVolumeSelector.class, "0", "2");
	}

}
