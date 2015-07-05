package com.tuxlib.util.layer;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.TextLayout;
import pythagoras.f.Rectangle;

public class SegmentedCanvasLayer extends LayerWrapper {


	protected final GroupLayer layer;
	protected final ImageLayer backgroundLayer;
	protected final Canvas[][] canvases;
	protected final boolean[][] dirty;
	protected final int rows, cols;
	protected final float height, width;
	protected final int segmentSize;
	protected final Canvas testCanvas;

	public SegmentedCanvasLayer(float width, float height, int segmentSize) {
		super(graphics().createGroupLayer());
		layer = (GroupLayer) layerAddable();

		this.width = width;
		this.height = height;
		this.segmentSize = segmentSize;

		rows = (int)height / segmentSize + 1;
		cols = (int)width / segmentSize + 1;

		backgroundLayer = graphics().createImageLayer();
		backgroundLayer.setDepth(-1);
		layer.add(backgroundLayer);

		canvases = new Canvas[rows][cols];
		dirty = new boolean[rows][cols];


		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				ImageLayer canvasLayer = graphics().createImageLayer();
				canvasLayer.setImage(graphics().createImage(segmentSize, segmentSize));
				canvasLayer.setTranslation(j * segmentSize, i * segmentSize);
				canvases[i][j] = ((CanvasImage) canvasLayer.image()).canvas();
				canvases[i][j].translate(-j * segmentSize, -i * segmentSize);

				layer.add(canvasLayer);
			}
		}
		
		testCanvas = graphics().createImage(1, 1).canvas();
	}


	private Rectangle rect = new Rectangle();
	public void operate(CanvasOperation op) {
		op.clip().clip(rect);
		op(rect.x, rect.maxX(), rect.y, rect.maxY(), op);
	}
	
//	private void op(CanvasOperation op) {
//		op(0, 0, width, height, op);
//	}

	private void op(float x0, float x1, float y0, float y1,
			CanvasOperation op) {
		float strokeWidth = 0;
		float minX = Math.min(x0, x1) - strokeWidth;
		float maxX = Math.max(x0, x1) + strokeWidth;
		float minY = Math.min(y0, y1) - strokeWidth;
		float maxY = Math.max(y0, y1) + strokeWidth;


		int startRow = (int)(minY / segmentSize);
		int endRow = (int)(maxY / segmentSize);
		int startCol = (int)(minX / segmentSize);
		int endCol = (int)(maxX / segmentSize);

		for (int i = startRow; i <= endRow; i++) {
			for (int j = startCol; j <= endCol; j++) {
				if (i < 0 || i >= rows) continue;
				if (j < 0 || j >= cols) continue;

				dirty[i][j] = true;
				op.operate(canvases[i][j]);
			}
		}
		op.operate(testCanvas);
	}

	public interface CanvasOperation {
		void operate(Canvas canvas);
		Clip clip();
	}
	
	public interface Clip {
		void clip(Rectangle rect);
	}
	
	public static class RectClip implements Clip {
		private float x, y, width, height;
		
		public RectClip(float x, float y, float width, float height) {
			this.x = x; this.y = y;
			this.width = width; this.height = height;
		}
		
		@Override
		public void clip(Rectangle rect) {
			rect.setBounds(x, y, width, height);
		}
	}
	
	public static class LineClip extends RectClip {
		public LineClip(float x0, float y0, float x1, float y1) {
			super(Math.min(x0, x1), Math.min(y0, y1),
					Math.abs(x0 - x1), Math.abs(y0 - y1));
		}
	}
	
	public static class CircleClip implements Clip {
		private float x, y, rad;
		
		public CircleClip(float x, float y, float rad) {
			this.x = x; this.y = y;
			this.rad = rad;
		}
		
		@Override
		public void clip(Rectangle rect) {
			rect.setBounds(x - rad, y - rad, rad * 2, rad * 2);
		}
	}
	
	public class AllClip implements Clip {
		@Override
		public void clip(Rectangle rect) {
			rect.setBounds(0, 0, width, height);
		}
	}
	
	public class TextClip implements Clip {
		private float x, y;
		private TextLayout layout;
		
		public TextClip(float x, float y, TextLayout layout) {
			this.x = x; this.y = y;
			this.layout = layout;
		}
		
		@Override
		public void clip(Rectangle rect) {
			rect.setBounds(x, y, x + layout.width(), y + layout.height());
		}
	}
}
