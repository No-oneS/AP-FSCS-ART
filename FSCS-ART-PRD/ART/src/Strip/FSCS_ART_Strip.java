package Strip;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.timer.TimerMBean;

import Common.Testcase;

public class FSCS_ART_Strip {
	ArrayList<Dimension> dim_list = new ArrayList<>();// �������ά�ȵķ�Χֵ
	ArrayList<Double> mut_area_r; // ���ڴ洢���Ƕ�������ͬʱ��ÿ��ʧЧ�������r
	ArrayList<Double> points = new ArrayList<>(); // ��Ե������ɵ�ʧЧ����ĵ�����
	ArrayList<ArrayList<Double>> mutiple; // ��ŵ��� ���ʧЧ��������
	ArrayList<Testcase> candidate = new ArrayList<>();
	ArrayList<Testcase> Selected;;
	double totalsize; // ��������������
	double fail_rate; // ʧЧ��
	double fail_length; // ʧЧ��ı߳�
	int fail_number;// ʧЧ��ĸ���
	boolean prodomain;// ���ʧЧ�����Ƿ���������
	double fail_totalsize;
	ArrayList<Double> random_number = new ArrayList<>();// ��������������
	Double sum = 0.0; // ���ڶ�������ͬ��
	Double r = 0.0;// ���ڶ�������ͬ�������R����
	// TwoLinesPos cal=new TwoLinesPos();
	FaultZone_Strip fzb;
	double p;

	public FSCS_ART_Strip(int[][] bd2, float failerate, double p) {
		/*
		 * ����������������ǹ����ڲ���
		 */

		for (int i = 0; i < bd2.length; i++) {
			dim_list.add(new Dimension(bd2[i][0], bd2[i][1]));
		}
		this.fail_rate = failerate;

		/*
		 * ��Եĵ�����������ʧЧ���������ı߳�
		 */
		fzb = new FaultZone_Strip(bd2, failerate, 0.9);

		this.p = p;
	}

	public int run() {

		int count = 1;
		Testcase testcase = new Testcase();
		for (int i = 0; i < dim_list.size(); i++) {
			testcase.list.add(dim_list.get(i).getMin() + ((dim_list.get(i).getRange()) * new Random().nextDouble()));
		}
		Selected = new ArrayList<>();

		while (!isCorrect(testcase)) {
			Selected.add(testcase);
			count++;
			candidate = new ArrayList<>();

			for (int i = 0; i < 10; i++) {
				Testcase temp = new Testcase();
				for (int j = 0; j < dim_list.size(); j++) {
					temp.list.add(dim_list.get(j).getMin() + ((dim_list.get(j).getRange()) * new Random().nextDouble()));
				}
				candidate.add(temp);
			}
			testcase = Best_candidate(Selected, candidate);

		}

		return count;
	}

	private double Euclidean_Distance(Testcase testcase, Testcase testcase2) {
		// TODO �Զ����ɵķ������
		double sum = 0.0;

		for (int i = 0; i < testcase.list.size(); i++) {
			sum = sum + (Math.pow(Math.abs(testcase.list.get(i) - testcase2.list.get(i)), p));
		}
		return Math.pow(sum, 1.0 / p);
	}

	private boolean isCorrect(Testcase testcase) {
		// TODO �Զ����ɵķ������
		Point mapp = new Point(dim_list.size());
		float[] a = new float[dim_list.size()];
		for (int i = 0; i < testcase.list.size(); i++) {
			a[i] = testcase.list.get(i).floatValue();
		}
		mapp.coordPoint = a;

		return fzb.findTarget(mapp);
	}

	static double sqrt(double d, double i) {
		i = 1 / i;
		return Math.pow(d, i);
	}

