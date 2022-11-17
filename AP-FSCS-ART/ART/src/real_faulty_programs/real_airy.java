package real_faulty_programs;
import java.util.Random;
public class real_airy {
	static 
	{
		System.loadLibrary("real_airy");					//javaCallcpp就是要加载的dll的名字，这是相对路径加载方式
	}
	
	public native boolean Produces_Error(double x);		// 调用真实程序

	public static void main(String args[]){
		real_airy real = new real_airy();
		
//		System.out.println("Produces_Error = " + real.Produces_Error(630.0986248529725)); 
		//[630.0986248529725, 2291.52241903108, 2656.187431657574]  .doubleValue()
		Random random1 = new Random();
		
		for(int i =0;i< 100 ; i++) {
			
			int j = random1.nextInt(2);
			System.out.println("j ="+j+" , j == 1 ?"+(j == 1)); //100是不包含在内的，只产生0~99之间的数。

		}
	}
}

