package Common;

import java.util.ArrayList;

public class Partitions implements Comparable<Partitions>{
	ArrayList<Testcase> testcases=new ArrayList<>();//��ŵ��Ǹ�����Ĳ�������
	public ArrayList<Testcase> getTestcases() {
		return testcases;
	}




	ArrayList<Double> unitLength=new ArrayList<>();
	ArrayList<Double> point=new ArrayList<>();  //��������������
	ArrayList<Double> CC=new ArrayList<>();   //������ĵ�ǰ��central�����
	ArrayList<Double> IC=new ArrayList<>();  //���۵�central ��������������
	double dif_f;  //IC ��CC�ľ���ֵ
	int dimension;
	int tc; //�������success Test cases ����Ŀ
	public Partitions(ArrayList<Double> point,ArrayList<Double>unitlength) {
		// TODO �Զ����ɵĹ��캯�����
		this.point=point;
		this.unitLength=unitlength;
	    dimension=unitlength.size();
		for(int i=0;i<dimension;i++){
			IC.add(point.get(i)+unitlength.get(i)/2);
		}      
	}
	
	
	
	
	
	public ArrayList<Double> getPoint() {
		return point;
	}





	public ArrayList<Double> getCC() {
//		if (testcases.size() != 0) {
//
//			for (int j = 0; j < dimension; j++) {
//				Double sum = 0.0;
//				for (int i = 0; i < testcases.size(); i++) {
//					sum = sum + testcases.get(i).list.get(j);
//				}
//				CC.add(sum / testcases.size());
//			}
//		}
		return CC;        
	}





	public ArrayList<Double> getIC() {
		return IC;
	}





	public double getDif_f() {
//		dif_f=Euclidean_Distance(IC, getCC());
		return dif_f;
	}





	public void setTestcases(ArrayList<Testcase> testcases) {
		this.testcases = testcases;
	}





	public void setPoint(ArrayList<Double> point) {
		this.point = point;
	}





	public void setCC(ArrayList<Double> cC) {
		CC = cC;
	}





	public void setIC(ArrayList<Double> iC) {
		IC = iC;
	}





	public void setDif_f(double dif_f) {
		this.dif_f = dif_f;
	}





	public void setDimension(int dimension) {
		this.dimension = dimension;
	}





	public void setTc(int tc) {
		this.tc = tc;
	}





	public int getTc() {
		return tc;
	}


	


	private double Euclidean_Distance(ArrayList<Double> arrayList, ArrayList<Double> templist) {
		// TODO �Զ����ɵķ������
		double sum=0.0;
		for(int i=0;i<arrayList.size();i++){
			sum=sum+(Math.pow(arrayList.get(i)-templist.get(i),2));
		}
		return Math.sqrt(sum);
	}





	@Override
	public int compareTo(Partitions o) {
		// TODO �Զ����ɵķ������
		if(this.getDif_f()<o.getDif_f()){
			return 1;
		}else if(this.getDif_f()==o.getDif_f()){
			return 0;
		}else{
			return -1;
		}
	}





	public boolean Choice(Testcase test) {
		// TODO �Զ����ɵķ������
		/*
		 * false ��Ϊ�����partition
		 */
		boolean jutice = false;
			for (int i = 0; i < this.point.size(); i++) {
				if (test.list.get(i) < this.point.get(i) || test.list.get(i) > this.point.get(i) +unitLength.get(i) ) {
					jutice = true;
				}
			}
//			System.out.println(this.point+"  �Ƿ���"+jutice);
		if(!jutice){
			tc=tc+1;
			testcases.add(test);
			double distance=Euclidean_Distance(test.list, IC);
			if(Euclidean_Distance(test.list, IC)<dif_f){
				dif_f=distance;
			}
		}
		return jutice;
	}
}
