package com.tuxlib.util.layer;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;

public class GroupLayerWrapper extends LayerWrapper {

	protected final GroupLayer layer;
	
	public GroupLayerWrapper() {
		super(graphics().createGroupLayer());
		layer = (GroupLayer) layerAddable();
	}

}
