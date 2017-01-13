package web;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;



import transpermatrix.transpermatrix;
import vote.generatevote;

public class TestGetVotes {

	private String drivername = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost/travel";
	private String user = "root";
	private String password = "1234";

	public double[][] matrixO = null;
	public double[][] matrixT = null;
	public static int[] availablescenicindex;// 可达景点在scenic表中的序号
	public String voteid = null;// 本次投票的id，在获取得票数时使用
	public static int action_num = 0;// 行为数目
	public static int observation_num = 0;
	public static int state_num = 0;// 状态数目
	public static String[] availablescenicname;// 交通状况好的景点的名字
	public static int availablescenicnum = 0;// 交通状况好的景点的数目
	public static int startscenicindex = 0;// 起始景点在scenic表中的序号
	public static int votenum=0;//记录投票的次数，每次将reward函数的花费乘上对应的比重

	public File generatezmdp_init(String startscenicname) {
		File file = new File("test.pomdp");
		try {
			Class.forName(drivername);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select name from scenic");
			int counter = 0;
			String[] scenicname = new String[50];
			// 获取起始景点在scenic表中的序号
			while (rs.next()) {
				if ((scenicname[counter] = rs.getString("name"))
						.equals(startscenicname))
					startscenicindex = counter;
				counter++;
			}
			counter = 0;
			// 生成T矩阵
			matrixT = new transpermatrix().generateMatrix();
			// 统计交通状况好的景点的数目
			for (int i = 0; i < matrixT.length; i++)
				if (i != startscenicindex && matrixT[startscenicindex][i] > 0)
					availablescenicnum++;
			availablescenicindex = new int[availablescenicnum];
			availablescenicname = new String[availablescenicnum];
			// 保存交通状况好的景点的名字和在scenic表中的序号
			counter = 0;
			for (int i = 0; i < matrixT.length; i++)
				if (i != startscenicindex && matrixT[startscenicindex][i] > 0) {
					availablescenicindex[counter] = i;
					availablescenicname[counter++] = scenicname[i];
				}

			FileWriter out = new FileWriter(file);
			out.write("discount: 0.9\n");
			out.write("values: reward\n");
			out.write("states: ");
			out.write("s_pre");
			for (int i = 0; i < availablescenicnum; i++)
				out.write("s_" + availablescenicindex[i] + " ");
			out.write("\n");
			out.write("actions:");
			out.write("b ");
			for (int i = 0; i < availablescenicnum; i++)
				out.write("a_" + availablescenicindex[i] + " ");
			out.write("a_end");
			out.write("\n");
			out.write("obersvations: ");
			for (int i = 1; i < 6; i++)
				out.write("stf");// 意思是满意度
			out.write("\n\n");
			out.write("start:\n");
			out.write("1 ");
			for (int i = 0; i < availablescenicnum; i++)
				out.write("0 ");
			out.write("\n");
			out.write("T:b\n");
			for (int i = 0; i < availablescenicnum + 1; i++)
				for (int j = 0; j < availablescenicnum + 1; j++) {
					if (i == j)
						out.write("1 ");
					else
						out.write("0 ");
					out.write("\n");
				}
			out.write("\n");
			for (int i = 0; i < availablescenicnum; i++) {
				out.write("T:a_" + availablescenicindex[i] + "\n");
				for (int j = 0; j < availablescenicnum + 1; j++) {
					for (int k = 0; k < availablescenicnum + 1; k++) {
						if (j == 0 && i == (k + 1))
							out.write(matrixT[startscenicindex][availablescenicindex[i]]
											+ " ");
						else
							out.write("0 ");
					}
					out.write("\n");
				}
			}
			out.write("T:a_end\n");
			for (int i = 0; i < availablescenicnum + 1; i++) {
				for (int j = 0; j < availablescenicnum + 1; j++)
					out.write("0 ");
				out.write("\n");
			}
			out.write("\n");
			out.write("O:b\n");
			// 初始化状态的概率
			Distribution.init(availablescenicnum + 1);
			for (int i = 0; i < availablescenicnum; i++)
				out.write("O:a_" + availablescenicindex[i] + "\n\n");
			out.write("O:a_end\n\n");
            
			out.write("\n");

			out.close();
			System.out.println("Success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	// 得到一次投票后更新POMDP文件的O矩阵
	public void generatezmdp_renew(int vote_scenic) {
		// 生成O矩阵
		

	}

	public static void main(String args[]) {

//	    new TestGetVotes().generatezmdp("天安门广场");
	}

}
