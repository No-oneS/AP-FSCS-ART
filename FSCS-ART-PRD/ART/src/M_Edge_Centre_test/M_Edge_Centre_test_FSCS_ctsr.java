package M_Edge_Centre_test;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Common.Dimension;
import Common.StripParameters;
import Common.Testcase;
import Common.point;
//import FSCS.FSCS_dt.ThreadWithCallback;

import java.util.concurrent.Callable;
import ARTreal.real_Parameters;
import Common.FaultZone_Strip;
import real_faulty_programs.*;
import Fault_10d.*;
import Fault_8d.*;
import Fault_6d.*;
import Fault_5d.*;

public class M_Edge_Centre_test_FSCS_ctsr {
	ArrayList<Dimension> dim_list=new ArrayList<>();;// 当前的候选测试点
	ArrayList<Double> mut_area_r; // 用于存储的是多个面积不同时的每个失效的面积的r
	ArrayList<Double> points = new ArrayList<>(); // 针对的是生成的失效区域的点坐标
	ArrayList<ArrayList<Double>> mutiple; // 存放的是 多个失效域的坐标点
	ArrayList<Testcase> candidate=new ArrayList<>();
	ArrayList<Testcase> Selected;
	double totalsize; // 整个输入域的面积
	double fail_rate; // 失效率
	double fail_length; // 失效域的边长
	int fail_number;// 失效域的个数
	boolean prodomain;// 多个失效区域是否有主区域
	double fail_totalsize;
	int f_k;
	ArrayList<Double> random_number = new ArrayList<>();// 存放随机生成数；
	Double sum = 0.0; // 用于多个面积不同的
	Double r = 0.0;// 用于多个面积不同的面积的R参数
	FaultZone_Strip fzb;	//斜条形状缺陷
	double fzb_rate = 0.9;//	
//	el2 real = new el2();			// 初始化真实程序类	
	real_el2 real = new real_el2();			// 初始化真实程序类
	
	M_Edge_Centre Edge_Centre;		//计算M_Edge_Centre的类
	
	int Edge_Centre_center = 1;
	
	int Edge_Centre_edge = 1;
	
	int[] result_three =new int[3];

	public M_Edge_Centre_test_FSCS_ctsr(double[][]  bd, double failrate, int fail_number, boolean prodomain,
			double r,int f_k) {
		for (int i = 0; i < bd.length; i++) {
//			System.out.println("bd["+ i +"][0] = "+bd[i][0]+" bd["+ i +"][1] = "+bd[i][1]);
			dim_list.add(new Dimension(bd[i][0], bd[i][1]));
		}
		/*                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
		 * 求输入域面积，这是共存在步骤
		 */
		
		totalsize = dim_list.get(0).getRange();
		for (int i = 1; i < dim_list.size(); i++) {
			totalsize = totalsize * dim_list.get(i).getRange();
		}
		this.dim_list = dim_list;
		this.fail_rate = failrate;
		this.f_k=f_k;
		this.fail_totalsize = failrate * totalsize;
		this.fail_number = fail_number;
		this.prodomain = prodomain;
		this.r = r;
		
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
		if (fail_number == 1) {
			fail_length = sqrt(fail_totalsize, dim_list.size());}
		 else if (prodomain) { // 多个面积不同的正方形
			mut_area_r = new ArrayList<>();
			for (int i = 0; i < fail_number - 1; i++) {
				random_number.add(Math.random());
				sum = sum + random_number.get(i);
			}
		} else if (fail_number >1) { // 多个面积相同的正方形
			fail_length = sqrt(fail_totalsize / fail_number, dim_list.size());
		}
//		System.out.println("总面积为" + totalsize + " fail_length:" + fail_length);
		
		//Strip型缺陷需要把bd改成int型
//		fzb = new FaultZone_Strip(bd, (float)failrate, fzb_rate);
//		System.out.println("bd2 =" + bd.length+"\tfailerate ="+failrate);
		
		Edge_Centre = new M_Edge_Centre(dim_list);		//初始化M_Edge_Centre类

	}

