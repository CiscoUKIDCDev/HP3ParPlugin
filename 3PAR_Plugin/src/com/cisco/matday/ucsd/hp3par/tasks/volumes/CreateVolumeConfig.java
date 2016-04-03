package com.cisco.matday.ucsd.hp3par.tasks.volumes;

import javax.jdo.annotations.Persistent;

import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;

public class CreateVolumeConfig implements TaskConfigIf {
    @UserInputField(type = WorkflowInputFieldTypeDeclaration.NETWORK_SAN_DEVICE_ALIAS_IDENTITY)
    @Persistent
    private String             devAliasName;

	@Override
	public long getActionId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getConfigEntryId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDisplayLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActionId(long actionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		// TODO Auto-generated method stub
		
	}
}
