/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedImage extends BufferedImage {

	public int width = 0;
	public int height = 0;
	private Graphics _graphics;

	public RedImage(int Width, int Height, int ImageType) {
		super(Width, Height, ImageType);
		width = Width;
		height = Height;
		_graphics = super.getGraphics();
	}

	public RedImage(BufferedImage Source) {
		super(Source.getWidth(), Source.getHeight(), TYPE_INT_ARGB);
		width = Source.getWidth();
		height = Source.getHeight();
		setData(Source.getData());
		_graphics = super.getGraphics();
	}
	
   public RedImage(ColorModel cm, WritableRaster wr, boolean bln, Hashtable<?, ?> hshtbl) {
        super(cm, wr, bln, hshtbl);
		_graphics = super.getGraphics();
    }

	public static RedImage loadImage(String ImagePath) {
		RedImage result = new RedImage(1, 1, TYPE_INT_ARGB);
		int w, h;
		try {
			w = ImageIO.read(BufferedImage.class.getResource(ImagePath)).getWidth();
			h = ImageIO.read(BufferedImage.class.getResource(ImagePath)).getHeight();
			result = new RedImage(ImageIO.read(RedImage.class.getResource(ImagePath)));
		} catch (IOException ex) {
			System.err.println(ex + "\nat " + ImagePath);
		}

		return new RedImage(result);
	}

	;
	
    public RedImage getCopy() {
		RedImage copy = new RedImage(this.getWidth(), this.getHeight(), getType());
		copy.setData(getData());
		return copy;
	}

	public RedImage getSubImage(int BeginX, int BeginY, int Width, int Height) {
		RedImage copy = new RedImage(Math.max(Width, 1), Math.max(Height, 1), getType());
		for (int X = Math.max(BeginX, 0); X < Math.min(BeginX + Width, width); X++) {
			for (int Y = Math.max(BeginY, 0); Y < Math.min(BeginY + Height, height); Y++) {
				copy.setRGB(X - BeginX, Y - BeginY, getRGB(X, Y));
			}
		}
		return copy;
	}

	public void fillRect(int BeginX, int BeginY, int Width, int Height, int Color) {
//		WritableRaster raster = getRaster();
//		int[] rasterPixels = null;
//		rasterPixels = raster.getPixels(BeginX, BeginY, Width, Height, rasterPixels);
//		for (int i = 0; i < rasterPixels.length; i++) {
//			rasterPixels[i] = Color;
//		}
//		raster.setPixels(BeginX, BeginY, Width, Height, rasterPixels);
//		setData(raster);
		
		int[] pixels = ((DataBufferInt) getRaster().getDataBuffer()).getData();
		for (int X = BeginX; X < BeginX + Width; X++) {
			for (int Y = BeginY; Y < BeginY + Height; Y++) {
				pixels[X + Y * width] = Color;
			}
		}
		
//		for (int X = Math.max(BeginX, 0); X < Math.min(BeginX + Width, width); X++) {
//			for (int Y = Math.max(BeginY, 0); Y < Math.min(BeginY + Height, height); Y++) {
//				setRGB(X, Y, Color);
//			}
//		}
	}

	public void noiseRGB(int BeginX, int BeginY, int FinishX, int FinishY, int Red, int Green, int Blue, double Seed) {
		int red, green, blue, alpha;
		Color color;
		for (int X = BeginX; X < FinishX; X++) {
			for (int Y = BeginY; Y < FinishY; Y++) {
				color = new Color(getRGB(X, Y), true);
				red = color.getRed();
				green = color.getGreen();
				blue = color.getBlue();
				alpha = color.getAlpha();
				red = Math.max(Math.min((int) (red + (Red * RedG.srandom(Seed)) - Red / 2), 255), 0);
				Seed = RedG.srandom(Seed * Seed);
				green = Math.max(Math.min((int) (green + (Green * RedG.srandom(Seed)) - Green / 2), 255), 0);
				Seed = RedG.srandom(Seed * Seed);
				blue = Math.max(Math.min((int) (blue + (Blue * RedG.srandom(Seed)) - Blue / 2), 255), 0);
				color = new Color(red, green, blue, alpha);
				setRGB(X, Y, color.getRGB());
			}
		}
	}

	public void noiseHSB(int BeginX, int BeginY, int FinishX, int FinishY, float Hue, float Saturation, float Brightness, double Seed) {
		float[] hsb;
		int alpha;
		Color color;
		RedG.globalSeed = Seed;
		for (int X = BeginX; X < FinishX; X++) {
			for (int Y = BeginY; Y < FinishY; Y++) {
				color = new Color(getRGB(X, Y), true);
				alpha = color.getAlpha();
				hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
				hsb[0] = (float) (hsb[0] + (Hue * RedG.srandom(Seed)) - Hue / 2);
				Seed = RedG.srandom(Seed * Seed);
				hsb[1] = (float) Math.max(Math.min((hsb[1] + (Saturation * RedG.srandom(Seed)) - Saturation / 2), 1), 0);
				Seed = RedG.srandom(Seed * Seed);
				hsb[2] = (float) Math.max(Math.min((hsb[2] + (Brightness * RedG.srandom(Seed)) - Brightness / 2), 1), 0);
				color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
				setRGB(X, Y, color.getRGB() - 0xff000000 + alpha * 0x1000000);
			}
		}
	}

	public void setHSB(int BeginX, int BeginY, int FinishX, int FinishY, float Hue, float Saturation, float Brightness) {
		if (Hue != 0 || Saturation != 0 || Brightness != 0) {
			int alpha;
			float[] hsb;
			Color color;
			for (int X = BeginX; X < FinishX; X++) {
				for (int Y = BeginY; Y < FinishY; Y++) {
					color = new Color(getRGB(X, Y));
					alpha = new Color(getRGB(X, Y), true).getAlpha() * 0x1000000;
					if (getRGB(X, Y) != 0) {
						hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
						hsb[0] = (float) Math.max(Math.min(hsb[0] + Hue, 1), 0);
						hsb[1] = (float) Math.max(Math.min(hsb[1] + Saturation, 1), 0);
						hsb[2] = (float) Math.max(Math.min(hsb[2] + Brightness, 1), 0);
						color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), true);
						setRGB(X, Y, color.getRGB() - 0xff000000 + alpha);
					}
				}
			}
		}
	}

	public static RedImage merge(RedImage Back, RedPoint StartBack, RedImage Front) {
		RedImage result = Back.getCopy();
		int alpha;
		Color color;
		for (int X = 0; X < Front.width; X++) {
			for (int Y = 0; Y < Front.height; Y++) {
				color = new Color(Front.getRGB(X, Y), true);
				alpha = color.getAlpha();
				if (alpha != 0) {
					result.setRGB((int) (X + StartBack.x), (int) (Y + StartBack.y), color.getRGB());
				}
			}
		}
		return result;
	}

	public void colorize(int BeginX, int BeginY, int Width, int Heigh, float Hue, float Saturation, float Brightness) {
		int alpha;
		float[] hsb;
		Color color;
		for (int X = BeginX; X < BeginX + Width; X++) {
			for (int Y = BeginY; Y < BeginY + Heigh; Y++) {
				color = new Color(getRGB(X, Y));
				alpha = new Color(getRGB(X, Y), true).getAlpha() * 0x1000000;
				if (getRGB(X, Y) != 0) {
					hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
					hsb[0] = Hue;
					hsb[1] = Saturation / 2 + hsb[1] / 2;
					hsb[2] = Brightness / 2 + hsb[2] / 2;
					color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), true);
					setRGB(X, Y, color.getRGB() - 0xff000000 + alpha);
				}
			}
		}
	}

	public void colorize(int BeginX, int BeginY, int Width, int Height, int ColorizeColor) {
		float[] hsb;
		Color color = new Color(ColorizeColor);
		hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		colorize(BeginX, BeginY, Width, Height, hsb[0], hsb[1], hsb[2]);
	}

	public void noiseFromColors(int BeginX, int BeginY, int FinishX, int FinishY, int[] Colors, double Seed) {
		RedG.globalSeed = Seed;
		for (int X = BeginX; X < FinishX; X++) {
			for (int Y = BeginY; Y < FinishY; Y++) {
				setRGB(X, Y, Colors[(int) Math.abs(Colors.length * RedG.random())]);
			}
		}
	}
	
	@Override
	public Graphics getGraphics() {
		return _graphics;
	}
}
