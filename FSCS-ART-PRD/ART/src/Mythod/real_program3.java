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

//import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

//import Bisection.WriteExcel;
//import jdk.nashorn.internal.ir.LiteralNode.ArrayLiteralNode;
//import real_program.main;
import Mythod.real_program;

/*
 * 假设性划分 +辅助矩阵
 */




public class real_program3 {
	ArrayList<Dimension> dim_list;// 负责的是维度的范围值
	ArrayList<Double> points = new ArrayList<>(); // 针对的是生成的失效区域的点坐标
	ArrayList<point> candidate;
	ArrayList<point> Selected;
	double totalsize; // 整个输入域的面积
	point testcase;
	point firstcase;
	Region firstRegion = null;
//	main m = new main();
	real_program m = new real_program();
	int count;
	long startTime;
	ArrayList<Double> single_length=new ArrayList<>();  //存放的是单位的长度
	ArrayList strcut=new ArrayList<>();
	public real_program3(ArrayList<Dimension> dim_list) {
		/*
		 * 求输入域面积，这是共存在步骤
		 */
		this.dim_list = dim_list;
		for(int i=0;i<dim_list.size();i++){
			single_length.add(dim_list.get(i).getRange());
		}
		int number=(int) sqrt(100000, dim_list.size());
		
		product_struct(strcut, 1, dim_list.size(), number);
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */
	}

