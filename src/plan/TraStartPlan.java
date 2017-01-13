package plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.TraConstants;
import entities.TraRequireVoteRoute;
import entities.TraRoute;
import entities.Scene;

//每次把最好的k条路线从routes中移除，添加到requireVoteRoute中接受投票，新产生的route在加入到routes中
public class TraStartPlan {
	public Scene scenes;// 所有景点的集合
	public List<TraRoute> routes;// 所有路线的集合
	public List<Integer> voteIds;// 记录发放投票时分配的ID，在获取投票结果时要用
	public TraRequireVoteRoute requireVoteRoute;// 需要投票的top-k条路线

	public TraStartPlan(String startScene) {
		scenes = new Scene();
		routes = new ArrayList<TraRoute>();
		voteIds = new ArrayList<Integer>();
		requireVoteRoute = new TraRequireVoteRoute();
		List<String> remainScenes = new ArrayList<String>();
		for (String a : scenes.getSceneName())
			if (!a.equals(startScene))
				remainScenes.add(a);

		List<String> tempscene = new ArrayList<String>();
		tempscene.add(startScene);
		TraRoute firstRoute = new TraRoute(tempscene, 1, 1, 0, remainScenes,
				scenes);
		requireVoteRoute.addRequireVoteRoute(firstRoute);

	}

	public TraRoute startplan() {
		Random rand = new Random();

		while (requireVoteRoute.getBestRoute() == null) {
			// System.out.println("本轮已有的路线：");
			// for (TraRoute route : routes)
			// System.out
			// .println(route + "  潜力分：" + route.getPotentialScore());
			int remainvotes = 0;
			for (TraRoute route : requireVoteRoute.getRequireVoteRoute()) {
				if (route == null)
					continue;
				System.out.println("被选择进行投票的路线：" + route);
				// if (requireVoteRoute.getBestRoute() != null)
				// return requireVoteRoute.getBes tRoute();
				remainvotes = route.getAvailableScene().size() * 5;
				TraConstants.questionnum += remainvotes;
				if (route != null) {
					for (String nextscene : route.getAvailableScene()) {
						int route_votes = rand.nextInt() % remainvotes;
						TraRoute a = route.addScene(nextscene, Math
								.abs((double) 0.6*route_votes / remainvotes));
						routes.add(a);
						remainvotes -= route_votes;
					}

				}
			}

			requireVoteRoute.clear();
			for (int i = 0; i < routes.size(); i++) {
				if (requireVoteRoute.addRequireVoteRoute(routes.get(i)))
					routes.remove(i--);
			}
		}
		return requireVoteRoute.getBestRoute();
	}

	public static void main(String args[]) {

		int sum;
		int i = 8;// 景点数
		int j = 2;// topk
		TraConstants.topk = j;
		TraConstants.userRequireSceneNum = i;
		TraStartPlan startplan = new TraStartPlan("天安门广场");
		TraRoute bestroute = startplan.startplan();
		sum = TraConstants.questionnum;
		System.out.println("用户请求的景点数是:" + i + " topk=" + j + " 问题总数是：" + sum
				+ " 最佳路线的得分是：" + bestroute.getScore());

		// Constants.topk = 1;
		// Constants.userRequireSceneNum = 3;
		// new StartPlan("天安门广场").startplan();
		// System.out.println(Constants.questionnum);
	}
}