package preprocess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

import com.tencent.weibo.utils.QHttpClient;

import models.Constants;

public class RoadsInRoutes {
	public static void main(String args[]) {
		 String
		 url="http://api.map.baidu.com/geocoder/v2/?location=39.983424051248,116.32298703399&output=json&ak=2ec523fe70b7ab261079cf043986cc30";
		 QHttpClient testClient=new QHttpClient();
		 try {
		 String result=testClient.simpleHttpGet(url, null);
		 System.out.println(result);
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }

		// 创建数据库并初始化，数据库存储的是scenic表对应的交通值
//		try {
//			Random rand = new Random();
//			Class.forName(Constants.drivername);
//			Connection conn = DriverManager.getConnection(Constants.url,
//					Constants.user, Constants.password);
//			if (!conn.isClosed())
//				System.out.println("Succeeded connecting to the Database!");
//			Statement stmt = conn.createStatement();
//			int count = 1;
//			stmt.execute("update scenictraffic set value=3.54 where start=1 and end=0");
//
//			
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}

	}

}
