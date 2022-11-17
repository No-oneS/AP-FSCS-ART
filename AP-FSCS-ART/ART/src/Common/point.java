package Common;

import java.util.ArrayList;

public class point {
	public ArrayList<Double> point=new ArrayList<>();
	public double minDistance;
	
	public int n; // 维度
	public float[] coordPoint; //存储该点各个维度的坐标值

	public point(ArrayList<Double> point){
		this.point=point;
	}
	
	public point(int n) {
		this.n = n;
		this.coordPoint = new float[n];

	}
	
	public point(){	
	}
	
    public boolean equals(Object o) {  
        if (o == this) return true;  
        if (!(o instanceof point)) {  
            return false;  
        }  
        point testcase = (point) o; 
        for(int i=0;i<this.point.size();i++){
        //	if(!(this.point.get(i).equals(testcase.point.get(i)))){
        	if(this.point.get(i)!=testcase.point.get(i)){
        		return false;
        	}
        }
        return true;
    } 
}