	public int[] run() {
		// 根据fail_number，生成不同缺陷区域，若为1，则表示block，大于1则表示point，若为负数则表示strip，strip时记得更新fzb
		// 如为负数，在isCorrect(testcase)方法中专门做处理
//		System.out.println("fail_number =" + fail_number+"\t prodomain ="+prodomain);
//		if (fail_number == 1) {
//			// 随机生成失效区域，考虑到边界情况，最大值只能是1-fail_rate
//			for (int i = 0; i < dim_list.size(); i++) {
//				double temp = dim_list.get(i).getMin() + ((dim_list.get(i).getRange()-fail_length) * new Random().nextDouble());
//				points.add(temp);
//			}
////			System.out.println("失效的坐标点" + points);		
//		}else if (prodomain) {
//			mutiple = new ArrayList<>(); // 存放多个坐标点
//			ArrayList<Double> templist = new ArrayList<>();
//			double first_r=sqrt( random_number.get(0) / sum * (1 - this.r) * fail_totalsize, dim_list.size());
//			for (int j = 0; j < dim_list.size(); j++) {
//				double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-first_r) * new Random().nextDouble());
//				templist.add(temp);
//			}
//			mutiple.add(templist);
//			mut_area_r.add(first_r);
//			for (int i = 1; i < fail_number - 1; i++) {
//				templist = new ArrayList<>();
//				Double area = random_number.get(i) / sum * (1 - this.r) * fail_totalsize;
//				double r = sqrt(area, dim_list.size());
//				mut_area_r.add(r);
//				// System.out.println("此时的面积为："+area+"此时的半径"+r);
//				for (int j = 0; j < dim_list.size(); j++) {
//					double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-r) * new Random().nextDouble());
//					templist.add(temp);
//				}
//				while(fail2(templist)){
//					templist=new ArrayList<>();
//					for (int j = 0; j < dim_list.size(); j++) {
//						double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-r) * new Random().nextDouble());
//						templist.add(temp);
//					}
//				}
//				mutiple.add(templist);
//			}
//			// System.out.println("前面的坐标点:"+mutiple);
//			templist = new ArrayList<>();
//			for (int j = 0; j < dim_list.size(); j++) {
//				double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-sqrt(r * fail_totalsize, dim_list.size())) * new Random().nextDouble());
//				templist.add(temp);
//			}while(fail2(templist)){
//				templist=new ArrayList<>();
//				for (int j = 0; j < dim_list.size(); j++) {
//					double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-sqrt(r * fail_totalsize, dim_list.size())) * new Random().nextDouble());
//					templist.add(temp);
//				}
//			}
//			mutiple.add(templist);
//			mut_area_r.add(sqrt(r * fail_totalsize, dim_list.size()));
//			// System.out.println("坐标点"+mutiple);
//			// System.out.println("半径"+mut_area_r);
//		}else if (fail_number >1) {
//			// System.out.println("此时的为多个面积相同的");
//			mutiple = new ArrayList<>(); // 存放多个坐标点
//			ArrayList<Double> templist = new ArrayList<>();
//			for (int j = 0; j < dim_list.size(); j++) {
//				double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-fail_length) * new Random().nextDouble());		
//				templist.add(temp);
//			}
//			mutiple.add(templist);
//	//		System.out.println("第一个点"+mutiple);
//			for (int i = 0; i < fail_number-1; i++) {
//				templist.clear();
//				for (int j = 0; j < dim_list.size(); j++) {
//					double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-fail_length) * new Random().nextDouble());	
//					templist.add(temp);
//				}
//	//			System.out.println("for循环中"+templist);
//				while (fail1(templist)) {
//					templist=new ArrayList<>();
//					for (int j = 0; j < dim_list.size(); j++) {
//						double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange()-fail_length) * new Random().nextDouble());	
//						templist.add(temp);
//					}
//				}
//				mutiple.add(templist);
//			}
//			// System.out.println("失效的坐标点" + mutiple);
//		}
		int count = 0;
		Testcase testcase = new Testcase();
		for (int i = 0; i < dim_list.size(); i++) {
			testcase.list.add(dim_list.get(i).getMin() + ((dim_list.get(i).getRange()) * new Random().nextDouble()));
		}
		// System.out.println("生成的测试用例为" + testcase.list);
		// isCorrect(testcase);
		Selected=new ArrayList<>();
		candidate=new ArrayList<Testcase>();
		for(int i=0;i<5;i++){
			Testcase temp=new Testcase();
			for (int j = 0; j < dim_list.size(); j++) {
				temp.list.add(dim_list.get(j).getMin() + ((dim_list.get(j).getRange()) * new Random().nextDouble()));
			}
			candidate.add(temp);
		}
		for(int i=0;i<5;i++){
			candidate.get(i).minDistance=Euclidean_Distance(candidate.get(i), testcase);
		}
		while(true){
			if(count <999){				
				Selected.add(testcase);	
				count++;
				for(int i=f_k;i<10;i++){
					Testcase temp=new Testcase();
					for (int j = 0; j < dim_list.size(); j++) {
						temp.list.add(dim_list.get(j).getMin() + ((dim_list.get(j).getRange()) * new Random().nextDouble()));
					}
					candidate.add(temp);
				}
				Best_candidate(Selected, candidate);			
				sort(candidate);	
				testcase=candidate.get(0);
				candidate.remove(0);	
				update(candidate, testcase);	
				sort(candidate);
				for (int i = 8; i >= f_k; i--) {
					candidate.remove(i);
	
				}
				
				if(Edge_Centre.if_In_Center(testcase)) {
					Edge_Centre_center++;
//					System.out.println("Edge_Centre_center ="+Edge_Centre_center);
				}else {
					Edge_Centre_edge++;
//					System.out.println("Edge_Centre_edge ="+Edge_Centre_edge);

				}
			}else{
				count++;
				result_three[0] = count;
				result_three[1] = Edge_Centre_edge;
				result_three[2] = Edge_Centre_center;
				
				return result_three;
			}
		}
	}

