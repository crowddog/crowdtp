package models;

import java.util.Arrays;
import java.util.Random;

/*
 * 这个类主要是初始化每个景点的满意度的取值，取值范围是（0,0.01,0.02,....,1）
 */

public class Distribution {
	public double distributionVal[];
	public double meanVal;

	// 初始化，每次都是Beta分布开始
	public Distribution() {
		distributionVal = new double[100];
		for (int j = 0; j < 100; j++) {
			distributionVal[j] = 9 * Math.pow((1 - (double) j / 100), 8);
		}
		meanVal = 0.1;
	}

	// another是另一个分布，signal表示投票的结果，0表示投给了这个分布，1表示投给了另一个分布
	public void renewdistributionVal(Distribution another, int signal) {
		if (signal == 0)
			for (int i = 0; i < 100; i++) {
				distributionVal[i] = distributionVal[i]
						* voteItself(i, another);
				// System.out.println("d[" + i + "]=" + distributionVal[i]);
			}
		else
			for (int i = 0; i < 100; i++)
				distributionVal[i] = distributionVal[i]
						* voteAnother(i, another);
		// renewMeanVal();
	}

	// 投票给了这个分布
	public double voteItself(int q, Distribution another) {
		double sum = 0, a = 0, b = 0;
		double[] anotherdistributionVal = another.getdistributionVal();

		double M = Constants.M;
		double r = Constants.r;
		for (int i = 0; i < q; i++) {
			a = 100 * (anotherdistributionVal[i + 1] - anotherdistributionVal[i]);
			b = (1 - i) * anotherdistributionVal[i] - i
					* anotherdistributionVal[i + 1];
			double temp1 = 0, temp2 = 0;
			if (q - (i + 1) != 0)
				temp1 = Math.pow((double) q / 100 - ((double) i / 100 + 0.01),
						M * r);
			if (q - i != 0)
				temp2 = Math.pow((double) q / 100 - (double) (i / 100), M * r);
			sum += (b * ((double) i / 100 + 0.01))
					/ 2
					+ (a * ((double) i / 100 + 0.01) * ((double) i / 100 + 0.01))
					/ 4
					+ temp1
					* (((a + a * M * r) * ((double) i / 100 + 0.01) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4)
							+ ((2 * b + b * M * r - a * M * q * r / 100) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4) - (2 * b * q
							/ 100 + a * q * q / 10000 + b * M * q * r / 100)
							/ (2 * M * M * r * r + 6 * M * r + 4))
					- ((b * (double) i / 100) / 2
							+ (a * (double) i * (double) i / 10000) / 4 + temp2
							* (((a + a * M * r) * (double) i / 100 * i / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4)
									+ ((2 * b + b * M * r - a * M * q * r / 100)
											* (double) i / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4) - (2
									* b * q / 100 + a * q * q / 10000 + b * M
									* q * r / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4)));

		}

		for (int i = q; i < 99; i++) {
			a = 100 * (anotherdistributionVal[i + 1] - anotherdistributionVal[i]);
			b = (1 - i) * anotherdistributionVal[i] - i
					* anotherdistributionVal[i + 1];
			double temp1 = 0, temp2 = 0;
			if (i + 1 - q != 0)
				temp1 = Math.pow((double) i / 100 + 0.01 - (double) q / 100, M
						* r);
			if (i - q != 0)
				temp2 = Math.pow((double) i / 100 - (double) q / 100, M * r);
			sum += ((double) i / 100 + 0.01)
					/ 2
					- temp1
					* (((a + a * M * r) * Math.pow((double) i / 100 + 0.01, 2))
							/ (2 * M * M * r * r + 6 * M * r + 4)
							+ ((2 * b + b * M * r - a * M * q * r / 100) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4) - (2 * b * q
							/ 100 + a * q * q / 10000 + b * M * q * r / 100)
							/ (2 * M * M + 6 * M * r + 4))
					- ((double) i / 200 - temp2
							* (((a + a * M * r) * Math.pow((double) i / 100, 2))
									/ (2 * M * M * r * r + 6 * M * r + 4)
									+ ((2 * b + b * M * r - a * M * q * r / 100)
											* (double) i / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4) - (2
									* b * q / 100 + a * q * q / 10000 + b * M
									* q * r / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4)));

		}
		return sum;
	}

