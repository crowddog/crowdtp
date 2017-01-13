package url;

import java.net.MalformedURLException;
import java.net.URL;

import log.Log;

public class CrawlerUrl {
		private int 	depth 			= 0;
		private String 	urlString 		= null;
		private String 	title;
		private URL 	url				= null;
		private boolean isAllowedToVisit;
		private boolean isCheckedForPermission 	= false;
		private boolean isVisited				= false; 
		
		public CrawlerUrl(String urlString, int depth)
		{
			this.depth 		= depth;
			this.urlString	= urlString;
			computeURL();
		}
		
		private void computeURL()
		{
			try
			{
				url = new URL(urlString);
			}
			catch(MalformedURLException e)
			{
				Log.logInfo(e);
			}
		}
		
		public URL getURL()
		{
			return this.url;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
		}
		
		public String getTitle()
		{
			return this.title;
		}
		
		public int getDepth()
		{
			return this.depth;
		}
		
		public boolean isAllowedToVisit()
		{
			return this.isAllowedToVisit;
		}
		
		public void setAllowedToVisit(boolean isAllowedToVisit)
		{
			this.isAllowedToVisit 		= isAllowedToVisit;
			this.isCheckedForPermission	= true;
		}
		
		public boolean isCheckedForPermission()
		{
			return this.isCheckedForPermission;
		}
		
		public boolean isVisited()
		{
			return this.isVisited;
		}
		
		public void setIsVisited()
		{
			this.isVisited = true;
		}
		
		public String getUrlString()
		{
			return this.urlString;
		}
		
		public String toString()
		{
			return this.urlString + " [depth]=" + depth + "" +
					"visit=" + this.isAllowedToVisit + " check="
					+ this.isCheckedForPermission + "]";
		}
		
		public int hashCode()
		{
			return this.urlString.hashCode();
		}
		
		public boolean equals(Object obj)
		{
			return obj.hashCode() == this.hashCode();
		}
}

