package transpermatrix;

import java.io.*;

import java.sql.*;
import java.util.*;
import java.lang.*;

import models.Constants;

import dataAnalyser.DataAnalyser;

public class transpermatrix {

	RoadInfo[] roadinfo = new RoadInfo[100];

	class RoadInfo {
		public String roadStart;
		public int roadStartNumber;
		public String roadEnd;
		public int roadEndNumber;
		public String[][] roadDetail;
		public double roadValue;
		public int roadcount;

	}

	public boolean judgeStartRoadEqual(String s) {
		for (int i = 0; roadinfo[i] != null; i++) {
			if (roadinfo[i].roadStart.equals(s))
				return true;
		}
		return false;
	}

	public boolean judgeStartEndEqual(String s) {
		for (int i = 0; roadinfo[i] != null; i++) {
			if (roadinfo[i].roadEnd.equals(s))
				return true;
		}
		return false;
	}

	public int judgeRoadEqual(String[] s) {
		for (int i = 0; roadinfo[i] != null; i++) {
			if (roadinfo[i].roadStart.equals(s[0])
					&& roadinfo[i].roadEnd.equals(s[1]))
				return i;
		}
		return -1;

	}

	/*
	 * public void getRoadValues() { //String road = null;
	 * 
	 * BufferedReader reader = null; int roadnum = 0; try {
	 * System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�"); reader = new
	 * BufferedReader(new InputStreamReader(new
	 * FileInputStream("roadsystem.rd"),"UTF-8")); String tempString = null; int
	 * line = 1; // һ�ζ���һ�У�ֱ������nullΪ�ļ����� while ((tempString =
	 * reader.readLine()) != null) { // ��ʾ�к�,ÿ�η���һ��
	 * System.out.println("line " + line + ": " + tempString); String[] ss =
	 * tempString.split(";");
	 * 
	 * DataAnalyser analyser = new DataAnalyser();
	 * if(roadnum>0&&(this.judgeStartRoadEqual
	 * (ss[0]))&&(this.judgeStartEndEqual(ss[1]))){ String[] otherroad =
	 * ss[2].split(","); double[] index = new double[20];
	 * 
	 * double valueSum = 0; double valueAverage = 0; double valueVariance = 0;
	 * double roadvalue = 0;
	 * 
	 * for(int i = 0;i < otherroad.length;i++){ index[i] =
	 * analyser.returnValues(otherroad[i]); //index[i] = 1; valueSum = valueSum
	 * + index[i]; } valueAverage = valueSum / otherroad.length; double
	 * squareSum = 0; for(int i = 0;i < otherroad.length;i++) squareSum =
	 * squareSum + (valueAverage - index[i])*(valueAverage - index[i]);
	 * valueVariance = Math.sqrt(squareSum / otherroad.length); //���÷��� or
	 * ��׼�� roadvalue = (valueAverage + valueVariance) / 2; if(roadvalue <
	 * roadinfo[roadnum-1].roadValue){ roadinfo[roadnum-1].roadValue =
	 * roadvalue; roadinfo[roadnum-1].roadDetail = otherroad; }
	 * 
	 * } else{ roadinfo[roadnum] = new RoadInfo(); roadinfo[roadnum].roadStart =
	 * ss[0]; roadinfo[roadnum].roadEnd = ss[1]; roadinfo[roadnum].roadDetail =
	 * ss[2].split(","); System.out.println(roadinfo[roadnum].roadStart);
	 * System.out.println(roadinfo[roadnum].roadEnd); double[] index = new
	 * double[20]; double valueSum = 0; double valueAverage = 0; double
	 * valueVariance = 0;
	 * 
	 * for(int i = 0;i < roadinfo[roadnum].roadDetail.length;i++){ index[i] =
	 * analyser.returnValues(roadinfo[roadnum].roadDetail[i]); //index[i] = 1;
	 * valueSum = valueSum + index[i]; } valueAverage = valueSum /
	 * roadinfo[roadnum].roadDetail.length;
	 * 
	 * double squareSum = 0; for(int i = 0;i <
	 * roadinfo[roadnum].roadDetail.length;i++) squareSum = squareSum +
	 * (valueAverage - index[i])*(valueAverage - index[i]); valueVariance =
	 * Math.sqrt(squareSum / roadinfo[roadnum].roadDetail.length); //���÷��� or
	 * ��׼��
	 * 
	 * roadinfo[roadnum].roadValue = (valueAverage + valueVariance) / 2;
	 * 
	 * System.out.println(roadinfo[roadnum].roadValue);
	 * 
	 * roadnum++; }
	 * 
	 * line++; } reader.close(); } catch (IOException e) { e.printStackTrace();
	 * } finally { if (reader != null) { try { reader.close(); } catch
	 * (IOException e1) { } } } }
	 */

