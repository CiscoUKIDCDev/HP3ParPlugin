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
