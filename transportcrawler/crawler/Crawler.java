package crawler;

import url.CrawlerUrl;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import log.Log;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;


public class Crawler {

	private static String bjjtgl 	= "http://www.bjjtgl.gov.cn";
	private static String shjt1 	= "http://www.jtcx.sh.cn";
	private static String shjt2 	= "http://t.xinmin.cn/lukuang";
	private static String googleGetFore = "https://www.google.com.hk/search?newwindow=1&safe=strict&q=";
	private static String googleGetLatter = "&gs_l=serp.3...91107.136338.0.139711.17.15.0.0.0.0.1658.4115.2-1j5-2j0j1j1.5.0....0.0..1c.1j2j4.20.serp.GnvBKapM0WU";

	private List<CrawlerUrl> googleSiteUrl = new ArrayList<CrawlerUrl>(Arrays.asList(new CrawlerUrl(bjjtgl, 0), 
			new CrawlerUrl(shjt1, 0), new CrawlerUrl(shjt2, 0)));
	
	private List<CrawlerUrl> tovisitUrl = new ArrayList<CrawlerUrl>();
	
	private GoogleLinkParser googleLinkParser = new GoogleLinkParser();
	
	private int timeInterval 	= 36;
	private String keyword;
	private long msToHour = 1000 * 60 * 60;
	
	public Crawler(String keyword)
	{
		this(keyword, new String[]{});
	}
	
	/**
	 * 用户自主添加新的网站以供搜索。
	 * @author Qinger
	 * @param keyword
	 * @param seeds
	 */
	public Crawler(String keyword, String[] seeds)
	{
		this.keyword = keyword;
		for(int i = 0; i < seeds.length; i++)
		{
			for(int j = 0; j < this.googleSiteUrl.size(); j++)
			{
				if(!this.googleSiteUrl.get(j).getUrlString().equals(seeds[i]))
					this.googleSiteUrl.add(new CrawlerUrl(seeds[i], 0));
			}
		}
	}
	
	public int getTimeInterval()
	{
		return this.timeInterval;
	}
	
	public void setTimeInterval(int timeInterval)
	{
		if(timeInterval > 0)
		{
			this.timeInterval = timeInterval;
		}
	}
	
	/**
	 * 通过GoogleLinkParser来获取站内含有关键字的的网页。
	 * @author Qinger
	 */
	private void findUrlByGoogle()
	{
		for(int i = 0; i < this.googleSiteUrl.size(); i++)
		{
			String qValue = null;
			try {
				qValue = URLEncoder.encode("site:" + googleSiteUrl.get(i).getUrlString() + " " + this.keyword, "UTF-8");	
			} catch (UnsupportedEncodingException e) {
				Log.logInfo(e);
			}
			if(qValue != null)
			{
				String siteUrl = googleGetFore + qValue + "&oq=" + qValue + googleGetLatter;
				CrawlerUrl crawlerUrl = new CrawlerUrl(siteUrl, 0);
				this.googleLinkParser.setCrawlerUrl(crawlerUrl);
				List<String> urls = this.googleLinkParser.extractUrls();
				for(int j = 0; j < urls.size(); j++)
				{
					if(!this.tovisitUrl.contains(urls.get(j)));
						this.tovisitUrl.add(new CrawlerUrl(urls.get(j), 0));
				}
			}
		}
	}

	/**
	 * 对链接进行分析，因为有的网页可能会很陈旧，所以通过时间过滤掉部分网页，将剩余网页中含有关键字的段落文字记录下来
	 * @author Qinger
	 * @return 含有关键字的原始文字
	 */
	public List<String> getRelevantPara()
	{
		findUrlByGoogle();
		List<String> validPara = new ArrayList<String>();
		for(int i = 0 ; i < this.tovisitUrl.size(); i++)
		{
			Parser parser = null;
			try {
				parser = new Parser(this.tovisitUrl.get(i).getUrlString());
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				Log.logInfo(e);
			}
			if(isTimely(parser))
			{
				validPara.addAll(parsePage(parser));
			}
		}
		validPara.addAll(SpecificUrlParser.getTodayTrafficMessage(keyword));
		return validPara;
	}
	
	/**
	 * 使用HtmlParser对网页进行分析，得到含有关键字的文字段落
	 * @author Qinger
	 * @param parser 对某一网页的htmlParser
	 * @return 含有关键字的文字段落
	 */
	private List<String> parsePage(Parser parser)
	{
		List<String> keywordList = new ArrayList<String>();
		if(parser != null)
		{
			try {
				parser.setEncoding("UTF-8");
				KeywordTextExtractingVisitor keywordVisitor = new KeywordTextExtractingVisitor(this.keyword);
				parser.visitAllNodesWith(keywordVisitor);
				keywordList = keywordVisitor.getKeyWordText();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				Log.logInfo(e);
			}
		}
		
		return keywordList;
	}
	
	/**
	 * 判断网页是否过时
	 * @author Qinger
	 * @param parser 对某一网页的htmlParser
	 * @return 是否符合时间要求
	 */
	private boolean isTimely(Parser parser)
	{
		return (parser != null) && (notOutOfTime(new Date(parser.getConnection().getLastModified())));
	}
	
	/**
	 * 判断时间间隔是否没有超出时间限制
	 * @author Qinger
	 * @param date
	 * @return
	 */
	private boolean notOutOfTime(Date date)
	{
		Date now = new Date();
		long timeIntervalByMs = now.getTime() - date.getTime();
		int timeIntervalByHour = (int)(timeIntervalByMs / msToHour );
		return (Math.abs(timeIntervalByHour)) < this.timeInterval;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Crawler crawler = new Crawler("知春路");
		crawler.setTimeInterval(365 * 24);
		List<String> keywordList = crawler.getRelevantPara();
		for(int i = 0; i < keywordList.size(); i++)
		{
			System.out.println("Index: " + i);
			System.out.println(keywordList.get(i));
		}
	}

}