	public double[][] generateMatrix() {

		BufferedReader reader = null;
		int roadnum = 0;

		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("roadsystem.rd"), "UTF-8"));
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�,ÿ�η���һ��
				System.out.println("line " + line + ": " + tempString);
				String[] ss = tempString.split(";");
				int index;
				// DataAnalyser analyser = new DataAnalyser();
				if (roadnum > 0 && ((index = this.judgeRoadEqual(ss)) != -1)) {
					// String[] otherroad = ss[2].split(",");
					// double[] index = new double[20];

					roadinfo[index].roadcount++;
					roadinfo[index].roadDetail[roadinfo[index].roadcount] = ss[2]
							.split(",");

				} else {
					roadinfo[roadnum] = new RoadInfo();
					roadinfo[roadnum].roadDetail = new String[10][100];
					roadinfo[roadnum].roadStart = ss[0];
					roadinfo[roadnum].roadEnd = ss[1];
					roadinfo[roadnum].roadDetail[0] = ss[2].split(",");
					System.out.println(roadinfo[roadnum].roadStart);
					System.out.println(roadinfo[roadnum].roadEnd);

					roadinfo[roadnum].roadcount = 0;

					roadnum++;
				}

				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		// /int roadall = roadinfo.length;
		String[] scenicname = new String[50];
		int counter = 0;
		double[][] transpermatrix = null;

		try {
			Class.forName(Constants.drivername);
			Connection conn = DriverManager.getConnection(Constants.url, Constants.user, Constants.password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select name from scenic");
			while (rs.next()) {
				scenicname[counter] = rs.getString("name");
				System.out.println(scenicname[counter] + "" + counter);
				for (int j = 0; roadinfo[j] != null; j++) {
					if (roadinfo[j].roadStart.equals(scenicname[counter]))
						roadinfo[j].roadStartNumber = counter;
					else if (roadinfo[j].roadEnd.equals(scenicname[counter]))
						roadinfo[j].roadEndNumber = counter;
				}
				counter++;
			}

			transpermatrix = new double[counter][counter];
			int[][] roadNumber = new int[counter][20];

			// ��ʼ��
			for (int i = 0; i < counter; i++)
				for (int j = 0; j < 20; j++)
					roadNumber[i][j] = -1;

			for (int i = 0; i < counter; i++)
				for (int j = 0; j < counter; j++)
					transpermatrix[i][j] = 0;

			for (int i = 0; i < counter; i++) {
				int index = 0;
				for (int j = 0; roadinfo[j] != null; j++) {
					if (roadinfo[j].roadStartNumber == i) {
						roadNumber[i][index] = j;
						index++;
					}
				}

				if (roadNumber[i][0] >= 0) {
					double s = 0;
					DataAnalyser analyser = new DataAnalyser();
					if (roadNumber[i][1] >= 0) {
						for (int j = 0; roadNumber[i][j] >= 0; j++) {

							roadinfo[roadNumber[i][j]].roadValue = 0;

							if (roadinfo[roadNumber[i][j]].roadcount > 0) {
								for (int x = 0; x <= roadinfo[roadNumber[i][j]].roadcount; x++) {
									double[] roadValueIndex = new double[20];

									double valueSum = 0;
									double valueAverage = 0;
									double valueVariance = 0;

									for (int y = 0; y < roadinfo[roadNumber[i][j]].roadDetail[x].length; y++) {
										roadValueIndex[y] = analyser
												.returnValues(roadinfo[roadNumber[i][j]].roadDetail[x][y]);
										// roadValueIndex[y] = 1;
										valueSum = valueSum + roadValueIndex[y];
									}
									valueAverage = valueSum
											/ roadinfo[roadNumber[i][j]].roadDetail[x].length;

									double squareSum = 0;
									for (int y = 0; y < roadinfo[roadNumber[i][j]].roadDetail[x].length; y++)
										squareSum = squareSum
												+ (valueAverage - roadValueIndex[y])
												* (valueAverage - roadValueIndex[y]);
									valueVariance = Math
											.sqrt(squareSum
													/ roadinfo[roadNumber[i][j]].roadDetail[x].length); // ���÷���
																										// or
																										// ��׼��

									if (x == 0
											|| roadinfo[roadNumber[i][j]].roadValue > ((valueAverage + valueVariance) / 2))
										roadinfo[roadNumber[i][j]].roadValue = (valueAverage + valueVariance) / 2;

								}

							} else {
								double[] roadValueIndex = new double[20];

								double valueSum = 0;
								double valueAverage = 0;
								double valueVariance = 0;

								for (int x = 0; x < roadinfo[roadNumber[i][j]].roadDetail[0].length; x++) {
									roadValueIndex[x] = analyser
											.returnValues(roadinfo[roadNumber[i][j]].roadDetail[0][x]);
									// roadValueIndex[x] = 1;
									valueSum = valueSum + roadValueIndex[x];
								}
								valueAverage = valueSum
										/ roadinfo[roadNumber[i][j]].roadDetail[0].length;

								double squareSum = 0;
								for (int x = 0; x < roadinfo[roadNumber[i][j]].roadDetail[0].length; x++)
									squareSum = squareSum
											+ (valueAverage - roadValueIndex[x])
											* (valueAverage - roadValueIndex[x]);
								valueVariance = Math
										.sqrt(squareSum
												/ roadinfo[roadNumber[i][j]].roadDetail[0].length); // ���÷���
																									// or
																									// ��׼��

								roadinfo[roadNumber[i][j]].roadValue = (valueAverage + valueVariance) / 2;

							}

							s = s + 1 / roadinfo[roadNumber[i][j]].roadValue;

						}
						for (int j = 0; roadNumber[i][j] >= 0; j++) {
							if (roadinfo[roadNumber[i][j]].roadValue == 0)
								System.out.println(i + " " + j + "error!");

							transpermatrix[roadinfo[roadNumber[i][j]].roadStartNumber][roadinfo[roadNumber[i][j]].roadEndNumber] = 1
									/ s
									* 1
									/ roadinfo[roadNumber[i][j]].roadValue;

						}
					} else
						transpermatrix[roadinfo[roadNumber[i][0]].roadStartNumber][roadinfo[roadNumber[i][0]].roadEndNumber] = 1;
				}

			}

			for (int i = 0; i < transpermatrix.length; i++) {
				for (int j = 0; j < transpermatrix.length; j++)
					System.out.print(transpermatrix[i][j] + "\t");
				System.out.println(" ");
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transpermatrix;
	}

	public static void main(String[] args) {
		transpermatrix a = new transpermatrix();
		// a.getRoadValues();
		a.generateMatrix();
	}

}
