package crawler;

import url.CrawlerUrl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleLinkParser {

	private static final String REGEXP_H3ContainLink 	= "<h3 class=\"r\">(.)*?</h3>";  //观察
	private static final String REGEXP_LINK				= "<a href=\"(.)*\">";
	private static final String REGEXP_NextPage			= "<td class=\"b navend\"><a(.)*?</td>";
	private static List<String> ignoreSuffix = new ArrayList<String>(Arrays.asList("avi", "bmp", "cab", "css", "js", "dat", "rar", "doc", "dot", "docx", "exe", "gif", "image", "img", "jar", "jpg", "jpeg", "pdf", "swf", "txt", "zip","XLS","xls"));
	
	private Pattern 	h3Pattern 		= null;
	private Pattern 	linkPattern 	= null;
	private Pattern		nextPagePattern	= null;
	private CrawlerUrl 	crawlerUrl 	= null; 
	
	private int maxLink = 100;		//连接数量上限
	
	public GoogleLinkParser()
	{
		this.h3Pattern 		= Pattern.compile(REGEXP_H3ContainLink);
		this.linkPattern 	= Pattern.compile(REGEXP_LINK);
		this.nextPagePattern=	Pattern.compile(REGEXP_NextPage);
	}
	
	public void setCrawlerUrl(CrawlerUrl crawlerUrl)
	{
		this.crawlerUrl = crawlerUrl;
	}
	
	public CrawlerUrl getCrawlerUrl()
	{
		return this.crawlerUrl;
	}
	
	public List<String> extractUrls()
	{
		List<String> urlList = new ArrayList<String>();
		String text = null;
		if(this.crawlerUrl != null)
		{
			do
			{
				text = GetContent.getContent(crawlerUrl);
				extractLinkInH3(urlList, text);
			}while(((crawlerUrl = getNextPageLink(text)) != null)&&urlList.size() <= maxLink);
		}
		return urlList;
	}
	
	/**
	 * 对传送进来的文本抽取所含有的下一页的链接连接。
	 * @param text 被抽取链接的文本。一般为调用GetContent获取的网页的html内容
	 * @return 如果含有下一页链接，返回，否则返回null
	 */
	private CrawlerUrl getNextPageLink(String text)
	{
		CrawlerUrl 	nextPageUrl 	= null;
		String 		nextPageLink 	= null;
		if(text == null)
			return null;		
		Matcher m = this.nextPagePattern.matcher(text);
		while(m.find())
		{
			String td = m.group();
			if(td.contains("下一页") || td.contains("下一頁"))
			{	
				nextPageLink = extractLink(td);
				nextPageLink = nextPageLink.replace(';', '&');
			}
		}
		if(nextPageLink != null)
			nextPageUrl = new CrawlerUrl(nextPageLink, 0);
		return nextPageUrl;
	}
	
	/**
	 * 对传送进来的文本抽取合法链接，存储进入列表中
	 * @author Qinger
	 * @param urlList 存储链接的列表
	 * @param text 要被抽取的文本内容，正常情况应为html网页内容。
	 */
	private void extractLinkInH3(List<String> urlList, String text)
	{
		if(text == null)
			return ;
		Matcher m = this.h3Pattern.matcher(text);
		String h3 = null;
		while(m.find())
		{
			h3 = m.group();
			String url = extractLink(h3);
			if(url != null && checkSuffix(url))
				urlList.add(url);
		}
	}
	
	/**
	 * 检查链接的合法性，有些链接是下载资源，不是html网页，则可忽略
	 * @author Qinger
	 * @param s 正常情况应为链接
	 * @return 是否合法
	 */
	private boolean checkSuffix(String s)
	{
		String[] terms = s.split("\\.");
		int length = terms.length;
		String suffix = terms[length - 1];
		return !ignoreSuffix.contains(suffix);
	}
	
	/**
	 * 因为google返回的html网页中，合法的站内链接都是在<h3 class="r">。。。</h3>结构内，所以抽取其中的链接
	 * @author Qinger
	 * @param h3 含有合法链接的结构
	 * @return 抽取到的链接
	 */
	private String extractLink(String h3)
	{
		Matcher link = this.linkPattern.matcher(h3);
		URL localUrl = this.crawlerUrl.getURL();
		String host = localUrl.getHost();
		String url = null;
		if(link.find())
		{
			url = link.group();
			String[] terms = url.split("href=\"");
			for(String term : terms)
			{
				if(term.startsWith("/"))
				{
					int index = term.indexOf("\"");
					if(index > 0)
					{
						term = term.substring(0, index);
						term = "http://" + host + term;
						return term;
					}
				}else if(term.startsWith("http"))
				{
					int index = term.indexOf("\"");
					if(index > 0)
					{
						term = term.substring(0, index);
						return term;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String seed = "https://www.google.com.hk/search?newwindow=1&safe=strict&q=%E7%9F%A5%E6%98%A5%E8%B7%AF+site%3Awww.bjjtgl.gov.cn%2Fpublish%2Fportal0%2Ftab65%2F&oq=%E7%9F%A5%E6%98%A5%E8%B7%AF+site%3Awww.bjjtgl.gov.cn%2Fpublish%2Fportal0%2Ftab65%2F&gs_l=serp.3...91107.136338.0.139711.17.15.0.0.0.0.1658.4115.2-1j5-2j0j1j1.5.0....0.0..1c.1j2j4.20.serp.GnvBKapM0WU";
		CrawlerUrl crawlerUrl = new CrawlerUrl(seed, 0);
		GoogleLinkParser googleLinkParser = new GoogleLinkParser();
		googleLinkParser.setCrawlerUrl(crawlerUrl);
		List<String> urls = googleLinkParser.extractUrls();
		for(int i = 0; i < urls.size(); i++)
		{
			System.out.println(urls.get(i));
		}
	}

}

