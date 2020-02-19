package fr.theorozier.procgen.common.world.feature;

import fr.theorozier.procgen.common.world.WorldAccessor;
import fr.theorozier.procgen.common.world.feature.config.FeatureConfig;
import fr.theorozier.procgen.common.world.gen.chunk.ChunkGenerator;
import fr.theorozier.procgen.common.world.position.BlockPositioned;

import java.util.Random;

public abstract class Feature<C extends FeatureConfig> {

	public abstract boolean place(WorldAccessor world, ChunkGenerator generator, Random rand, BlockPositioned at, C config);
	
}
