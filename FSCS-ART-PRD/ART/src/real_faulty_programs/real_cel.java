package real_faulty_programs;

public class real_cel {
	static 
	{
		System.loadLibrary("real_cel");					//javaCallcpp就是要加载的dll的名字，这是相对路径加载方式
	}
	
	public native boolean Produces_Error(double x,double y,double z,double z_x);		// 调用真实程序

	public static void main(String args[]){
		real_cel real = new real_cel();
//		System.out.println("Produces_Error = " + real.Produces_Error(630.0986248529725)); 
		//[630.0986248529725, 2291.52241903108, 2656.187431657574]  .doubleValue()
	}
}
