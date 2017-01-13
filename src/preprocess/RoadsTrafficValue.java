package preprocess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import models.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tencent.weibo.utils.QHttpClient;

import dataAnalyser.DataAnalyser;

public class RoadsTrafficValue {
	public static double[][] trafficValue = new double[41][41];

	public static double getTrafficValue(Set roads) {
		DataAnalyser analyser = new DataAnalyser();
		double valueSum = 0;
		double valueAverage = 0;

		for (Object a : roads) {
			double roadValue = analyser.returnValues((String) a);
			valueSum = valueSum + roadValue;
		}
		valueAverage = valueSum / roads.size();
		if((int)valueAverage == 1)
			valueAverage = new Random().nextDouble()*5;
		return valueAverage;
	}

	public static Set getRoads(String start, String end) {
		Set roads = new HashSet();
		String urlgeo = "http://api.map.baidu.com/direction/v1?mode=driving&origin="
				+ start
				+ "&destination="
				+ end
				+ "&origin_region=北京&destination_region=北京&output=json&ak=2ec523fe70b7ab261079cf043986cc30";
		QHttpClient testClient = new QHttpClient();
		try {
			String result = testClient.simpleHttpGet(urlgeo, null);
			System.out.println(result);
			JSONObject jo = new JSONObject(result);
			JSONArray jaRoutes = jo.getJSONObject("result").getJSONArray(
					"routes");
			JSONArray jaSteps = jaRoutes.getJSONObject(0).getJSONArray("steps");
			GeoObject[] geostr = new GeoObject[jaSteps.length()];
			for (int i = 0; i < jaSteps.length(); i++) {
				geostr[i] = new RoadsTrafficValue.GeoObject();
				geostr[i].lng = jaSteps.getJSONObject(i).getJSONObject(
						"stepOriginLocation").getString("lng");
				geostr[i].lat = jaSteps.getJSONObject(i).getJSONObject(
						"stepOriginLocation").getString("lat");
			}
			for (int i = 0; i < geostr.length; i++) {
				String urlReverseLoc = "http://api.map.baidu.com/geocoder/v2/?ak=2ec523fe70b7ab261079cf043986cc30&callback=renderReverse&location="
						+ geostr[i].lat
						+ ","
						+ geostr[i].lng
						+ "&output=json&pois=0";
				String tempstr = testClient.simpleHttpGet(urlReverseLoc, null);
				String callbackstr = tempstr
						.substring(29, tempstr.length() - 1);
				JSONObject callbackjsonobj = new JSONObject(callbackstr);
				String street = callbackjsonobj.getJSONObject("result")
						.getJSONObject("addressComponent").getString("street");
				roads.add(street);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// for(Object a: roads)
		// System.out.println(a);
		return roads;

	}

	public static class GeoObject {
		public String lng;
		public String lat;

		public GeoObject() {

		}
	}

	//更新景点之间的交通值
	public boolean renewTrafficValue() {
		try {
			Class.forName(Constants.drivername);
			Connection conn = DriverManager.getConnection(Constants.url,
					Constants.user, Constants.password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select name from scenic");
			List<String> sceneName = new ArrayList<String>();
			while (rs.next())
				sceneName.add(rs.getString("name"));

			for (int i = 1, size = sceneName.size(); i < size; i++)
				for (int j = 0; j < i; j++) {
					Set roads = new HashSet();
					rs = stmt.executeQuery("select roads from scenictraffic where start="+i+" and end="+j);
					String tmp = null;
					while (rs.next())
							tmp = rs.getString("roads");
					String[] res = tmp.split(";");
					for(String s : res)
						roads.add(s);
					trafficValue[i][j] = getTrafficValue(roads);
				}
			for (int i = 1, size = sceneName.size(); i < size; i++)
				for (int j = 0; j < i; j++)
					stmt.execute("update scenictraffic set value='"
							+ trafficValue[i][j] + "' where start=" + i
							+ " and end=" + j);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

	// 插入景点之间的道路
//	public static void main(String args[]) {
//		try {
//			Class.forName(Constants.drivername);
//			Connection conn = DriverManager.getConnection(Constants.url,
//					Constants.user, Constants.password);
//			if (!conn.isClosed())
//				System.out.println("Succeeded connecting to the Database!");
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("select name from scenic");
//			List<String> sceneName = new ArrayList<String>();
//			while (rs.next())
//				sceneName.add(rs.getString("name"));
//
//			for (int i = 1, size = sceneName.size(); i < size; i++)
//				for (int j = 0; j < 39; j++) {
//					Set roads = getRoads(sceneName.get(39), sceneName.get(j));
//					StringBuilder sb = new StringBuilder();
//					for (Object a : roads)
//						sb.append((String) a + ";");
//					stmt.execute("update scenictraffic set roads='"
//							+ sb.toString() + "' where start=" + 39
//							+ " and end=" + j);
//
//				}
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}
	
	public static void main(String args[]) {
		RoadsTrafficValue a = new RoadsTrafficValue();
		a.renewTrafficValue();
	}

}
