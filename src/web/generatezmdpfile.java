package web;
/**
 * 本java文件用于测试算法所用
 * 
 */

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import entities.Route;

import transpermatrix.transpermatrix;
import vote.generatevote;

public class generatezmdpfile {

	private String drivername 	= "com.mysql.jdbc.Driver";
	private String url 			= "jdbc:mysql://localhost/travel"; 
	private String user 		= "root";
	private String password		= "1234";
	
	private double[][] matrixO = null;
	
	public void getvoteid() {
		String[] scenicname = new String[50];
		String[] voteid = new String[50];
		int counter = 0;
		
		try {
        	Class.forName(drivername);
        	Connection conn = DriverManager.getConnection(url, user, password);
        	if(!conn.isClosed()) 
			System.out.println("Succeeded connecting to the Database!");
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery("select voteid from scenic");
        	while(rs.next()){
				//scenicname[counter] = rs.getString("name");
				voteid[counter] = rs.getString("voteid");
				System.out.println(voteid[counter]+"123");
				counter++;	
			}  
        } catch (Exception e) {
        	e.printStackTrace();
	    }
	}
	
	public File generatezmdp(String startpoint){
		File file = new File("test.pomdp");
		try {
			Class.forName(drivername);
        	Connection conn = DriverManager.getConnection(url, user, password);
        	if(!conn.isClosed()) 
			System.out.println("Succeeded connecting to the Database!");
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery("select id from scenic");
			FileWriter out = new FileWriter(file);
			out.write("discount: 0.9\n");
			out.write("values: reward\n");
			out.write("states: ");
			while(rs.next()){
				String id = rs.getString("id");
				out.write("s"+id + " ");
			}
			out.write("\n");
			out.write("actions:");
			//a1 自然风景
			//a2 娱乐
			//a3 文化
			//a4 都市
			//a5 休闲 
			String[] scenictype = {"自然风景","娱乐","文化","都市","休闲"};
			
			for(int i = 1 ;i < 6;i++)
				out.write("a"+i+" ");
			out.write("\n");
			out.write("obersvations: ");
			for(int i = 1;i < 6;i++)
				out.write("p"+i+" ");
			out.write("\n\n");
			out.write("start:\n");
			
			//String startpoint = "东单";
			rs = stmt.executeQuery("select name from scenic");
			while(rs.next()){
				String name = rs.getString("name");
				if(name.equals(startpoint))
					out.write("1 ");
				else
					out.write("0 ");
			}
			out.write("\n\n");
			
			
			
			//生成O矩阵
			matrixO = new transpermatrix().generateMatrix();
			
			double[][] matrixT = new double[matrixO.length][matrixO.length];
			
			for(int i = 0; i < matrixO.length;i++)
				for(int j = 0;j < matrixO.length;j++)
					matrixT[i][j] = matrixO[i][j];
				
			//打印T矩阵

//			SelectDbData connect = new SelectDbData();
			List<String> index = null;
			String name = null;
			String voteid = null;
			
			double[][][] record = new double[2][matrixT.length][5];
			for(int i = 0;i < matrixT.length;i++){
				int t = i + 1;
				rs = stmt.executeQuery("select * from scenic where id = '"+ t +"'");
				rs.next();
				name  = rs.getString("name");
				
//				index = connect.keywordRank(name);
				//云游天下部分为 0 
				voteid = rs.getString("voteid");
				System.out.println(t + " " + name + "  "+ voteid);
				
				for(int j = 0;j < matrixT.length;j++)
					for(int k = 0 ;k < 5;k++)
						record[0][j][k] = 0.5;
				
				if(index != null){
					for(int j = 0;j < 5;j++){
						for(int k = 0; k < 5;k++){
							if(index.get(k).equals(scenictype[j]))
									record[0][i][j] = 5 - k;
						}
					}
				}
				//微博部分为1
				int[] count = new int[3];
				for(int j =0;j < 5;j++)
					record[1][i][j] = count[j];				
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
			
			for(int i = 0;i < 5;i++){
				int t = i + 1;
				out.write("T:a"+ t +"\n");
				//String id = t +"";
				for(int j = 0;j < matrixT.length;j++){
					for(int k = 0; k < matrixT.length;k++){
						if(matrixT[j][k] > 0){
							//云游天下部分
							matrixT[j][k] = record[0][k][i];
							matrixT[j][k] += record[1][k][i];
						}						
					}
					double countrow = 0;
					for(int k = 0;k < matrixT.length;k++)
						if(matrixT[j][k] > 0)
							countrow += matrixT[j][k];
					for(int k = 0;k < matrixT.length;k++)
						if(matrixT[j][k] > 0)
							matrixT[j][k] = matrixT[j][k] / countrow ;
				}
				for(int j = 0;j < matrixT.length;j++){
					for(int k = 0;k < matrixT.length;k++)
						out.write(matrixT[j][k]+" ");
					out.write("\n");
				}
				out.write("\n");
			}
			
			/*生成O矩阵
			*************************
			*************************
			*************************/
			for(int i = 0; i < 5;i++){
				int t = i + 1;
				out.write("O:a"+ t +"\n");
				for(int j = 0 ;j < matrixO.length;j++){
					for(int k = 0;k < matrixO.length;k++)
						out.write(matrixO[j][k] + " ");
					out.write("\n");
				}
				out.write("\n");
			}
			
			rs = stmt.executeQuery("select reward from scenic");
			String[] reward = new String[41];
			int x = 0;
			while(rs.next()){
				reward[x] = rs.getString("reward");
				x++;
			}
			
			for(int i = 1; i < 6;i++){
				for(int j = 1;j <= matrixT.length;j++){
					out.write("R:a"+i+"\t "+"s"+j+"\t : * : *"+ reward[j-1]);
					out.write("\n");
				}
				out.write("\n");
			}
			
			
			out.close();
			System.out.println("Success!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return file;
	}
	
	public static void main(String[] args){
//		new generatezmdpfile().getvoteid();
		new generatezmdpfile().generatezmdp("天安门广场");
	}

}
