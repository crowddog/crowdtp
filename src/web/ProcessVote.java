package web;

public class ProcessVote {
	//得票的景点，是在（0，m）范围内的，跟distributionVal对应
	public void getVote(int vote_scene)
	{
		renew(vote_scene);
	}
	public void renew(int vote_scene)
	{
		if(Distribution.getMaxMean_index()==vote_scene)
			renew_distributionValandmeanVal(0,vote_scene);
		else
			renew_distributionValandmeanVal(1,vote_scene);
	}
	public void renew_distributionValandmeanVal(int type,int vote_scene)
	{
		if(type==0)//得票的景点正好是当前满意度最高的景点,alpha
		{
			for(int i=0;i<Distribution.size;i++)
			{
				if(i==vote_scene)//当前景点是得票景点的话，区别对待
				{
					double acc=1;
					for(int j=0;j<Distribution.size;j++)
						if(j!=vote_scene)
							acc*=Distribution.meanVal[j];
					for(int j=0;j<100;j++)
						Distribution.distributionVal[i][j]=acc*(j/100)*Distribution.distributionVal[i][j];
					Distribution.renewMeanVal(i);//分布变了，那么均值也相应改变
				}
				else//当前景点不是得票景点
				{
					double acc=1;
					for(int j=0;j<Distribution.size;j++)
						if(j!=i&&j!=vote_scene)
							acc*=Distribution.meanVal[j];
					double acc_votescene=0;
					for(int j=0;j<99;j++)
						acc_votescene+=(Distribution.distributionVal[vote_scene][j+1]-Distribution.distributionVal[vote_scene][j])/0.03*(Math.pow((j+1)/100,3)-Math.pow(j/100, 3))+((j+1)*Distribution.distributionVal[vote_scene][j]-j*Distribution.distributionVal[vote_scene][j+1])/0.02*(Math.pow((j+1)/100,2)-Math.pow((j/100), 2));
					for(int j=0;j<100;j++)
						Distribution.distributionVal[i][j]=acc*acc_votescene*Distribution.distributionVal[i][j];
					Distribution.renewMeanVal(i);
				}
			}
		}
		else//得票的景点不是当前满意度最高的景点，1-alpha
		{
			for(int i=0;i<Distribution.size;i++)
			{
				if(i==vote_scene)//当前景点是得票景点的话，区别对待
				{
					double acc=1;
					for(int j=0;j<Distribution.size;j++)
						if(j!=vote_scene)
							acc*=Distribution.meanVal[j];
					for(int j=0;j<100;j++)
						Distribution.distributionVal[i][j]=acc*(1-j/100)/Distribution.size*Distribution.distributionVal[i][j];
					Distribution.renewMeanVal(i);
				}
				else//当前景点不是得票景点
				{
					double acc=1;
					for(int j=0;j<Distribution.size;j++)
						if(j!=i&&j!=vote_scene)
							acc*=Distribution.meanVal[j];
					double acc_votescene=0;
					for(int j=0;j<99;j++)
						acc_votescene+=(Distribution.distributionVal[vote_scene][j+1]-Distribution.distributionVal[vote_scene][j])/0.03*(Math.pow(j/100,3)-Math.pow((j+1)/100,3))+((j+1)*Distribution.distributionVal[vote_scene][j]-j*Distribution.distributionVal[vote_scene][j+1])/0.02*(j*j/10000-(j/100+0.01)*(j/100+0.01))+((Distribution.distributionVal[vote_scene][j+1]-Distribution.distributionVal[vote_scene][j])/0.02*(Math.pow(j/100+0.01,2)-Math.pow(j/100, 2))+((j+1)*Distribution.distributionVal[vote_scene][j]-j*Distribution.distributionVal[vote_scene][j+1]));
					for(int j=0;j<100;j++)
						Distribution.distributionVal[i][j]=acc*acc_votescene*Distribution.distributionVal[i][j];
					Distribution.renewMeanVal(i);
				}
			}
		}
	}

}
