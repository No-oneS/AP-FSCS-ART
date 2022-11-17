/**
 * @author No one
 * 
 *
 */
package ARTreal;

import Common.Dimension;
import Common.Testcase;
import Common.FaultZone_Strip;
import Common.point;
import Common.StripParameters;

import real_faulty_programs.*;
import Fault_10d.*;
import Fault_8d.*;
import Fault_6d.*;
import Fault_5d.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ARTreal.real_Parameters;


public class ART_test {
	ArrayList<Double> points = new ArrayList<>(); // 针对的是生成的单个失效区域的点坐标
	
	ArrayList<ArrayList<Double>> multi_points = new ArrayList<>(); // 针对的是生成的多个相同大小失效区域的点坐标

	ArrayList<Testcase> Selected=new ArrayList<>();	// 已测试集

	ArrayList<Testcase> Candidate=new ArrayList<>();	// 符合条件的候选集

	double input_domain_size; // 整个输入域的面积

	public ArrayList<Dimension> dimlist=new ArrayList<>();  // 当前的候选测试点

	int count;
	
	int fail_number;		// 若为1，则表示block，大于1则表示point，若为负数则表示strip

	double fail_rate;

	double fail_totalsize;

	double fail_length;
	
	FaultZone_Strip fzb;
	
	double fzb_rate = 0.9;
	
	int bound_num = 1;
	
	int center_num = 1;
	
//	Testcase center_point = new Testcase();		// 中心点
	
	double bound_center_rate = 1;
	
//	real_el2 real = new real_el2();			// 初始化真实程序类
	
	PntLinePos real = new PntLinePos();			// 初始化真实程序类

	public ART_test(double[][]  bd, double failrate, int fail_number){
		
		for (int i = 0; i < bd.length; i++) {
//			System.out.println("bd2["+ i +"][0] = "+bd2[i][0]+" bd2["+ i +"][1] = "+bd2[i][1]);
			dimlist.add(new Dimension(bd[i][0], bd[i][1]));
		}		
		
//		input_domain_size = dimlist.get(0).getRange();
//		for (int i = 1; i < dimlist.size(); i++) {
//			input_domain_size = input_domain_size * dimlist.get(i).getRange();
//		}
//		
//		this.fail_number = fail_number;
		this.fail_rate = failrate;
//		this.fail_totalsize = failrate * input_domain_size;
//		this.bound_center_rate = Math.pow( 2, dimlist.size());
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
//		fail_length = (fail_number == 1) ? Math.pow(fail_totalsize, 1.0/dimlist.size()) : Math.pow(fail_totalsize / fail_number, 1.0/dimlist.size());
		
//		fzb = new FaultZone_Strip(bd, (float)failrate, fzb_rate);
		
		/*
		 * ART_test failrate ： 7.13E-4 ,fail_number 25
			Fm: 1584.1264  且最后的Fart/Frt: 1.1294821232
			Time : 699.721
		 * */
//		bound_center_rate = dimlist.size() == 1 ? 1 : (1 - Math.pow( 0.9, dimlist.size() - 1)) / Math.pow( 0.9, dimlist.size() - 1);  // 调下中心区域的边长，观察下结果
		bound_center_rate = (1 - Math.pow( 0.9, dimlist.size())) / Math.pow( 0.9, dimlist.size());  // 调下中心区域的边长，观察下结果

		
	}
	
	public int  run(){
		
		// 生成测绘用例
		Testcase testcase=new Testcase();
		for(int i=0;i<dimlist.size();i++){
			testcase.list.add(dimlist.get(i).getMin()+
					new Random().nextDouble()*dimlist.get(i).getRange());
//			center_point.list.add(dimlist.get(i).getMin() + (dimlist.get(i).getRange() / 2.0));
		}
			
//		System.out.println("testcase ="+testcase);
		while(true){
			// 循环确定测试用例是否在缺陷区域中，不在则反复生成测试用例，并使用相关规则筛选最佳测试用例
//			if(!real.Produces_Error(testcase.list.get(0), testcase.list.get(1), testcase.list.get(2), testcase.list.get(3))){
			if(!real.Produces_Error(testcase.list.get(0).intValue(), testcase.list.get(1).intValue(), testcase.list.get(2).intValue(), testcase.list.get(3).intValue(), testcase.list.get(4).intValue(), testcase.list.get(5).intValue())){
				// isCorrect(testcase) 判断测试用例是否在缺陷区域中，若不在则为True
				count++;
				Selected.add(testcase);			
				Candidate=new ArrayList<>();	
				for(int j=0;j<20;j++){
					Testcase tempcase=null;
					tempcase=new Testcase();
					for(int i=0;i<dimlist.size();i++){  //生成多维度的坐标
						double temp =
								dimlist.get(i).getMin()+
										dimlist.get(i).getRange()*new Random().nextDouble();
						tempcase.list.add(temp);
					}
					Candidate.add(tempcase);
				}
				testcase=Best_candidate(Selected, Candidate);
			}else{
				count++;
				return count;
			}
		}
	}

