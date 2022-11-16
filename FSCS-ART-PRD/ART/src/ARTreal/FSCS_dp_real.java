package ARTreal;

import Common.Dimension;
import Common.FaultZone_Strip;
import Common.StripParameters;
import Common.Testcase;
import Common.point;

import real_faulty_programs.*;

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
 * 2.再到main函数改对应的缺陷率,或者改real_Parameters.failure_rate
 * 3.以及改run方法中判断是否找到缺陷的判断语句也要改
 * 4.最后改下项目的依赖 ： 
 *   右键项目--properties--java build path--libraries--JRE system library--native library location 改成dll的文件夹
 * */
public class FSCS_dp_real {
	ArrayList<Double> points = new ArrayList<>(); // 生成的block缺陷区域的左下角点坐标，用来判断点是否在缺陷区域
	
	ArrayList<ArrayList<Double>> multi_points = new ArrayList<>(); // 针对的是生成的多个相同大小失效区域的点坐标

	ArrayList<Testcase> Selected=new ArrayList<>();	// 已测试集

	ArrayList<Testcase> Candidate=new ArrayList<>();	// 符合条件的候选集

	double input_domain_size; // 整个输入域的面积

	public ArrayList<Dimension> dimlist=new ArrayList<>();  // 当前的候选测试点

	int count;

	double fail_rate;
	
	int fail_number;		// 若为1，则表示block，大于1则表示point，若为负数则表示strip

	double fail_totalsize;

	double fail_length;

	FaultZone_Strip fzb;
	
	double fzb_rate = 0.9;
	
	real_tanh real = new real_tanh();			// 初始化真实程序类

