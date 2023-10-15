import java.util.Arrays;
import java.util.TreeSet;

public class CABasedChoquetIntegral {

	private AuxiliaryMethods am = new AuxiliaryMethods();

	// the collection must contain both the empty and the whole set
	// numbering of collection from zero!!!
	// measure must be defined on complements of sets
	// thus, if collection={{}, {0}, {1}, {1,2}, {0,1,2}}, then measure on
	// collection={{}, {0}, {0,2}, {1,2}, {0,1,2}} in that order!!!

	public double CABCHI(double[] A, double[] measureOnComplementsOfSetsOfCollection) {
		// with the following indexing, I want to connect the collections - the set with
		// its complement, but these are not lines in the visualization!!!
		double[][] AWithIndices = new double[A.length][2];
		double[][] measureWithIndices = new double[A.length][2];
		TreeSet<Double> ao = new TreeSet();
		TreeSet<Double> mu = new TreeSet<>();
		for (int i = 0; i < AWithIndices.length; i++) {
			AWithIndices[i][0] = A[i];
			AWithIndices[i][1] = i;
			ao.add(A[i]);
			measureWithIndices[i][0] = measureOnComplementsOfSetsOfCollection[measureOnComplementsOfSetsOfCollection.length
					- 1 - i];
			measureWithIndices[i][1] = i;
			mu.add(measureWithIndices[i][0]);
		}
		double[][] AWithIndicesIncreasing = am.increasingArrangeByFirstElement(AWithIndices);
		double[][] measureWithIndicesIncreasing = am.increasingArrangeByFirstElement(measureWithIndices);
		// lines for visualization
		int[][] ijLines = new int[A.length][2];// fisrt index i, second j
		int[][] jiLines = new int[A.length][2];// first index j, second i
		int count = 0;
		for (int i = 0; i < AWithIndicesIncreasing.length; i++) {
			for (int j = 0; j < measureWithIndicesIncreasing.length; j++) {
				if (AWithIndicesIncreasing[i][1] == measureWithIndicesIncreasing[j][1]) {
					ijLines[count][0] = i;
					ijLines[count][1] = j;
					jiLines[count][0] = j;
					jiLines[count][1] = i;
					count++;
				}
			}
		}
		jiLines = am.increasingArrangeByFirstElement(jiLines);
		int[] boldI = makeBoldI(ijLines);
		int[] boldJ = makeBoldJ(jiLines);
		int[] varphi = varphiUpperStar(measureWithIndicesIncreasing); // varphi^*
		int[] psi = psiLowerStar(measureWithIndicesIncreasing, boldI); // psi_*
		double result = 0;
		if (ao.size() <= mu.size()) {
			// calculation through AO
			for (int i = 0; i < AWithIndicesIncreasing.length - 1; i++) {
				if (AWithIndicesIncreasing[psi[i + 1]][0] != AWithIndicesIncreasing[psi[i]][0]) {
					result = result + ((AWithIndicesIncreasing[psi[i + 1]][0] - AWithIndicesIncreasing[psi[i]][0])
							* measureWithIndicesIncreasing[boldI[i]][0]);
				}
			}
		} else {
			// calculation through measure mu
			for (int i = measureWithIndicesIncreasing.length - 1; i >= 1; i--) {
				if (AWithIndicesIncreasing[boldJ[varphi[i - 1]]][0] != AWithIndicesIncreasing[boldJ[varphi[i]]][0]) {
					result = result + ((AWithIndicesIncreasing[boldJ[varphi[i - 1]]][0]
							- AWithIndicesIncreasing[boldJ[varphi[i]]][0]) * measureWithIndicesIncreasing[i][0]);
				}
			}
		}
		return result;
	}

	private int[] makeBoldI(int[][] ijLines) {
		int[] result = new int[ijLines.length];
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < result.length; i++) {
			if (min > ijLines[i][1]) {
				min = ijLines[i][1];
			}
			result[i] = min;
		}
		return result;
	}

	private int[] makeBoldJ(int[][] jiLines) {
		int[] result = new int[jiLines.length];
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < result.length; i++) {
			if (min > jiLines[i][1]) {
				min = jiLines[i][1];
			}
			result[i] = min;
		}
		return result;
	}

	private int[] varphiUpperStar(double[][] measureWithIndicesIncreasing) {
		int[] result = new int[measureWithIndicesIncreasing.length];
		int max = 0;
		for (int i = 0; i < result.length; i++) {
			max = i;
			for (int j = i; j < result.length; j++) {
				if (measureWithIndicesIncreasing[max][0] == measureWithIndicesIncreasing[j][0]) {
					max = j;
				} else {
					break;
				}
			}
			result[i] = max;
		}
		return result;
	}

	private int[] psiLowerStar(double[][] measureWithIndicesIncreasing, int[] boldI) {
		int[] result = new int[measureWithIndicesIncreasing.length];
		int min = Integer.MAX_VALUE;
		for (int i = result.length - 1; i >= 0; i--) {
			min = i;
			for (int j = i - 1; j >= 0; j--) {
				if (measureWithIndicesIncreasing[boldI[j]][0] == measureWithIndicesIncreasing[boldI[i]][0]) {
					min = j;
				} else {
					break;
				}
			}
			result[i] = min;
		}
		return result;
	}
}