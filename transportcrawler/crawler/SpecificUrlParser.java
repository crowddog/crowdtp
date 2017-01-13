package crawler;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import log.Log;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

public class SpecificUrlParser {
	
	static final NodeFilter aFilter = new TagNameFilter("A");
	static final NodeFilter spanFilter = new TagNameFilter("SPAN");
	static final NodeFilter styleFilter = new HasAttributeFilter("style");
	static final NodeFilter notstyleFilter = new NotFilter(styleFilter);
	static final NodeFilter andFilter = new AndFilter(spanFilter, notstyleFilter);
	
	public static List<String> getTodayTrafficMessage(String keyword)
	{
		List<String> keyTrafficMessage 	= new ArrayList<String>();
		List<String> trafficMessage 	= getTodayTrafficMessage();
		for(int i = 0; i < trafficMessage.size(); i++)
		{
			String message = trafficMessage.get(i);
			if(message.contains(keyword))
				keyTrafficMessage.add(message);
		}
		
		return keyTrafficMessage;
	}
	
	public static List<String> getTodayTrafficMessage()
	{
		List<String> trafficMessage = new ArrayList<String>();
		try
		{
			Parser beijingParser = new Parser((HttpURLConnection)(new URL("http://www.bjjtgl.gov.cn/portals/0/xml/realinfo1.htm")).openConnection());
			Parser duchemeParser = new Parser((HttpURLConnection)(new URL("http://duche.me/")).openConnection());
			getPlainString(beijingParser, aFilter, trafficMessage);
			getPlainString(duchemeParser, andFilter, trafficMessage);
		}
		catch (Exception e)
		{
			Log.logInfo(e);
		}
		return trafficMessage;
	}
	
	public static void getPlainString(Parser parser, NodeFilter filter, List<String> trafficMessage) 
	{
		NodeList nodes = null;
		try {
			parser.setEncoding("UTF-8");
			nodes = parser.extractAllNodesThatMatch(filter);
		}catch(Exception e)
		{
			Log.logInfo(e);
		}
		
		if(nodes != null)
		{
			for(int i = 0; i < nodes.size(); i++){
				Node textnode = (Node)nodes.elementAt(i);
				String plainTextString = textnode.toPlainTextString();
				trafficMessage.add(plainTextString);
			}
		}
	}	
	
	public static void main(String[] args) {
		List<String> trafficMessage = SpecificUrlParser.getTodayTrafficMessage();
		for(int i = 0; i < trafficMessage.size(); i++)
			System.out.println(trafficMessage.get(i));
	}

}