	public int  run() {

		// System.out.println(points);
		// System.out.println(points.get(0)+" "+(points.get(1)+fail_length));
		// System.out.println((points.get(0)+fail_length)+" "+points.get(1));
		// System.out.println((points.get(0)+fail_length)+"
		// "+(points.get(1)+fail_length));
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
		if (isCorrect(firstcase)) { // 第一个就是失效
			
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
					// for(int i=0;i<region.collection.size();i++){
					// for(int j=0)
					// }
					// System.out.println("选取的是"+region);
					// System.out.println("L的"+L.size());

					// System.out.println("选择的情况中L的情况"+neaySelect.size());

					/*
					 * 下面的For循环是为了形成一种动态的Restric
					 */

//					for (int i = 0; i < dim_list.size(); i++) {
//						testcase.point.add(region.point.point.get(i) + region.length.get(i) * a.get(i)
//								+ (new Random().nextDouble() * region.length.get(i) * (1 - 2 * a.get(i))));
//					}
//					for (int i = 0; i < dim_list.size(); i++) {
//						testcase.point.add(region.point.point.get(i)
//								+ (new Random().nextDouble() * region.length.get(i)*2 * a.get(i)));
////						testcase.point.add(region.point.point.get(i)
////						+ (region.length.get(i)* a.get(i)));
//					}
//					ArrayList<point>res=new ArrayList<>();
//					for(int j=0;j<sourceRegion.size();j++){
//						point temppoint=new point();
//						for (int i = 0; i < dim_list.size(); i++) {
//							temppoint.point.add(
//									sourceRegion.get(j).testcase.point.get(i) + (region.point.point.get(i) - sourceRegion.get(j).point.point.get(i)));
//						}	
//						res.add(temppoint);
//					}
					
					/*
					 * 普通的区域映射同时不加子区域划分
					 */
					
//					for (int i = 0; i < dim_list.size(); i++) {
//					testcase.point.add(firstcase.point.get(i) + (region.point.point.get(i) - firstRegion.point.point.get(i)));
//				}	
					
					
					
					LinkedList<Region> b=new LinkedList<>();
					divide(region, b, null);
					
					ArrayList<point> res=new ArrayList();
					for(int j=0;j<b.size();j++){
						point temppoint=new point();
						for (int i = 0; i < dim_list.size(); i++) {		
							//是区域的动态映射
	//			temppoint.point.add(b.get(j).point.point.get(i) + b.get(j).length.get(i) * 0.3
	//						+ (new Random().nextDouble() * b.get(j).length.get(i) * (1 - 2 * 0.3)));
							//实现的是区域的子区域的映射
						//	temppoint.point.add(b.get(j).point.point.get(i)+(firstcase.point.get(i)-firstRegion.point.point.get(i))/firstRegion.length.get(i)*b.get(j).length.get(i));
							//实现的是子区域的随机
							temppoint.point.add(b.get(j).point.point.get(i)+(new Random().nextDouble()*b.get(j).length.get(i)));	
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
					//		System.out.println("邻接的点区域"+temp.point.point);
					//		System.out.println("是否有"+testRegion.contains(temp));
//							if((index=testRegion.indexOf(temp))!=-1){		
//								close.add(testRegion.get(index));
//							}
							
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
					//		System.out.println(3);
					//		System.out.println(i+" "+region.point.point.get(i)+" "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i)-region.length.get(i));
					//		System.out.println("邻接的点区域"+temp.point.point);
					//		System.out.println("是否有"+testRegion.contains(temp));
//							if((index=testRegion.indexOf(temp))!=-1){		
//								close.add(testRegion.get(index));
//							}
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
					//		System.out.println("邻接的点区域"+temp.point.point);
					//		System.out.println("是否有"+testRegion.contains(temp));
							
//							if((index=testRegion.indexOf(temp))!=-1){		
//								close.add(testRegion.get(index));
//							}
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
					
					if (isCorrect(testcase)) {
						flag = false;
					}else {
						testRegion.add(region);
					}
				}
				// System.out.println(lun.size()+" "+flag);
				// java.io.File file2 = new
				// java.io.File("D:/data/"+count+".txt");
				// try {
				// if(!file2.exists()){
				// file2.createNewFile();
				// }
				// FileWriter stream = new FileWriter(file2);
				//// stream.write("sdfdsfdsfds");
				// for (int j = 0; j < lun.size(); j++) {
				// point temp = lun.get(j);
				// for(int i=0;i<temp.point.size();i++){
				// stream.write(temp.point.get(i)+" ");
				// }
				// stream.write("\r\n");
				// }
				// stream.close();
				//
				// } catch (IOException e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }
				//
				// lun=new ArrayList<>();
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

//			java.io.File file2 = new java.io.File("D:/data/" + 8 + ".txt");
//			try {
//				if (!file2.exists()) {
//					file2.createNewFile();
//				}
//				FileWriter stream = new FileWriter(file2);
//				// stream.write("sdfdsfdsfds");
//				for (int j = 0; j < Selected.size(); j++) {
//					point temp = Selected.get(j);
//					for (int i = 0; i < temp.point.size(); i++) {
//						stream.write(temp.point.get(i) + " ");
//					}
//					stream.write("\r\n");
//				}
//				stream.close();
//
//			} catch (IOException e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
	
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
				if (region.testcase.equals(firstcase)) {
					firstRegion = region1;
				}
				F = false;
				region1.testcase = region.testcase;
				tempRegion.add(region1);
			}
			// int number = 0;

			// while (number < Selected.size() && F) {
			// if (!region1.isIn_area(Selected.get(number))) {
			// if (Selected.get(number).equals(firstcase)) {
			// firstRegion = region1;
			// // System.out.println("original region"+firstRegion);
			// }
			// F = false;
			// tempRegion.add(region1);
			// }
			// number++;
			// }
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
		// TODO 自动生成的方法存根

		// 测试用例落在了失效域，则返回false
	//	 return m.getResult("el2", testcase.point.get(0),testcase.point.get(1),testcase.point.get(2),testcase.point.get(3));
		//	 return m.getResult("plgndr", testcase.point.get(0),testcase.point.get(1),testcase.point.get(2));
//		return m.getResult("bessj", testcase.point.get(0),testcase.point.get(1),0.0 ,0.0);
		 return m.getResult("el2", testcase.point.get(0),testcase.point.get(1),testcase.point.get(2),testcase.point.get(3));

		//return false;
	}

	static double sqrt(double d, double i) {
		i = 1 / i;

		return Math.pow(d, i);
	}

	public static void main(String args[]) {
		int times = 5000;
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试a用例落在失效域的使用的测试用例的个数
		double failrate = 0.000690;
		ArrayList<Integer> result = new ArrayList<>();
		ArrayList<Long> Time=new ArrayList<>();
		Dimension x = new Dimension(0,250);
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
			temp = new real_program3(list).run();
			long endTime = System.currentTimeMillis();
			result.add(temp);
			System.out.println("第" + i + "次试验F-measure:" + temp);
			Time.add(endTime-startTime);
			sums += temp;
			total+=endTime-startTime;
		}
		
		System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数
		System.out.println("Time: " + (total) / (double) times);// 测试所使用的时间
//		java.io.File file2 = new java.io.File("FSCS_dt_bound_center_data/real_software/FSCS_NNDC_ART_real_data/FSCS_NNDC_ART_real_golden.txt");

//		try {
//			if (!file2.exists()) {
//				file2.createNewFile();
//			}
//			FileWriter stream = new FileWriter(file2);
//			stream.write("real_Parameters :  golden \t failrate:  "+ fail_number+"\t Fm:  "+sums / (double) times+"\t 且最后的Fart/Frt:  "+sums / (double) times*failrate+"\t\t   Time :  "+(endTime - startTime) / (double)times+"\r\n\n");
//
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
//		file2 = new java.io.File("D:/data/Time.txt");
//		try {
//			if (!file2.exists()) {
//				file2.createNewFile();
//			}
//			FileWriter stream = new FileWriter(file2);
//			// stream.write("sdfdsfdsfds");
//			for (int j = 0; j < Time.size(); j++) {
//				stream.write(Time.get(j) + "\r\n");
//			}
//			stream.close();
//
//		} catch (IOException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}

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
	            mindist = Double.MAX_VALUE;
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
