package fr.theorozier.procgen.renderer.world.layer;

import fr.theorozier.procgen.block.BlockRenderLayer;
import fr.theorozier.procgen.renderer.world.ChunkRenderManager;
import fr.theorozier.procgen.renderer.world.ChunkRenderer;
import fr.theorozier.procgen.world.chunk.Chunk;
import io.msengine.client.renderer.texture.TextureMap;

public class ChunkDirectLayerData extends ChunkLayerData {
	
	public ChunkDirectLayerData(Chunk chunk, BlockRenderLayer layer, ChunkRenderManager renderManager) {
		super(chunk, layer, renderManager);
	}
	
	@Override
	public void handleNewViewPosition(ChunkRenderer cr, int x, int y, int z) {}
	
	@Override
	public void handleChunkUpdate(ChunkRenderer cr) {
		this.renderManager.scheduleUpdateTask(cr, this.layer, this::rebuildData);
	}
	
	private void rebuildData() {
		this.rebuildData(this.renderManager.getWorldRenderer().getTerrainMap());
	}
	
	public void rebuildData(TextureMap terrainMap) {
		
		this.rebuildArrays(() -> {
			
			this.foreachBlocks((x, y, z, block, renderer, faces) -> {
				renderer.getRenderData(block, x, y, z, faces, terrainMap, this.dataArray);
			});
			
		});
		
	}
	
}