	public FSCS_dp_real(double[][] bd, double failrate, int fail_number){
		for (int i = 0; i < bd.length; i++) {
			dimlist.add(new Dimension(bd[i][0], bd[i][1]));
		}
		
		input_domain_size = dimlist.get(0).getRange();
		for (int i = 1; i < dimlist.size(); i++) {
			input_domain_size = input_domain_size * dimlist.get(i).getRange();
		}
		this.fail_number = fail_number;
		this.fail_rate = failrate;
		this.fail_totalsize = failrate * input_domain_size;
		/*
		 * 针对的单个的正方形失效区域的面积的边长,只有point和block模式会用到该值
		 */
		fail_length = (fail_number == 1) ? Math.pow(fail_totalsize, 1.0/dimlist.size()) : Math.pow(fail_totalsize / fail_number, 1.0/dimlist.size());
		
//		fzb = new FaultZone_Strip(bd, (float)failrate, fzb_rate);

	}
	public int  run(){
		// 生成缺陷区域的左下角点坐标
		if(fail_number == 1) {
			for (int i = 0; i < dimlist.size(); i++) {
				double temp = dimlist.get(i).getMin()
						+ ((dimlist.get(i).getRange() - fail_length) * new Random().nextDouble());
				points.add(temp);
			}
		}else if(fail_number > 1 ) {
//			System.out.println("fail_number > 1");
			// System.out.println("此时的为多个面积相同的");
			multi_points = new ArrayList<>(); // 存放多个坐标点
			ArrayList<Double> templist = new ArrayList<>();
			for (int j = 0; j < dimlist.size(); j++) {
				double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());		
				templist.add(temp);
			}
			multi_points.add(templist);
	//		System.out.println("第一个点"+mutiple);
			for (int i = 0; i < fail_number-1; i++) {
				templist.clear();
				for (int j = 0; j < dimlist.size(); j++) {
					double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());	
					templist.add(temp);
				}
	//			System.out.println("for循环中"+templist);
				while (is_overlap(templist)) {  // 缺陷是否重叠，重叠返回true
					templist=new ArrayList<>();
					for (int j = 0; j < dimlist.size(); j++) {
						double temp = dimlist.get(j).getMin() + ((dimlist.get(j).getRange()-fail_length) * new Random().nextDouble());	
						templist.add(temp);
					}
				}
				multi_points.add(templist);
			}
		}else if(fail_number < 0) {
//			System.out.println("fail_number < 0");
		}else {
			System.out.println("fail_number is fault !");
		}
		//  生成第一个测试用例
		Testcase testcase=new Testcase();
		for(int i=0;i<dimlist.size();i++){
			testcase.list.add(dimlist.get(i).getMin()+
					new Random().nextDouble()*dimlist.get(i).getRange());
		}
		while(true){
			if(!real.Produces_Error((double)testcase.list.get(0))){
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
			}else{
				count++;
				return count;
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
	//		boolean flag=false;
		for(int i=0;i<list1.size();i++){
			if(Math.abs(list1.get(i)-list2.get(i))>r){
				return false;
			}
		}
		return true;
	}

	// 判断测试用例是否在缺陷区域,若不在，返回True
	private boolean isCorrect(Testcase testcase) {
		boolean jutice = false;
		if (fail_number == 1) {
			for (int i = 0; i < points.size(); i++) {
				if (testcase.list.get(i) < points.get(i) || testcase.list.get(i) > points.get(i) + fail_length) {
					jutice = true;
				}
			}
		}else if(fail_number > 1) {
//			System.out.println(" }else if(fail_number > 1) {");
			jutice = true;
			for (int i = 0; i < multi_points.size(); i++) {
				ArrayList<Double> temp = multi_points.get(i);
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
		}else if(fail_number < 0) {
			// strip 缺陷
			point mapp = new point(dimlist.size());
			float[] a = new float[dimlist.size()];
//			System.out.println(" testcase.list.toString()= "+testcase.list.toString());
			for (int i = 0; i < testcase.list.size(); i++) {
				a[i] = testcase.list.get(i).floatValue();
//				System.out.println(" testcase.list.get(i)= "+testcase.list.get(i));
//				System.out.println(" a[i]= "+a[i]);
			}	
			mapp.coordPoint = a;
			jutice = !fzb.findTarget(mapp);
		}
	//	System.out.println("是否在失效" + !jutice);
		return jutice;
	}	
	//  Best_candidate(Selected, Candidate);  利用dp公式进行筛选最佳测试用例
	public Testcase Best_candidate(ArrayList<Testcase> e, ArrayList<Testcase> c) {
		int ei_abs = e.size() + 1;
		double  dispersion = Double.MAX_VALUE;
        double mindist = 0;
        int Candidate_index = 0;		// 最后返回的最佳候选点的索引值
        //候选集的每个元素遍历一次
        for (int i = 0; i < c.size(); i++) {
            double tmp_dispersion = 0;
//            System.out.println(" for (int i = 0; i < c.size(); i++) {  i =" + i);
//        	mindist = Euclidean_Distance(c.get(i), e.get(0));
            // 对已测试集中个每个元素遍历
            e.add(c.get(i));
            for (int j = 0; j < ei_abs; j++) {
            	mindist = Double.MAX_VALUE;
            	double tmp_dist;
//            	 System.out.println(" for (int j = 0; j < e.size(); j++) { j =" + j);
            	for (int k = 0; k < ei_abs; k++) {
//               	System.out.println(" for (int k = 0; k < e.size(); k++) { k =" + k);
            		if(j != k) {
	        			tmp_dist = Euclidean_Distance( e.get(j), e.get(k) );
	        			if( tmp_dist < mindist){
	        				mindist = tmp_dist;
	        			}
            		}	
                }
            	if(mindist > tmp_dispersion) {
            		tmp_dispersion = mindist;
            	}
            }
            if(dispersion > tmp_dispersion) {
            	dispersion = tmp_dispersion;
            	Candidate_index = i;
            }
            e.remove(c.get(i));
        }
//      System.out.println(" Candidate_index =" + Candidate_index);
        return c.get(Candidate_index);
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
		ThreadWithCallback(double[][] list,double fail_rate,int fail_number){
			this.list=list;
			this.fail_rate=fail_rate;
			this.fail_number=fail_number;
		}
		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			int temp=new FSCS_dp_real(list,fail_rate,fail_number).run();;
			return temp;
		}
	}

	public static void main(String args[]){
		int times = StripParameters.run_times;
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数
		double failrate= real_Parameters.failure_rate_tanh; 		// 不同的真实程序缺陷率不同，注意改
		int fail_number = 1;			// 若为1，则表示block，大于1则表示point，若为负数则表示strip，strip时记得更新fzb
		ArrayList<Integer> result=new ArrayList<>();
		ArrayList<Dimension> list = new ArrayList<>();

//		for(int i=0;i<4;i++){
//			// 改变生成区域的范围
//			list.add(new Dimension(-1100, 1100));
//				
//		}	
//		long startTime = System.currentTimeMillis();
//		for (int i = 1; i <= times; i++) {
//			temp=new abandon(list,0.8,failrate).run();
//			System.out.println("第" + i + "次试验F-measure:" + temp);
//			sums += temp;
//		}
//		long endTime = System.currentTimeMillis();		
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(StripParameters.thread_pool_num);
		CompletionService<String> cService = new ExecutorCompletionService<String>(fixedThreadPool);
		for (int i=1; i <= times; i++) {
			 cService.submit(new ThreadWithCallback(real_Parameters.bd_tanh, failrate, fail_number));	
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
		System.out.println("FSCS_dp_real failrate ： "+ failrate +" , StripParameters.bd "+StripParameters.bd+" ,fail_number "+fail_number+"\nFm: " + sums / (double) times+"  且最后的Fart/Frt: "+sums / (double) times*failrate);// 平均每次使用的测试用例数
		System.out.println("Time : "+(endTime - startTime) / (double)times );
		
//		java.io.File file2 = new java.io.File("E:/FSCS_dp_data.txt");
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
		System.exit(0);
	}
}
