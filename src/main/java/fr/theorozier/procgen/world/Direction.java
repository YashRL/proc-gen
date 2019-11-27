package fr.theorozier.procgen.world;

public enum Direction {
	
	TOP    ( 0,  1,  0, 1, Axis.Y),
	BOTTOM ( 0, -1,  0, 0, Axis.Y),
	NORTH  ( 1,  0,  0, 3, Axis.X),
	SOUTH  (-1,  0,  0, 2, Axis.X),
	EAST   ( 0,  0,  1, 5, Axis.Z),
	WEST   ( 0,  0, -1, 4, Axis.Z);
	
	public final int rx, ry, rz;
	public final int opositeOrdinal;
	public final Axis axis;
	public final WorldBlockPosition pos;
	
	Direction(int rx, int ry, int rz, int opositeOrdinal, Axis axis) {
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.opositeOrdinal = opositeOrdinal;
		this.axis = axis;
		
		this.pos = new WorldBlockPosition(rx, ry, rz);
		
	}
	
	public Direction oposite() {
		return Direction.values()[this.opositeOrdinal];
	}
	
}