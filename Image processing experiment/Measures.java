public class Measures {
	
	private AuxiliaryMethods am= new AuxiliaryMethods();

	// \mu(E)=(\frac{|X|}{n})^q
	// produces for each set from the powerset its monotone measure
	
	public double[] powerMeasureOnPowerset(int n, double q) {
		double[] result=new double[(int) Math.pow(2,n)];
		int count=1;
		int numberOfElements=0;
		for (int i = 1; i <= n; i++) {
			numberOfElements=am.factorial(n)/(am.factorial(n-i)*am.factorial(i));
			for (int j = count; j < count+numberOfElements; j++) {
				result[j]= Math.pow(((double) i/ n), q) ;
			}
		count=count+numberOfElements;	
		}
		return result;
	}
}
