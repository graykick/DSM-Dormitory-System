package org.boxfox.dms.utilities.database;

public enum Query {
	MEAL("replace into meal (date, breakfast, lunch, dinner, breakfast_allergy, lunch_allergy, dinner_allergy) values(?, ?, ?, ?, ?, ?, ? )", "select ? from meal where date=?"),
	PLAN("replace into plan (years, month, data) values(?, ?, ?)", "select data from plan where year=? and month=?"),
	POST("insert into app_content (title,writer,date,content) values(?, ?, ?, ?)","select ? from app_content"),
	ATTACHMENT("insert into attachments (no, name, link) values(?, ?, ?)", "select ? from attachements where no=?");

	public String insertFormat, selectFormat, table;

	Query(String insertFormat, String selectFormat) {
		this.table = insertFormat.split(" ")[2];
		this.insertFormat = insertFormat;
		this.selectFormat = selectFormat;
	}

}