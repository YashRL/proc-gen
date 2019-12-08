package fr.theorozier.procgen.world.biome;

import fr.theorozier.procgen.world.biome.surface.BiomeSurface;
import fr.theorozier.procgen.world.biome.surface.DesertSurface;
import fr.theorozier.procgen.world.biome.surface.GrassSurface;

import java.util.HashMap;
import java.util.Map;

public class Biomes {

	private static final Map<Short, Biome> uidRegister = new HashMap<>();

	public static final BiomeSurface      NO_SURFACE = new BiomeSurface(0);
	public static final GrassSurface   GRASS_SURFACE = new GrassSurface();
	public static final DesertSurface DESERT_SURFACE = new DesertSurface();
	
	public static final EmptyBiome            EMPTY = registerBiome(new EmptyBiome(1, "empty"));
	public static final PlainBiome            PLAIN = registerBiome(new PlainBiome(2, "plain"));
	public static final ForestBiome          FOREST = registerBiome(new ForestBiome(3, "forest"));
	public static final LowHillBiome       LOW_HILL = registerBiome(new LowHillBiome(4, "low_hill"));
	public static final DesertBiome          DESERT = registerBiome(new DesertBiome(5, "desert"));
	public static final DesertHillBiome DESERT_HILL = registerBiome(new DesertHillBiome(6, "desert_hill"));
	
	public static <B extends Biome> B registerBiome(B biome) {
	
		uidRegister.put(biome.getUid(), biome);
		
		return biome;
	
	}
	
}