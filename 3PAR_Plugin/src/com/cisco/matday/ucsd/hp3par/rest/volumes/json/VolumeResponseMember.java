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
package com.cisco.matday.ucsd.hp3par.rest.volumes.json;

/**
 * JSON representation of a 3PAR rest call
 *
 * See the HP 3PAR documentation to see what these fields mean
 *
 * @author Matt Day
 *
 */
public class VolumeResponseMember {
	private int id;
	private String name;
	private int provisioningType;
	private int copyType;
	private int baseId;
	private boolean readOnly;
	private int state;
	private String snapCPG;
	private String comment;
	// Provisioning types
	private static final String[] provTypes = {
			"Unknown", "Full", "Thin", "Snapshot", "Peer", "Unknown", "Deduplicated"
	};

	// private <List> failedStates;
	// private <List> degradedStates;
	// private <List> additionalStates;
	private VolumeResponseMembersAdminSpace adminSpace;

	private VolumeResponseMembersSpace snapshotSpace;

	private VolumeResponseMembersSpace userSpace;

	@SuppressWarnings("javadoc")
	public VolumeResponseMembersAdminSpace getAdminSpace() {
		return this.adminSpace;
	}

	@SuppressWarnings("javadoc")
	public void setAdminSpace(VolumeResponseMembersAdminSpace adminSpace) {
		this.adminSpace = adminSpace;
	}

	@SuppressWarnings("javadoc")
	public VolumeResponseMembersSpace getSnapshotSpace() {
		return this.snapshotSpace;
	}

	@SuppressWarnings("javadoc")
	public void setSnapshotSpace(VolumeResponseMembersSpace snapshotSpace) {
		this.snapshotSpace = snapshotSpace;
	}

	@SuppressWarnings("javadoc")
	public VolumeResponseMembersSpace getUserSpace() {
		return this.userSpace;
	}

	@SuppressWarnings("javadoc")
	public void setUserSpace(VolumeResponseMembersSpace userSpace) {
		this.userSpace = userSpace;
	}

	@SuppressWarnings("javadoc")
	public VolumeResponseMembersPolicies getPolicies() {
		return this.policies;
	}

	@SuppressWarnings("javadoc")
	public void setPolicies(VolumeResponseMembersPolicies policies) {
		this.policies = policies;
	}

	/**
	 * Get the provisioning type (e.g. Full, Thin, etc)
	 *
	 * @return Provisioning type as text
	 */
	public String getProvisioningTypeAsText() {
		// Check for safety in case API changes
		if (this.provisioningType < provTypes.length) {
			return provTypes[this.provisioningType];
		}
		return "Unknown";

	}

	private long sizeMiB = 10240;
	private String wwn;
	private long creationTimeSec;
	private String creationTime8601;
	private int ssSpcAllocWarningPct;
	private int ssSpcAllocLimitPct;
	private int usrSpcAllocWarningPct;
	private int usrSpcAllocLimitPct;

	@SuppressWarnings("javadoc")
	public int getId() {
		return this.id;
	}

	@SuppressWarnings("javadoc")
	public void setId(int id) {
		this.id = id;
	}

	@SuppressWarnings("javadoc")
	public String getName() {
		return this.name;
	}

	@SuppressWarnings("javadoc")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("javadoc")
	public int getProvisioningType() {
		return this.provisioningType;
	}

	@SuppressWarnings("javadoc")
	public void setProvisioningType(int provisioningType) {
		this.provisioningType = provisioningType;
	}

	@SuppressWarnings("javadoc")
	public int getCopyType() {
		return this.copyType;
	}

	@SuppressWarnings("javadoc")
	public void setCopyType(int copyType) {
		this.copyType = copyType;
	}

	@SuppressWarnings("javadoc")
	public int getBaseId() {
		return this.baseId;
	}

	@SuppressWarnings("javadoc")
	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}

	@SuppressWarnings("javadoc")
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * Returns "Read Only" or "Read/Write" depending on the volume type
	 *
	 * @return Read only status of the volume
	 */
	public String isReadOnlyAsText() {
		return (this.readOnly) ? "Read Only" : "Read/Write";
	}

	@SuppressWarnings("javadoc")
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@SuppressWarnings("javadoc")
	public int getState() {
		return this.state;
	}

	@SuppressWarnings("javadoc")
	public void setState(int state) {
		this.state = state;
	}

	@SuppressWarnings("javadoc")
	public long getSizeMiB() {
		return this.sizeMiB;
	}

	@SuppressWarnings("javadoc")
	public void setSizeMiB(long sizeMiB) {
		this.sizeMiB = sizeMiB;
	}

	@SuppressWarnings("javadoc")
	public String getWwn() {
		return this.wwn;
	}

	@SuppressWarnings("javadoc")
	public void setWwn(String wwn) {
		this.wwn = wwn;
	}

	@SuppressWarnings("javadoc")
	public long getCreationTimeSec() {
		return this.creationTimeSec;
	}

	@SuppressWarnings("javadoc")
	public void setCreationTimeSec(long creationTimeSec) {
		this.creationTimeSec = creationTimeSec;
	}

	@SuppressWarnings("javadoc")
	public String getCreationTime8601() {
		return this.creationTime8601;
	}

	@SuppressWarnings("javadoc")
	public void setCreationTime8601(String creationTime8601) {
		this.creationTime8601 = creationTime8601;
	}

	@SuppressWarnings("javadoc")
	public int getSsSpcAllocWarningPct() {
		return this.ssSpcAllocWarningPct;
	}

	@SuppressWarnings("javadoc")
	public void setSsSpcAllocWarningPct(int ssSpcAllocWarningPct) {
		this.ssSpcAllocWarningPct = ssSpcAllocWarningPct;
	}

	@SuppressWarnings("javadoc")
	public int getSsSpcAllocLimitPct() {
		return this.ssSpcAllocLimitPct;
	}

	@SuppressWarnings("javadoc")
	public void setSsSpcAllocLimitPct(int ssSpcAllocLimitPct) {
		this.ssSpcAllocLimitPct = ssSpcAllocLimitPct;
	}

	@SuppressWarnings("javadoc")
	public int getUsrSpcAllocWarningPct() {
		return this.usrSpcAllocWarningPct;
	}

	@SuppressWarnings("javadoc")
	public void setUsrSpcAllocWarningPct(int usrSpcAllocWarningPct) {
		this.usrSpcAllocWarningPct = usrSpcAllocWarningPct;
	}

	@SuppressWarnings("javadoc")
	public int getUsrSpcAllocLimitPct() {
		return this.usrSpcAllocLimitPct;
	}

	@SuppressWarnings("javadoc")
	public void setUsrSpcAllocLimitPct(int usrSpcAllocLimitPct) {
		this.usrSpcAllocLimitPct = usrSpcAllocLimitPct;
	}

	@SuppressWarnings("javadoc")
	public String getUuid() {
		return this.uuid;
	}

	@SuppressWarnings("javadoc")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@SuppressWarnings("javadoc")
	public String getUserCPG() {
		return this.userCPG;
	}

	@SuppressWarnings("javadoc")
	public String getComment() {
		return this.comment;
	}

	@SuppressWarnings("javadoc")
	public String getCopyCPG() {
		return this.snapCPG;
	}

	@SuppressWarnings("javadoc")
	public void setUserCPG(String userCPG) {
		this.userCPG = userCPG;
	}

	private VolumeResponseMembersPolicies policies;

	private String uuid;

	private String userCPG;
}
