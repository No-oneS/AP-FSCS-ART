package Mythod;

/*
 * 实现的是 在随机对一个子区域时候 实现动荡
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import real_program.main;







public class RT {
	ArrayList<Dimension> dim_list;// 负责的是维度的范围值
	ArrayList<Double> points = new ArrayList<>(); // 针对的是生成的失效区域的点坐标
	ArrayList<point> candidate;
	ArrayList<point> Selected;
	double totalsize; // 整个输入域的面积
	point testcase;
	point firstcase;
	Region firstRegion = null;
	main m = new main();

	public RT(ArrayList<Dimension> dim_list) {
		/*
		 * 求输入域面积，这是共存在步骤
		 */
		totalsize = dim_list.get(0).getRange();
		for (int i = 1; i < dim_list.size(); i++) {
			totalsize = totalsize * dim_list.get(i).getRange();
		}
		this.dim_list = dim_list;
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
	}

	public int run() {

		// System.out.println(points);
		// System.out.println(points.get(0)+" "+(points.get(1)+fail_length));
		// System.out.println((points.get(0)+fail_length)+" "+points.get(1));
		// System.out.println((points.get(0)+fail_length)+"
		// "+(points.get(1)+fail_length));

		int count = 0;
		boolean flag=true;
		while(flag){
			testcase=new point();
			for(int i=0;i<dim_list.size();i++){
				testcase.point.add(dim_list.get(i).getMin()+dim_list.get(i).getRange()*new Random().nextDouble());
			}
			count++;
			if (isCorrect(testcase)) {
				flag=false;
			}
		}
		
			return count;
		}

	
	private boolean isCorrect(point testcase) {
		// TODO 自动生成的方法存根

		// 测试用例落在了失效域，则返回false
		return m.getResult("el2", testcase.point.get(0),testcase.point.get(1),testcase.point.get(2),testcase.point.get(3));
	//		 return m.getResult("sncndn", testcase.point.get(0), testcase.point.get(1));
			
	//	return m.getResult("plgndr", testcase.point.get(0),testcase.point.get(1),testcase.point.get(2));
	}

	static double sqrt(double d, double i) {
		i = 1 / i;

		return Math.pow(d, i);
	}

	public static void main(String args[]) {
		int times = 3000;
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试a用例落在失效域的使用的测试用例的个数
		double failrate = 0.000690;
		ArrayList<Integer> result = new ArrayList<>();
		ArrayList<Long> Time=new ArrayList<>();
		Dimension x = new Dimension(0, 250);
		Dimension y = new Dimension(0,250);
		Dimension z = new Dimension(0,250);
		Dimension u = new Dimension(0,250);
		ArrayList<Dimension> list = new ArrayList<>();
		long  total=0;
		list.add(x);
     	list.add(y);
	    list.add(z);
		list.add(u);
		// for(int i=0;i<2;i++){
		// Dimension a=new Dimension(0, 1);
		// list.add(a);
		// }
		// public FSCS_ART_Single_Sequare(ArrayList<Dimension> dim_list,
		// double failrate, int fail_number, boolean prodomain,double r)
		
		for (int i = 1; i <= times; i++) {
			long startTime = System.currentTimeMillis();
			temp = new RT(list).run();
			long endTime = System.currentTimeMillis();
			result.add(temp);
			System.out.println("第" + i + "次试验F-measure:" + temp);
			Time.add(endTime-startTime);
			sums += temp;
			total+=endTime-startTime;
		}
		
		System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数
		System.out.println("Time: " + (total) / (double) times);// 测试所使用的时间
		java.io.File file2 = new java.io.File("D:/data/F_measure.txt");
		try {
			if (!file2.exists()) {
				file2.createNewFile();
			}
			FileWriter stream = new FileWriter(file2);
			// stream.write("sdfdsfdsfds");
			for (int j = 0; j < result.size(); j++) {
				stream.write(result.get(j) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		file2 = new java.io.File("D:/data/Time.txt");
		try {
			if (!file2.exists()) {
				file2.createNewFile();
			}
			FileWriter stream = new FileWriter(file2);
			// stream.write("sdfdsfdsfds");
			for (int j = 0; j < Time.size(); j++) {
				stream.write(Time.get(j) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}


	
	
	
}
