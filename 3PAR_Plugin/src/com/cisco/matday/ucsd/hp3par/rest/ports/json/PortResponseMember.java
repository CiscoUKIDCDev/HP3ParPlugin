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
package com.cisco.matday.ucsd.hp3par.rest.ports.json;

import java.util.List;

/**
 * 3PAR Port list member JSON enumeration
 *
 * For more info see the HP 3PAR REST API documentation
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class PortResponseMember {
	private PortResponsePos portPos;
	private int mode;
	private int linkState;
	private String nodeWWN;
	private String portWWN;
	private int type;
	private int protocol;
	private String label;
	private List<String> device;

	public PortResponsePos getPortPos() {
		return this.portPos;
	}

	public void setPortPos(PortResponsePos portPos) {
		this.portPos = portPos;
	}

	public int getMode() {
		return this.mode;
	}

	public String getModeAsString() {
		switch (this.mode) {
		case 1:
			return "Suspended";
		case 2:
			return "Target";
		case 3:
			return "Initiator";
		case 4:
			return "Peer";
		default:
			return "Unknown";
		}
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getLinkState() {
		return this.linkState;
	}

	public String getLinkStateAsString() {
		switch (this.linkState) {
		case 1:
			return "Configuration Wait";
		case 2:
			return "ALPA Wait";
		case 3:
			return "Login Wait";
		case 4:
			return "Ready";
		case 5:
			return "Link is loss sync";
		case 6:
			return "In Error State";
		case 7:
			return "xxx";
		case 8:
			return "Link did not participate";
		case 9:
			return "Taking coredump";
		case 10:
			return "Offline";
		case 11:
			return "Firmware is dead";
		case 12:
			return "Link is idle for reset.";
		case 13:
			return "DHCP is in progress";
		case 14:
			return "Link reset is pending";
		default:
			return "Unknown";
		}
	}

	public void setLinkState(int linkState) {
		this.linkState = linkState;
	}

	public String getNodeWWN() {
		return this.nodeWWN;
	}

	public void setNodeWWN(String nodeWWN) {
		this.nodeWWN = nodeWWN;
	}

	public String getPortWWN() {
		return this.portWWN;
	}

	public void setPortWWN(String portWWN) {
		this.portWWN = portWWN;
	}

	public int getType() {
		return this.type;
	}

	public String getTypeAsString() {
		switch (this.type) {
		case 1:
			return "FC port connected to hosts or fabric";
		case 2:
			return "FC port connected to disks";
		case 3:
			return "Port is not connected to hosts or disks";
		case 4:
			return "Port is in iport mode";
		case 5:
			return "FC port used for Remote Copy";
		case 6:
			return "FC port used for data migration";
		case 7:
			return "IP (Ethernet) port used for remote copy";
		case 8:
			return "iSCSI (Ethernet) port connected to hosts";
		case 9:
			return "CNA port, which can be FCoE or iSCSI";
		case 10:
			return "Ethernet File Persona ports";
		default:
			return "Unknown";
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getProtocol() {
		return this.protocol;
	}

	public String getProtocolAsString() {
		switch (this.protocol) {
		case 1:
			return "Fibre Channel";
		case 2:
			return "iSCSI";
		case 3:
			return "Fibre Channel over Ethernet";
		case 4:
			return "IP (remote copy)";
		case 5:
			return "Serial-attached SCSI";
		default:
			return "Unknown";
		}
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getDevice() {
		return this.device;
	}

	public void setDevice(List<String> device) {
		this.device = device;
	}

}
