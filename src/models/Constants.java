package models;

public class Constants {
	public static int transRatio = 1000;// 交通状况在score中占得比重
	public static double maxVoteRatio = 5.747994049499416E48/1E52;// 所有投票都给了next，对路线的改进比例
	public static int topk =2;// 潜力分最高的k条路线，优先放出投票
	public static int userRequireSceneNum = 0;// 用户请求的景点数目
	public static double M = 0.5;// 问题难度系数
	public static double r = 0.6;// 用户的准确度系数
	public static String drivername = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost/travel";
	public static String user = "root";
	public static String password = "sdp123";
	public static int questionnum=0;
}
