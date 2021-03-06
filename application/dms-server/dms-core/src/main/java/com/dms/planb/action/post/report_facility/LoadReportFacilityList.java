package com.dms.planb.action.post.report_facility;

import java.sql.SQLException;

import org.boxfox.dms.utilities.actions.ActionRegistration;
import org.boxfox.dms.utilities.actions.Actionable;
import org.boxfox.dms.utilities.actions.support.Sender;
import org.boxfox.dms.utilities.database.SafeResultSet;
import org.boxfox.dms.utilities.json.EasyJsonObject;

import com.dms.planb.support.Commands;

@ActionRegistration(command = Commands.LOAD_REPORT_FACILITY_LIST)
public class LoadReportFacilityList implements Actionable {
	EasyJsonObject tempObject;
	SafeResultSet resultSet;
	
	@Override
	public EasyJsonObject action(Sender sender, int command, EasyJsonObject requestObject) throws SQLException {		
		if(requestObject.isEmpty()) {
			resultSet = database.executeQuery("SELECT * FROM facility_report");
			/*
			 * Responses all of posts
			 */
		} else {
			int page = requestObject.getInt("page");
			int limit = requestObject.getInt("limit");
			
			resultSet = database.executeQuery("SELECT * FROM facility_report limit ", ((page - 1) * limit), ", ", limit);
		}
		
		int postCount = 0;
		if(resultSet.next()) {
			do {
				tempObject = new EasyJsonObject();
				
				tempObject.put("no", resultSet.getInt("no"));
				tempObject.put("title", resultSet.getString("title"));
				tempObject.put("room", resultSet.getInt("room"));
				tempObject.put("write_date", resultSet.getString("write_date"));
				tempObject.put("writer", resultSet.getString("writer"));
				if(resultSet.getString("result") != null) {
					tempObject.put("has_result", true);
				} else {
					tempObject.put("has_result", false);
				}
				
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
