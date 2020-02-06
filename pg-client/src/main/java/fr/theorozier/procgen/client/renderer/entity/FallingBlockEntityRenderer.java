package fr.theorozier.procgen.client.renderer.entity;

import fr.theorozier.procgen.client.ProcGenGame;
import fr.theorozier.procgen.client.renderer.entity.part.EntityBlockRendererPart;
import fr.theorozier.procgen.client.renderer.world.WorldRenderDataArray;
import fr.theorozier.procgen.client.renderer.world.WorldShaderManager;
import fr.theorozier.procgen.common.block.state.BlockState;
import fr.theorozier.procgen.common.entity.FallingBlockEntity;
import io.msengine.client.renderer.model.ModelHandler;
import io.msengine.client.renderer.texture.Texture;
import io.msengine.client.renderer.texture.TextureMap;

import java.util.HashMap;
import java.util.Map;

public class FallingBlockEntityRenderer extends MotionEntityRenderer<FallingBlockEntity> {
	
	private final Map<BlockState, EntityBlockRendererPart> blockStateRenderers = new HashMap<>();
	private final WorldRenderDataArray dataArray = new WorldRenderDataArray();
	private WorldShaderManager worldShaderManager;
	private TextureMap terrainMap;
	
	public FallingBlockEntityRenderer() { }
	
	@Override
	public void initRenderer(WorldShaderManager shaderManager, WorldRenderDataArray dataArray) {
		super.initRenderer(shaderManager, dataArray);
		this.worldShaderManager = shaderManager;
	}
	
	@Override
	public void initTexture() {
		this.terrainMap = ProcGenGame.getGameInstance().getWorldRenderer().getTerrainMap();
	}
	
	@Override
	public Texture getTexture(FallingBlockEntity entity) {
		return this.terrainMap;
	}
	
	public EntityBlockRendererPart getRendererPart(BlockState state) {
		
		return this.blockStateRenderers.computeIfAbsent(state, st -> {
			
			EntityBlockRendererPart part = new EntityBlockRendererPart(st);
			part.initPart(this.worldShaderManager, this.dataArray);
			return part;
			
		});
		
	}
	
	@Override
	public void renderMotionEntity(float alpha, ModelHandler model, FallingBlockEntity entity) {
		
		EntityBlockRendererPart part = this.getRendererPart(entity.getState());
		part.render();
		
	}
	
}
