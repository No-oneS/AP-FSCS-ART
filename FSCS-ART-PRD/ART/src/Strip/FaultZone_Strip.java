package Strip;
import java.util.Random;
//
//import auxiliary.model.FaultZone;
//import auxiliary.model.Point;

/**
 * 
 * @author zxz ����Ƕȵ�Strip����
 */
public class FaultZone_Strip extends FaultZone {   //��״ʧЧģʽ

	public int[][] inputDomain;
	public long edge; // ÿһά�ȵı߳�
	public double aboveLineDelta; // �Ϸ��߶εĽؾ�
	public double belowLineDelta; // �·��߶εĽؾ�
	public double ratio; // �����߶ε�б��

	public FaultZone_Strip() {

	}

	public FaultZone_Strip(int[][] boundary, float area, double rate) { // rateΪ���Ʊ߽�ķ�Χ 
		inputDomain = boundary;
		theta = area; // ʧЧ��
		edge = inputDomain[0][1] - inputDomain[0][0]; // ά�ȱ߳�

		Random random = new Random();
		int lineLocation = random.nextInt(3); // �����������ȷ���߶ε�����λ��

		double p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y; // p1��p2��ͬ��x=-5000 p3��p4��y=5000 p1-p3�߶ξ���p2-p4���Ϸ�

		if (lineLocation == 0) { // ���Ϸ���������
			while (true) {
				p1x = -5000;
				p2x = -5000;
				p2y = -5000 + (10000 * rate * Math.random());
				p3y = 5000;
				p4x = (-5000 + (10000 * (1 - rate))) + (10000 * rate * Math.random());
				p4y = 5000;
				double bigTriangleArea = (5000 - p2y) * (p4x + 5000) / 2;
				ratio = (p4y - p2y) / (p4x - p2x);
				double temp = 2 * (bigTriangleArea - 10000 * 10000 * area) / ratio;
				p3x = Math.sqrt(temp) - 5000;
				p1y = 5000 - ratio * (p3x + 5000);
				if ((p3x >= (-5000 + (10000 * (1 - rate)))) && (p1y <= (-5000 + 10000 * rate))) { // p3x��p1yҲ��ָ����Χ��
					break;
				}
			}
		} else if (lineLocation == 1) { // ���ұ���������
			while (true) {
				p1x = -5000;
				p2x = -5000;
				p2y = -5000 + (10000 * Math.random()); // ����p2��
				p3x = 5000;
				p4x = 5000;
				p4y = (-5000 + (10000 * Math.random()));
				p1y = p2y + 10000 * theta;
				p3y = p4y + 10000 * theta;
				ratio = (p4y - p2y) / (p4x - p2x);
				if (p1y <= 5000 && p3y <= 5000) { // p3x��p1yҲ��ָ����Χ��
					break;
				}
			}
		} else { // ���·���������
			while (true) {
				p1x = -5000;
				p1y = (-5000 + (10000 * (1 - rate))) + (10000 * rate * Math.random()); // ����p2��
				p2x = -5000;
				p3x = (-5000 + (10000 * (1 - rate))) + (10000 * rate * Math.random());
				p3y = -5000;
				p4y = -5000;
				ratio = (p3y - p1y) / (p3x - p1x);
				double bigTriangleArea = (p1y + 5000) * (p3x + 5000) / 2;
				double temp = 2 * (10000 * 10000 * area - bigTriangleArea) / ratio;
				p4x = Math.sqrt(temp) - 5000;
				p2y = -ratio * (p4x + 5000) - 5000;
				if ((p4x >= (-5000 + (10000 * (1 - rate)))) && (p2y >= (-5000 + (10000 * (1 - rate))))) {
					break;
				}
			}
		}
		aboveLineDelta = p1y - ratio * p1x;
		belowLineDelta = p4y - ratio * p4x;
	}

	@Override
	public Boolean findTarget(Point p) {  // �ж��Ƿ���ʧЧ
		// TODO Auto-generated method stub
		if (p.coordPoint[1] - ratio * p.coordPoint[0] >= belowLineDelta
				&& p.coordPoint[1] - ratio * p.coordPoint[0] <= aboveLineDelta) {
			return true;
		} else
			return false;

	}
}
