package ARTreal;

/*		1.在eclipse创建一个类Test，并写明静态库的调用和静态方法
 * 		2.在cmd命令行进入类Test.java的文件夹时，再使用命令行将该类的.java文件编译成.class文件和.h文件
 * 				javac Test.java
 * 				javac -h ./ Test.java
 * 				或者一步到位		javac -h ./ Test.java   //Test是步骤1创建的类名
 * 		3.在vs中生成window桌面向导控制台应用程序,注意选择dll，vs的文件名最好与eclipse的类名如Test一致，将2生成的.h文件和jdk文件的bin目录下的jni.h 、 jni_md.h 、 jawt.h 、jawt_md.h文件引入vs项目的头文件，并将这些文件复制到vs项目的文件目录下
 * 		4.将Test.h文件中的#include <jni.h> 改成#include "jni.h" 
 * 		5.在vs中新建Test.cpp，来实现头文件声明的方法，最后点击生成，则在vs项目文件下的x64/文件夹下生成了Test.dll
 * 		6.将Test.dll放到jdk的文件下的bin/文件夹下，此时就可以再eclipse中调用c++生成的dll文件，具体看下面的网址
 * 
 * 		java 调 c++ : https://zhuanlan.zhihu.com/p/219919386
 * */
public class ArtRealTest {
	
	static		//	静态库的调用 
	{
		System.loadLibrary("ArtRealTest");//javaCallcpp就是要加载的dll的名字，这是相对路径加载方式
	}
 
	public native int myadd(int a , int b);	//	myadd就是要在dll中实现的方法，静态方法的声明
	
	public static void main(String args[])
	{		
		

		ArtRealTest test = new ArtRealTest();	//	类实例化
		System.out.println(test.myadd(116, 6));	//	调用类的方法
		
		
	}
}
