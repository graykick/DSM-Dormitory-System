package com.dms.planb.action.post.rule;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.database.SafeResultSet;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.LOAD_RULE)
public class LoadRuleWithList implements Actionable {
	EasyJsonObject tempObject;
	SafeResultSet resultSet;
	
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {
		// Both list and content
		if(requestObject.isEmpty()) {
			resultSet = database.executeQuery("SELECT * FROM rule");
			/*
			 * Responses all of posts
			 */
		} else {
			int page = requestObject.getInt("page");
			int limit = requestObject.getInt("limit");
			
			resultSet = database.executeQuery("SELECT * FROM rule limit ", ((page - 1) * limit), ", ", limit);
		}
		
		int postCount = 0;
		if(resultSet.next()) {
			do {
				tempObject = new EasyJsonObject();
							
				tempObject.put("no", resultSet.getInt("no"));
				tempObject.put("title", resultSet.getString("title"));
				tempObject.put("content", resultSet.getString("content"));
							
				array.add(tempObject);
				
				postCount++;
			} while(resultSet.next());
			
			responseObject.put("num_of_post", postCount);
			responseObject.put("result", array);
		} else {
			responseObject.put("status", 404);
		}
		
		return responseObject;
	}
}