	 boolean fail1(ArrayList<Double> templist) {
			// TODO 自动生成的方法存根
			  for(int i=0;i<mutiple.size();i++){
		//		  System.out.println(mutiple.get(i));
		//		  System.out.println(ex(mutiple.get(i), templist,fail_length));
		//		  System.out.println(1);
				   if(ex(mutiple.get(i), templist,fail_length)){
				//	   ex(mutiple.get(i), templist,fail_length)
				//	   Euclidean_Distance(mutiple.get(i), templist)<fail_length
					   return true;
				   }
					  
		        }
		        return false;
		}
		 
		public boolean ex(ArrayList<Double> list1,ArrayList<Double> list2,Double r){
	//		boolean flag=false;
			for(int i=0;i<list1.size();i++){
				if(Math.abs(list1.get(i)-list2.get(i))>r){
					return false;
				}
			}
			return true;
		}
	 
	 
	 boolean fail2(ArrayList<Double> templist) {
		// TODO 自动生成的方法存根
		  for(int i=0;i<mutiple.size();i++){
	//		  System.out.println(mutiple.get(i));
	//		  System.out.println(templist);
			   if(ex(mutiple.get(i), templist,mut_area_r.get(i))){
				   return true;
			   }
				  
	        }
	        return false;
	}


	private double Euclidean_Distance(ArrayList<Double> arrayList, ArrayList<Double> templist) {
		// TODO 自动生成的方法存根
		double sum=0.0;
		for(int i=0;i<arrayList.size();i++){
			sum=sum+(Math.pow(arrayList.get(i)-templist.get(i),2));
		}
		return Math.sqrt(sum);
	}

	private double Euclidean_Distance(Testcase testcase, Testcase testcase2) {
		// TODO 自动生成的方法存根
		double sum=0.0;
//		System.out.println(testcase.list);
//		System.out.println("2"+testcase2.list);
		for(int i=0;i<testcase.list.size();i++){
			sum=sum+(Math.pow(testcase.list.get(i)-testcase2.list.get(i),2));
		}
		return Math.sqrt(sum);
	}

