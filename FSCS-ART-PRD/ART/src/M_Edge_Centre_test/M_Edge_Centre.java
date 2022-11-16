package M_Edge_Centre_test;

import java.util.ArrayList;

import Common.Dimension;
import Common.Testcase;
import Mythod.point;

/*
 * 计算M(Edge:Centre)= |Eedge| / |Ecentre|
 * FROM :On Test Case Distributions of Adaptive Random Testing.
 * @author Xia
 * 默认设置：
 * 	输入域：{ 0, 1 }
 *  维度： 1 - 4 D
 *  缺陷率：0.001（不影响）
 *  count： 10--100-1000--10000
 * */
public class M_Edge_Centre {
	public int d;				//测试用例的维度
	public double ratio;		//由|Eedge| = |Ecentre|和维度d,确定ratio
	public ArrayList<Dimension> dimlist=new ArrayList<>();  // 当前的候选测试点的范围

	public M_Edge_Centre() {}
	
	public M_Edge_Centre(ArrayList<Dimension> dimlist1) {
		//point是当前测试点，bd是当前点的输入域范围
		d = dimlist1.size();
		ratio = Math.pow(0.5,1.0/d);
		dimlist = dimlist1;
//		System.out.println("d ="+d);
//		System.out.println("ratio ="+ratio);
		
	}
	
	public boolean if_In_Center(Testcase point) {
		//point是当前测试点，bd是当前点的输入域范围		

		double point_ratio = Double.MAX_VALUE;
//		System.out.println("point ="+point.toString());
		for(int j = 0; j < dimlist.size(); j++) {//每个维度
//			System.out.println("point.size("+j+") ="+point.list.get(j));
        	double tmp_min =Math.abs(point.list.get(j) -dimlist.get(j).getMin());
        	point_ratio = (tmp_min / Math.abs(dimlist.get(j).getMax() - dimlist.get(j).getMin()));   	
//        	point_ratio = point_ratio < (2 * tmp_min / Math.abs(dimlist.get(j).getMax() - dimlist.get(j).getMin())) ? ratio : (2 * tmp_min / Math.abs(dimlist.get(j).getMax() - dimlist.get(j).getMin()));   	
//        	System.out.println("point_ratio ="+point_ratio);
        	if(point_ratio < 0.5-(ratio /2) || point_ratio > 0.5+(ratio /2)) {
    			return false;		//表示点位于bound区域
    		}
		}
		return true;
	}
	
	//only for FSCS-NNDC-ART
	public boolean if_In_Center(point point) {
		//point是当前测试点，bd是当前点的输入域范围		

		double point_ratio = Double.MAX_VALUE;
		for(int j = 0; j < dimlist.size(); j++) {//每个维度
        	double tmp_min = Math.abs(point.point.get(j) -dimlist.get(j).getMin());
        	
        	point_ratio =( tmp_min / Math.abs(dimlist.get(j).getMax() - dimlist.get(j).getMin())) ;   
        	if(point_ratio < 0.5-(ratio /2) || point_ratio > 0.5+(ratio /2)) {
    			return false;		//表示点位于bound区域
    		}
        }
		
		return true;
	}
	
	
}
