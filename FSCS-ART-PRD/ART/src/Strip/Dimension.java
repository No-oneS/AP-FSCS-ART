package Strip;

public class Dimension {
	double min;   //��ŵ��Ǹ�ά�ȵ���Сֵ
	double max;   //��Ÿ�ά�ȵ����ֵ
	double range; //��Ÿ�ά�ȵ�ȡֵ�ķ�Χ
	public Dimension(double min,double max) {
		// TODO �Զ����ɵĹ��캯�����
		this.min=min;
		this.max=max;
		range=max-min;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getRange() {
		return range;
	}
	

	
}
