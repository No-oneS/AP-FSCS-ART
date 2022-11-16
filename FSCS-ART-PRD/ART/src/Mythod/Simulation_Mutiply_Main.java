package Mythod;

/*
 * 实现的是 在随机对一个子区域时候 随机划分子区域 同时 进行Localization
 * 需要创建一个
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;





public class Simulation_Mutiply_Main {
	ArrayList<Dimension> dim_list;// 负责的是维度的范围值
	ArrayList<ArrayList<Double>> mutiple; // 存放的是 多个失效域的坐标点
	ArrayList<point> candidate;
	ArrayList<point> Selected;
	ArrayList<Double> mut_area_r; // 用于存储的是多个面积不同时的每个失效的面积的r
	double totalsize; // 整个输入域的面积
	point testcase;
	point firstcase;
	Region firstRegion = null;
	double fail_rate;
	double fail_totalsize;
	int fail_number;
	Double sum = 0.0; // 用于多个面积不同的
	ArrayList<Double> random_number = new ArrayList<>();// 存放随机生成数；
	double w;
	public Simulation_Mutiply_Main(ArrayList<Dimension> dim_list, double failrate, int number,double w) {
		/*
		 * 求输入域面积，这是共存在步骤
		 */
		totalsize = dim_list.get(0).getRange();
		for (int i = 1; i < dim_list.size(); i++) {
			totalsize = totalsize * dim_list.get(i).getRange();
		}
		this.dim_list = dim_list;
		this.fail_rate = failrate;
		this.fail_totalsize = failrate * totalsize;
		this.fail_number = number;
		this.w=w;
		/*
		 * 针对的单个的正方形失效区域的面积的边长
		 */

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

	public int run() {

		mut_area_r = new ArrayList<>();
		for (int i = 0; i < fail_number-1; i++) {
			random_number.add(Math.random());
			sum = sum + random_number.get(i);
		}
		mutiple = new ArrayList<>(); // 存放多个坐标点
		// ArrayList<Double> templist = new ArrayList<>();
		// double first_r=sqrt( random_number.get(0) / sum * (1 - this.r) *
		// fail_totalsize, dim_list.size());
		// for (int j = 0; j < dim_list.size(); j++) {
		// double temp = dim_list.get(j).getMin() +
		// ((dim_list.get(j).getRange()-first_r) * new Random().nextDouble());
		// templist.add(temp);
		// }
		// mutiple.add(templist);
		// mut_area_r.add(first_r);
		
		if (fail_number == 1) {
			ArrayList<Double> templist = new ArrayList<>();
			// 随机生成失效区域，考虑到边界情况，最大值只能是1-fail_rate
			double r = sqrt(fail_totalsize, dim_list.size());
			for (int i = 0; i < dim_list.size(); i++) {
				double temp = dim_list.get(i).getMin()
						+ ((dim_list.get(i).getRange() - r) * new Random().nextDouble());
				templist.add(temp);
			}
			mutiple.add(templist);
			mut_area_r.add(r);
			// System.out.println("失效的坐标点" + points);

		} else{
			for (int i = 0; i < fail_number-1; i++) {
				ArrayList<Double> templist = new ArrayList<>();
				Double area = random_number.get(i) / sum*(1 - this.w) * fail_totalsize;
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
			
			ArrayList<Double>  templist = new ArrayList<>();
			for (int j = 0; j < dim_list.size(); j++) {
				double temp = dim_list.get(j).getMin()
						+ ((dim_list.get(j).getRange() - sqrt(this.w * fail_totalsize, dim_list.size()))
								* new Random().nextDouble());
				templist.add(temp);
			}
			while (fail2(templist)) {
				templist = new ArrayList<>();
				for (int j = 0; j < dim_list.size(); j++) {
					double temp = dim_list.get(j).getMin()
							+ ((dim_list.get(j).getRange() - sqrt(this.w * fail_totalsize, dim_list.size()))
									* new Random().nextDouble());
					templist.add(temp);
				}
			}
			mutiple.add(templist);
			mut_area_r.add(sqrt(this.w * fail_totalsize, dim_list.size()));
		}
		

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

		firstcase = new point();
		for (int i = 0; i < dim_list.size(); i++) {
			firstcase.point.add(region.point.point.get(i) + ((region.length.get(i)) * new Random().nextDouble()));
		}
		region.testcase = firstcase;
		// System.out.println("生成的测试用例为" + firstcase.point);
		if (!isCorrect(firstcase)) { // 第一个就是失效
			return count;
		} else {
			LinkedList<Region> L = new LinkedList<>();
			ArrayList<Region> testRegion = new ArrayList<>();
			ArrayList<Region> tempRegion = new ArrayList<>();

			Selected.add(firstcase);
			testRegion.add(region);
			for (int i = 0; i < testRegion.size(); i++) {
				Region test = testRegion.get(i);
				divide(test, L, tempRegion); // 划分
			}

			testRegion = tempRegion;

			boolean flag = true;
			ArrayList<point> lun = new ArrayList<>();
			while (flag) {

				while (!L.isEmpty() && flag) {
					// for(int p=0;p<2;p++){
					testcase = new point();
					int rem = L.size();
					int re = new Random().nextInt(rem);
					// System.out.println(re);
					region = L.get(re);

					L.remove(region);

					LinkedList<Region> b = new LinkedList<>();
					divide(region, b, null);

					ArrayList<point> res = new ArrayList();
					for (int j = 0; j < b.size(); j++) {
						point temppoint = new point();
						for (int i = 0; i < dim_list.size(); i++) {
							// 是区域的动态映射
							// temppoint.point.add(b.get(j).point.point.get(i) +
							// b.get(j).length.get(i) * 0.3
							// + (new Random().nextDouble() *
							// b.get(j).length.get(i) * (1 - 2 * 0.3)));
							//
							// 实现的是子区域的随机
							temppoint.point.add(
									b.get(j).point.point.get(i) + (new Random().nextDouble() * b.get(j).length.get(i)));
						}
						res.add(temppoint);
					}

					ArrayList<Region> close = new ArrayList<>();
					for (int i = 0; i < region.point.point.size(); i++) { // 针对每个维度
						int index = -1;
						Region temp = new Region();
						point point = new point();
						point.point.addAll(region.point.point);
						ArrayList<Double> length = new ArrayList<>();
						length.addAll(region.length);
						temp.setPoint(point);
						temp.setLength(length);
						// System.out.println(testRegion.size());
						if (region.point.point.get(i) == 0 && region.length.get(i) < dim_list.get(i).getMax()) {
							// System.out.println(1);
							// System.out.println(i+"
							// "+region.point.point.get(i)+"
							// "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i) + region.length.get(i));
							// System.out.println("邻接的点区域"+temp.point.point);
							// System.out.println("是否有"+testRegion.contains(temp));
							if ((index = testRegion.indexOf(temp)) != -1) {
								close.add(testRegion.get(index));
							}

						} else if (region.point.point.get(i) == dim_list.get(i).getMax() - region.length.get(i)
								&& region.length.get(i) < dim_list.get(i).getMax()) {
							// System.out.println(2);
							// System.out.println(i+"
							// "+region.point.point.get(i)+"
							// "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i) - region.length.get(i));
							// System.out.println("邻接的点区域"+temp.point.point);
							// System.out.println("是否有"+testRegion.contains(temp));
							if ((index = testRegion.indexOf(temp)) != -1) {
								close.add(testRegion.get(index));
							}
						} else if (region.length.get(i) < dim_list.get(i).getMax()) {
							// System.out.println(3);
							// System.out.println(i+"
							// "+region.point.point.get(i)+"
							// "+region.length.get(i));
							temp.point.point.set(i, region.point.point.get(i) - region.length.get(i));
							// System.out.println("邻接的点区域"+temp.point.point);
							// System.out.println("是否有"+testRegion.contains(temp));
							if ((index = testRegion.indexOf(temp)) != -1) {
								close.add(testRegion.get(index));
							}
							temp.point.point.set(i, region.point.point.get(i) + region.length.get(i));
							// System.out.println("邻接的点区域"+temp.point.point);
							// System.out.println("是否有"+testRegion.contains(temp));
							if ((index = testRegion.indexOf(temp)) != -1) {
								close.add(testRegion.get(index));
							}
						}
					}

					/*
					 * 确定邻接区域的已经执行的点的坐标，存放到closelist数据中；
					 */
					ArrayList<point> closelist = new ArrayList<>();
					for (int j = 0; j < close.size(); j++) {
						// System.out.println(close.get(j));
						closelist.add(close.get(j).testcase);
					}

					testcase = Best_candidate(closelist, res);
					//

					region.testcase = testcase;
					Selected.add(testcase);
					// lun.add(testcase) ;
					// System.out.println("测试用例"+testcase.point);
					count++;
					if (!isCorrect(testcase)) {
						flag = false;
					} else {
						testRegion.add(region);
					}
				}

				if (flag) {
					tempRegion = new ArrayList<>();
					for (int i = 0; i < testRegion.size(); i++) {
						Region test = testRegion.get(i);
						divide(test, L, tempRegion);
					}
					testRegion = tempRegion;

				}
			}

			return count;
		}

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
		long sums = 0;// 初始化使用的测试用例数
		int temp = 0;// 初始化测试a用例落在失效域的使用的测试用例的个数
		double failrate = 0.005;
		ArrayList<Integer> result = new ArrayList<>();
		Dimension x = new Dimension(0, 1);
		Dimension y = new Dimension(0, 1);
		Dimension z = new Dimension(0, 1);
		Dimension u = new Dimension(0, 1);
		ArrayList<Dimension> list = new ArrayList<>();
		list.add(x);
		list.add(y);
//		list.add(z);
//		list.add(u);
		int a=100;
		double w=0.8;
		// for(int i=0;i<5;i++){
		// Dimension a=new Dimension(0, 1);
		// list.add(a);
		// }
		// public FSCS_ART_Single_Sequare(ArrayList<Dimension> dim_list,
		// double failrate, int fail_number, boolean prodomain,double r)
		long startTime = System.currentTimeMillis();
		for (int i = 1; i <= times; i++) {
			temp = new Simulation_Mutiply_Main(list, failrate,a,w).run();
			result.add(temp);
			System.out.println("第" + i + "次试验F-measure:" + temp);
			sums += temp;
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Fm: " + sums / (double) times + "  且最后的Fart/Frt: " + sums / (double) times * failrate);// 平均每次使用的测试用例数
		System.out.println("Time: " + (endTime - startTime) / (double) times);// 测试所使用的时间
		java.io.File file2 = new java.io.File("D:/data/5.txt");
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
		ArrayList<Integer> result = new ArrayList<>();
		double mindist, maxmin = 0;
		int cixu = -1;
		for (int i = 0; i < c.size(); i++) {
			mindist = 12000;
			for (int j = 0; j < e.size(); j++) {
				// 计算距离，Math.sqrt()开平方，Math.pow(a,b)求a的b次方
				// double dist = Math.sqrt((Math.pow(c.get(i).getX()
				// - e.get(j).getX(), 2))
				// + Math.pow((c.get(i).getY() - e.get(j).getY()), 2));
				double dist = Euclidean_Distance(c.get(i), e.get(j));
				// System.out.println(dist);
				if (dist < mindist) {
					mindist = dist;
				}
			}
			if (maxmin == mindist) {
				result.add(i);
			} else if (maxmin < mindist) {
				maxmin = mindist;
				result = new ArrayList<>();
				result.add(i);
				// cixu = i;

			}
		}
		return c.get(result.get(new Random().nextInt(result.size())));
	}

}
