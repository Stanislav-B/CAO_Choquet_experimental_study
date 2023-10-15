import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class GreyScale {

	private BufferedImage image;
	private AuxiliaryMethods am = new AuxiliaryMethods();

	public GreyScale() {
		// TODO Auto-generated constructor stub
	}
	
	public GreyScale(String inputFile) {
		try {
			image = ImageIO.read(new File(inputFile));
			image=makeGreyscaleImage(image);
			String s=am.nameOfInputFileWithoutSuffix(inputFile)+"_greyscale.png";
			File outputFile = new File(s);
			ImageIO.write(image, "png", outputFile);

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
			System.exit(1);
		}
		System.out.println("Images were written succesfully.");
	}
	
	public BufferedImage makeGreyscaleImage(BufferedImage bI) {
		BufferedImage outputImage=new BufferedImage(bI.getWidth(), bI.getHeight(), bI.getType());
		for (int j = 0; j < bI.getHeight(); j++) {	
			for (int i = 0; i < bI.getWidth(); i++) {
				Color c = new Color(bI.getRGB(i, j));
				int red = (int) (c.getRed() * 0.299);
				int green = (int) (c.getGreen() * 0.587);
				int blue = (int) (c.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
				outputImage.setRGB(i, j, newColor.getRGB());
			}
		}
		return outputImage;
	}
}
