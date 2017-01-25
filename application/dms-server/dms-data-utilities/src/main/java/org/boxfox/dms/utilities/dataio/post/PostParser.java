package org.boxfox.dms.utilities.dataio.post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.boxfox.dms.utilities.database.DataBase;
import org.boxfox.dms.utilities.database.DataSaveAble;
import org.boxfox.dms.utilities.database.Query;
import org.boxfox.dms.utilities.database.QueryUtils;
import org.boxfox.dms.utilities.dataio.Parser;
import org.boxfox.dms.utilities.dataio.ParserUtils;
import org.boxfox.dms.utilities.datamodel.post.Attachment;
import org.boxfox.dms.utilities.datamodel.post.AttachmentList;
import org.boxfox.dms.utilities.datamodel.post.Post;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.nodes.Document;

public class PostParser<T> extends Parser {
	public static final int CATEGORY_BROAD = 0;
	public static final int CATEGORY_FAMILER = 1;
	public static final int CATEGORY_CHALLENGE = 2;
	
	private int category, postNum;

	public PostParser(int category,int postNum, String postKey) {
		this.category = category;
		this.postNum = postNum;
		url = LINK + postKey;
	}

	public static String getUrlFromCategory(int category) {
		String url = null;
		switch (category) {
		case CATEGORY_BROAD:
			url = URL_BROAD;
			break;
		case CATEGORY_FAMILER:
			url = URL_FAMILER;
			break;
		case CATEGORY_CHALLENGE:
			url = URL_CHALLENGE;
			break;
		}
		return url;
	}
	@Override
	public DataSaveAble[] parseAll(){
		throw new RuntimeException("Is not Implemented method!");
	};

	@Override
	public DataSaveAble parse() {
		Document doc = ParserUtils.getDoc(url);
		String title = doc.getElementsByClass("read_title").get(0).text().replaceAll("'", "&quot;");
		String writer = doc.getElementsByClass("user_icon").get(0).text().replaceAll("'", "&quot;");
		String dateTime = doc.getElementsByClass("text").get(0).text().replaceAll("'", "&quot;");
		String content = doc.getElementsByClass("context_view").get(0).html().replaceAll("'", "&quot;");
		String html = doc.html();
		html = html.substring(html.indexOf("var PostFiles = ") + "var PostFiles = ".length(),
				html.indexOf("var POST_ID="));
		html = html.substring(0, html.lastIndexOf(";")).replaceAll("'", "&quot;");
		JSONArray files = (JSONArray) JSONValue.parse(html);
		AttachmentList list = new AttachmentList();

		Post post = null;
		try {
			int num = DataBase.getInstance().executeQuery(Query.POST.selectFormat, " ORDER BY `no` DESC LIMIT 1").nextAndReturn().getInt(1)+1;
			DataBase.getInstance().executeUpdate(QueryUtils.querySetter(Query.POST.insertFormat, num, category, postNum, "", "", "1999-01-01", ""));
			post = PostModel.getPost(category, postNum);
			if (post != null) {
				for (Object file : files) {
					JSONArray arr = (JSONArray) file;
					Attachment attachment = new Attachment(post.getNumber(), arr.get(0).toString(),
							arr.get(2).toString());
					list.add(attachment);
				}
				post.setTitle(title);
				post.setWriter(writer);
				post.setDateTime(dateTime);
				post.setContent(content);
				post.setFileList(list);
				DataBase.getInstance().execute(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(post.getTitle());
			System.out.println(post.getContent());
		}
		return post;
	}

}