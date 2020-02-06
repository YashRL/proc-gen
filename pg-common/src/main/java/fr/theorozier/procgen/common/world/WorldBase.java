package fr.theorozier.procgen.common.world;

import fr.theorozier.procgen.common.block.state.BlockState;
import fr.theorozier.procgen.common.entity.Entity;
import fr.theorozier.procgen.common.phys.AxisAlignedBB;
import fr.theorozier.procgen.common.world.biome.Biome;
import fr.theorozier.procgen.common.world.chunk.WorldChunk;
import fr.theorozier.procgen.common.world.chunk.WorldSection;
import fr.theorozier.procgen.common.world.event.WorldEntityListener;
import fr.theorozier.procgen.common.world.event.WorldMethodEventManager;
import fr.theorozier.procgen.common.world.position.*;
import io.msengine.common.util.event.MethodEventManager;
import io.sutil.math.MathHelper;
import io.sutil.pool.FixedObjectPool;

import java.util.*;
import java.util.function.Consumer;

/**
 *
 * Base world class storing data needed for client and server.<br>
 * In reality this a world dimension, stored and managed by a {@link WorldDimensionManager}.
 *
 * @author Theo Rozier
 *
 */
public abstract class WorldBase implements WorldAccessor {

	protected final Map<SectionPositioned, WorldSection> sections = new HashMap<>();
	protected final Map<Long, Entity> entitiesById = new HashMap<>();
	protected final List<Entity> entities = new ArrayList<>();
	protected final List<Entity> entitiesView = Collections.unmodifiableList(this.entities);
	
	protected long time;
	
	protected final MethodEventManager eventManager;
	
	public WorldBase() {
		
		this.time = 0L;
		
		this.eventManager = new WorldMethodEventManager();
		
	}
	
	public boolean isServer() {
		return false;
	}
	
	public WorldServer getAsServer() {
		throw new UnsupportedOperationException("This world is not a server world.");
	}
	
	/**
	 * Run a single tick in this world.
	 */
	public void update() {
		
		++this.time;
		
	}
	
	/**
	 * @return Get currnet game tick time.
	 */
	public long getTime() {
		return this.time;
	}
	
	/**
	 * @return Number of chunks in a section's height.
	 */
	public int getVerticalChunkCount() {
		return 16;
	}
	
	/**
	 * @return The height limit of the world.
	 */
	public int getHeightLimit() {
		return this.getVerticalChunkCount() * 16;
	}
	
	public MethodEventManager getEventManager() {
		return this.eventManager;
	}
	
	// SECTIONS //
	
	/**
	 * Internal method to get a section a specific position.
	 * @param pos The section position.
	 * @return Section at this position, or <b>NULL</b> if no section loaded there.
	 */
	protected WorldSection getSectionAt(SectionPositioned pos) {
		return this.sections.get(pos);
	}
	
	/**
	 * Internal method to get a section at specific position.
	 * @param x The X section coordinate.
	 * @param z The Z section coordinate.
	 * @return Section at this position, or <b>NULL</b> if no section loaded there.
	 */
	protected WorldSection getSectionAt(int x, int z) {
		try (FixedObjectPool<SectionPosition>.PoolObject pos = SectionPosition.POOL.acquire()) {
			return this.sections.get(pos.get().set(x, z));
		}
	}
	
	/**
	 * Internal method to get a section at specific block position.
	 * @param x The X block coordinate.
	 * @param z The Y block coordinate.
	 * @return Section where the block is positionned, or <b>NULL</b> if no section loaded there.
	 */
	protected WorldSection getSectionAtBlock(int x, int z) {
		return this.getSectionAt(x >> 4, z >> 4);
	}
	
	public boolean isSectionLoadedAt(int x, int z) {
		try (FixedObjectPool<SectionPosition>.PoolObject pos = SectionPosition.POOL.acquire()) {
			return this.sections.containsKey(pos.get().set(x, z));
		}
	}
	
	public boolean isSectionLoadedAtBlock(int x, int z) {
		return this.isSectionLoadedAt(x >> 4, z >> 4);
	}
	
	public void forEachSection(Consumer<WorldSection> cons) {
		this.sections.values().forEach(cons);
	}
	
	// CHUNKS //
	
	@Override
	public WorldChunk getChunkAt(int x, int y, int z) {
		WorldSection section = this.getSectionAt(x, z);
		return section == null ? null : section.getChunkAt(y);
	}
	
	// BIOMES //
	
