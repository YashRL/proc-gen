package fr.theorozier.procgen.world.biome.surface;

import fr.theorozier.procgen.block.Blocks;

public class DesertSurface extends BiomeSurface {
	
	public DesertSurface() {
		
		super(8);
		
		this.addLayer(0, Blocks.SAND);
		this.addLayer(4, Blocks.SANDSTONE);
		
	}
	
}
