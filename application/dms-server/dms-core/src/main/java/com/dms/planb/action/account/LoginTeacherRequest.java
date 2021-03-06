package com.dms.planb.action.account;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.database.SafeResultSet;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.LOGIN_TEACHER_REQUEST)
public class LoginTeacherRequest implements Actionable {
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		String id = requestObject.getString("id");
		String password = requestObject.getString("password");
		
		SafeResultSet resultSet = database.executeQuery("SELECT password FROM FROM teacher_account WHERE id='", id, "'");
		
		if(!resultSet.next() || resultSet.getString("password").equals(password)) {
			/**
			 * !resultSet.next() : Can't find id
			 * !resultSet.getString("password").equals(password) : Incorrect password
			 * 
			 * Can't find id or incorrect password, set permit to false
			 */
			responseObject.put("permit", false);
		}
		else if(resultSet.getString("password").equals(password)) {
			// Correct password
			responseObject.put("permit", true);
		}
		
		return responseObject;
	}
}