	@Override
	public Biome getBiomeAt(int x, int z) {
		WorldSection section = this.getSectionAtBlock(x, z);
		return section == null ? null : section.getBiomeAtBlock(x, z);
	}
	
	// BLOCKS //
	
	@Override
	public BlockState getBlockAt(int x, int y, int z) {
		WorldChunk chunk = this.getChunkAtBlock(x, y, z);
		return chunk == null ? null : chunk.getBlockAt(x & 15, y & 15, z & 15);
	}
	
	@Override
	public void setBlockAt(int x, int y, int z, BlockState state) {
		WorldChunk chunk = this.getChunkAtBlock(x, y, z);
		if (chunk != null) chunk.setBlockAt(x & 15, y & 15, z & 15, state);
	}
	
	// ENTITIES //
	
	protected final boolean addEntity(Entity entity) {
		
		if (this.entitiesById.put(entity.getUid(), entity) == null) {
			
			this.entities.add(entity);
			this.eventManager.fireListeners(WorldEntityListener.class, l -> l.worldEntityAdded(this, entity));
			
			return true;
			
		} else {
			return false;
		}
		
	}
	
	public final List<Entity> getEntitiesView() {
		return this.entitiesView;
	}
	
	// UTILITES //
	
	public void forEachBoundingBoxesIn(AxisAlignedBB boundingBox, Consumer<AxisAlignedBB> bbConsumer) {
	
		int xMin = MathHelper.floorDoubleInt(boundingBox.getMinX()) - 1;
		int xMax = MathHelper.ceilingDoubleInt(boundingBox.getMaxX()) + 1;
		
		int yMin = MathHelper.floorDoubleInt(boundingBox.getMinY()) - 1;
		int yMax = MathHelper.ceilingDoubleInt(boundingBox.getMaxY()) + 1;
		
		int zMin = MathHelper.floorDoubleInt(boundingBox.getMinZ()) - 1;
		int zMax = MathHelper.ceilingDoubleInt(boundingBox.getMaxZ()) + 1;
		
		BlockState state;
		
		for (int y = yMin; y <= yMax; ++y)
			for (int x = xMin; x <= xMax; ++x)
				for (int z = zMin; z <= zMax; ++z)
					if ((state = this.getBlockAt(x, y, z)) != null)
						state.forEachCollidingBoundingBox(x, y, z, boundingBox, bbConsumer);
		
	}
	
	public void forEachChunkPosNear(float x, float y, float z, int range, boolean wholeY, Consumer<BlockPosition> consumer) {
		
		ImmutableBlockPosition chunkPos = new ImmutableBlockPosition(MathHelper.floorFloatInt(x) >> 4, MathHelper.floorFloatInt(y) >> 4, MathHelper.floorFloatInt(z) >> 4);
		BlockPosition minPos = new BlockPosition(chunkPos).sub(range, range, range);
		
		int xmax = chunkPos.getX() + range;
		int ymax = wholeY ? this.getVerticalChunkCount() : chunkPos.getY() + range;
		int zmax = chunkPos.getZ() + range;
		
		BlockPosition temp = new BlockPosition();
		
		for (int xv = minPos.getX(); xv <= xmax; ++xv)
			for (int yv = (wholeY ? 0 : minPos.getY()); yv <= ymax; ++yv)
				for (int zv = minPos.getZ(); zv <= zmax; ++zv)
					consumer.accept(temp.set(xv, yv, zv));
		
	}
	
	public void forEachChunkNear(float x, float y, float z, int range, Consumer<WorldChunk> consumer) {
		
		this.forEachChunkPosNear(x, y, z, range, false, pos -> {
			WorldChunk ck = this.getChunkAt(pos.getX(), pos.getY(), pos.getZ());
			if (ck != null) consumer.accept(ck);
		});
		
	}
	
	public void forEachSectionPosNear(float x, float z, int range, Consumer<SectionPosition> consumer) {
		
		ImmutableSectionPosition sectionPos = new ImmutableSectionPosition(MathHelper.floorFloatInt(x) >> 4, MathHelper.floorFloatInt(z) >> 4);
		SectionPosition minPos = new SectionPosition(sectionPos).sub(range, range);
		
		int xmax = sectionPos.getX() + range;
		int zmax = sectionPos.getZ() + range;
		
		try (FixedObjectPool<SectionPosition>.PoolObject obj = SectionPosition.POOL.acquire()) {
			
			for (int xv = minPos.getX(); xv <= xmax; ++xv)
				for (int zv = minPos.getZ(); zv <= zmax; ++zv)
					consumer.accept(obj.get().set(xv, zv));
			
		}
		
	}
	
}
