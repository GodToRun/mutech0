package techtest;

import mutech0.UVCoords;

public class Block {
	public int id;
	public UVCoords topcoord, sidecoord, bottomcoord;
	public static Block[] tiles = new Block[128];
	public static final Block GRASS, DIRT, STONE;
	public static final float imgwidth = 128f;
	public static final float totalBlock = imgwidth / 16f;
	static {
		GRASS = new Block(1, sort(0), sort(1), sort(2));
		DIRT = new Block(2, sort(2), sort(2), sort(2));
		STONE = new Block(3, sort(3), sort(3), sort(3));
	}
	public static UVCoords sort(int x) {
		return new UVCoords((float)x, 0f, totalBlock, 1f);
	}
	public Block(int id, UVCoords topcoord, UVCoords sidecoord, UVCoords bottomcoord) {
		this.topcoord = topcoord;
		this.sidecoord = sidecoord;
		this.bottomcoord = bottomcoord;
		this.id = id;
		tiles[id] = this;
	}
}
