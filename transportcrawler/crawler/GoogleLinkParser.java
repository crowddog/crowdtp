package crawler;

import url.CrawlerUrl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleLinkParser {

	private static final String REGEXP_H3ContainLink 	= "<h3 class=\"r\">(.)*?</h3>";  //�۲�
	private static final String REGEXP_LINK				= "<a href=\"(.)*\">";
	private static final String REGEXP_NextPage			= "<td class=\"b navend\"><a(.)*?</td>";
	private static List<String> ignoreSuffix = new ArrayList<String>(Arrays.asList("avi", "bmp", "cab", "css", "js", "dat", "rar", "doc", "dot", "docx", "exe", "gif", "image", "img", "jar", "jpg", "jpeg", "pdf", "swf", "txt", "zip","XLS","xls"));
	
	private Pattern 	h3Pattern 		= null;
	private Pattern 	linkPattern 	= null;
	private Pattern		nextPagePattern	= null;
	private CrawlerUrl 	crawlerUrl 	= null; 
	
	private int maxLink = 100;		//������������
	
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
	 * �Դ��ͽ������ı���ȡ�����е���һҳ���������ӡ�
	 * @param text ����ȡ���ӵ��ı���һ��Ϊ����GetContent��ȡ����ҳ��html����
	 * @return ���������һҳ���ӣ����أ����򷵻�null
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
			if(td.contains("��һҳ") || td.contains("��һ�"))
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
	 * �Դ��ͽ������ı���ȡ�Ϸ����ӣ��洢�����б���
	 * @author Qinger
	 * @param urlList �洢���ӵ��б�
	 * @param text Ҫ����ȡ���ı����ݣ��������ӦΪhtml��ҳ���ݡ�
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
	 * ������ӵĺϷ��ԣ���Щ������������Դ������html��ҳ����ɺ���
	 * @author Qinger
	 * @param s �������ӦΪ����
	 * @return �Ƿ�Ϸ�
	 */
	private boolean checkSuffix(String s)
	{
		String[] terms = s.split("\\.");
		int length = terms.length;
		String suffix = terms[length - 1];
		return !ignoreSuffix.contains(suffix);
	}
	
	/**
	 * ��Ϊgoogle���ص�html��ҳ�У��Ϸ���վ�����Ӷ�����<h3 class="r">������</h3>�ṹ�ڣ����Գ�ȡ���е�����
	 * @author Qinger
	 * @param h3 ���кϷ����ӵĽṹ
	 * @return ��ȡ��������
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