	public static void main(String args[]) {
		int times = StripParameters.run_times;
		long sums = 0;// ��ʼ��ʹ�õĲ���������
		int temp = 0;// ��ʼ��������������ʧЧ���ʹ�õĲ��������ĸ���
		ArrayList<Integer> data =new ArrayList<>();
		ArrayList<Long> time =new ArrayList<>();
		float failrate = StripParameters.failure_rate;
		double p = StripParameters.lp;
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(StripParameters.thread_pool_num);
		CompletionService<String> cService = new ExecutorCompletionService<String>(fixedThreadPool);
		for (int i = 1; i <= times; i++) {
			cService.submit(new ThreadWithCallback1(StripParameters.bd, failrate, p));
		}
		long startTime = System.currentTimeMillis();
		try {
			for (int i = 1; i <= times; i++) {
				long startTime1 = System.currentTimeMillis();
				Future future = cService.take();
				temp = (int) future.get();
				long endTime1 = System.currentTimeMillis();
				time.add(endTime1-startTime1);
				data.add(temp);
				System.out.println("��" + i + "������F_Measure��" + temp);
				sums += temp;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();

//		System.out.println("��ǰ������dimension = " + StripParameters.dimension +  "   failure-rate = " + failrate); // �����ǰ������Ϣ
//		System.out.println("Fm: " + sums / (double) times + "  ������Fart/Frt: " + sums / (double) times * failrate);// ƽ��ÿ��ʹ�õĲ���������
	//	System.out.println("Time: " + (endTime - startTime) / (double) times);// ������ʹ�õ�ʱ��
		System.out.println("Fm: " + sums / (double) times+"  ������Fart/Frt: "+sums / (double) times*failrate);// ƽ��ÿ��ʹ�õĲ���������
		System.out.println("Time: " + (endTime - startTime) / (double) times);// ������ʹ�õ�ʱ��
		
		
		ArrayList<Double> result =new ArrayList<>();
		result.add(sums / (double) times*failrate);
		result.add((endTime - startTime) / (double) times);
		java.io.File file = new java.io.File("D:/f_k_data/"+"fscsstrip"+StripParameters.dimension+"D"+failrate+"fm.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter stream = new FileWriter(file);
			// stream.write("sdfdsfdsfds");
			for (int i=0;i<data.size();i++){
				stream.write(data.get(i) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		java.io.File file2 = new java.io.File("D:/f_k_data/"+"fscsstrip"+StripParameters.dimension+"D"+failrate+".txt");
		try {
			if (!file2.exists()) {
				file2.createNewFile();
			}
			FileWriter stream = new FileWriter(file2);
			// stream.write("sdfdsfdsfds");
			for (int i=0;i<result.size();i++){
				stream.write(result.get(i) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		java.io.File file3 = new java.io.File("D:/f_k_data/"+"fscstrip"+StripParameters.dimension+"D"+failrate+"time.txt");
		try {
			if (!file3.exists()) {
				file3.createNewFile();
			}
			FileWriter stream = new FileWriter(file3);
			// stream.write("sdfdsfdsfds");
			for (int i=0;i<time.size();i++){
				stream.write(time.get(i) + "\r\n");
			}
			stream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public Testcase Best_candidate(ArrayList<Testcase> e, ArrayList<Testcase> c) {
        double mindist, maxmin = 0;
        int cixu = -1;
        for (int i = 0; i < c.size(); i++) {
            mindist = 12000;
            for (int j = 0; j < e.size(); j++) {
                //������룬Math.sqrt()��ƽ����Math.pow(a,b)��a��b�η�
//                double dist = Math.sqrt((Math.pow(c.get(i).getX()
//                        - e.get(j).getX(), 2))
//                        + Math.pow((c.get(i).getY() - e.get(j).getY()), 2));
            	double dist=Euclidean_Distance(c.get(i), e.get(j));
   //         	System.out.println(dist);
                if (dist < mindist) {
                    mindist = dist;
                }
            }
            if (maxmin < mindist) {
                maxmin = mindist;
                cixu = i;
            }
        }
        return c.get(cixu);
    }
}

class ThreadWithCallback1 implements Callable {
	private int[][] list;
	private float fail_rate;
	double p;

	ThreadWithCallback1(int[][] list, float fail_rate, double p) {
		this.list = list;
		this.fail_rate = fail_rate;
		this.p = p;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		int temp = new FSCS_ART_Strip(list, fail_rate, p).run();
		return temp;
	}
}