package web;

import java.util.Arrays;




/*
 * 这个类主要是初始化每个景点的满意度的取值，取值范围是（0,0.01,0.02,....,1）
*/

public class Distribution {
	public static double distributionVal[][];
	public static double meanVal[];
	public static int size;
	public static void init(int scene_size)
	{
		size=scene_size;
		distributionVal=new double[scene_size][100];
		meanVal=new double[scene_size];
		for(int i=0;i<scene_size;i++)
		{
			
			for(int j=0;j<100;j++)
			{
				distributionVal[i][j]=9*Math.pow((1-(double)j/100),8);
//				System.out.println(Distribution.distributionVal[i][j]);
			}
			meanVal[i]=0.1;
		}
		
	}
	public static void setDisVal(int index_scene,int x,int newVal)
	{
		distributionVal[index_scene][x]=newVal;
	}
	public static void setMeanVal(int index_scene,int newVal)
	{
		meanVal[index_scene]=newVal;
	}
	public static int getMaxMean_index()
	{
		double max=-100000;
		int index=0;
		for(int i=0;i<size;i++)
			if(meanVal[i]>max)
			{
				max=meanVal[i];
				index=i;
			}
		
		return index;
		
	}
	public static void renewMeanVal(int index_scene)
	{
		double sum=0;
		for(int i=0;i<99;i++)
			sum+=(distributionVal[index_scene][i+1]-distributionVal[index_scene][i])/0.02*(i+1)*(i+1)/10000+((i+1)*distributionVal[index_scene][i]-i*distributionVal[index_scene][i+1])*(i+1)/100-((distributionVal[index_scene][i+1]-distributionVal[index_scene][i])/0.02*i*i/10000+((i+1)*distributionVal[index_scene][i]-i*distributionVal[index_scene][i+1])*i/100);
	}
	public static void main(String args[])
	{
		Distribution.init(2);
		
		System.out.println(Distribution.meanVal[0]);
	}


}
