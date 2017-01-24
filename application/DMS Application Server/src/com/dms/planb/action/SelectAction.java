package com.dms.planb.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dms.boxfox.database.DataBase;
import com.dms.planb.support.Commands;

public class SelectAction implements Actionable {
	@Override
	public JSONObject action(int command, JSONObject requestObject) throws JSONException, SQLException {
		ResultSet resultSet;
		JSONObject responseObject = new JSONObject();
		JSONArray array = new JSONArray();
		DataBase database = DataBase.getInstance();
		
		// For account
		String id = null;
		String password = null;
		int number;
		
		// For post
		int no;
		int qnaNo;
		int category;
		
		// For post list
		int count; // JSON Object's sequence count
		
		switch(command) {
		case Commands.LOAD_MYPAGE:
			number = requestObject.getInt("number");
			
			resultSet = database.executeQuery("SELECT * FROM student_data WHERE number=", number);
			
			responseObject.put("sex", resultSet.getInt("sex"));
			responseObject.put("status", resultSet.getInt("status"));
			responseObject.put("name", resultSet.getString("name"));
			responseObject.put("phone", resultSet.getString("phone"));
			responseObject.put("p_name", resultSet.getString("p_name"));
			responseObject.put("merit", resultSet.getInt("merit"));
			responseObject.put("demerit", resultSet.getInt("demerit"));
			
			break;
		case Commands.LOAD_ACCOUNT:
			// when login
			id = requestObject.getString("id");
			password = requestObject.getString("password");
			
			resultSet = database.executeQuery("SELECT password FROM account WHERE id='", id, "'");
			if(resultSet.getString("password").equals(password)) {
				responseObject.put("status", true);
			}
			
			break;
		case Commands.LOAD_NOTICE_LIST:
		case Commands.LOAD_NEWSLETTER_LIST:
		case Commands.LOAD_COMPETITION_LIST:
			count = 1;
			
			category = requestObject.getInt("category");
			
			resultSet = database.executeQuery("SELECT * FROM app_content WHERE category=", category);
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getInt("number"));
				array.put(resultSet.getString("title"));
				array.put(resultSet.getString("writer"));
				array.put(resultSet.getString("date"));
				
				responseObject.put("sequence".concat(Integer.toString(count++)), array);
			}
			break;
		case Commands.LOAD_QNA_LIST:
			count = 1;
			