	// 现在是bound_center_rate，先从边界找，可以尝试center_bound_rate先从中心找，毕竟中心的点更少
	public Testcase Best_candidate(ArrayList<Testcase> e, ArrayList<Testcase> c) {
        double mindist, maxmin;
        int cixu = -1;
//        System.out.println("bound_num ="+bound_num+"\ncenter_num ="+center_num+"\nbound_center_rate = "+bound_center_rate);
//        System.out.println("(bound_num / (double)center_num) ="+(bound_num / (double)center_num));
        if((bound_num / (double)center_num) > bound_center_rate) {
        	//在中心寻找
//        	System.out.println("进入 center_num");
        	maxmin = 0;
            for (int i = 0; i < c.size(); i++) {
                mindist = Double.MAX_VALUE;
                for (int j = 0; j < e.size(); j++) {
                	double dist=Euclidean_Distance(c.get(i), e.get(j));
                    if (dist < mindist) {
                        mindist = dist;
                    }
                }   
                
                // 计算点到边界的距离，并加入计算，
                double band_dist = Double.MAX_VALUE;			// 点与边界的距离
                for(int j = 0; j < dimlist.size(); j++) {		// 每个维度
                	double tmp_min = Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j)) > Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) ? Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) : Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j));
                	band_dist = band_dist > tmp_min ? tmp_min : band_dist; // 保存最小的边界距离
                }
                mindist = mindist > band_dist ? band_dist : mindist;
                        
                // 找靠近中心点
                if (maxmin < mindist) {
                    maxmin = mindist;
                    cixu = i;
                }
            }
            center_num++;
        	
        }else {
        	//在边界寻找
        	maxmin = Double.MAX_VALUE;
        	
            for (int i = 0; i < c.size(); i++) {
            	mindist = Double.MAX_VALUE;
                for (int j = 0; j < e.size(); j++) {
                	double dist=Euclidean_Distance(c.get(i), e.get(j));
                    if (dist < mindist) {
                        mindist = dist;
                    }
                }   
//                
                // 计算点到边界的距离，并加入计算，
                double band_dist = Double.MAX_VALUE;			// 点与边界的距离
                for(int j = 0; j < dimlist.size(); j++) {		// 每个维度
                	double tmp_min = Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j)) > Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) ? Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) : Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j));
                	band_dist = band_dist > tmp_min ? tmp_min : band_dist; // 保存最小的边界距离
                }
                mindist = mindist > band_dist ? band_dist : mindist;
                        
                // 找靠近边界点
                if (maxmin > mindist) {
                    maxmin = mindist;
                    cixu = i;
                }
            }
	    	bound_num++; 
        }  
	    return c.get(cixu);      
	}
	
	private double Euclidean_Distance(Testcase testcase, Testcase testcase2) {
		// TODO 自动生成的方法存根
		double sum=0.0;
		double p=2;
		for(int i=0;i<testcase.list.size();i++){
			sum=sum+(Math.pow(testcase.list.get(i)-testcase2.list.get(i),p));
		}
		return Math.pow(sum,1.0/p);
	}
	
	static class ThreadWithCallback implements Callable{
		private double[][] list;
		private double fail_rate;
		private int fail_number;
		ThreadWithCallback(double[][] list,double fail_rate, int fail_number){
			this.list=list;
			this.fail_rate=fail_rate;
			this.fail_number=fail_number;
		}
		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			int temp=new ART_test(list,fail_rate,fail_number).run();;
			return temp;
		}
	}
	
	public static void main(String args[]){
		int times =  StripParameters.run_times;
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数
		double failrate= real_Parameters.failure_rate_PntLinePos;
		int fail_number = 25;		// 若为1，则表示block，大于1则表示point，若为负数则表示strip，strip时记得更新fzb
		ArrayList<Integer> result=new ArrayList<>();
//		ArrayList<Dimension> list = new ArrayList<>();

//		for(int i=0;i<4;i++){
//			// 输入域的范围
//			list.add(new Dimension(0, 1000));
//		}
//		list = StripParameters.bd ;
//		for(int i=0;i<StripParameters.bd.length;i++) {
//			list.add(new Dimension(StripParameters.bd[i][0], StripParameters.bd[i][1]));
//		}
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(StripParameters.thread_pool_num);
		CompletionService<String> cService = new ExecutorCompletionService<String>(fixedThreadPool);
		for (int i=1; i <= times; i++) {
			 cService.submit(new ThreadWithCallback(real_Parameters.bd_PntLinePos, failrate,fail_number));	
		}
		long startTime = System.currentTimeMillis();
		try {
			for(int i=1;i<=times;i++){
				Future future=cService.take();
//				System.out.println("cService.take() = " + cService.take().get() );
				temp=(int) future.get();
//				System.out.println("future.take() = " + future.get() );
				result.add(temp);
				System.out.println("第"+i+"次试验F_Measure："+temp);
				sums+=temp;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("ART_test failrate ： "+ failrate +" ,fail_number "+fail_number+"\nFm: " + sums / (double) times+"  且最后的Fart/Frt: "+sums / (double) times*failrate);// 平均每次使用的测试用例数
		System.out.println("Time : "+(endTime - startTime) / (double)times );
//		
//		java.io.File file2 = new java.io.File("E:/FSCS_dt_data.txt");
//		try {
//			FileWriter stream = new FileWriter(file2);
//			// stream.write("sdfdsfdsfds");
//			for (int j = 0; j < result.size(); j++) {
//				stream.write(result.get(j) + "\r\n");
//			}
//			stream.close();
//
//		} catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
		System.exit(0);
	}
}
