import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FeatureExtractionAndBlending {

	private BufferedImage image;
	
	private int sigma; // the radius of the area of aggregated pixels
	private String whichAO; // which aggregation operator for aggregation of pixels
	private double q; // parameter (power) of power measure
	private double p; // parameter for p-Mean, p>0

	private int width; // width of input image
	private int height; // height of input image

	private int[][] resultImage; // array of aggregated pixels
	private int[] set; // auxiliary set for calculations
	
	ConditionalAggregationOperators ao = new ConditionalAggregationOperators();
	AuxiliaryMethods am= new AuxiliaryMethods();
	Measures measures = new Measures();

	private String nameOfOutputFile;
	
	// which aggregation operator for conditional aggregation based Choquet integral as aggregation operator
	private String whichAOChoquet; 
	
	// for conditional aggregation based Choquet integral
	private int[][] collection;
	private double[] measure;
	
	DetectionMeasure dm= new DetectionMeasure();
	
	public FeatureExtractionAndBlending(int sigma, String whichAO, double q, double p, String whichAOChoquet, String inputFile) {
		try {
			if (sigma<=0 || sigma>3) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Sigma must be >0 a <=3.");
			System.exit(1);
		}
		try {
			image = ImageIO.read(new File(inputFile));
			this.sigma=sigma;
			this.whichAO=whichAO;
			this.q=q;
			this.p=p;
			this.whichAOChoquet=whichAOChoquet;
			width = image.getWidth();
			height = image.getHeight();
			resultImage = new int[width][height];
			set=new int[(int) Math.pow(2*sigma+1, 2)-1];
			for (int i = 0; i < set.length; i++) {
				set[i]=i;
			}
			collection=am.makePowersetOfSet(set);
			measure=dm.detectionMeasureArray;
		} catch (IOException e) {
			System.err.println("The file " + inputFile + " failed to load.");
			System.exit(1);
		}
		nameOfOutputFile=am.nameOfInputFileWithoutSuffix(inputFile);
		GreyScale gs = new GreyScale();
		image=gs.makeGreyscaleImage(image);
		doWork();
		makeImage();
	}
	
	private void doWork() {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				resultImage[i][j]=aggregatePixelDifferences(neighborAndCenterPixelDifferences(i, j));
			}
		}
	}
	
	private double[] neighborAndCenterPixelDifferences(int x, int y) {
		double[] result=new double[(int) Math.pow(2*sigma+1, 2)-1];
		int positionX=x-sigma;
		int positionY=y-sigma;
		int counter=0;
		Color colorCenter=new Color(image.getRGB(x, y)); //image is greyscal
		double greyCenter=colorCenter.getRed(); // R=G=B
		Color colorNeighbor; 
		double greyNeighbor;
		//we adjust the coordinates for the image edges
		int forX;
		int forY;
		for (int i = 0; i < 2*sigma+1; i++) {
			for (int j = 0; j < 2*sigma+1; j++) {
				if (positionX+j!=x && positionY+i!=y) {
					forX=positionX+j;
					forY=positionY+i;
					if (forX<0) {
						forX=Math.abs(forX);
					}
					if (forY<0) {
						forY=Math.abs(forY);
					}
					if (forX>=width) {
						forX=x-(forX-x);
					}
					if (forY>=height) {
						forY=y-(forY-y);
					}
					colorNeighbor= new Color(image.getRGB(forX, forY));
					greyNeighbor=colorNeighbor.getRed();
					result[counter]=Math.abs(greyCenter-greyNeighbor);
					counter++;
				}
				
			}
		}
		return result;
	}
	
	private int aggregatePixelDifferences(double[] values) {
		int result=-1;
		//NORM SUM
		if (whichAO.equals("normSum")) {
			result=(int) ao.normSumAO(values, set);
		}
		//p-Mean
		if (whichAO.equals("pMean")) {
			result=(int) ao.pMeanAO(values, set, p);
		}
		//Mean
		if (whichAO.equals("mean")) {
			result=(int) ao.meanAO(values, set);
		}
		//MAXIMUM
		if (whichAO.equals("max")) {
			result=(int) ao.maximumAO(values, set);
		}
		//MINIMUM
		if (whichAO.equals("min")) {
			result=(int) ao.minimumAO(values, set);
		}
		//MAXMIN
		if (whichAO.equals("maxMin")) {
			result=(int) ao.maxMinAO(values, set);
		}
		//CLASSICAL CHOQUET UNIFORM MEASURE
		if (whichAO.equals("chiUniform")) {
			result=(int) ao.ChoquetAOUniformMeasure(values, set, q);
		}
		//CONDITIONAL AGGREGATION BASED CHOQUET INTEGRAL
		if (whichAO.equals("cabCHI")) {
			result=(int) ao.CABChoquetAO(values, whichAOChoquet, collection, measure, q, p);
		}
		if (result>255) {
			result=255;
		}
		if (result<0) {
			result=0;
		}
		return result;
	}
	
	private void makeImage() {
		BufferedImage outputImage = new BufferedImage(width, height, image.getType());
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				outputImage.setRGB(i, j, new Color(resultImage[i][j], resultImage[i][j], resultImage[i][j]).getRGB());
			}
		}
		try {
			if (whichAO.equals("normSum")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(normSum_sigma="+sigma+").png"));
			}
			if (whichAO.equals("pMean")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(pMean_sigma="+sigma+").png"));
			}
			if (whichAO.equals("mean")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(arithMean_sigma="+sigma+").png"));
			}
			if (whichAO.equals("max")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(max_sigma="+sigma+").png"));
			}
			if (whichAO.equals("min")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(min_sigma="+sigma+").png"));
			}
			if (whichAO.equals("maxMin")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(maxMin_sigma="+sigma+").png"));
			}
			if (whichAO.equals("chiUniform")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(choquetUniform_sigma="+sigma+",q="+q+").png"));
			}
			if (whichAO.equals("cabCHI")) {
				ImageIO.write(outputImage, "png", new File(nameOfOutputFile+"_feature_extraction(caoChoquetUniform_sigma="+sigma+",q="+q+",AO="+whichAOChoquet+").png"));
			}
		} catch (Exception e) {
			System.out.println("Exception occured :" + e.getMessage());
		}
		System.out.println("Image was written succesfully.");
	}
	
	
	public static void main(String[] args) {
				FeatureExtractionAndBlending feab = new FeatureExtractionAndBlending(
				1, // int sigma - the radius of the area of aggregated pixels (radius from the center pixel)
				"cabCHI", // String whichAO - method for for pixel aggregation: "normSum", "pMean", "mean", "max", "min", "maxMin", "chiUniform", "cabCHI"
				0.9, // double q (if needed) - power for power measure
				3, // double p (if needed) - parameter for p-mean, p>0
				"maxMin", // String whichAOChoquet - ONLY IF whichAo=cabCHI - conditional aggregation operator for cabCHI: "sum", "normSum", "pMean", "mean", "max", "min", "maxMin", "chiUniform"
				"lenna.png" // String inputFile - colour or grey image (if the input is color image, it will be automatically transformed to grayscale)
				);
	}

}