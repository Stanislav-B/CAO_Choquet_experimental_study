public class ConditionalAggregationOperators {

	private AuxiliaryMethods am = new AuxiliaryMethods();
	private ChoquetIntegral chi = new ChoquetIntegral();
	private CABasedChoquetIntegral cabchi = new CABasedChoquetIntegral();

	public double sumAO(double[] vector, int[] set) {
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = result + vector[set[i]];
		}
		return result;
	}

	//averaging type of aggregation frac{1}{|E|}\cdot\sum_{i\in E} x_i
	public double normSumAO(double[] vector, int[] set) {
		if (set.length == 0) {
			return 0;
		}
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = (1.0 / (double) set.length) * (sumAO(vector, set));
		}
		return result;
	}

	public double pMeanAO(double[] vector, int[] set, double p) {
		if (set.length == 0) {
			return 0;
		}
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			result = result + Math.pow(vector[set[i]], p);
		}
		result = result / ((double) set.length);
		result = Math.pow(result, 1.0 / p);
		return result;
	}

	public double meanAO(double[] vector, int[] set) {
		return pMeanAO(vector, set, 1.0);
	}

	public double maximumAO(double[] vector, int[] set) {
		double result = 0;
		for (int i = 0; i < set.length; i++) {
			if (result < vector[set[i]]) {
				result = vector[set[i]];
			}
		}
		return result;
	}

	public double minimumAO(double[] vector, int[] set) {
		double result = Double.MAX_VALUE;
		for (int i = 0; i < set.length; i++) {
			if (result > vector[set[i]]) {
				result = vector[set[i]];
			}
		}
		return result;
	}

	public double maxMinAO(double[] vector, int[] set) {
		double max = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < set.length; i++) {
			if (max < vector[set[i]]) {
				max = vector[set[i]];
			}
			if (min > vector[set[i]]) {
				min = vector[set[i]];
			}
		}
		return ((max + min) / 2);
	}

	public double ChoquetAO(double[] vector, int[] set, double[] measure) {
		double[] newVector = new double[vector.length];
		for (int i = 0; i < set.length; i++) {
			newVector[set[i]] = vector[set[i]];
		}
		return chi.CHI(newVector, measure);
	}

	public double ChoquetAOUniformMeasure(double[] vector, int[] set, double q) {
		double[] newVector = new double[vector.length];
		for (int i = 0; i < set.length; i++) {
			newVector[set[i]] = vector[set[i]];
		}
		double[] newVectorIncreasing = am.increasingArrange(newVector);
		double result = newVectorIncreasing[0];
		double n = newVectorIncreasing.length;
		for (int i = 1; i < n; i++) {
			result = result + ((newVectorIncreasing[i] - newVectorIncreasing[i - 1]) * (Math.pow((n - i) / n, q)));
		}
		return result;
	}

	
	
	// the collection must contain both the empty and the whole set
	// numbering of collection from zero!!!
	// measure must be defined on complements of sets
	// thus, if collection={{}, {0}, {1}, {1,2}, {0,1,2}}, then measure on
	// collection={{}, {0}, {0,2}, {1,2}, {0,1,2}} in that order!!!
	
	public double CABChoquetAO(double[] vector, String whichAO, int[][] collection, double[] measure, double q, double p) {
		double[] valuesAO = new double[collection.length];
		valuesAO[0] = 0;
		if (whichAO.equals("sum")) {
			// SUM
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = sumAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("normSum")) {
			//averaging type of aggregation frac{1}{|E|}\cdot\sum_{i\in E} x_i
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = normSumAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("pMean")) {
			// p-MEAN
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = pMeanAO(vector, collection[i], p);
			}
		}
		if (whichAO.equals("mean")) {
			// MEAN
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = meanAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("max")) {
			// MAXIMUM
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = maximumAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("min")) {
			// MINIMUM
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = minimumAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("maxMin")) {
			// MAXMIN
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = maxMinAO(vector, collection[i]);
			}
		}
		if (whichAO.equals("chiUniform")) {
			// CHOQUET WITH UNIFORM MEASURE
			for (int i = 1; i < collection.length; i++) {
				valuesAO[i] = ChoquetAOUniformMeasure(vector, collection[i], q);
			}
		}
		return cabchi.CABCHI(valuesAO, measure);
	}
}
