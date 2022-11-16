package Common;

import java.util.ArrayList;

public class Testcase {
	public ArrayList<Double> list=new ArrayList<>();
	public double minDistance;
//	public Region region;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.list.toString();
	}
	public Testcase(){

	}
	public Testcase(ArrayList<Double> list){
		this.list=list;
	}
}
