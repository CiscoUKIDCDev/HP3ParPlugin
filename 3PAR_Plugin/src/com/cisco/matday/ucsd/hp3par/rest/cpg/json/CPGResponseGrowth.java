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
package com.cisco.matday.ucsd.hp3par.rest.cpg.json;

/**
 * REST object representation of the HP3PAR CPG growth. See the HP 3PAR REST
 * documentation for detail on these methods and values
 *
 * @author Matt Day
 *
 */
public class CPGResponseGrowth {
	private int warningMiB;
	private int limitMiB;
	private int incrementMiB;
	private CPGResponseLDLayout LDLayout;

	/**
	 * @return the warningMiB
	 */
	public int getWarningMiB() {
		return this.warningMiB;
	}

	/**
	 * @param warningMiB
	 *            the warningMiB to set
	 */
	public void setWarningMiB(int warningMiB) {
		this.warningMiB = warningMiB;
	}

	/**
	 * @return the limitMiB
	 */
	public int getLimitMiB() {
		return this.limitMiB;
	}

	/**
	 * @param limitMiB
	 *            the limitMiB to set
	 */
	public void setLimitMiB(int limitMiB) {
		this.limitMiB = limitMiB;
	}

	/**
	 * @return the incrementMiB
	 */
	public int getIncrementMiB() {
		return this.incrementMiB;
	}

	/**
	 * @param incrementMiB
	 *            the incrementMiB to set
	 */
	public void setIncrementMiB(int incrementMiB) {
		this.incrementMiB = incrementMiB;
	}

	/**
	 * @return the lDLayout
	 */
	public CPGResponseLDLayout getLDLayout() {
		return this.LDLayout;
	}

	/**
	 * @param lDLayout
	 *            the lDLayout to set
	 */
	public void setLDLayout(CPGResponseLDLayout lDLayout) {
		this.LDLayout = lDLayout;
	}

}
