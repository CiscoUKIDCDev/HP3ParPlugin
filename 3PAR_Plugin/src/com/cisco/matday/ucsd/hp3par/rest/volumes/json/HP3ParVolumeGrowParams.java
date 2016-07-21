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
 * Parameters to grow a volume.
 *
 * See the 3PAR documentation for more details
 *
 * @author Matt Day
 *
 */
public class HP3ParVolumeGrowParams {
	private int action;
	private int sizeMiB;

	/**
	 * Create new parameteres to grow an existing volume
	 *
	 * @param action
	 * @param sizeMiB
	 */
	public HP3ParVolumeGrowParams(int action, int sizeMiB) {
		super();
		this.action = action;
		this.sizeMiB = sizeMiB;
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
	 * @return the sizeMiB
	 */
	public int getSizeMiB() {
		return this.sizeMiB;
	}

	/**
	 * @param sizeMiB
	 *            the sizeMiB to set
	 */
	public void setSizeMiB(int sizeMiB) {
		this.sizeMiB = sizeMiB;
	}

}