	private boolean isCorrect(Testcase testcase) {
		// TODO 自动生成的方法存根

		// 测试用例落在了失效域，则返回false
		boolean jutice = false;
		if(fail_number == 1) {
			for (int i = 0; i < points.size(); i++) {
				if (testcase.list.get(i) < points.get(i) || testcase.list.get(i) > points.get(i) + fail_length) {
					jutice = true;
				}
			}
		}else if(prodomain){
			jutice = true;
			for (int i = 0; i < mutiple.size(); i++) {
				boolean flag = false;
				if (i != mutiple.size() - 1) {
					double rr = mut_area_r.get(i);
					ArrayList<Double> temp = mutiple.get(i); // 坐标点
					// System.out.println("此时的失效区域的面积为:"+area);
					for (int j = 0; j < temp.size(); j++) {
						if ((testcase.list.get(j) < temp.get(j) || testcase.list.get(j) > temp.get(j) + rr)) {
							// 测试用例落在了失效域，则返回false
							// System.out.println("命中"+temp.getX()+","+temp.getY()+"此时的生成的测试点"+x+","+y+"
							// 半径"+rr);
							flag = true;
						}
					}

				} else {
					double rr = mut_area_r.get(i);
					ArrayList<Double> temp = mutiple.get(i); // 坐标点
					// System.out.println("此时的失效区域的面积为:"+area);
					for (int j = 0; j < temp.size(); j++) {
						if ((testcase.list.get(j) < temp.get(j) || testcase.list.get(j) > temp.get(j) + rr)) {
							// 测试用例落在了失效域，则返回false
							// System.out.println("命中"+temp.getX()+","+temp.getY()+"此时的生成的测试点"+x+","+y+"
							// 半径"+rr);
							flag = true;
						}
					}
				}
				if (!flag) {
					jutice = false;
					break;
				}
			}
		}else if(fail_number>0){
			jutice = true;
			for (int i = 0; i < mutiple.size(); i++) {
				ArrayList<Double> temp = mutiple.get(i);
				// System.out.println("(x,y) "+temp.x+","+temp.y);
				boolean flag = false;
				for (int j = 0; j < temp.size(); j++) {
					if ((testcase.list.get(j) < temp.get(j) || testcase.list.get(j) > temp.get(j) + fail_length)) {
						// 测试用例落在了失效域，则返回false
						// System.out.println("命中"+temp.getX()+","+temp.getY()+"
						// 此时的生成的测试点"+x+","+y+" 半径"+rr);
						flag = true;
					}
				}
				if (!flag) {
					jutice = false;
					break;
				}

			}
		}else if(fail_number<0){
			point mapp = new point(dim_list.size());
			float[] a = new float[dim_list.size()];
			for (int i = 0; i < testcase.list.size(); i++) {
				a[i] = testcase.list.get(i).floatValue();
			}
			mapp.coordPoint = a;

			return !fzb.findTarget(mapp);
		}
	//	System.out.println("是否在失效" + !jutice);
		return jutice;
	}

	static double sqrt(double d, double i) {
		i = 1 / i;

		return Math.pow(d, i);
	}

