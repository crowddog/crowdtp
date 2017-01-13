package preprocess;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Constants;

public class ScenicCrawler {

	public static String getHTML(String url) {
		try {
			URL newUrl = new URL(url);
			URLConnection connect = newUrl.openConnection();
			connect.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			DataInputStream dis = new DataInputStream(connect.getInputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(dis,
					"UTF-8"));
			String html = "";
			String readLine = null;
			while ((readLine = in.readLine()) != null) {
				html = html + readLine;
			}
			in.close();
			return html;
		} catch (MalformedURLException me) {
			System.out.println("MalformedURLException" + me);
		} catch (IOException ioe) {
			System.out.println("ioeException" + ioe);
		}
		return null;
	}

	public static void main(String args[]) throws IOException {
		String result[] = new String[40];
		String url = null;
		int count;
		try {
			Class.forName(Constants.drivername);
			Connection conn = DriverManager.getConnection(Constants.url,
					Constants.user, Constants.password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = conn.createStatement();
		
			for (int i = 1; i <= 1; i++) {
				count = 0;
				if (i == 1)
					url = "http://www.tuniu.com/guide/d-beijing-200/jingdian";
				else
					url = "http://www.tuniu.com/guide/d-beijing-200/jingdian/"
							+ i + "/";
				
				String html_file = getHTML(url);
				String reg = "<div class=\"jingdian_name\">.{0,100}<p>.{0,20}";
				Pattern p = Pattern.compile(reg);
				Matcher m = p.matcher(html_file);
				while (m.find()) {
					result[count] = m.toString();
					System.out.println(result[count]);
					count++;
				}
				 
//				for (int j = 0; j < count; j++) {
//					String temp[] = result[j].split("<p>");
//					String temp_scenic[] = temp[2].split("</p>");
//					System.out.println(temp_scenic[0]);
//					 stmt.execute("insert into scene (name) values ('"+temp_scenic[0]+"')");
//					System.out.println(temp_scenic[0]);
//				}
			}
			

		} catch (Exception e) {
            System.out.println(e.toString());
		}
	}
}
