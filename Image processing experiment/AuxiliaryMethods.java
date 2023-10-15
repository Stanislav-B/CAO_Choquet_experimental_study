public class AuxiliaryMethods {

	public String nameOfInputFileWithoutSuffix(String name) {
		int positionOfDot = -1;
		for (int i = 0; i < name.length(); i++) {
			if (String.valueOf((name.charAt(i))).equals(".")) {
				positionOfDot = i;
			}
		}
		return name.substring(0, positionOfDot);
	}

	public double[] increasingArrange(double[] array) {
		double[] result = new double[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i];
		}
		boolean itIs = true;
		double auxiliary;
		while (itIs) {
			itIs = false;
			for (int i = 1; i < result.length; i++) {
				if (result[i - 1] > result[i]) {
					itIs = true;
					auxiliary = result[i - 1];
					result[i - 1] = result[i];
					result[i] = auxiliary;
				}
			}
		}
		return result;
	}

	public double[][] increasingArrangeByFirstElement(double[][] array) {
		double[][] result = new double[array.length][array[0].length];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = array[i][j];
			}
		}
		boolean itIs = true;
		double auxiliary;
		double auxiliary2;
		while (itIs) {
			itIs = false;
			for (int i = 1; i < result.length; i++) {
				if (result[i - 1][0] > result[i][0]) {
					itIs = true;
					auxiliary = result[i - 1][0];
					auxiliary2 = result[i - 1][1];
					result[i - 1][0] = result[i][0];
					result[i - 1][1] = result[i][1];
					result[i][0] = auxiliary;
					result[i][1] = auxiliary2;
				}
			}
		}
		return result;
	}

	public int[][] increasingArrangeByFirstElement(int[][] array) {
		int[][] result = new int[array.length][array[0].length];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = array[i][j];
			}
		}
		boolean itIs = true;
		int auxiliary;
		int auxiliary2;
		while (itIs) {
			itIs = false;
			for (int i = 1; i < result.length; i++) {
				if (result[i - 1][0] > result[i][0]) {
					itIs = true;
					auxiliary = result[i - 1][0];
					auxiliary2 = result[i - 1][1];
					result[i - 1][0] = result[i][0];
					result[i - 1][1] = result[i][1];
					result[i][0] = auxiliary;
					result[i][1] = auxiliary2;
				}
			}
		}
		return result;
	}

	public double[][] increasingArrangeAndAddIndices(double[] array) {
		double[][] result = new double[array.length][2];
		for (int i = 0; i < result.length; i++) {
			result[i][0] = array[i];
			result[i][1] = i + 1;
		}
		boolean itIs = true;
		double auxiliary;
		double auxiliary2;
		while (itIs) {
			itIs = false;
			for (int i = 1; i < result.length; i++) {
				if (result[i - 1][0] > result[i][0]) {
					itIs = true;
					auxiliary = result[i - 1][0];
					auxiliary2 = result[i - 1][1];
					result[i - 1][0] = result[i][0];
					result[i - 1][1] = result[i][1];
					result[i][0] = auxiliary;
					result[i][1] = auxiliary2;
				}
			}
		}
		return result;
	}

	public double findMeasure(double[][] increasingVectorAndIndices, double[] measure, int i) {
		double[] indicesDouble = new double[increasingVectorAndIndices.length - i];
		int[] indicesInt = new int[increasingVectorAndIndices.length - i];
		int counter = 0;
		for (int j = i; j < increasingVectorAndIndices.length; j++) {
			indicesDouble[counter] = increasingVectorAndIndices[j][1];
			counter++;
		}
		indicesDouble = increasingArrange(indicesDouble);
		for (int j = 0; j < indicesInt.length; j++) {
			indicesInt[j] = (int) indicesDouble[j];
		}
		int cardinalityOfSet = indicesInt.length;
		int positionOfSet = 0;
		for (int j = 0; j < cardinalityOfSet; j++) {
			positionOfSet = positionOfSet + (factorial(increasingVectorAndIndices.length)
					/ (factorial(increasingVectorAndIndices.length - j) * factorial(j)));
		}
		positionOfSet = positionOfSet + searchWithinTuple(increasingVectorAndIndices.length, indicesInt);
		return measure[positionOfSet - 1];
	}

	public int searchWithinTuple(int n, int[] indices) {
		int result = 0;
		int auxiliary = 1;
		for (int i = 1; i < indices[0]; i++) {
			for (int j = 0; j < indices.length - 1; j++) {
				auxiliary = auxiliary * (n - i - j);
			}
			auxiliary = auxiliary / factorial(indices.length - 1);
			result = result + auxiliary;
			auxiliary = 1;
		}
		for (int k = 1; k < indices.length; k++) {
			for (int i = indices[k - 1] + 1; i < indices[k]; i++) {
				for (int j = 0; j < indices.length - 1 - k; j++) {
					auxiliary = auxiliary * (n - i - j);
				}
				auxiliary = auxiliary / factorial(indices.length - 1 - k);
				result = result + auxiliary;
				auxiliary = 1;
			}
		}
		return result + 1;
	}

	public int factorial(int n) {
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result = result * i;
		}
		return result;
	}

	// for generating a powerset
	private int[] basicSet;
	private int[][] powersetOfBasicSet;
	private int[] auxiliaryArray;
	private int counter = 0;

	public int[][] makePowersetOfSet(int[] X) {
		basicSet = X;
		powersetOfBasicSet = new int[(int) Math.pow(2, X.length)][];
		// i - the number of subset elements
		for (int i = 0; i <= X.length; i++) {
			if (i == 0) { // for 0-element subset, to return [null]
				auxiliaryArray = new int[0];
				processIt();
			} else { // for 1 to x-element subset
				auxiliaryArray = new int[i];
				makeCombinationsWithoutRepeating(0, i - 1, 0);
			}
		}
		counter = 0;
		return powersetOfBasicSet;
	}

	// int l - ensures that the combinations are without repetition
	public void makeCombinationsWithoutRepeating(int from, int to, int l) {
		for (int i = l; i < basicSet.length; i++) {
			auxiliaryArray[from] = basicSet[i];
			if (from == to) {
				processIt();
			} else {
				makeCombinationsWithoutRepeating(from + 1, to, i + 1);
			}
		}
	}

	public void processIt() {
		powersetOfBasicSet[counter] = new int[auxiliaryArray.length];
		for (int i = 0; i < powersetOfBasicSet[counter].length; i++) {
			powersetOfBasicSet[counter][i] = auxiliaryArray[i];
		}
		counter++;
	}
}