			resultSet = database.executeQuery("SELECT * FROM qna");
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getInt("no"));
				array.put(resultSet.getString("title"));
				array.put(resultSet.getString("question_date"));
				array.put(resultSet.getString("wrtier"));
				array.put(resultSet.getInt("privacy"));
				
				responseObject.put("sequence".concat(Integer.toString(count++)), array);
			}
			break;
		case Commands.LOAD_FAQ_LIST:
			count = 1;
			
			resultSet = database.executeQuery("SELECT * FROM faq");
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getInt("no"));
				array.put(resultSet.getString("title"));
				
				responseObject.put("sequence".concat(Integer.toString(count++)), array);
			}
			break;
		case Commands.LOAD_REPORT_FACILITY_LIST:
			count = 1;
			
			resultSet = database.executeQuery("SELECT * FROM facility_report");
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getInt("no"));
				array.put(resultSet.getString("title"));
				array.put(resultSet.getString("room"));
				array.put(resultSet.getString("write_date"));
				array.put(resultSet.getInt("writer"));
				if(!resultSet.getString("result").isEmpty()) {
					responseObject.put("has_result", true);
				}
				
				responseObject.put("sequence".concat(Integer.toString(count++)), array);
			}
			break;
		case Commands.LOAD_AFTERSCHOOL_LIST:
			count = 1;
			
			resultSet = database.executeQuery("SELECT * FROM afterschool_list");
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getInt("no"));
				array.put(resultSet.getString("title"));
				array.put(resultSet.getInt("target"));
				array.put(resultSet.getString("place"));
				array.put(resultSet.getInt("day"));
				array.put(resultSet.getString("instructor"));
				
				responseObject.put("sequence".concat(Integer.toString(count++)), array);
			}
			break;
		case Commands.LOAD_NOTICE:
		case Commands.LOAD_NEWSLETTER:
		case Commands.LOAD_COMPETITION:
			number = requestObject.getInt("number");
			category = requestObject.getInt("category");
			
			resultSet = database.executeQuery("SELECT * FROM app_content WHERE number=", number, " AND category=", category);
			
			responseObject.put("title", resultSet.getString("title"));
			responseObject.put("content", resultSet.getString("content"));
			responseObject.put("writer", resultSet.getString("writer"));
			responseObject.put("date", resultSet.getString("date"));
			
			break;
		case Commands.LOAD_RULE:
			no = requestObject.getInt("no");
			
			resultSet = database.executeQuery("SELECT * FROM rule WHERE no=", no);
			
			responseObject.put("title", resultSet.getString("resultSet"));
			responseObject.put("content", resultSet.getString("content"));
			
			break;
		case Commands.LOAD_QNA:
			no = requestObject.getInt("no");
			
			resultSet = database.executeQuery("SELECT * FROM qna WHERE no=", no);
			
			responseObject.put("title", resultSet.getString("title"));
			responseObject.put("question_content", resultSet.getString("question_content"));
			responseObject.put("question_date", resultSet.getString("question_date"));
			responseObject.put("questioner", resultSet.getString("questioner"));
			responseObject.put("privacy", resultSet.getInt("privacy"));
			if(!resultSet.getString("answer_content").isEmpty()) {
				responseObject.put("has_answer", true);
				responseObject.put("answer_content", resultSet.getString("answer_content"));
				responseObject.put("answer_date", resultSet.getString("answer_date"));
			}
			else {
				responseObject.put("hasAnswer", false);
			}
			
			break;
		case Commands.LOAD_QNA_COMMENT:
			count = 1;
			
			qnaNo = requestObject.getInt("no");
			
			resultSet = database.executeQuery("SELECT * FROM qna_comment WHERE no=", qnaNo);
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				array.put(resultSet.getString("writer"));
				array.put(resultSet.getString("comment_date"));
				array.put(resultSet.getString("content"));
			}
			
			break;
		case Commands.LOAD_FAQ:
			no = requestObject.getInt("no");
			
			resultSet = database.executeQuery("SELECT * FROM faq WHERE no=", no);
			
			responseObject.put("title", resultSet.getString("title"));
			responseObject.put("content", resultSet.getString("content"));
			
			break;
		case Commands.LOAD_REPORT_FACILITY:
			no = requestObject.getInt("no");
			
			resultSet = database.executeQuery("SELECT * FROM facility_report WHERE no=", no);
			
			responseObject.put("title", resultSet.getString("title"));
			responseObject.put("content", resultSet.getString("content"));
			responseObject.put("room", resultSet.getInt("room"));
			responseObject.put("write_date", resultSet.getString("write_date"));
			responseObject.put("writer", resultSet.getString("writer"));
			if(!resultSet.getString("result").isEmpty()){
				responseObject.put("has_result", true);
				responseObject.put("result", resultSet.getString("result"));
				responseObject.put("result_date", resultSet.getString("result_date"));
			} else {
				responseObject.put("has_result", false);
			}
			
			break;
		case Commands.LOAD_EXTENTION_STATUS:
			id = requestObject.getString("id");
			
			resultSet = database.executeQuery("SELECT * FROM extension_apply WHERE id='", id, "'");
			
			responseObject.put("class", resultSet.getInt("class"));
			responseObject.put("seat", resultSet.getInt("seat"));
			
			break;
		case Commands.LOAD_STAY_STATUS:
			// Apply on a monthly basis
			id = requestObject.getString("id");
			
			resultSet = database.executeQuery("SELECT * FROM stay_apply WHERE id='", id, "'");
			
			responseObject.put("value", resultSet.getInt("value"));
			responseObject.put("date", resultSet.getString("date"));
			
			break;
		case Commands.LOAD_GOINGOUT_STATUS:
			id = requestObject.getString("id");
			
			resultSet = database.executeQuery("SELECT * FROM goingout_apply WHERE id='", id, "'");
			
			responseObject.put("dept_date", resultSet.getDate("dept_date"));
			responseObject.put("reason", resultSet.getString("reason"));
			
			break;
		case Commands.LOAD_MERIT_APPLY_STATUS:
			id = requestObject.getString("id");
			
			resultSet = database.executeQuery("SELECT * FROM merit_apply WHERE id='", id, "'");
			
			responseObject.put("content", resultSet.getString("content"));
			if(!resultSet.getString("target").isEmpty()) {
				responseObject.put("has_target", true);
				responseObject.put("target", resultSet.getString("target"));
			} else {
				responseObject.put("hasTarget", false);
			}
			
			break;
		case Commands.LOAD_AFTERSCHOOL_STATUS:
			count = 1;
			
			id = requestObject.getString("id");
			
			resultSet = database.executeQuery("SELECT * FROM afterschool_apply WHERE id='", id, "'");
			
			responseObject.put("row_count", resultSet.getRow());
			while(resultSet.next()) {
				responseObject.put("sequence".concat(Integer.toString(count++)), resultSet.getInt("no"));
			}
			
			break;
		case Commands.LOAD_MEAL:
			resultSet = database.executeQuery("SELECT * FROM meal WHERE date=curDate()");
			
			responseObject.put("breakfast", resultSet.getString("breakfast"));
			responseObject.put("lunch", resultSet.getString("lunch"));
			responseObject.put("dinner", resultSet.getString("dinner"));
			responseObject.put("breakfast_allergy", resultSet.getString("breakfast_allergy"));
			responseObject.put("lunch_allergy", resultSet.getString("lunch_allergy"));
			responseObject.put("dinner_allergy", resultSet.getString("dinner_allergy"));
			
			break;
		case Commands.LOAD_PLAN:
			int year = requestObject.getInt("year");
			int month = requestObject.getInt("month");
			
			resultSet = database.executeQuery("SELECT * FROM plan WHERE year=", year, "and month=", month);
			
			responseObject.put("data", resultSet.getString("data"));
			
			break;
		case Commands.LOAD_SCORE:
			number = requestObject.getInt("number");
			
			resultSet = database.executeQuery("SELECT * FROM student_data WHERE number=", number);
			
			responseObject.put("merit", resultSet.getInt("merit"));
			responseObject.put("demerit", resultSet.getInt("demerit"));
			
			break;
		}
		
		return responseObject;
	}

}