package com.dms.planb.action.goingout;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.database.SafeResultSet;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.LOAD_GOINGOUT_APPLY_STATUS)
public class LoadGoingoutApplyStatus implements Actionable {
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		String id = requestObject.getString("id");
		
		SafeResultSet resultSet = database.executeQuery("SELECT * FROM goingout_apply WHERE id='", id, "'");
		
		if(resultSet.next()) {
			responseObject.put("date", resultSet.getInt("date"));
			responseObject.put("reason", resultSet.getString("reason"));
		} else {
			responseObject.put("status", 404);
		}
		
		return responseObject;
	}
}
