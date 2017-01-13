package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Constants;

import transpermatrix.transpermatrix;

public class Scene {

	private List<String> sceneName;
	private double[][] trafficInfo = new double[1700][1700];
	private int sceneNum = 0;

	public Scene() {
		sceneName = new ArrayList<String>();
		init();

	}

	// 初始化，获取交通状况，所有景点的名字，List中的下标是ID
	private void init() {
		// trafficInfo = new transpermatrix().generateMatrix();获取实时交通值
		try {
			Class.forName(Constants.drivername);
			Connection conn = DriverManager.getConnection(Constants.url,
					Constants.user, Constants.password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select name from scenic");
			while (rs.next()) {
				sceneName.add(rs.getString("name"));
				sceneNum++;
			}
			for (int i = 0; i < sceneNum; i++)
				for (int j = 0; j < sceneNum; j++)
					if (i > j) {
						rs = stmt
								.executeQuery("select value from scenictraffic where start="
										+ i + " and end=" + j);
						while (rs.next()) 
						trafficInfo[i][j] = rs.getDouble("value");
					}
		} catch (Exception e) {
               System.out.println(e);
		}
	}

	public String getSceneNameById(int id) {
		return sceneName.get(id);
	}

	public int getSceneIdByName(String name) {
		return sceneName.indexOf(name);
	}

	public double getTrafficInfo(int id1, int id2) {
		if (id1 > id2)
			return trafficInfo[id1][id2];
		else
			return trafficInfo[id2][id1];
		
	}

	public int getSceneNum() {
		return sceneNum;
	}

	public List<String> getSceneName() {
		return sceneName;
	}

	public String toString() {
		String result = null;
		for (String a : sceneName)
			result += " " + a;
		return result;
	}

	public static void main(String args[]) {
		System.out.println(new Scene().getTrafficInfo(2, 3));
	}
}
