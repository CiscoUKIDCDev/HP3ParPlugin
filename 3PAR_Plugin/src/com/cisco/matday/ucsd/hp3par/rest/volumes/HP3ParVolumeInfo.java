package com.cisco.matday.ucsd.hp3par.rest.volumes;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.matday.ucsd.hp3par.account.HP3ParCredentials;
import com.cisco.matday.ucsd.hp3par.rest.InvalidHP3ParTokenException;
import com.cisco.matday.ucsd.hp3par.rest.UCSD3ParHttpWrapper;
import com.cisco.matday.ucsd.hp3par.rest.volumes.json.VolumeResponseMember;
import com.google.gson.Gson;

/**
 * Gets information about a single volume
 * 
 * @author Matt Day
 *
 */
public class HP3ParVolumeInfo {

	private VolumeResponseMember member;

	/**
	 * Get information about a specific volume
	 * 
	 * @param loginCredentials
	 *            Account credentials for this array
	 * @param volName
	 *            Name of the volume to query
	 * @throws HttpException
	 * @throws IOException
	 * @throws InvalidHP3ParTokenException
	 *             If credentials are invalid
	 */
	public HP3ParVolumeInfo(HP3ParCredentials loginCredentials, String volName)
			throws HttpException, IOException, InvalidHP3ParTokenException {
		UCSD3ParHttpWrapper request = new UCSD3ParHttpWrapper(loginCredentials);
		// Use defaults for GET method
		request.setGetDefaults();
		// Volume info uri:
		String uri = "/api/v1/volumes/" + volName;
		request.setUri(uri);
		request.execute();
		String response = request.getHttpResponse();
		Gson gson = new Gson();
		this.member = gson.fromJson(response, VolumeResponseMember.class);
	}

	/**
	 * @return Information about this volume
	 */
	public VolumeResponseMember getMember() {
		return member;
	}

}
