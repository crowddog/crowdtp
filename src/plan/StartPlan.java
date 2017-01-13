package plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.Constants;
import models.Distribution;
import entities.RequireVoteRoute;
import entities.Route;
import entities.Scene;

//每次把最好的k条路线从routes中移除，添加到requireVoteRoute中接受投票，新产生的route在加入到routes中
public class StartPlan {
	public Scene scenes;// 所有景点的集合
	public List<Route> routes;// 所有路线的集合
	public List<Integer> voteIds;// 记录发放投票时分配的ID，在获取投票结果时要用
	public RequireVoteRoute requireVoteRoute;// 需要投票的top-k条路线

	public StartPlan(String startScene) {
		scenes = new Scene();
		routes = new ArrayList<Route>();
		voteIds = new ArrayList<Integer>();
		requireVoteRoute = new RequireVoteRoute();
		List<String> remainScenes = new ArrayList<String>();
		for (String a : scenes.getSceneName())
			if (!a.equals(startScene))
				remainScenes.add(a);

		List<String> tempscene = new ArrayList<String>();
		tempscene.add(startScene);
		Route firstRoute = new Route(tempscene, 1, 1, 0, remainScenes, scenes);
		requireVoteRoute.addRequireVoteRoute(firstRoute);

	}

	//以下是模拟投票
	public Route startplan(String requirement) {
		Random rand = new Random();

		while (requireVoteRoute.getBestRoute() == null) {
			System.out.println("本轮已有的路线：");
			for (Route route : routes)
				System.out
						.println(route + "  潜力分：" + route.getPotentialScore());
			for (Route route : requireVoteRoute.getRequireVoteRoute()) {
				System.out.println("被选择进行投票的路线：" + route);
				// if (requireVoteRoute.getBestRoute() != null)
				// return requireVoteRoute.getBes tRoute();
				Constants.questionnum += 5;
				if (route != null) {
					for (String nextscene : route.getAvailableScene()) {

						Distribution cur_distribution = new Distribution();
						Distribution next_distribution = new Distribution();
						for (int i = 0; i < 5; i++)
							if (rand.nextInt() % 2 == 0) {
								cur_distribution.renewdistributionVal(
										next_distribution, 0);
								next_distribution.renewdistributionVal(
										cur_distribution, 1);
							} else {
								cur_distribution.renewdistributionVal(
										next_distribution, 1);
								next_distribution.renewdistributionVal(
										cur_distribution, 0);
							}
						cur_distribution.renewMeanVal();
						next_distribution.renewMeanVal();
						routes
								.add(route.addScene(nextscene, Math
										.abs(next_distribution.getmeanVal()
												/ cur_distribution.getmeanVal()
												/ 1E53)));

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

	//以下是微博投票
//	public Route startplan(String requirement) {
//		
//		while (requireVoteRoute.getBestRoute() == null) {
//			System.out.println("本轮已有的路线：");
//			for (Route route : routes)
//				System.out.println(route + "  潜力分：" + route.getPotentialScore());
//			for (Route route : requireVoteRoute.getRequireVoteRoute()) {
//				System.out.println("被选择进行投票的路线：" + route);
//				Constants.questionnum += 5;
//				if (route != null) {
//					generatevote[] gvote = new generatevote[route.getAvailableScene().size()];
//					final ExecutorService exec = Executors.newCachedThreadPool();
//					for (int i = 0; i < route.getAvailableScene().size(); i++) {
//						gvote[i] = new generatevote();
//						final generatevote tmp_gvote = gvote[i];
//						final Route tmp_route = route;
//						final String tmp_nextscene = route.getAvailableScene().get(i);
//						final String tmp_requirement = requirement;
//						Runnable run = new Runnable() {
//							public void run() {
//								tmp_gvote.pullVote(tmp_route, tmp_nextscene, tmp_requirement);
//							}
//						};
//						exec.execute(run);
//						try {
//							Thread.sleep(20000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//					}
//					
//					Distribution cur_distribution = new Distribution();
//					Distribution next_distribution = new Distribution();
//					for(int i = 0; i < gvote.length; i++) {
//						for(int j = 0; j < gvote[i].getYesVoteNum(); j++) {
//							cur_distribution.renewdistributionVal(
//									next_distribution, 0);
//							next_distribution.renewdistributionVal(
//									cur_distribution, 1);
//						}
//						for(int j = 0; j < gvote[i].getNoVoteNum(); j++) {
//							cur_distribution.renewdistributionVal(
//								next_distribution, 1);
//							next_distribution.renewdistributionVal(
//								cur_distribution, 0);
//						}
//						cur_distribution.renewMeanVal();
//						next_distribution.renewMeanVal();
//						routes.add(route.addScene(route.getAvailableScene().get(i), Math
//										.abs(next_distribution.getmeanVal()
//												/ cur_distribution.getmeanVal()
//												/ 1E53)));
//
//					}
//				}
//			}
//			requireVoteRoute.clear();
//			for (int i = 0; i < routes.size(); i++) {
//				if (requireVoteRoute.addRequireVoteRoute(routes.get(i)))
//					routes.remove(i--);
//			}
//		}
//		return requireVoteRoute.getBestRoute();
//	}
	
	public static void main(String args[]) {

		int sum = 0;
		double score = 0;
		int i = 4;// 景点数
		int j = 2;// topk
		String startscene = "天安门广场";
		String requirement = "我想看08年奥运会相关的景点";
		Constants.userRequireSceneNum = i;
		StartPlan startplan = new StartPlan(startscene);
		Route bestroute = startplan.startplan(requirement);
		score += bestroute.getScore();
		sum += Constants.questionnum;
        
		System.out.println("用户请求的景点数是:" + i + " topk=" + j + " 问题总数是：" + sum
				+ " 得分是：" + score);
        System.out.println("最佳路线是："+bestroute);
		// Constants.topk = 1;
		// Constants.userRequireSceneNum = 3;
		// new StartPlan("天安门广场").startplan();
		// System.out.println(Constants.questionnum);
	}
}