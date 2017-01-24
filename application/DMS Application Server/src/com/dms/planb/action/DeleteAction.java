package com.dms.planb.action;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dms.boxfox.database.DataBase;
import com.dms.planb.support.Commands;

public class DeleteAction implements Actionable {
	@Override
	public JSONObject action(int command, JSONObject requestObject) throws JSONException, SQLException {
		JSONObject responseObject = new JSONObject();
		DataBase database = DataBase.getInstance();
		
		// For account
		String id = null;
		
		// For post
		int no;
		int number;
		int category;
		
		// For status
		int status = 0;
		
		switch(command) {
		case Commands.DELETE_ACCOUNT:
			id = requestObject.getString("id");
			
			status = database.executeUpdate("DELETE FROM account WHERE id=", id);
			break;
		case Commands.DELETE_NOTICE:
		case Commands.DELETE_NEWSLETTER:
		case Commands.DELETE_COMPETITION:
			number = requestObject.getInt("number");
			category = requestObject.getInt("category");
			
			status = database.executeUpdate("DELETE FROM app_content WHERE number=", number, " AND category=", category);
			break;
		case Commands.DELETE_RULE:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("DELETE FROM app_content WHERE no=", no);
			break;
		case Commands.DELETE_QUESTION:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("DELETE FROM app_content WHERE no=", no);
			break;
		case Commands.DELETE_ANSWER:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("UPDATE app_content SET answer_content=NULL, answer_date=NULL WHERE no=", no);
			break;
		case Commands.DELETE_QNA_COMMENT:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("DELETE qna_comment WHERE no=", no);
			break;
		case Commands.DELETE_FAQ:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("DELETE faq WHERE no=", no);
			break;
		case Commands.DELETE_REPORT_FACILITY:
			no = requestObject.getInt("no");
			
			status = database.executeUpdate("DELETE facility_report WHERE no=", no);
			break;
		case Commands.DEAPPLY_EXTENTION:
			id = requestObject.getString("id");
			
			status = database.executeUpdate("DELETE extension_apply WHERE id='", id, "'");
			break;
		case Commands.DEAPPLY_GOINGOUT:
			id = requestObject.getString("id");
			
			status = database.executeUpdate("DELETE goingout_apply WHERE id='", id, "'");
			break;
		case Commands.DEAPPLY_MERIT:
			id = requestObject.getString("id");
			
			status = database.executeUpdate("DELETE merit_apply WHERE id='", id, "'");
			break;
		}
		
		responseObject.put("status", status);
		return responseObject;
	}
}