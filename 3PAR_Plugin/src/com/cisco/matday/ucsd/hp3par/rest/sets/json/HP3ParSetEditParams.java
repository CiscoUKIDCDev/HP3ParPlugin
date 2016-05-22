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
package com.cisco.matday.ucsd.hp3par.rest.sets.json;

/**
 * HP 3PAR volume/host set editing enumeration
 *
 * See the 3PAR API docs for more info
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class HP3ParSetEditParams {
	private int action;
	private String newName;
	private String comment;
	private String[] setmembers;

	public HP3ParSetEditParams(int action, String[] setmembers) {
		super();
		this.action = action;
		this.setmembers = setmembers;
	}

	public HP3ParSetEditParams(String newName) {
		// Store as null if empty
		this.newName = ("".equals(newName)) ? null : newName;
	}

	public HP3ParSetEditParams() {
	}

	/**
	 * @return the action
	 */
	public int getAction() {
		return this.action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(int action) {
		this.action = action;
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

	/**
	 * @return the setmembers
	 */
	public String[] getSetmembers() {
		return this.setmembers;
	}

	/**
	 * @param setmembers
	 *            the setmembers to set
	 */
	public void setSetmembers(String[] setmembers) {
		this.setmembers = setmembers;
	}

}
