package fr.theorozier.procgen.client.renderer.world.block;

import fr.theorozier.procgen.client.renderer.world.WorldRenderDataArray;
import fr.theorozier.procgen.common.block.state.BlockState;
import fr.theorozier.procgen.common.world.WorldBase;
import fr.theorozier.procgen.common.world.position.Direction;
import io.msengine.client.renderer.texture.TextureMap;
import io.msengine.client.renderer.texture.TextureMapTile;
import io.msengine.common.util.Color;

public class BlockGrassRenderer extends BlockRenderer {
	
	public TextureMapTile getFaceTile(BlockState block, TextureMap map, Direction face) {
		
		if (face == Direction.TOP) {
			return map.getTile("grass_top");
		} else if (face == Direction.BOTTOM) {
			return map.getTile("dirt");
		} else {
			return map.getTile("grass_side");
		}
		
	}
	
	public TextureMapTile getSideColorTile(TextureMap map) {
		return map.getTile("grass_side_color");
	}
	
	public Color getColorization(WorldBase world, BlockState block, int x, int y, int z) {
		return world.getBiomeAt(x, z).getFoliageColor();
	}
	
	@Override
	public void getRenderData(WorldBase world, BlockState block, int bx, int by, int bz, float x, float y, float z, BlockFaces faces, TextureMap map, WorldRenderDataArray dataArray) {
		
		Color color = this.getColorization(world, block, bx, by, bz);
		TextureMapTile sideTile = this.getSideColorTile(map);
		int occlData = block.isBlockOpaque() ? computeAmbientOcclusion(world, bx, by, bz, faces) : 0;
		
		if (faces.isTop()) {
			
			dataArray.faceTop(x, y + 1, z, 1, 1);
			dataArray.faceTopColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.TOP), posRand(x, y, z) % 4);
			dataArray.faceIndices();
			
		}
		
		if (faces.isBottom()) {
			
			dataArray.faceBottom(x, y, z, 1, 1);
			dataArray.faceBottomColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.BOTTOM));
			dataArray.faceIndices();
			
		}
		
		if (faces.isNorth()) {
			
			dataArray.faceNorth(x + 1, y, z, 1, 1);
			dataArray.faceNorth(x + 1, y, z, 1, 1);
			dataArray.faceNorthColor(Color.WHITE, OCCLUSION_FACTOR, occlData);
			dataArray.faceNorthColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.NORTH));
			dataArray.faceTexcoords(sideTile);
			dataArray.faceIndices();
			dataArray.faceIndices();
			
		}
		
		if (faces.isSouth()) {
			
			dataArray.faceSouth(x, y, z, 1, 1);
			dataArray.faceSouth(x, y, z, 1, 1);
			dataArray.faceSouthColor(Color.WHITE, OCCLUSION_FACTOR, occlData);
			dataArray.faceSouthColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.SOUTH));
			dataArray.faceTexcoords(sideTile);
			dataArray.faceIndices();
			dataArray.faceIndices();
			
		}
		
		if (faces.isEast()) {
			
			dataArray.faceEast(x, y, z + 1, 1, 1);
			dataArray.faceEast(x, y, z + 1, 1, 1);
			dataArray.faceEastColor(Color.WHITE, OCCLUSION_FACTOR, occlData);
			dataArray.faceEastColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.EAST));
			dataArray.faceTexcoords(sideTile);
			dataArray.faceIndices();
			dataArray.faceIndices();
			
		}
		
		if (faces.isWest()) {
			
			dataArray.faceWest(x, y, z, 1, 1);
			dataArray.faceWest(x, y, z, 1, 1);
			dataArray.faceWestColor(Color.WHITE, OCCLUSION_FACTOR, occlData);
			dataArray.faceWestColor(color, OCCLUSION_FACTOR, occlData);
			dataArray.faceTexcoords(this.getFaceTile(block, map, Direction.WEST));
			dataArray.faceTexcoords(sideTile);
			dataArray.faceIndices();
			dataArray.faceIndices();
			
		}
		
	}
	
}