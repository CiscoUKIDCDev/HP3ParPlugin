package com.cisco.matday.ucsd.hp3par.rest.volumes.json;

public class VolumeResponseMembers {
	private int id;
	private String name;
	private int provisioningType;
	private int copyType;
	private int baseId;
	private boolean readOnly;
	private int state;
	// private <List> failedStates;
	// private <List> degradedStates;
	// private <List> additionalStates;

	private VolumeResponseMembersAdminSpace adminSpace;

	private VolumeResponseMembersSpace snapshotSpace;

	private VolumeResponseMembersSpace userSpace;

	public VolumeResponseMembersAdminSpace getAdminSpace() {
		return adminSpace;
	}

	public void setAdminSpace(VolumeResponseMembersAdminSpace adminSpace) {
		this.adminSpace = adminSpace;
	}

	public VolumeResponseMembersSpace getSnapshotSpace() {
		return snapshotSpace;
	}

	public void setSnapshotSpace(VolumeResponseMembersSpace snapshotSpace) {
		this.snapshotSpace = snapshotSpace;
	}

	public VolumeResponseMembersSpace getUserSpace() {
		return userSpace;
	}

	public void setUserSpace(VolumeResponseMembersSpace userSpace) {
		this.userSpace = userSpace;
	}

	public VolumeResponseMembersPolicies getPolicies() {
		return policies;
	}

	public void setPolicies(VolumeResponseMembersPolicies policies) {
		this.policies = policies;
	}

	private long sizeMiB = 10240;
	private String wwn;
	private long creationTimeSec;
	private String creationTime8601;
	private int ssSpcAllocWarningPct;
	private int ssSpcAllocLimitPct;
	private int usrSpcAllocWarningPct;
	private int usrSpcAllocLimitPct;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvisioningType() {
		return provisioningType;
	}

	public void setProvisioningType(int provisioningType) {
		this.provisioningType = provisioningType;
	}

	public int getCopyType() {
		return copyType;
	}

	public void setCopyType(int copyType) {
		this.copyType = copyType;
	}

	public int getBaseId() {
		return baseId;
	}

	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getSizeMiB() {
		return sizeMiB;
	}

	public void setSizeMiB(long sizeMiB) {
		this.sizeMiB = sizeMiB;
	}

	public String getWwn() {
		return wwn;
	}

	public void setWwn(String wwn) {
		this.wwn = wwn;
	}

	public long getCreationTimeSec() {
		return creationTimeSec;
	}

	public void setCreationTimeSec(long creationTimeSec) {
		this.creationTimeSec = creationTimeSec;
	}

	public String getCreationTime8601() {
		return creationTime8601;
	}

	public void setCreationTime8601(String creationTime8601) {
		this.creationTime8601 = creationTime8601;
	}

	public int getSsSpcAllocWarningPct() {
		return ssSpcAllocWarningPct;
	}

	public void setSsSpcAllocWarningPct(int ssSpcAllocWarningPct) {
		this.ssSpcAllocWarningPct = ssSpcAllocWarningPct;
	}

	public int getSsSpcAllocLimitPct() {
		return ssSpcAllocLimitPct;
	}

	public void setSsSpcAllocLimitPct(int ssSpcAllocLimitPct) {
		this.ssSpcAllocLimitPct = ssSpcAllocLimitPct;
	}

	public int getUsrSpcAllocWarningPct() {
		return usrSpcAllocWarningPct;
	}

	public void setUsrSpcAllocWarningPct(int usrSpcAllocWarningPct) {
		this.usrSpcAllocWarningPct = usrSpcAllocWarningPct;
	}

	public int getUsrSpcAllocLimitPct() {
		return usrSpcAllocLimitPct;
	}

	public void setUsrSpcAllocLimitPct(int usrSpcAllocLimitPct) {
		this.usrSpcAllocLimitPct = usrSpcAllocLimitPct;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserCPG() {
		if (userCPG == null) {
			return "-";
		}
		return userCPG;
	}

	public void setUserCPG(String userCPG) {
		this.userCPG = userCPG;
	}

	private VolumeResponseMembersPolicies policies;

	private String uuid;
	
	private String userCPG;
}
