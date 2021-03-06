package org.boxfox.dms.secure.action;

import java.sql.SQLException;

import org.boxfox.dms.algorithm.AES256;
import org.boxfox.dms.algorithm.RSA;
import org.boxfox.dms.secure.SecureManager;
import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.database.DataBase;
import org.boxfox.dms.utilities.json.EasyJsonObject;

@ActionRegistration(command = 7514)
public class AesAction implements Actionable {
	private SecureManager manager;
	
	public AesAction(){
		manager = SecureManager.getInstance();
	}
	
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		String aesKey = RSA.decrypt(requestObject.getString("AESKey"));
		manager.registerKey(sender, new AES256(aesKey));
		EasyJsonObject response = new EasyJsonObject();
		if (aesKey != null) {
			response.put("Result", true);
		} else {
			response.put("Result", false);
		}
		return response;
	}

}
