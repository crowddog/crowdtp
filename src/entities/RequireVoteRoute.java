package entities;

import models.Constants;

public class RequireVoteRoute {
	private Route[] requireVoteRoute;
	private int currentNum;

	public RequireVoteRoute() {
		requireVoteRoute = new Route[Constants.topk];
		currentNum = 0;
	}

	public Route getBestRoute() {
		int maxScore_Index = getIndexOfMaxScoreInRequireVoteRoute();
		if (Constants.userRequireSceneNum == requireVoteRoute[maxScore_Index]
				.getLengthOfRoute())
			return requireVoteRoute[maxScore_Index];
		else
			return null;
	}

	// 获取得分最高的路线的下标
	public int getIndexOfMaxScoreInRequireVoteRoute() {
		int index = 0;
		for (int i = 1; i < currentNum; i++)
			if (requireVoteRoute[i].getScore() > requireVoteRoute[index]
					.getScore())
				index = i;
		return index;
	}

	// 将r添加到requireVoteRoute中，requireVoteRoute是有序的，从大到小排序
	public boolean addRequireVoteRoute(Route r) {
		boolean success=false;
		if (currentNum == 0)
		{
			requireVoteRoute[currentNum++] = r;
			success=true;
		}
		else if (currentNum == Constants.topk) {
			if (r.getPotentialScore() > requireVoteRoute[Constants.topk - 1]
					.getPotentialScore()) {
				requireVoteRoute[Constants.topk - 1] = r;
				for (int i = Constants.topk - 2; i >= 0; i--) {
					if (requireVoteRoute[i].getPotentialScore() < requireVoteRoute[i + 1]
							.getPotentialScore()) {
						Route temp = requireVoteRoute[i + 1];
						requireVoteRoute[i + 1] = requireVoteRoute[i];
 						requireVoteRoute[i] = temp;
						success=true;
					} else
						break;
				}
			}
		} else {
			requireVoteRoute[currentNum++] = r;
			for (int i = currentNum - 2; i >= 0; i--) {
				if (requireVoteRoute[i].getPotentialScore() < requireVoteRoute[i + 1]
						.getPotentialScore()) {
					Route temp = requireVoteRoute[i + 1];
					requireVoteRoute[i + 1] = requireVoteRoute[i];
					requireVoteRoute[i] = temp;
					success=true;
				} else
					break;
			}
		}
		return success;
	}

	public Route[] getRequireVoteRoute() {
		return requireVoteRoute;
	}

	public int getCurrentNum() {
		return currentNum;
	}
    public void clear()
    {
    	currentNum=0;
    }
}
