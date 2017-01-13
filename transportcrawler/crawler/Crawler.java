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
	 * �û���������µ���վ�Թ�������
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
	 * ͨ��GoogleLinkParser����ȡվ�ں��йؼ��ֵĵ���ҳ��
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
	 * �����ӽ��з�������Ϊ�е���ҳ���ܻ�ܳ¾ɣ�����ͨ��ʱ����˵�������ҳ����ʣ����ҳ�к��йؼ��ֵĶ������ּ�¼����
	 * @author Qinger
	 * @return ���йؼ��ֵ�ԭʼ����
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
	 * ʹ��HtmlParser����ҳ���з������õ����йؼ��ֵ����ֶ���
	 * @author Qinger
	 * @param parser ��ĳһ��ҳ��htmlParser
	 * @return ���йؼ��ֵ����ֶ���
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
	 * �ж���ҳ�Ƿ��ʱ
	 * @author Qinger
	 * @param parser ��ĳһ��ҳ��htmlParser
	 * @return �Ƿ����ʱ��Ҫ��
	 */
	private boolean isTimely(Parser parser)
	{
		return (parser != null) && (notOutOfTime(new Date(parser.getConnection().getLastModified())));
	}
	
	/**
	 * �ж�ʱ�����Ƿ�û�г���ʱ������
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
		Crawler crawler = new Crawler("֪��·");
		crawler.setTimeInterval(365 * 24);
		List<String> keywordList = crawler.getRelevantPara();
		for(int i = 0; i < keywordList.size(); i++)
		{
			System.out.println("Index: " + i);
			System.out.println(keywordList.get(i));
		}
	}

}