	public double voteAnother(int q, Distribution another) {
		double sum = 0, a = 0, b = 0;
		double anotherdistributionVal[] = another.getdistributionVal();
		double M = Constants.M;
		double r = Constants.r;
		for (int i = 0; i < q; i++) {
			a = 100 * (anotherdistributionVal[i + 1] - anotherdistributionVal[i]);
			b = (1 - i) * anotherdistributionVal[i] - i
					* anotherdistributionVal[i + 1];
			double temp1 = 0, temp2 = 0;
			if (q - (i + 1) != 0)
				temp1 = Math.pow((double) q / 100 - ((double) i / 100 + 0.01),
						M * r);
			if (q - i != 0)
				temp2 = Math.pow((double) q / 100 - (double) i / 100, M * r);
			sum += (b * ((double) i / 100 + 0.01))
					/ 2
					+ (a * ((double) i / 100 + 0.01) * ((double) i / 100 + 0.01))
					/ 4
					+ temp1
					* (((a + a * M * r) * ((double) i / 100 + 0.01) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4)
							+ ((2 * b + b * M * r - a * M * q * r / 100) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4) - (2 * b * q
							/ 100 + a * q * q / 10000 + b * M * q * r / 100)
							/ (2 * M * M * r * r + 6 * M * r + 4))
					- ((b * (double) i / 100) / 2
							+ (a * (double) i * (double) i / 10000) / 4 + temp2
							* (((a + a * M * r) * (double) i * (double) i / 10000)
									/ (2 * M * M * r * r + 6 * M * r + 4)
									+ ((2 * b + b * M * r - a * M * q * r / 100)
											* (double) i / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4) - (2
									* b * q / 100 + a * q * q / 10000 + b * M
									* q * r / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4)));
		}
		for (int i = q; i < 99; i++) {
			a = 100 * (anotherdistributionVal[i + 1] - anotherdistributionVal[i]);
			b = (1 - i) * anotherdistributionVal[i] - i
					* anotherdistributionVal[i + 1];
			double temp1 = 0, temp2 = 0;
			if (i + 1 - q != 0)
				temp1 = Math.pow((double) i / 100 + 0.01 - (double) q / 100, M
						* r);
			if (i - q != 0)
				temp2 = Math.pow((double) i / 100 - (double) q / 100, M * r);
			sum += ((double) i / 100 + 0.01)
					/ 2
					- temp1
					* (((a + a * M * r) * Math.pow((double) i / 100 + 0.01, 2))
							/ (2 * M * M * r * r + 6 * M * r + 4)
							+ ((2 * b + b * M * r - a * M * q * r / 100) * ((double) i / 100 + 0.01))
							/ (2 * M * M * r * r + 6 * M * r + 4) - (2 * b * q
							/ 100 + a * q * q / 10000 + b * M * q * r / 100)
							/ (2 * M * M * r * r + 6 * M * r + 4))
					- ((double) i / 200 - temp2
							* (((a + a * M * r) * Math.pow((double) i / 100, 2))
									/ (2 * M * M * r * r + 6 * M * r + 4)
									+ ((2 * b + b * M * r - a * M * q * r / 100)
											* (double) i / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4) - (2
									* b * q / 100 + a * q * q / 10000 + b * M
									* q * r / 100)
									/ (2 * M * M * r * r + 6 * M * r + 4)));
		}
		return sum;
	}

	public double[] getdistributionVal() {
		return distributionVal;
	}

	public void renewMeanVal() {
		double sum = 0, a = 0, b = 0;
		for (int i = 0; i < 99; i++) {
			a = 100 * (distributionVal[i + 1] - distributionVal[i]);
			b = 101 * distributionVal[i] - 100 * distributionVal[i];
			sum += (double) 1
					/ 3
					* a
					* Math.pow(((double) (i + 1)) / 100, 3)
					+ (double) 1
					/ 2
					* b
					* Math.pow(((double) (i + 1)) / 100, 2)
					- ((double) 1 / 3 * a * Math.pow(((double) i) / 100, 3) + (double) 1
							/ 2 * b * Math.pow(((double) i) / 100, 2));
		}

		meanVal = sum;
	}

	public double getmeanVal() {
		return meanVal;
	}

	public static void main(String args[]) {
		Random rand = new Random();
		Distribution cur_distribution = new Distribution();
		Distribution next_distribution = new Distribution();
		for (int i = 0; i < 5; i++)
			if (3 % 2 == 0) {
				cur_distribution.renewdistributionVal(next_distribution, 0);
				next_distribution.renewdistributionVal(cur_distribution, 1);
			} else {
				cur_distribution.renewdistributionVal(next_distribution, 1);
				next_distribution.renewdistributionVal(cur_distribution, 0);
			}
		cur_distribution.renewMeanVal();
		next_distribution.renewMeanVal();
		System.out.println("current meanVal=" + cur_distribution.getmeanVal());
		System.out.println("next meanVal=" + next_distribution.getmeanVal());
		System.out.println("ratio=" + next_distribution.getmeanVal()
				/ cur_distribution.getmeanVal());

	}

}
