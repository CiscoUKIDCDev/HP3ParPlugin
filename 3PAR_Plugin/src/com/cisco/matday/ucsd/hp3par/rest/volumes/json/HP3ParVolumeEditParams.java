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
 * Creates an object to send to the 3PAR array to create a volume.
 *
 * Objects implementing this class should be passed to
 * HP3ParVolumeRestCall.create
 *
 * @author Matt Day
 *
 */
public class HP3ParVolumeEditParams {
	private String newName;
	private String userCPG;
	private String comment;
	private String snapCPG;

	/**
	 * @param newName
	 *            New name for this volume
	 * @param userCpg
	 *            User CPG to use
	 * @param comment
	 *            Comment
	 * @param snapCPG
	 *            Snap CPG to use
	 */
	public HP3ParVolumeEditParams(String newName, String userCpg, String comment, String snapCPG) {
		this.newName = newName;
		this.userCPG = userCpg;
		this.comment = (comment == null) ? "" : comment;
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

	/**
	 * @return the newName
	 */
	public String getNewName() {
		return this.newName;
	}

	/**
	 * @param newName
	 *            the newName to set
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * @return the userCpg
	 */
	public String getUserCpg() {
		return this.userCPG;
	}

	/**
	 * @param userCpg
	 *            the userCpg to set
	 */
	public void setUserCpg(String userCpg) {
		this.userCPG = userCpg;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
