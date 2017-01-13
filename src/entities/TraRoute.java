package entities;

import java.util.ArrayList;
import java.util.List;

import models.TraConstants;

public class TraRoute {
	private List<String> route;
	private double score;
	private double satScore;// 满意度得分
	private double traScore;// 交通得分
	private List<String> availableScene;// 可用的景点
	private Scene scene;// 保存所有景点的集合
	private double potentialScore;// 潜力分

	public TraRoute(List<String> newRoute, double newscore, double newsatScore,
			double newtraScore, List<String> newavailableScene, Scene allScene) {
		route = newRoute;
		score = newscore;
		satScore = newsatScore;
		traScore = newtraScore;
		availableScene = newavailableScene;
		scene = allScene;
		potentialScore = -100;
		List<String> temp_availableScene = new ArrayList<String>();
		temp_availableScene.addAll(availableScene);
		calPotentialScore(TraConstants.userRequireSceneNum - getLengthOfRoute(),
				temp_availableScene, scene.getSceneIdByName(route
						.get(getLengthOfRoute() - 1)), satScore, traScore);
	}

	public void calPotentialScore(int counter,
			List<String> availableScene_start, int lastSceneId,
			double lastRouteSatScore, double lastRouteTraScore) {
		if (counter == 0) {
			double currentScore = lastRouteSatScore - lastRouteTraScore
					/ TraConstants.transRatio;
			if (currentScore > potentialScore)
				potentialScore = currentScore;
			return;
		}
		for (int i = 0; i < availableScene_start.size(); i++) {
			List<String> temp_availableScene = new ArrayList<String>();
			temp_availableScene.addAll(availableScene_start);
			temp_availableScene.remove(i);
			double temp_satScore = lastRouteSatScore * 1;
			double temp_traScore = lastRouteTraScore
					+ scene.getTrafficInfo(lastSceneId, scene
							.getSceneIdByName(availableScene.get(i)));// 交通得分是累加的，分数为正，在计算最终score时，需要减掉这部分花费
			calPotentialScore(counter - 1, temp_availableScene, scene
					.getSceneIdByName(availableScene_start.get(i)),
					temp_satScore, temp_traScore);

		}

	}

	public void setScore(double sceneScore) {
		score = sceneScore;
	}

	public int getLengthOfRoute() {
		return route.size();
	}

	public double getScore() {
		return score;
	}

	// 在当前路线添加一个新景点，返回一条新路线，参数：景点名字，满意度的改进比例
	public TraRoute addScene(String sceneName, double impSatRatio) {
		List<String> newroute = new ArrayList<String>();
		List<String> newavailableScene = new ArrayList<String>();
		newroute.addAll(route);
		newroute.add(sceneName);
		newavailableScene.addAll(availableScene);
		newavailableScene.remove(sceneName);
		double newsatScore = satScore * impSatRatio;
		double newtraScore = scene.getTrafficInfo(scene.getSceneIdByName(route
				.get(getLengthOfRoute() - 1)), scene
				.getSceneIdByName(sceneName))
				/ TraConstants.transRatio + traScore;
		double newscore = newsatScore - newtraScore;
		TraRoute newRoute = new TraRoute(newroute, newscore, newsatScore,
				newtraScore, newavailableScene, scene);
//		newRoute.calPotentialScore(Constants.userRequireSceneNum - newroute.size(),
//				newavailableScene, scene.getSceneIdByName(sceneName),
//				newsatScore, newtraScore);
		System.out.println("此路线的满意度是："+newsatScore+" 交通分:"+newtraScore+" 潜力分:"+newRoute.getPotentialScore()+" 改进比例:"+impSatRatio);
		return newRoute;

	}

	public double getPotentialScore() {
		return potentialScore;
	}

	public List<String> getRoute() {
		return route;
	}

	public List<String> getAvailableScene() {
		return availableScene;
	}

	public String toString() {
		String result = route.get(0);
		for (int i = 1; i < route.size(); i++)
			result += "-" + route.get(i);
		return result;
	}
}
