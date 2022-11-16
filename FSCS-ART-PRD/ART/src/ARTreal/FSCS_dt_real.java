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

/*
 * 更改真实程序时，
 * 1.注意引入相关包和实例化相关实体类，
 * 2.再到main函数改对应的缺陷率和输入域,或者改real_Parameters.failure_rate、 real_Parameters.bd_***
 * 3.以及改run方法中判断是否找到缺陷的判断语句也要改
 * 4.最后改下项目的依赖 ： 
 *   右键项目--properties--java build path--libraries--JRE system library--native library location 改成dll的文件夹
 * */


public class FSCS_dt_real {

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
	
//	tanh real = new tanh();			// 初始化真实程序类
	
	real_tanh real = new real_tanh();			// 初始化真实程序类
	
//	double band_dist_max = 0;				// 点到边界的最大值
	
	M_Edge_Centre Edge_Centre;		//计算M_Edge_Centre的类
	
	int Edge_Centre_center = 1;
	
	int Edge_Centre_edge = 1;
	
	int[] result =new int[3];

	public FSCS_dt_real(double[][]  bd, double failrate, int fail_number){
		for (int i = 0; i < bd.length; i++) {
//			System.out.println("bd2["+ i +"][0] = "+bd2[i][0]+" bd2["+ i +"][1] = "+bd2[i][1]);
			dimlist.add(new Dimension(bd[i][0], bd[i][1]));
		}
		
		input_domain_size = dimlist.get(0).getRange();
		for (int i = 1; i < dimlist.size(); i++) {
			input_domain_size = input_domain_size * dimlist.get(i).getRange();
		}
//		System.out.println("dimlist = "+dimlist);
		this.fail_number = fail_number;
		this.fail_rate = failrate;
		this.fail_totalsize = failrate * input_domain_size;
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
		fail_length = (fail_number == 1) ? Math.pow(fail_totalsize, 1.0/dimlist.size()) : Math.pow(fail_totalsize / fail_number, 1.0/dimlist.size());
		
//		fzb = new FaultZone_Strip(bd, (float)failrate, fzb_rate);
		
		Edge_Centre = new M_Edge_Centre(dimlist);

	}
	public int[]  run(){
////		 1.确定缺陷区域类型和数量   
//		if(fail_number == 1) {
////			 block型
//			for (int i = 0; i < dimlist.size(); i++) {
//				double temp = dimlist.get(i).getMin()
//						+ ((dimlist.get(i).getRange() - fail_length) * new Random().nextDouble());
//				points.add(temp);
//			}
//		}else if(fail_number > 1 ) {
////			 point型
//			System.out.println("fail_number > 1");
//			multi_points = new ArrayList<>(); // 存放多个坐标点
//			ArrayList<Double> templist = new ArrayList<>();
//			for (int j = 0; j < dimlist.size(); j++) {
//				double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());		
//				templist.add(temp);
//			}
//			multi_points.add(templist);
//			for (int i = 0; i < fail_number-1; i++) {
//				templist.clear();
//				for (int j = 0; j < dimlist.size(); j++) {
//					double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());	
//					templist.add(temp);
//				}
//				while (is_overlap(templist)) {  // 缺陷是否重叠，重叠返回true
//					templist=new ArrayList<>();
//					for (int j = 0; j < dimlist.size(); j++) {
//						double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());	
//						templist.add(temp);
//					}
//				}
//				multi_points.add(templist);
//			}
//		}else if(fail_number < 0) {
////			 strip型
//			System.out.println("fail_number < 0");
//		}else {
//			System.out.println("fail_number is fault !");
//		}
				
		// 生成测绘用例
		Testcase testcase=new Testcase();
		for(int i=0;i<dimlist.size();i++){
			testcase.list.add(dimlist.get(i).getMin()+
					new Random().nextDouble()*dimlist.get(i).getRange());
		}	
//		System.out.println("testcase ="+testcase);
//		System.out.println("real.producesError(testcase.list.get(0).intValue(), testcase.list.get(1).intValue(), testcase.list.get(2).intValue(), testcase.list.get(3).intValue(), testcase.list.get(4).intValue(), testcase.list.get(5).intValue(), testcase.list.get(6).intValue(), testcase.list.get(7).intValue(), testcase.list.get(8).intValue(), testcase.list.get(9).intValue()) ="+real.producesError(testcase.list.get(0).intValue(), testcase.list.get(1).intValue(), testcase.list.get(2).intValue(), testcase.list.get(3).intValue(), testcase.list.get(4).intValue(), testcase.list.get(5).intValue(), testcase.list.get(6).intValue(), testcase.list.get(7).intValue(), testcase.list.get(8).intValue(), testcase.list.get(9).intValue()));
		while(true){
			// 循环确定测试用例是否在缺陷区域中，不在则反复生成测试用例，并使用相关规则筛选最佳测试用例
			if(!real.Produces_Error(testcase.list.get(0))){
//			if(!real.Produces_Error(testcase.list.get(0).intValue(), testcase.list.get(1).intValue(), testcase.list.get(2).intValue(), testcase.list.get(3).intValue(), testcase.list.get(4).intValue(), testcase.list.get(5).intValue())){
				//, (double)testcase.list.get(1), (double)testcase.list.get(2), (double)testcase.list.get(3)
				// isCorrect(testcase) 判断测试用例是否在缺陷区域中，若不在则为True
				count++;
				Selected.add(testcase);			
				Candidate=new ArrayList<>();	
				for(int j=0;j<10;j++){
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
				if(Edge_Centre.if_In_Center(testcase)) {
					Edge_Centre_center++;
//					System.out.print("Edge_Centre_center ="+Edge_Centre_center);
				}else {
					Edge_Centre_edge++;
//					System.out.print("Edge_Centre_edge ="+Edge_Centre_edge);

				}
			}else{
				count++;
				result[0] = count;
				result[1] = Edge_Centre_edge;
				result[2] = Edge_Centre_center;
				return result;
			}
		}
	}
		
		// 是否重叠，重叠返回true
	boolean is_overlap(ArrayList<Double> templist) {
	// TODO 自动生成的方法存根
	  for(int i=0;i<multi_points.size();i++){
		   if(dist_twopoint(multi_points.get(i), templist,fail_length)){
			   return true;
		   }	  
	  }
	  return false;
	}
		// 判断两个点在每个维度的距离是否大于缺陷半径fail_length，只要有一个维度大于缺陷半径，就不可能重叠
	public boolean dist_twopoint(ArrayList<Double> list1,ArrayList<Double> list2,Double r){
	//	boolean flag=false;
		for(int i=0;i<list1.size();i++){
			if(Math.abs(list1.get(i)-list2.get(i))>r){
				return false;
			}
		}
		return true;
	}

	// 判断测试用例是否在缺陷区域
	private boolean isCorrect(Testcase testcase) {
		boolean jutice = false;
		if (fail_number == 1) {
			// block缺陷
			for (int i = 0; i < points.size(); i++) {
				if (testcase.list.get(i) < points.get(i) || testcase.list.get(i) > points.get(i) + fail_length) {
					jutice = true;
				}
			}
		}else if(fail_number > 1) {
			// point 缺陷
			jutice = true;
			for (int i = 0; i < multi_points.size(); i++) {
				ArrayList<Double> temp = multi_points.get(i);
				boolean flag = false;
				for (int j = 0; j < temp.size(); j++) {
					if ((testcase.list.get(j) < temp.get(j) || testcase.list.get(j) > temp.get(j) + fail_length)) {
						// 测试用例落在了失效域，则返回false
						flag = true;
					}
				}
				if (!flag) {
					jutice = false;
					break;
				}
			}
		}else if(fail_number < 0) {
			// strip 缺陷
			point mapp = new point(dimlist.size());
			float[] a = new float[dimlist.size()];
			for (int i = 0; i < testcase.list.size(); i++) {
				a[i] = testcase.list.get(i).floatValue();
			}	
			mapp.coordPoint = a;
			jutice = !fzb.findTarget(mapp);			
		}
		return jutice;
	}

	public Testcase Best_candidate(ArrayList<Testcase> e, ArrayList<Testcase> c) {
        double mindist, maxmin = 0;
        int cixu = -1;
        for (int i = 0; i < c.size(); i++) {
            mindist = Double.MAX_VALUE;
            for (int j = 0; j < e.size(); j++) {
            	double dist=Euclidean_Distance(c.get(i), e.get(j));
                if (dist < mindist) {
                    mindist = dist;
                }
            }
            
//        	// 计算点到边界的距离，并加入计算
//			double band_dist = Double.MAX_VALUE;			// 点与边界的距离		
//			for(int j = 0; j < dimlist.size(); j++) {//每个维度
//				double tmp_min = Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j)) > Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) ? Math.abs(c.get(i).list.get(j) -dimlist.get(j).getMin()) : Math.abs(dimlist.get(j).getMax() - c.get(i).list.get(j));
//				band_dist = band_dist > tmp_min ? tmp_min : band_dist; // 保存最小的边界距离
//				band_dist_max = band_dist_max > tmp_min ? band_dist_max : tmp_min;
//			}
//			mindist = mindist > band_dist ? band_dist : mindist;	

            //找靠近中心点
            if (maxmin < mindist) {
                maxmin = mindist;
                cixu = i;
            }
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
			int[] temp=new FSCS_dt_real(list,fail_rate,fail_number).run();;
			return temp;
		}
	}
		
	public static void main(String args[]){
		int times =  StripParameters.run_times;
		long sums = 0;// 初始化使用的测试用例数
//		int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数
		int[] temp = new int[3];
		double failrate= real_Parameters.failure_rate_tanh; 		// 不同的真实程序缺陷率不同，注意改
		int fail_number = StripParameters.fail_number;		// 若为1，则表示block，大于1则表示point，若为负数则表示strip，strip时记得更新fzb
		ArrayList<Integer> result=new ArrayList<>();
		
//		ArrayList<Dimension> list = new ArrayList<>();
//
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
			 cService.submit(new ThreadWithCallback(real_Parameters.bd_tanh, failrate,fail_number));	
		}
		long startTime = System.currentTimeMillis();
		try {
			for(int i=1;i<=times;i++){
				Future future=cService.take();
//				System.out.println("cService.take() = " + cService.take().get() );
				temp=(int[]) future.get();
//				System.out.println("future.take() = " + future.get() );
				result.add(temp[0]);
				System.out.println("第"+i+"次试验F_Measure："+temp[0]+" and M(Edge:Centre) ="+temp[1]*1.0 / temp[2]);
				sums+=temp[0];
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("FSCS_dt failrate ： "+ failrate +"\nFm: " + sums / (double) times+"  且最后的Fart/Frt: "+sums / (double) times*failrate);// 平均每次使用的测试用例数
		System.out.println("Time : "+(endTime - startTime) / (double)times );
	
		java.io.File file2 = new java.io.File("FSCS_dt_bound_center_data/real_software/FSCS_dt_real_data/FSCS_dt_real_k_10_final_tanh.txt");
		try {
			FileWriter stream = new FileWriter(file2);
			// stream.write("sdfdsfdsfds");
			stream.write("real_Parameters :  tanh \t failrate:  "+ fail_number+"\t Fm:  "+sums / (double) times+"\t 且最后的Fart/Frt:  "+sums / (double) times*failrate+"\t\t   Time :  "+(endTime - startTime) / (double)times+"\r\n\n");

			for (int j = 0; j < result.size(); j++) {
				stream.write(result.get(j) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}	
		System.exit(0);
	}
}

