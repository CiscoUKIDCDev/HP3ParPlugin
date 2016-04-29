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
package com.cisco.matday.ucsd.hp3par.rest.cpg.json;

import java.util.List;

/**
 * REST object representation of the HP3PAR LDLayout. See the HP 3PAR REST
 * documentation for detail on these methods and values
 *
 * @author Matt Day
 *
 */
public class CPGResponseLDLayout {

	private int RAIDType;
	private int setSize;
	private int HA;
	private int chunkletPosPref;
	private List<CPGResponseDiskPatterns> diskPatterns;

	/**
	 * @return the rAIDType
	 */
	public int getRAIDType() {
		return this.RAIDType;
	}

	/**
	 * @param rAIDType
	 *            the rAIDType to set
	 */
	public void setRAIDType(int rAIDType) {
		this.RAIDType = rAIDType;
	}

	/**
	 * @return the setSize
	 */
	public int getSetSize() {
		return this.setSize;
	}

	/**
	 * @param setSize
	 *            the setSize to set
	 */
	public void setSetSize(int setSize) {
		this.setSize = setSize;
	}

	/**
	 * @return the hA
	 */
	public int getHA() {
		return this.HA;
	}

	/**
	 * @param hA
	 *            the hA to set
	 */
	public void setHA(int hA) {
		this.HA = hA;
	}

	/**
	 * @return the chunkletPosPref
	 */
	public int getChunkletPosPref() {
		return this.chunkletPosPref;
	}

	/**
	 * @param chunkletPosPref
	 *            the chunkletPosPref to set
	 */
	public void setChunkletPosPref(int chunkletPosPref) {
		this.chunkletPosPref = chunkletPosPref;
	}

	/**
	 * @return the diskPatterns
	 */
	public List<CPGResponseDiskPatterns> getDiskPatterns() {
		return this.diskPatterns;
	}

	/**
	 * @param diskPatterns
	 *            the diskPatterns to set
	 */
	public void setDiskPatterns(List<CPGResponseDiskPatterns> diskPatterns) {
		this.diskPatterns = diskPatterns;
	}

}
