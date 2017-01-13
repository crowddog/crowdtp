package crawler;

import url.CrawlerUrl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import log.Log;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class GetContent {

	private  static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0";
	
	/**
	 * HttpClient 连接超时、读取数据超时时间设置（单位：毫秒）
	 */	
	private static final int HTTPCLIENT_CONNECTION_TIMEOUT = 5000;
	private static final int HTTPCLIENT_SO_TIMEOUT = 10000;
	
	/**
	 * 调用httpClient获取网页的html内容
	 * @param url
	 * @return
	 */
	public static String getContent(CrawlerUrl url)
	{
		HttpClient 	client	= new HttpClient();
		GetMethod 	method 	= new GetMethod(url.getUrlString());
		
		//设置连接超时时间（单位毫秒）
		client.getHttpConnectionManager().getParams().setConnectionTimeout(HTTPCLIENT_CONNECTION_TIMEOUT);
		//设置读数据超时时间（单位毫秒）
		client.getHttpConnectionManager().getParams().setSoTimeout(HTTPCLIENT_SO_TIMEOUT);
		
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.getParams().setParameter(HttpMethodParams.USER_AGENT, GetContent.userAgent);
		String text = null;
		try
		{
			int statusCode = client.executeMethod(method);
			if(statusCode == HttpStatus.SC_OK)
			{
				text = readContentsFormStream(new InputStreamReader(method.getResponseBodyAsStream(), 
						method.getRequestCharSet()));
			}
		}
		catch(Throwable t)
		{
			Log.logInfo(t);
		}
		finally
		{
			method.releaseConnection();
		}
		return text;
	}
	
	private static String readContentsFormStream(Reader input)
	throws IOException
	{
		BufferedReader bufferedReader = null;
		if(input instanceof BufferedReader)
		{
			bufferedReader = (BufferedReader) input;
		}
		else
		{
			bufferedReader = new BufferedReader(input);
		}
		StringBuilder sb = new StringBuilder();
		char [] buffer = new char[4 * 1024];
		int charsRead;
		while((charsRead = bufferedReader.read(buffer)) != -1)
		{
			sb.append(buffer, 0, charsRead);
		}
		bufferedReader.close();
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

