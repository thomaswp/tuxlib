package tuxkids.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import playn.core.CanvasImage;
import tuxkids.tuxlib.ImageSaver;

public class JavaImageSaver implements ImageSaver.Saver {

	@Override
	public void save(CanvasImage image, String path) {
		try {
			Method snapshot = image.getClass().getMethod("snapshot");
			snapshot.setAccessible(true);
			Object jImage = snapshot.invoke(image);
			Field img = jImage.getClass().getSuperclass().getDeclaredField("img");
			img.setAccessible(true);
			BufferedImage bImage = (BufferedImage) img.get(jImage);
			ImageIO.write(bImage, "png", new File(path));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
