package vote;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.sf.json.JSONObject;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.utils.QHttpClient;

import entities.RequireVoteRoute;
import entities.Route;
import entities.Scene;

public class generatevote {

	private static OAuthV2 oAuth = new OAuthV2();
	private String new_access_token = null;
	int[] count = new int[2];
    private boolean finish = false;
	public String generatevoteid(Route route, String scenicname, String requirement) {
		/*
		 * 初始化 OAuth ，设置 app key 和 对应的 secret 重复测试时，可直接对access token等鉴权参数赋值，
		 * 以便省略授权过程（完成一次正常的授权过程即可手动设置）
		 */

		init(oAuth);
		try {
			return this.postvoteid(route, scenicname, requirement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public void getvoteid(String name, String voteid) throws Exception {
//		init(oAuth);
//
//		System.out.println("Response from server:");
//
//		String response;
//		String format = "json";
//
//		TAPI tAPI = new TAPI(oAuth.getOauthVersion());// 根据oAuth配置对应的连接管理器
//
//		response = tAPI.getStatisticsInfo(oAuth, format, name, voteid);
//		System.out.println("response = " + response);
//
//		String info;
//
//		JSONObject responseJsonObject;
//		JSONObject dataJsonObject;
//
//		responseJsonObject = JSONObject.fromObject(response);
//		dataJsonObject = (JSONObject) responseJsonObject.get("data");
//		info = dataJsonObject.get("info").toString();
//
//		System.out.println(info + "  ");
//
//		String[] index = info.split("\\{");
//
//		for (int i = 1; i < index.length; i++) {
//			char[] x = index[i].toCharArray();
//			System.out.println(x[8]); // count
//			System.out.println(x[18]); // index
//			count[x[18] - '0'] = x[8] - '0';
//		}
//		
//		try {
//			Thread.sleep(15000);
//		} catch (InterruptedException ie) {
//		}
//		tAPI.shutdownConnection();// 关闭连接管理器
//	}

	private String postvoteid(Route route, String scenicname, String requirement) throws Exception {
		System.out.println("Response from server：");

		String response;
		String format = "json";
		String currentRoute = " ";
		for (int i = 0; i < route.getLengthOfRoute() - 1; i++)
			currentRoute += route.getRoute().get(i) + "-";
		currentRoute += route.getRoute().get(route.getLengthOfRoute() - 1);
		String title = "需求：“"+requirement+"”    当前路线：<" + currentRoute
				+ ">   比加入景点#" + scenicname + "#后的路线：<" + currentRoute + "-"
				+ scenicname + ">       是否更好？";
		String desc = "";
		String picurl = "";
		String choicetype = "1";
		String choicetexts = "是|否";
		String choicepics = "";
		String limit = "1";
		String status = "";
		String endtime = "";
		String clientip = "219.239.227.246";

		/*
		 * ---------------------------------------- 微博相关测试例
		 * begin--------------------------------------------- 注意：
		 * 微博服务器对发微博的频率有限制，如果不加 sleep() 直接执行下列多条发微博操作， 可能会出现 ret=4 errcode=10
		 * 的错误码，意思是：发表太快，被频率限制
		 */
		TAPI tAPI = new TAPI(oAuth.getOauthVersion());// 根据oAuth配置对应的连接管理器

		// 取得返回结果

		response = tAPI.createVote(oAuth, format, title, desc, picurl,
				choicetype, choicetexts, choicepics, limit, status, endtime);
		// json数据使用
		System.out.println("response = " + response);

		String voteid = null;

		JSONObject responseJsonObject;
		JSONObject dataJsonObject;

		responseJsonObject = JSONObject.fromObject(response);
		dataJsonObject = (JSONObject) responseJsonObject.get("data");
		voteid = dataJsonObject.get("voteid").toString();
		System.out.println(scenicname + " voteid " + voteid);

		try {
			Thread.sleep(20000);
		} catch (InterruptedException ie) {
		}

		// 发出此创建的投票

		response = tAPI.add(oAuth, format, "我发起了对" + scenicname
				+ "的信息的投票，快来参与吧！" + "http://vote.t.qq.com/vote/vote.php?id="
				+ voteid + "&u=satbuaa&type=1&tpl=index", clientip);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException ie) {
		}

		tAPI.shutdownConnection();// 关闭连接管理器

		return voteid;

	}

	// ----------------------------------------------
	// 以下为测试辅助方法------------------------------------------------

	private static void init(OAuthV2 oAuth) {
		// 景点信息系统
		oAuth.setClientId("801553526");
		oAuth.setClientSecret("478a1cd4989b4113f8ac79c07e22035f");
		oAuth.setRedirectUri("http://192.168.3.31/re");
		oAuth.setAccessToken("a83c96a85489f0b0e2d98e4c23eea513");
		oAuth.setOpenid("41c241c693141056cc812907194c0716");
		oAuth.setOpenkey("A7AA48EEFFF8A6FC0BF9BF6F208525E6");
		oAuth.setExpiresIn("8035200");
	}

	private static void openBrowser(OAuthV2 oAuth) {

		// 合成URL
		String implicitGrantUrl = OAuthV2Client.generateImplicitGrantUrl(oAuth);

		// 调用外部浏览器
		if (!java.awt.Desktop.isDesktopSupported()) {

			System.err.println("Desktop is not supported (fatal)");
			System.exit(1);
		}
		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		if (desktop == null
				|| !desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {

			System.err
					.println("Desktop doesn't support the browse action (fatal)");
			System.exit(1);
		}
		try {
			desktop.browse(new URI(implicitGrantUrl));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out
				.println("Input the authorization information (eg: access_token=ACCESS_TOKEN&expires_in=60&openid= OPENID &openkey= OPENKEY ) :");
		Scanner in = new Scanner(System.in);
		String responseData = in.nextLine();
		in.close();

		if (OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuth)) {
			System.out.println("Get Access Token Successfully");
		} else {
			System.out.println("Fail to Get Access Token");
			return;
		}
	}

	public int getYesVoteNum() {
		return count[0];
	}
	public int getNoVoteNum() {
		return count[1];
	}
	public boolean getState() {
		return finish;
	}
	public void pullVote(Route route, String scenicname, String requirement) {
		generatevote test = new generatevote();
		String testvoteid = test.generatevoteid(route, scenicname, requirement);
		 while (true) {
			 init(oAuth);
			 String response = null;
			 String format = "json";
			 TAPI tAPI = new TAPI(oAuth.getOauthVersion());// 根据oAuth配置对应的连接管理器
			 try {
				 response = tAPI.getStatisticsInfo(oAuth, format, "satbuaa",
						 testvoteid);
			 } catch (Exception e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 }
			 if (response != null) {
				 String info;
			
				 JSONObject responseJsonObject;
				 JSONObject dataJsonObject;
			
				 responseJsonObject = JSONObject.fromObject(response);
				 dataJsonObject = (JSONObject) responseJsonObject.get("data");
				 info = dataJsonObject.get("info").toString();
			
				 System.out.println(info + "  ");
			
				 String[] index = info.split("\\{");
			
				 for (int i = 1; i < index.length; i++) {
					 char[] x = index[i].toCharArray();
					 count[x[18]-'1']=x[8]-'0';
			 }
			 if(count[0]+count[1]>=5) {
				 finish = true;
				 break;
			 }
			 try {
				 Thread.sleep(15000);
			 } catch (InterruptedException ie) {
			 }
			 tAPI.shutdownConnection();// 关闭连接管理器
			
			 }
		}
	}
	public static void main(String args[]) {
//		 List<String> newRoute = new ArrayList<String>();
//		 newRoute.add("天安门");
//		 newRoute.add("长城");
//		 newRoute.add("南锣鼓巷");
//				
//		 //构造一条路线route
//		 Scene scenes = new Scene();
//		 List<String> remainScenes = new ArrayList<String>();
//		 for (String a : scenes.getSceneName())
//		 if (!newRoute.contains(a))
//		 remainScenes.add(a);
//				
//		 Route route = new Route(newRoute, 0, 0, 0, remainScenes, scenes);
//		 generatevote test = new generatevote();
//		 String testvoteid = test.generatevoteid(route, "国家大剧院");
//
//		 while (true) {
//		 init(oAuth);
//		
//		 String response = null;
//		 String format = "json";
//		
//		 TAPI tAPI = new TAPI(oAuth.getOauthVersion());// 根据oAuth配置对应的连接管理器
//		
//		 try {
//		 response = tAPI.getStatisticsInfo(oAuth, format, "satbuaa",
//		 testvoteid);
//		 } catch (Exception e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
//		 if (response != null) {
//		 String info;
//		
//		 JSONObject responseJsonObject;
//		 JSONObject dataJsonObject;
//		
//		 responseJsonObject = JSONObject.fromObject(response);
//		 dataJsonObject = (JSONObject) responseJsonObject.get("data");
//		 info = dataJsonObject.get("info").toString();
//		
//		 System.out.println(info + "  ");
//		
//		 String[] index = info.split("\\{");
//		
//		 for (int i = 1; i < index.length; i++) {
//		 char[] x = index[i].toCharArray();
//		 count[x[18]-'0']=x[8]-'0';
//		 System.out.println(x[8]); // count
//		 System.out.println(x[18]); // index
//		 System.out.println(x[8] - '0');
//		 }
//		
//		 // for(int i = 0; i < 5 ;i++)
//		 // System.out.println(count[i]);
//		 try {
//		 Thread.sleep(15000);
//		 } catch (InterruptedException ie) {
//		 }
//		 tAPI.shutdownConnection();// 关闭连接管理器
//		
//		 }
//		 }

//		QHttpClient a = new QHttpClient();
//		try {
//			System.out
//					.println(a
//							.newAccessTokenHttpGet("https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id=801553526&grant_type=refresh_token&refresh_token=d01ff298361be20e4dc10e8e799de85c"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}