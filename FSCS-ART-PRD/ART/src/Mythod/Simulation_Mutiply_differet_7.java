package Mythod;

import java.io.File;
/*
 * 实现的是 在随机对一个子区域时候 实现动荡
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import Bisection.WriteExcel;

import jdk.nashorn.internal.ir.LiteralNode.ArrayLiteralNode;
import real_program.main;







public class Simulation_Mutiply_differet_7 {
	ArrayList<Dimension> dim_list;// 负责的是维度的范围值
	ArrayList<Double> points = new ArrayList<>(); // 针对的是生成的失效区域的点坐标
	ArrayList<point> Selected;
	double totalsize; // 整个输入域的面积
	point testcase;
	point firstcase;

	main m = new main();
	ArrayList<Double> single_length=new ArrayList<>();  //存放的是单位的长度
	ArrayList strcut=new ArrayList<>();
	double fail_rate;
	double fail_totalsize;
	ArrayList<Double> random_number = new ArrayList<>();// 存放随机生成数；
	Double sum = 0.0; // 用于多个面积不同的

	ArrayList<Double> mut_area_r; // 用于存储的是多个面积不同时的每个失效的面积的r

	ArrayList<ArrayList<Double>> mutiple; // 存放的是 多个失效域的坐标点
	int fail_number;
	public Simulation_Mutiply_differet_7(ArrayList<Dimension> dim_list, double failrate, int a) {
		/*
		 * 求输入域面积，这是共存在步骤
		 */
		totalsize = dim_list.get(0).getRange();
		for (int i = 1; i < dim_list.size(); i++) {
			totalsize = totalsize * dim_list.get(i).getRange();
		}
		this.fail_rate = failrate;
		this.fail_totalsize = failrate * totalsize;
		
		this.dim_list = dim_list;
		for(int i=0;i<dim_list.size();i++){
			single_length.add(dim_list.get(i).getRange());
		}
		int number=(int) sqrt(100000, dim_list.size());
		this.fail_number = a;

		product_struct(strcut, 1, dim_list.size(), number);
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
	}

	boolean fail2(ArrayList<Double> templist) {
		// TODO 自动生成的方法存根
		for (int i = 0; i < mutiple.size(); i++) {
			// System.out.println(mutiple.get(i));
			// System.out.println(templist);
			if (ex(mutiple.get(i), templist, mut_area_r.get(i))) {
				return true;
			}

		}
		return false;
	}
	
	public int  run() {


		
		mut_area_r = new ArrayList<>();
		for (int i = 0; i < fail_number; i++) {
			random_number.add(Math.random());
			sum = sum + random_number.get(i);
		}
		mutiple = new ArrayList<>(); // 存放多个坐标点

		for (int i = 0; i < fail_number; i++) {
			ArrayList<Double> templist = new ArrayList<>();
			Double area = random_number.get(i) / sum * fail_totalsize;
			double r = sqrt(area, dim_list.size());
			mut_area_r.add(r);
			// System.out.println("此时的面积为："+area+"此时的半径"+r);
			for (int j = 0; j < dim_list.size(); j++) {
				double temp = dim_list.get(j).getMin() + ((dim_list.get(j).getRange() - r) * new Random().nextDouble());
				templist.add(temp);
			}
			while (fail2(templist)) {
				templist = new ArrayList<>();
				for (int j = 0; j < dim_list.size(); j++) {
					double temp = dim_list.get(j).getMin()
							+ ((dim_list.get(j).getRange() - r) * new Random().nextDouble());
					templist.add(temp);
				}
			}
			mutiple.add(templist);
		}

		ArrayList<Long> result=new ArrayList<>();
		int count = 1;
		
		
				
				
		// isCorrect(testcase);
		Selected = new ArrayList<>();

		point region_point = new point();
		ArrayList<Double> region_length = new ArrayList<>();
		for (int i = 0; i < dim_list.size(); i++) {
			region_point.point.add(dim_list.get(i).getMin());
			region_length.add(dim_list.get(i).getRange());
		}
		Region region = new Region(region_point, region_length);
		// System.out.println("面积为" + region.area);
		// for (int i = 0; i < region.collection.size(); i++) {
		// System.out.print(region.collection.get(i).point + " ");
		// }
		// System.out.println("");

		firstcase = new point();
		for (int i = 0; i < dim_list.size(); i++) {
			firstcase.point.add(region.point.point.get(i) + ((region.length.get(i)) * new Random().nextDouble()));
		}
		region.testcase = firstcase;
		// System.out.println("生成的测试用例为" + firstcase.point);
		if (!isCorrect(firstcase)) { // 第一个就是失效
			
		} else {
			LinkedList<Region> L = new LinkedList<>();
			ArrayList<Region> testRegion = new ArrayList<>();
			ArrayList<Region> tempRegion = new ArrayList<>();
//			ArrayList<Region> temporytest=new ArrayList<>();
//			ArrayList<Region> temporyblack=new ArrayList<>();
			Selected.add(firstcase);
			testRegion.add(region);
			for(int i=0;i<dim_list.size();i++){    
				single_length.set(i,single_length.get(i)/2);   //每个长度为一半；
			}
			for (int i = 0; i < testRegion.size(); i++) {
				Region test = testRegion.get(i);
				divide(test, L, tempRegion);  //划分
			}

			testRegion = tempRegion;
			
			
			
	//		sourceRegion.add(firstRegion);
			// Selected.add(testcase);
			boolean flag = true;
			ArrayList<point> lun = new ArrayList<>();
			while (flag) {
//				ArrayList<Double> a = new ArrayList<>();
//				for (int i = 0; i < dim_list.size(); i++) {
//					double temp = (firstcase.point.get(i)-firstRegion.point.point.get(i)) / firstRegion.point.point.size() < 0.5
//							?  (firstcase.point.get(i)-firstRegion.point.point.get(i)) / firstRegion.point.point.size()
//							: (1 -  (firstcase.point.get(i)-firstRegion.point.point.get(i)) / firstRegion.point.point.size());
//				//	double temp = (1 - firstcase.point.get(i) / firstRegion.point.point.size());
//					a.add(temp);
//				}
				while (!L.isEmpty() && flag) {
					// for(int p=0;p<2;p++){
					testcase = new point();
					int rem = L.size();
					int re = new Random().nextInt(rem);
					// System.out.println(re);
					region = L.get(re);

					L.remove(region);

					ArrayList<point> res=new ArrayList();
					for(int j=0;j<10;j++){
						point temppoint=new point();
						for (int i = 0; i < dim_list.size(); i++) {		

							temppoint.point.add(region.point.point.get(i)+(new Random().nextDouble()*region.length.get(i)));	
						}
						res.add(temppoint);
					}

					ArrayList<Region> close=new ArrayList<>();
					for(int i=0;i<region.point.point.size();i++){  //针对每个维度
				//		int index=-1;
						Region temp=new Region();
						point point=new point();
						point.point.addAll(region.point.point);
						ArrayList<Double> length=new ArrayList<>();
						length.addAll(region.length);
						temp.setPoint(point);
						temp.setLength(length);
				//		System.out.println(region.point.point);
				//		System.out.println(region.length);
						if(region.point.point.get(i)==dim_list.get(i).getMin()&&region.length.get(i)<dim_list.get(i).getRange()){
					//		System.out.println(1);
					//		System.out.println(i+" "+region.point.point.get(i)+" "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i)+region.length.get(i));
					//		System.out.println("邻接的点区域"+temp.point.point);
					//		System.out.println("是否有"+testRegion.contains(temp));
						//	ArrayList<Integer> index=new ArrayList<>();
							
							ArrayList<Integer> index=new ArrayList<>();
							for(int j=0;j<temp.point.point.size();j++){
								index.add((int) ((temp.point.point.get(j)-dim_list.get(j).min)/single_length.get(j)));
							}
						//	System.out.println(index);
							Region neight=getstruct(index, 1, dim_list.size(), this.strcut);
							if(neight.testcase!=null){
					//			System.out.println(neight);
								close.add(neight);
							}
//							if((index=testRegion.indexOf(temp))!=-1){		
//								close.add(testRegion.get(index));
//							}
						
						}
						else if(region.point.point.get(i)==dim_list.get(i).getMax()-region.length.get(i)&&region.length.get(i)<dim_list.get(i).getRange()){
					//		System.out.println(2);
					//		System.out.println(i+" "+region.point.point.get(i)+" "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i)-region.length.get(i));

							
							ArrayList<Integer> index=new ArrayList<>();
							for(int j=0;j<temp.point.point.size();j++){
								index.add((int) ((temp.point.point.get(j)-dim_list.get(j).min)/single_length.get(j)));
							}
				//			System.out.println(index);

							Region neight=getstruct(index, 1, dim_list.size(), this.strcut);
							if(neight.testcase!=null){
						//		System.out.println(neight);

								close.add(neight);
							}
						}
						else if(region.length.get(i)<dim_list.get(i).getRange()){

							temp.point.point.set(i, region.point.point.get(i)-region.length.get(i));

							ArrayList<Integer> index=new ArrayList<>();
							for(int j=0;j<temp.point.point.size();j++){
								index.add((int) ((temp.point.point.get(j)-dim_list.get(j).min)/single_length.get(j)));
							}
					//		System.out.println(index);

							Region neight=getstruct(index, 1, dim_list.size(), this.strcut);
							if(neight.testcase!=null){
						//		System.out.println(neight);

								close.add(neight);
							}
							temp.point.point.set(i, region.point.point.get(i)+region.length.get(i));

							index=new ArrayList<>();
							for(int j=0;j<temp.point.point.size();j++){
								
								index.add((int) ((temp.point.point.get(j)-dim_list.get(j).min)/single_length.get(j)));
							}
				//			System.out.println(index);

							neight=getstruct(index, 1, dim_list.size(), this.strcut);
							if(neight.testcase!=null){
					//			System.out.println(neight);

								close.add(neight);
							}
						}
					}
					

					/*
					 * 确定邻接区域的已经执行的点的坐标，存放到closelist数据中；
					 */
					ArrayList<point> closelist=new ArrayList<>();
						for(int j=0;j<close.size();j++){
						//	System.out.println(close.get(j));
							closelist.add(close.get(j).testcase);
						}
						
					testcase=Best_candidate(closelist, res);
//					
					
					
					
					region.testcase = testcase;
					Selected.add(testcase);
					// lun.add(testcase) ;
					// System.out.println("测试用例"+testcase.point);
					count++;
			//		System.out.println("sdfsdfdsfdsfsdfds:"+count);
					
					if (!isCorrect(testcase)) {
						flag = false;
					}else {
						testRegion.add(region);
					}
				}

				if (flag) {
					tempRegion = new ArrayList<>();
					for(int i=0;i<dim_list.size();i++){    
						single_length.set(i,single_length.get(i)/2);   //每个长度为一半；
					}
					for (int i = 0; i < testRegion.size(); i++) {
					//	System.out.println("分割");
						Region test = testRegion.get(i);
						divide(test, L, tempRegion);
					}
					testRegion = tempRegion;
				
					
					
				}
			}


	
		}
		
		
		
		
		return count;

	}

	private void divide(Region region, LinkedList<Region> l, ArrayList<Region> tempRegion) {
		//
		ArrayList<ArrayList<Double>> dim_value = new ArrayList<>();
		ArrayList<ArrayList<Double>> result_value = new ArrayList<>();
		ArrayList<Double> half_length = new ArrayList<>();
		for (int i = 0; i < dim_list.size(); i++) {
			ArrayList<Double> templist = new ArrayList<>();
			templist.add(region.point.point.get(i));
			templist.add(region.point.point.get(i) + region.length.get(i) / 2);
			dim_value.add(templist);
		}
		for (int i = 0; i < region.length.size(); i++) {
			half_length.add(region.length.get(i) / 2);
		}
		recursive(dim_value, result_value, 0, new ArrayList<>());
		for (int i = 0; i < result_value.size(); i++) {
			Region region1 = new Region(new point(result_value.get(i)), half_length);
			// System.out.println("划分"+region1);
			boolean F = true;
			if (region.testcase != null && !region1.isIn_area(region.testcase)) {
	
				F = false;
				region1.testcase = region.testcase;
				tempRegion.add(region1);
			}

			if (F) {
				l.add(region1);
			}
			if(region.testcase!=null){
				ArrayList<Integer> index=new ArrayList<>();
				for(int j=0;j<region1.length.size();j++){
			//		System.out.println(region1.point.point.get(j)+"  "+single_length.get(j)+"  "+region1.point.point.get(j)/single_length.get(j));
					index.add((int) ((region1.point.point.get(j)-dim_list.get(j).min)/single_length.get(j)));
				}
		//		System.out.println(index);
				SetStruct(region1, index, 1, dim_list.size(), this.strcut);
//				System.out.println(getstruct(index, 1, dim_list.size(), this.strcut));
			}	
		}
		//
		//
	}

	public boolean ex(ArrayList<Double> list1, ArrayList<Double> list2, Double r) {
		// boolean flag=false;
		for (int i = 0; i < list1.size(); i++) {
			if (Math.abs(list1.get(i) - list2.get(i)) > r) {
				return false;
			}
		}
		return true;
	}

	private double Euclidean_Distance(ArrayList<Double> arrayList, ArrayList<Double> templist) {
		// TODO 自动生成的方法存根
		double sum = 0.0;
		for (int i = 0; i < arrayList.size(); i++) {
			sum = sum + (Math.pow(arrayList.get(i) - templist.get(i), 2));
		}
		return Math.sqrt(sum);
	}

	private double Euclidean_Distance(point testcase, point testcase2) {
		// TODO 自动生成的方法存根
		double sum = 0.0;
		// System.out.println(testcase.point);
		// System.out.println("2"+testcase2.point);
		for (int i = 0; i < testcase.point.size(); i++) {
			sum = sum + (Math.pow(testcase.point.get(i) - testcase2.point.get(i), 2));
		}
		return Math.sqrt(sum);
	}

	private boolean isCorrect(point testcase) {
		boolean jutice = true;
		for (int i = 0; i < mutiple.size(); i++) {
			boolean flag = false;
			double rr = mut_area_r.get(i);
			ArrayList<Double> temp = mutiple.get(i); // 坐标点
			// System.out.println("此时的失效区域的面积为:"+area);
			for (int j = 0; j < temp.size(); j++) {
				if ((testcase.point.get(j) < temp.get(j) || testcase.point.get(j) > temp.get(j) + rr)) {
					// 测试用例落在了失效域，则返回false
					// System.out.println("命中"+temp.getX()+","+temp.getY()+"此时的生成的测试点"+x+","+y+"
					// 半径"+rr);
					flag = true;
				}
			}
			if (!flag) {
				jutice = false;
				break;
			}
		}
		return jutice;
	}

	static double sqrt(double d, double i) {
		i = 1 / i;

		return Math.pow(d, i);
	}

	public static void main(String args[]) {
		int times = 3000;
		ArrayList<Integer> result=new ArrayList<>();
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试用例落在失效域的使用的测试用例的个数
		double failrate=0.001;
		Dimension x = new Dimension(0, 1);
		Dimension y = new Dimension(0, 1);
		Dimension z = new Dimension(0, 1);
		Dimension u = new Dimension(0, 1);
		ArrayList<Dimension> list = new ArrayList<>();
		list.add(x);
		list.add(y);
		list.add(z);
		list.add(u);
		// public FSCS_ART_Single_Sequare(ArrayList<Dimension> dim_list,
		// double failrate, int fail_number, boolean prodomain,double r)
		long startTime = System.currentTimeMillis();
//		for (int i = 1; i <= times; i++) {
//			temp= new Simulation_strip_7(list,failrate,100).run();
//			result.add(temp);
//			System.out.println("第" + i + "次试验F-measure:" + temp);
//			sums += temp;
//		}
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
		CompletionService<String> cService = new ExecutorCompletionService<String>(fixedThreadPool);
		for (int i=1; i <= times; i++) {
			 cService.submit(new ThreadWithCallback3(list, failrate));
		}
		try {
			for(int i=1;i<=times;i++){
				Future future=cService.take();
				temp=(int) future.get();
				result.add(temp);
				System.out.println("第"+i+"次试验F_Measure："+temp);
				sums+=temp;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Fm: " + sums / (double) times+"  且最后的Fart/Frt: "+sums / (double) times*failrate);// 平均每次使用的测试用例数
		System.out.println("Time: " + (endTime - startTime) / (double) times);// 测试所使用的时间
		java.io.File file2 = new java.io.File("D:/data/4.txt");
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
		System.exit(0);

	}

	public static void recursive(ArrayList<ArrayList<Double>> dim_value, ArrayList<ArrayList<Double>> result_value,
			int layer, ArrayList<Double> curList) {
		if (layer < dim_value.size() - 1) {
			if (dim_value.get(layer).size() == 0) {
				recursive(dim_value, result_value, layer + 1, curList);
			} else {
				for (int i = 0; i < dim_value.get(layer).size(); i++) {
					ArrayList<Double> list = new ArrayList<Double>(curList);

					list.add(dim_value.get(layer).get(i));
					// System.out.println(list);
					recursive(dim_value, result_value, layer + 1, list);
				}
			}
		} else if (layer == dim_value.size() - 1) {
			if (dim_value.get(layer).size() == 0) {
				result_value.add(curList);
			} else {
				for (int i = 0; i < dim_value.get(layer).size(); i++) {
					ArrayList<Double> list = new ArrayList<Double>(curList);
					list.add(dim_value.get(layer).get(i));
					result_value.add(list);
				}
			}
		}
	}
	
	
	
	public point Best_candidate(ArrayList<point> e, ArrayList<point> c) {
		point p = null;
		if(e.size()==0){
			return c.get(new Random().nextInt(c.size()));
		}else{
			ArrayList<Integer> result=new ArrayList<>();
	        double mindist, maxmin = 0;
	        int cixu = -1;
	        for (int i = 0; i < c.size(); i++) {
	            mindist = Integer.MAX_VALUE;
	            for (int j = 0; j < e.size(); j++) {
	                //计算距离，Math.sqrt()开平方，Math.pow(a,b)求a的b次方
//	                double dist = Math.sqrt((Math.pow(c.get(i).getX()
//	                        - e.get(j).getX(), 2))
//	                        + Math.pow((c.get(i).getY() - e.get(j).getY()), 2));
	            	double dist=Euclidean_Distance(c.get(i), e.get(j));
	   //         	System.out.println(dist);
	                if (dist < mindist) {
	                    mindist = dist;
	                }
	            }
	            if(maxmin==mindist){
	            	result.add(i);
	            }else if (maxmin < mindist) {
	                maxmin = mindist;
	                result=new ArrayList<>();
	                result.add(i);
	             //   cixu = i;
	                
	            }
	        }
	        return c.get(result.get(new Random().nextInt(result.size())));
		}
	
    }
	static ArrayList product_struct(ArrayList result,int current,int dim_list,int number){
		 if(current==dim_list){
			 for(int i=0;i<number;i++){
				 result.add(new Region());
			 }
		 }else{
			 for(int i=0;i<number;i++){
				 ArrayList<ArrayList>list=new ArrayList();
				 result.add(list);
				 list.add(product_struct(list, current+1, dim_list, number));
			 }
		 }	 	 
		 return result;
	}
	
	static Region getstruct(ArrayList<Integer> index,int current,int dim_list,ArrayList struct){
		Region result=null;
		if(current==dim_list){
			result=(Region) struct.get(index.get(current-1));
		 }else{
			 ArrayList list=(ArrayList) struct.get(index.get(current-1));
			 result= getstruct(index,current+1,dim_list,list);
		 }
		return result;	 	 
	}
	
	static void SetStruct(Region region,ArrayList<Integer> index,int current,int dim_list,ArrayList struct){
		if(current==dim_list){
			struct.set(index.get(current-1), region);
		}else{
			 ArrayList list=(ArrayList) struct.get(index.get(current-1));
			 SetStruct(region,index,current+1,dim_list,list);
		 }
	}
}

class ThreadWithCallback3 implements Callable{
	private ArrayList<Dimension> list;
	private double fail_rate;
	ThreadWithCallback3(ArrayList<Dimension> list,double fail_rate){
		this.list=list;
		this.fail_rate=fail_rate;
	}
	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		int temp=new Simulation_Mutiply_differet_7(list,fail_rate,100).run();
		return temp;
	}
	
}
