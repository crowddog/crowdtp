package entities;

import models.TraConstants;

public class TraRequireVoteRoute {
	private TraRoute[] requireVoteRoute;
	private int currentNum;

	public TraRequireVoteRoute() {
		requireVoteRoute = new TraRoute[TraConstants.topk];
		currentNum = 0;
	}

	public TraRoute getBestRoute() {
		int maxScore_Index = getIndexOfMaxScoreInRequireVoteRoute();
		if (TraConstants.userRequireSceneNum == requireVoteRoute[maxScore_Index]
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
	public boolean addRequireVoteRoute(TraRoute r) {
		boolean success=false;
		if (currentNum == 0)
		{
			requireVoteRoute[currentNum++] = r;
			success=true;
		}
		else if (currentNum == TraConstants.topk) {
			if (r.getPotentialScore() > requireVoteRoute[TraConstants.topk - 1]
					.getPotentialScore()) {
				requireVoteRoute[TraConstants.topk - 1] = r;
				for (int i = TraConstants.topk - 2; i >= 0; i--) {
					if (requireVoteRoute[i].getPotentialScore() < requireVoteRoute[i + 1]
							.getPotentialScore()) {
						TraRoute temp = requireVoteRoute[i + 1];
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
					TraRoute temp = requireVoteRoute[i + 1];
					requireVoteRoute[i + 1] = requireVoteRoute[i];
					requireVoteRoute[i] = temp;
					success=true;
				} else
					break;
			}
		}
		return success;
	}

	public TraRoute[] getRequireVoteRoute() {
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

