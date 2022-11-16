package Mythod;

import real_faulty_programs.*;
import Fault_10d.*;
import Fault_8d.*;
import Fault_6d.*;
import Fault_5d.*;

public class real_program {
	
	public static void main(String args[]) {
		
	}

	public boolean getResult(String program_name, Double double1, Double double2, Double double3, Double double4) {
		// TODO Auto-generated method stub
//		real_golden real = new real_golden();			// 初始化真实程序类
//		System.out.println("program_name ="+program_name);
		String str = "real_";
		program_name = str.concat(program_name);
//		System.out.println("program_name ="+program_name);
		if(program_name.equals("real_airy")) {
			real_airy real = new real_airy();
			return real.Produces_Error(double1);
		}else if(program_name.equals("real_bessj0")) {
			real_bessj0 real = new real_bessj0();
			return real.Produces_Error(double1);
		}else if(program_name.equals("real_erfcc")) {
			real_erfcc real = new real_erfcc();
			return real.Produces_Error(double1);
		}else if(program_name.equals("real_probks")) {
			real_probks real = new real_probks();
			return real.Produces_Error(double1);
		}else if(program_name.equals("real_tanh")) {
			real_tanh real = new real_tanh();
			return real.Produces_Error(double1);
		}else if(program_name.equals("real_bessj")) {
			real_bessj real = new real_bessj();
			return real.Produces_Error(double1, double2);
		}else if(program_name.equals("real_gammq")) {
			real_gammq real = new real_gammq();
			return real.Produces_Error(double1, double2);
		}else if(program_name.equals("real_sncndn")) {
			real_sncndn real = new real_sncndn();
			return real.Produces_Error(double1, double2);
		}else if(program_name.equals("real_golden")) {
			real_golden real = new real_golden();
			return real.Produces_Error(double1, double2, double3);
		}else if(program_name.equals("real_plgndr")) {
			real_plgndr real = new real_plgndr();
			return real.Produces_Error(double1, double2, double3);
		}else if(program_name.equals("real_cel")) {
			real_cel real = new real_cel();
			return real.Produces_Error(double1, double2, double3, double4);
		}else if(program_name.equals("real_el2")) {
			real_el2 real = new real_el2();
			return real.Produces_Error(double1, double2, double3, double4);
		}
		return false;
	}
}
