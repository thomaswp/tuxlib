package tuxkids.java;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

public class JavaIconSetter {
	public static void setIcon(String icon16Path, String icon32Path) {
		try {
			URL url16 = JavaIconSetter.class.getClassLoader().getResource("assets/" + icon16Path);
			if (url16 == null) System.err.println("Could not load 'assets/" + icon16Path + "'");
			URL url32 = JavaIconSetter.class.getClassLoader().getResource("assets/" + icon32Path);
			if (url16 == null) System.err.println("Could not load 'assets/" + icon32Path + "'");
			BufferedImage icon16 = ImageIO.read(url16);
			BufferedImage icon32 = ImageIO.read(url32);

			Display.setIcon(new ByteBuffer[] { createBuffer2(icon16), createBuffer2(icon32) });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// modified from: http://www.jpct.net/forum2/index.php?topic=795.0
	private static ByteBuffer createBuffer2(BufferedImage img) {
		int len=img.getHeight(null)*img.getWidth(null);
		ByteBuffer temp=ByteBuffer.allocateDirect(len<<2);;
		temp.order(ByteOrder.LITTLE_ENDIAN);

		int[] pixels=new int[len];

		PixelGrabber pg = new PixelGrabber(img, 0, 0, 
				img.getWidth(null), img.getHeight(null), 
				pixels, 0, img.getWidth(null));

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i=0; i<len; i++) {
			int pos=i<<2;
			int texel=pixels[i];
			int a = (texel >> 24) & 0xff;
			int r = (texel >> 16) & 0xff;
			int g = (texel >> 8) & 0xff;
			int b = (texel >> 0) & 0xff;
			int color = a << 24 | b << 16 | g << 8 | r;
			temp.putInt(pos, color);
		}

		return temp;
	}
}
