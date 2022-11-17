package Common;
import java.util.ArrayList;



public class Region implements Comparable{
	public Testcase point=new Testcase();  //存放的就是最小点
	public ArrayList<Double> length=new ArrayList<>();  //存放的是该区域的长度
	public Testcase testcase;  //记录该区域的测试用例，与属性count 是矛盾的，不能一同使用；
	public double area;  //获得面积
	public int count;  //主要是统计该区域的测试用例个数
	public Double discrepancy;
	public Region(Testcase point,ArrayList<Double> length){
		this.point=point;
		this.length=length;
		area = 1;
		for(int i=0;i<length.size();i++){
			area=area*length.get(i);
		}
	}

	public boolean isIn_area(Testcase temp){
		boolean jutice = false; //在区域内；
			for (int i = 0; i < point.list.size(); i++) {
				if (temp.list.get(i) < this.point.list.get(i) || temp.list.get(i) > this.point.list.get(i) + length.get(i)) {
					jutice = true; //不在区域内；
					break;
				}
			}
			 return jutice;
		}

	@Override
	public String toString() {
		return "Region{" +
				"discrepancy=" + discrepancy +
				'}';
	}

	@Override
	public int compareTo(Object o) {
		return this.discrepancy.compareTo(((Region)o).discrepancy);
	}
}
