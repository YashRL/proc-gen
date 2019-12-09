package fr.theorozier.procgen.renderer.world.layer;

import fr.theorozier.procgen.block.BlockRenderLayer;
import fr.theorozier.procgen.renderer.world.WorldRenderer;
import fr.theorozier.procgen.renderer.world.block.BlockFaces;
import fr.theorozier.procgen.renderer.world.block.BlockRenderer;
import fr.theorozier.procgen.renderer.world.block.BlockRenderers;
import fr.theorozier.procgen.util.array.BufferedFloatArray;
import fr.theorozier.procgen.util.array.BufferedIntArray;
import fr.theorozier.procgen.world.Axis;
import fr.theorozier.procgen.world.Direction;
import fr.theorozier.procgen.world.World;
import fr.theorozier.procgen.world.chunk.Chunk;
import fr.theorozier.procgen.world.chunk.WorldBlock;

public abstract class ChunkLayerData {
	
	private final Chunk chunk;
	private final World world;
	private final BlockRenderLayer layer;
	
	protected final BufferedFloatArray vertices = new BufferedFloatArray();
	protected final BufferedFloatArray texcoords = new BufferedFloatArray();
	protected final BufferedIntArray indices = new BufferedIntArray();
	
	private boolean needUpdate = false;
	
	public ChunkLayerData(Chunk chunk, BlockRenderLayer layer) {
		
		this.chunk = chunk;
		this.world = chunk.getWorld();
		this.layer = layer;
		
	}
	
	public BlockRenderLayer getLayer() {
		return this.layer;
	}
	
	public boolean doNeedUpdate() {
		return this.needUpdate;
	}
	
	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}
	
	public abstract void handleNewViewPosition(WorldRenderer renderer, int x, int y, int z);
	public abstract void handleChunkUpdate(WorldRenderer renderer);
	
	protected void rebuildArrays(Runnable run) {
		
		this.vertices.setSize(0);
		this.texcoords.setSize(0);
		this.indices.setSize(0);
		
		run.run();
		
		this.vertices.checkOverflow();
		this.texcoords.checkOverflow();
		this.indices.checkOverflow();
		
	}
	
	public boolean isEmpty() {
		return this.indices.getSize() == 0;
	}
	
	public int getIndicesCount() {
		return this.indices.getSize();
	}
	
	public BufferedFloatArray getVertices() {
		return this.vertices;
	}
	
	public BufferedFloatArray getTexcoords() {
		return this.texcoords;
	}
	
	public BufferedIntArray getIndices() {
		return this.indices;
	}
	
	// Utils //
	
	protected void foreachBlocks(BlockConsumer consumer) {
		
		BlockFaces faces = new BlockFaces();
		WorldBlock worldBlock;
		BlockRenderer renderer;
		
		int cx = this.chunk.getChunkPosition().getX();
		int cy = this.chunk.getChunkPosition().getY();
		int cz = this.chunk.getChunkPosition().getZ();
		
		int wx, wy, wz;
		
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					
					if (!this.chunk.hasBlockAtRelative(x, y, z))
						continue;
					
					worldBlock = this.chunk.getBlockAtRelative(x, y, z);
					
					if (!worldBlock.isInRenderLayer(this.layer))
						continue;
					
					renderer = BlockRenderers.getRenderer(worldBlock.getBlockType());
					
					if (renderer != null) {
						
						wx = cx + x;
						wy = cy + y;
						wz = cz + z;
						
						if (y < 15)
							faces.setFaceBlock(worldBlock, Direction.TOP, this.chunk.getBlockAtRelative(x, y + 1, z));
						else faces.setFaceBlock(worldBlock, Direction.TOP, this.world.getBlockAt(wx, wy + 1, wz));
						
						if (y > 0)
							faces.setFaceBlock(worldBlock, Direction.BOTTOM, this.chunk.getBlockAtRelative(x, y - 1, z));
						else faces.setFaceBlock(worldBlock, Direction.BOTTOM, this.world.getBlockAt(wx, wy - 1, wz));
						
						if (x < 15)
							faces.setFaceBlock(worldBlock, Direction.NORTH, this.chunk.getBlockAtRelative(x + 1, y, z));
						else faces.setFaceBlock(worldBlock, Direction.NORTH, this.world.getBlockAt(wx + 1, wy, wz));
						
						if (x > 0)
							faces.setFaceBlock(worldBlock, Direction.SOUTH, this.chunk.getBlockAtRelative(x - 1, y, z));
						else faces.setFaceBlock(worldBlock, Direction.SOUTH, this.world.getBlockAt(wx - 1, wy, wz));
						
						if (z < 15)
							faces.setFaceBlock(worldBlock, Direction.EAST, this.chunk.getBlockAtRelative(x, y, z + 1));
						else faces.setFaceBlock(worldBlock, Direction.EAST, this.world.getBlockAt(wx, wy, wz + 1));
						
						if (z > 0)
							faces.setFaceBlock(worldBlock, Direction.WEST, this.chunk.getBlockAtRelative(x, y, z - 1));
						else faces.setFaceBlock(worldBlock, Direction.WEST, this.world.getBlockAt(wx, wy, wz - 1));
						
						consumer.accept(wx, wy, wz, worldBlock, renderer, faces);
						
					}
					
				}
			}
		}
		
	}
	
	protected interface BlockConsumer {
		void accept(int x, int y, int z, WorldBlock block, BlockRenderer renderer, BlockFaces faces);
	}
	
}
