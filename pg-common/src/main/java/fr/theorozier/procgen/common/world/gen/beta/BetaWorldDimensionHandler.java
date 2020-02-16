package fr.theorozier.procgen.common.world.gen.beta;

import fr.theorozier.procgen.common.world.WorldServer;
import fr.theorozier.procgen.common.world.gen.WorldDimensionHandler;
import fr.theorozier.procgen.common.world.gen.WorldChunkGeneratorProviders;
import fr.theorozier.procgen.common.world.gen.WorldIncompatException;
import fr.theorozier.procgen.common.world.gen.option.WorldGenerationOption;

public class BetaWorldDimensionHandler implements WorldDimensionHandler {
	
	public static final String OVERWORLD_DIMENSION = "overworld";
	
	@Override
	public void worldCreated(WorldServer dimensionManager, WorldGenerationOption generationOption) {
		
		dimensionManager.createNewDimension(OVERWORLD_DIMENSION, generationOption.getSeed(), WorldChunkGeneratorProviders.BETA_CHUNK_PROVIDER);
		// Add here nether in the future //
		
	}
	
	@Override
	public void worldLoaded(WorldServer dimensionManager) throws WorldIncompatException {
	
	}
	
}
