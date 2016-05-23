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
package com.cisco.matday.ucsd.hp3par.test;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.json.HP3ParRequestStatus;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.EditCpgConfig;
import com.cisco.matday.ucsd.hp3par.tasks.cpg.HP3ParCpgExecute;

// Don't document this test case, it changes too often
@SuppressWarnings("javadoc")
public class VolumeTest {

	final static String ipAddress = "10.51.8.210";
	final static String user = "3paradm";
	final static String password = "3pardata";

	@SuppressWarnings({
			"deprecation"
	})
	public static void main(String[] args) {

		try {

			EditCpgConfig config = new EditCpgConfig();
			HP3ParCredentials c = new HP3ParCredentials(ipAddress, user, password);

			config.setCpg("0@3PAR@API-Test");
			config.setNewName("Renamed-API");

			HP3ParRequestStatus s = HP3ParCpgExecute.edit(c, config);

			System.out.println(s.getError());

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
