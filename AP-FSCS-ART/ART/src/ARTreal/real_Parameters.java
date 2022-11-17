package ARTreal;

/*
 * 真实缺陷程序的一些参数
 * */
public interface real_Parameters {
	double failure_rate_airy = 0.000716;	// airy的缺陷率
	double[][] bd_airy = { { -5000, 5000 } };
	
	double failure_rate_bessj0 = 0.001373;	// bessj0的缺陷率
	double[][] bd_bessj0 = { { -300000, 300000 } };
	
	double failure_rate_erfcc = 0.000574;	// erfcc的缺陷率
	double[][] bd_erfcc = { { -30000, 30000 } };
	
	double failure_rate_probks = 0.000387;	// probks的缺陷率
	double[][] bd_probks = { { -50000, 50000 } };
	
	double failure_rate_tanh = 0.001817;	// tanh的缺陷率
	double[][] bd_tanh = { { -500, 500 } };
	
	double failure_rate_bessj = 0.001298;	// bessj的缺陷率
	double[][] bd_bessj = { { 2, 300 }, { -1000, 15000 } };
	
	double failure_rate_gammq = 0.000830;	// gammq的缺陷率
	double[][] bd_gammq = { { 0, 1700 }, { 0, 40 } };
	
	double failure_rate_sncndn = 0.001623;	// sncndn的缺陷率
	double[][] bd_sncndn = { { -5000, 5000 }, { -5000, 5000 } };
	
	double failure_rate_golden = 0.000550;	// golden的缺陷率
	double[][] bd_golden = { { -100, 60 }, { -100, 60 }, { -100, 60 } };
	
	double failure_rate_plgndr = 0.000368;	// plgndr的缺陷率
	double[][] bd_plgndr = { { 10, 500 }, { 0, 11 }, { 0, 1 } };
	
	double failure_rate_cel = 0.000332;	// cel的缺陷率
	double[][] bd_cel = { { 0.001, 1 }, { 0.001, 300 }, { 0.001, 10000 }, { 0.001, 1000 } };
	
	double failure_rate_el2 = 0.000690;	// el2的缺陷率
	double[][] bd_el2 = { { 0, 250 }, { 0, 250 }, { 0, 250 }, { 0, 250 } };
	
	double failure_rate_CalDay = 0.000632;		// CalDay的缺陷率5D
	double[][] bd_CalDay = { { 1, 12 }, { 1, 31 }, { 1, 12 }, { 1, 31 }, { 1800, 2200 } };
	
	double failure_rate_Complex = 0.000901;	// Complex的缺陷率6D
	double[][] bd_Complex = { { -20, 20 }, { -20, 20 }, { -20, 20 }, { -20, 20 }, { -20, 20 }, { -20, 20 } };
	
	double failure_rate_PntLinePos = 0.000728;		// PntLinePos的缺陷率6D
	double[][] bd_PntLinePos = { { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 } };	
	
	double failure_rate_Triangle = 0.000713;		// Triangle的缺陷率6Dtriangle
	double[][] bd_Triangle = { { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 }, { -25, 25 } };
	
	double failure_rate_Line = 0.000303;	// Line的缺陷率8D
	double[][] bd_Line = { { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 } };

	double failure_rate_PntTrianglePos = 0.000141;	// PntTrianglePos的缺陷率8D
	double[][] bd_PntTrianglePos = { { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 }, { -10, 10 } };

	double failure_rate_TwoLinesPos = 0.000133;	// TwoLinesPos的缺陷率8D
	double[][] bd_TwoLinesPos = { { -15, 15 }, { -15, 15 }, { -15, 15 }, { -15, 15 }, { -15, 15 }, { -15, 15 }, { -15, 15 }, { -15, 15 } };

	double failure_rate_NearestDistance = 0.000690;	// NearestDistance的缺陷率10D,缺陷率待计算，用rt计算
	double[][] bd_NearestDistance = { { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 }, { 1, 15 } };
	
	double M_Edge_Centre_1D_rate = 0.0001;
	double[][] M_Edge_Centre_1D = { { 0, 1 }};
	
	double M_Edge_Centre_2D_rate = 0.0001;
	double[][] M_Edge_Centre_2D = { { 0, 1 }, { 0, 1 }};
	
	double M_Edge_Centre_3D_rate = 0.0001;
	double[][] M_Edge_Centre_3D = { { 0, 1 }, { 0, 1 }, { 0, 1 }};
	
	double M_Edge_Centre_4D_rate = 0.0001;
	double[][] M_Edge_Centre_4D = { { 0, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 }};

}
