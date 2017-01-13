package weiboSearch;

import log.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.model.Paging;

public class GetWeiboContent{
	
	private final int maxWeiboPages = 100;
	private final int maxWeiboCountPerPage = 100;
	private final int minWeiboCountPerPage = 20;
	private int searchWeiboCountPerPage = 30;
	private int timeInterval = 12;
	private final String access_token = "2.00HZU7uD34B8gE56cf775367yHtGFD";
	private List<Status> weiboStatus = new ArrayList<Status>();
	private Timeline tm = new Timeline();
	private Integer baseApp = new Integer(0);
	private Integer feature = new Integer(0);
	private long msToHour  = 1000 * 60 *60;
	
	public GetWeiboContent()
	{
		
	}
	
	public GetWeiboContent(int timeInterval , int weiboCountPerPage)
	{
		if(timeInterval > 1)
			this.timeInterval = timeInterval;
		if(weiboCountPerPage >= this.minWeiboCountPerPage && weiboCountPerPage <= this.maxWeiboCountPerPage)
			this.searchWeiboCountPerPage = weiboCountPerPage;
	}
	
	void setTimeInterval(int timeInterval)
	{
		if(timeInterval > 1)
			this.timeInterval = timeInterval;
	}
	
	void setSearchWeiboCountPerPage(int weiboCountPerPage)
	{
		if(weiboCountPerPage > 20 && weiboCountPerPage <= 100)
			this.searchWeiboCountPerPage = weiboCountPerPage;
	}
	
	public List<Status> getWeiboStatus()
	{
		return this.weiboStatus;
	}
	
	/**
	 * 返回符合时间条件的所有微博内容
	 * @return
	 */
	public List<String> getWeiboContent()
	{
		return this.getWeiboContent("");
	}
	
	/**
	 * 获取含有关键字的微博内容
	 * @param keyWord 要搜索的关键字
	 * @return
	 */
	public List<String> getWeiboContent(String keyWord)
	{
		List<String> weiboContent = new ArrayList<String>();
		for(int i = 0; i < this.weiboStatus.size(); i++)
		{
			Status status = this.weiboStatus.get(i);
			String content = getContentInStatus(status);
			if(content.contains(keyWord))
				weiboContent.add(content);
		}
		return weiboContent;
	}
	
	/**
	 * 获取微博结构中含有的的微博文字内容
	 * @param status 一条微博结构
	 * @return 微博中的文字内容，包含转发文字
	 */
	private String getContentInStatus(Status status)
	{
		Status forwardStatus = status.getRetweetedStatus();
		StringBuffer stringBuffer = new StringBuffer();
		if(forwardStatus != null)
		{
			stringBuffer.append("。");
			stringBuffer.append(this.getContentInStatus(forwardStatus));
		}
		stringBuffer.append(status.getText());
		return stringBuffer.toString();
	}
	
	/**
	 * 检查微博是否符合时间限制
	 * @param date1 
	 * @param date2
	 * @return
	 */
	private boolean checkTimeInterval(Date date1, Date date2)
	{
		long timeIntervalByMs = date1.getTime() - date2.getTime();
		int timeIntervalByHour = (int) (timeIntervalByMs / msToHour);
		return (Math.abs(timeIntervalByHour) < this.timeInterval);
	}
	
	/***
	 * 通过调用微博的api来获取12个小时之内，所关注用户的发布信息。
	 * 将每条信息存储进入队列weiboContent中
	 * @author Qinger
	 * @param 
	 * 
	 */
	public void fetchWeiboContent()
	{
		Date nowDate = new Date();
		for(int page = 1; page <= maxWeiboPages; page++)
		{
			Paging paging = new Paging(page, this.searchWeiboCountPerPage);
			this.tm.client.setToken(access_token);
			try
			{
				StatusWapper status = tm.getFriendsTimeline(baseApp, feature, paging);
			
				for(Status s : status.getStatuses()){
					if(s != null)
					{
						if(checkTimeInterval(s.getCreatedAt(), nowDate))
							this.weiboStatus.add(s);
						else
							return;
					}
				}
			}catch(WeiboException e)
			{
				Log.logInfo(e);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GetWeiboContent searcher = new GetWeiboContent();
		searcher.fetchWeiboContent();
		for(int i = 0; i < searcher.getWeiboStatus().size(); i++)
		{
			Status sta = searcher.getWeiboStatus().get(i);
			System.out.println("Index:  " + i + "Author: " + sta.getUser().getScreenName() + " Content: " + sta.getText());
		}
	}

}

