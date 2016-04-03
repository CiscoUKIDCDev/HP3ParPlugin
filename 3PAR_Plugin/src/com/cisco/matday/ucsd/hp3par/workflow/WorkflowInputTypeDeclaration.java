package com.cisco.matday.ucsd.hp3par.workflow;

import org.apache.log4j.Logger;

import com.cisco.matday.ucsd.hp3par.inputs.HP3ParAccountSelector;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputTypeRegistry;
import com.cloupia.service.cIM.inframgr.forms.wizard.TabularFieldRegistry;

public class WorkflowInputTypeDeclaration {

	private static Logger logger = Logger.getLogger(WorkflowInputTypeDeclaration.class);

	public WorkflowInputTypeDeclaration() {
	}

	/**
	 * This method is used to register Workflow Input Types.
	 * 
	 * @return void This returns sum of numA and numB.
	 */
	public static void registerWFInputs() {

		registerSampleTabularWorkflowInputType();

	}

	/**
	 * This method is used to register Workflow input of type tabular.
	 * 
	 * @return void
	 */

	private static void registerSampleTabularWorkflowInputType() {
		String fieldName = "HP3ParTabularInput";
		logger.info("Adding account type: " + fieldName);

		WorkflowInputTypeRegistry sampleInputType = WorkflowInputTypeRegistry.getInstance();
		sampleInputType.addDeclaration(new WorkflowInputFieldTypeDeclaration("HP3ParTabularInputDecl",
				"HP 3PAR Account", FormFieldDefinition.FIELD_TYPE_TABULAR, fieldName));

		TabularFieldRegistry.getInstance().registerTabularField(fieldName, HP3ParAccountSelector.class, "0", "2");
	}

}
