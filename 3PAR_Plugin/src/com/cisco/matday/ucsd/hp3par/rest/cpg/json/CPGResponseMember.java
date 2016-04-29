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

/**
 * REST object representation of the HP3PAR CPG list. See the HP 3PAR REST
 * documentation for detail on these methods and values
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class CPGResponseMember {

	private String uuid;

	private String name;

	private int numFPVVs;

	private int numTPVVs;

	private CPGResponseSpace UsrUsage;

	private CPGResponseSpace SAUsage;

	private CPGResponseSpace SDUsage;

	private int state;

	private int id;

	private CPGResponseGrowth SAGrowth;

	private CPGResponseGrowth SDGrowth;

	/**
	 * Raid level 0
	 */
	public static int RAID_LEVEL_0 = 1;
	/**
	 * Raid level 1
	 */
	public static int RAID_LEVEL_1 = 2;
	/**
	 * Raid level 5
	 */
	public static int RAID_LEVEL_5 = 3;
	/**
	 * Raid level 6
	 */
	public static int RAID_LEVEL_6 = 4;

	// Raid level symbols
	private static final String[] raidLevelSymbols = {
			"N/A", "R0", "R1", "R5", "R6",
	};
	// Raid level names
	private static final String[] raidLevelNames = {
			"N/A", "RAID level 0", "RAID level 1", "RAID level 5", "Raid level 6",
	};

	// Disk type symbols
	private static final String[] diskTypeSymbols = {
			"N/A", "FC", "NL", "SSD",
	};
	// Raid level names
	private static final String[] diskTypeNames = {
			"N/A", "Fibre Channel", "Near Line", "SSD"
	};

	/**
	 * Get the raid level symbol from the member (e.g. R0, R1 etc)
	 *
	 * @param raidLevel
	 *            - RAID level from 3PAR response
	 * @return Raid level symbol
	 */
	public static String getRaidLevelSymbol(int raidLevel) {
		return raidLevelSymbols[raidLevel];
	}

	/**
	 * Get the raid level name from the member (e.g. RAID level 0, RAID level 1
	 * etc)
	 *
	 * @param raidLevel
	 * @return Raid level name
	 */
	public static String getRaidLevelNames(int raidLevel) {
		return raidLevelNames[raidLevel];
	}

	/**
	 * Get the disk layout symbol (e.g. FC, NL etc)
	 *
	 * @param diskType
	 * @return Disk type symbol
	 */
	public static String getDiskTypeSymbol(int diskType) {
		return diskTypeSymbols[diskType];
	}

	/**
	 * Get the disk layout name (e.g. Fibre Channel, SSD etc)
	 *
	 * @param diskType
	 * @return Disk type name
	 */
	public static String getDiskTypeNames(int diskType) {
		return diskTypeNames[diskType];
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumFPVVs() {
		return this.numFPVVs;
	}

	public void setNumFPVVs(int numFPVVs) {
		this.numFPVVs = numFPVVs;
	}

	public int getNumTPVVs() {
		return this.numTPVVs;
	}

	public void setNumTPVVs(int numTPVVs) {
		this.numTPVVs = numTPVVs;
	}

	public CPGResponseSpace getUsrUsage() {
		return this.UsrUsage;
	}

	public void setUsrUsage(CPGResponseSpace usrUsage) {
		this.UsrUsage = usrUsage;
	}

	public CPGResponseSpace getSAUsage() {
		return this.SAUsage;
	}

	public void setSAUsage(CPGResponseSpace sAUsage) {
		this.SAUsage = sAUsage;
	}

	public CPGResponseSpace getSDUsage() {
		return this.SDUsage;
	}

	public void setSDUsage(CPGResponseSpace sDUsage) {
		this.SDUsage = sDUsage;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the sAGrowth
	 */
	public CPGResponseGrowth getSAGrowth() {
		return this.SAGrowth;
	}

	/**
	 * @param sAGrowth
	 *            the sAGrowth to set
	 */
	public void setSAGrowth(CPGResponseGrowth sAGrowth) {
		this.SAGrowth = sAGrowth;
	}

	/**
	 * @return the sDGrowth
	 */
	public CPGResponseGrowth getSDGrowth() {
		return this.SDGrowth;
	}

	/**
	 * @param sDGrowth
	 *            the sDGrowth to set
	 */
	public void setSDGrowth(CPGResponseGrowth sDGrowth) {
		this.SDGrowth = sDGrowth;
	}

}