	static class ThreadWithCallback implements Callable{
		private double[][] list;
		private double fail_rate;
		private int fail_number;		
		private boolean prodomain;// 多个失效区域是否有主区域
		private Double r = 0.0;// 用于多个面积不同的面积的R参数
		private int f_k;
		ThreadWithCallback(double[][] list,double fail_rate, int fail_number, boolean prodomain, double r, int f_k){
			this.list=list;
			this.fail_rate=fail_rate;
			this.fail_number=fail_number;
			this.prodomain=prodomain;
			this.r=r;
			this.f_k=f_k;
		}
		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			int[] temp=new M_Edge_Centre_test_FSCS_ctsr(list, fail_rate, fail_number, prodomain, r,f_k).run();
			return temp;
		}
	}
	
	public static void main(String args[]) {
		int times = StripParameters.run_times;
//		int times = 1;
		double sums = 0.0;// 初始化使用的测试用例数
		int[] temp = new int[3];
		double failrate= real_Parameters.M_Edge_Centre_4D_rate; 		// 不同的真实程序缺陷率不同，注意改
		int f_k=5;
		int fail_number=1;		// 若为1，则表示block，大于1则表示point，若为负数则表示strip，strip时记得更新fzb
		ArrayList<Dimension> list = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			Dimension x = new Dimension(0, 1);
			list.add(x);
		}
		// public FSCS_ART_Single_Sequare(ArrayList<Dimension> dim_list,
		// double failrate, int fail_number, boolean prodomain,double r)
		ArrayList<Double> result=new ArrayList<>();
		ArrayList<Long> time =new ArrayList<>();
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(StripParameters.thread_pool_num);
		CompletionService<String> cService = new ExecutorCompletionService<String>(fixedThreadPool);
		for (int i=1; i <= times; i++) {
			 cService.submit(new ThreadWithCallback(real_Parameters.M_Edge_Centre_4D, failrate,fail_number, false, 0.3,f_k));	
		}
		long startTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= times; i++) {
	//			long startTime1 = System.currentTimeMillis();
	//			temp=new M_Edge_Centre_test_FSCS_ctsr(list, failrate, fail_number, false, 0.3,f_k).run();
				Future future=cService.take();			
				temp=(int[]) future.get();
	//			long endTime1 = System.currentTimeMillis();
				System.out.println("第"+i+"次试验F_Measure："+temp[0]+" and M(Edge:Centre) ="+temp[1]*1.0 / temp[2]);
	//			time.add(endTime1-startTime1);
				result.add(temp[1]*1.0 / temp[2]);
				sums+=temp[1]*1.0 / temp[2];
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("M_Edge_Centre_test_FSCS_ctsr failrate ： "+ failrate +" ,fail_number "+fail_number+"\n且最后的AVE(M(Edge:Centre)): "+sums / (double) times);
//		System.out.println("Fm: " + sums / (double) times+"  且最后的Fart/Frt: "+sums / (double) times*failrate);// 平均每次使用的测试用例数
		System.out.println("Time: " + (endTime - startTime) / (double) times);// 测试所使用的时间
		
		java.io.File file2 = new java.io.File("FSCS_dt_bound_center_data/M_Edge_Centre_test/M_Edge_Centre_test_FSCS_ctsr_1000_M_Edge_Centre_4D.txt");
		try {
			FileWriter stream = new FileWriter(file2);
			// stream.write("sdfdsfdsfds");
			stream.write("real_Parameters :  M_Edge_Centre_4D \t 最后的AVE(M(Edge:Centre)): "+sums / (double) times+"\r\n\n");

			for (int j = 0; j < result.size(); j++) {
				stream.write(result.get(j) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);

	}
	
	public void Best_candidate(ArrayList<Testcase> e, ArrayList<Testcase> c) {
		Testcase p = null;
        double mindist, maxmin = 0;
        int cixu = -1;
        for (int i = f_k; i < 10; i++) {
            mindist = 12000;
            for (int j = 0; j < e.size(); j++) {
                //计算距离，Math.sqrt()开平方，Math.pow(a,b)求a的b次方
//                double dist = Math.sqrt((Math.pow(c.get(i).getX()
//                        - e.get(j).getX(), 2))
//                        + Math.pow((c.get(i).getY() - e.get(j).getY()), 2));
            	double dist=Euclidean_Distance(c.get(i), e.get(j));
   //         	System.out.println(dist);
                if (dist < mindist) {
                    mindist = dist;
                }
            }
            if (maxmin < mindist) {
                maxmin = mindist;
            }
            c.get(i).minDistance=mindist;
        }
       
    }
	
	public void sort(ArrayList<Testcase> c){
		Testcase temp=new Testcase();
		for (int i=0;i<c.size()-1;i++){
			for(int j=0;j<c.size()-i-1;j++){
				if (c.get(j).minDistance<c.get(j+1).minDistance) {
					temp=c.get(j);
					c.set(j, c.get(j+1));
					c.set(j+1, temp);
				}
			}
		}
	}
	public void update(ArrayList<Testcase> final_c,Testcase t) {
		double dist;
		for(int i=0;i<final_c.size();i++){
			dist=Euclidean_Distance(final_c.get(i), t);
			if (dist<final_c.get(i).minDistance)
				final_c.get(i).minDistance=dist;
		}
	}
}
