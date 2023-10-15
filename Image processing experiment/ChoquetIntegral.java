public class ChoquetIntegral {

	private AuxiliaryMethods am = new AuxiliaryMethods();

	// measure always in shape:
	// {emptyset, {0},{1}, ...,{n-1}, {0,1},{0,2},...,{0,n-1}, {1,2},...,{1,n-1},
	// ...,{0,1,...,n-1} }

	public double CHI(double[] vector, double[] measure) {
		double[][] increasingVectorAndIndices = am.increasingArrangeAndAddIndices(vector);
		double integralResult = increasingVectorAndIndices[0][0] * measure[measure.length - 1];
		for (int i = 1; i < increasingVectorAndIndices.length; i++) {
			integralResult = integralResult + ((increasingVectorAndIndices[i][0] - increasingVectorAndIndices[i - 1][0])
					* am.findMeasure(increasingVectorAndIndices, measure, i));
		}
		return integralResult;
	}
}
