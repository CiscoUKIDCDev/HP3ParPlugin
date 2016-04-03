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

public class CPGResponseMembers {
    int id;
    String uuid;
    String name;
    int numFPVVs;
    int numTPVVs;
    CPGResponseSpace UsrUsage;
    CPGResponseSpace SAUsage;
    CPGResponseSpace SDUsage;
    int state;
/*  
    "SAGrowth": {
      "incrementMiB": 8192,
      "LDLayout": {
        "diskPatterns": [
          {
            "diskType": 1
          }
        ],
        "HA": 2,
        "RAIDType": 2
      }
    },
    "SDGrowth": {
      "incrementMiB": 32768,
      "LDLayout": {
        "setSize": 2,
        "HA": 2,
        "RAIDType": 2,
        "diskPatterns": [
          {
            "diskType": 1
          }
        ]
      }
    },*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumFPVVs() {
		return numFPVVs;
	}
	public void setNumFPVVs(int numFPVVs) {
		this.numFPVVs = numFPVVs;
	}
	public int getNumTPVVs() {
		return numTPVVs;
	}
	public void setNumTPVVs(int numTPVVs) {
		this.numTPVVs = numTPVVs;
	}
	public CPGResponseSpace getUsrUsage() {
		return UsrUsage;
	}
	public void setUsrUsage(CPGResponseSpace usrUsage) {
		UsrUsage = usrUsage;
	}
	public CPGResponseSpace getSAUsage() {
		return SAUsage;
	}
	public void setSAUsage(CPGResponseSpace sAUsage) {
		SAUsage = sAUsage;
	}
	public CPGResponseSpace getSDUsage() {
		return SDUsage;
	}
	public void setSDUsage(CPGResponseSpace sDUsage) {
		SDUsage = sDUsage;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

/*    "failedStates": [],
    "degradedStates": [],
    "additionalStates": [
*/
}
