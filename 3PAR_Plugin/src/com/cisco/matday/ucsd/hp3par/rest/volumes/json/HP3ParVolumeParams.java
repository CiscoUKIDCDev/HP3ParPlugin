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
package com.cisco.matday.ucsd.hp3par.rest.volumes.json;

/**
 * Creates an object to send to the 3PAR array to create a volume.
 * 
 * Objects implementing this class should be passed to
 * HP3ParVolumeRestCall.create
 * 
 * @author Matt Day
 *
 */
public class HP3ParVolumeParams {
	private String name;
	private String cpg;
	private long sizeMiB;
	private String comment;
	private boolean tpvv = false;
	private String snapCPG = null;

	/**
	 * @param name
	 *            Volume name
	 * @param cpg
	 *            CPG on which to place volume
	 * @param sizeMiB
	 *            Size in MiB
	 * @param comment
	 *            Optional comment (can be null)
	 * @param thinProvision
	 *            True if this should be thin provisioned
	 * @param snapCPG
	 *            Snap or copy CPG to use
	 */
	public HP3ParVolumeParams(String name, String cpg, long sizeMiB, String comment, boolean thinProvision,
			String snapCPG) {
		this.name = name;
		this.cpg = cpg;
		this.sizeMiB = sizeMiB;
		this.comment = comment;
		this.tpvv = thinProvision;
		this.snapCPG = snapCPG;
	}

	@SuppressWarnings("javadoc")
	public String getSnapCPG() {
		return this.snapCPG;
	}

	@SuppressWarnings("javadoc")
	public void setSnapCPG(String snapCPG) {
		this.snapCPG = snapCPG;
	}

	@SuppressWarnings("javadoc")
	public boolean isTpvv() {
		return this.tpvv;
	}

	@SuppressWarnings("javadoc")
	public void setTpvv(boolean tpvv) {
		this.tpvv = tpvv;
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
	public String getCpg() {
		return this.cpg;
	}

	@SuppressWarnings("javadoc")
	public void setCpg(String cpg) {
		this.cpg = cpg;
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
	public String getComment() {
		return this.comment;
	}

	@SuppressWarnings("javadoc")
	public void setComment(String comment) {
		this.comment = comment;
	}
}
