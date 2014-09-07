/**
 * Do what you want 'cause a pirate is free, You are a pirate! This work is licensed under the
 * Creative Commons Attribution 4.0 International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, 171 Second
 * Street, Suite 300, San Francisco, California, 94105, USA.
 */
package Engine;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author hara
 */
public class RedTilemap extends RedObject {

	public int[][] data;
	public RedSprite[] tiles;
	public RedImage tilesImage;
	public double tileWidth;
	public double tileHeight;
	public double widthInTiles;
	public double heightInTiles;
	public boolean dirty = false;

	public RedTilemap(int X, int Y) {
		super(X, Y, 0, 0);
	}

	public void loadTilemap(int[][] Data, RedImage TilesImage, double TileWidth, double TileHeight) {
		data = new int[Data.length][Data[0].length];
		width = Data.length * TileWidth;
		height = Data[0].length * TileHeight;
		for (int X = 0; X < Data.length; X++) {
			System.arraycopy(Data[X], 0, data[X], 0, Data[0].length);
		}
		tilesImage = TilesImage;
		tileWidth = TileWidth;
		tileHeight = TileHeight;
		if (TilesImage != null) {
			tiles = new RedSprite[(int) ((TilesImage.width / TileWidth) * (TilesImage.height / TileHeight))];
			for (int Y = 0; Y < TilesImage.height / TileHeight; Y++) {
				for (int X = 0; X < TilesImage.width / TileWidth; X++) {

					tiles[(int) (Y * TilesImage.height / TileHeight + X)] = new RedSprite(0, 0, TileWidth, TileHeight);
					tiles[(int) (Y * TilesImage.height / TileHeight + X)].loadImage(TilesImage.getSubImage(X * (int) TileWidth, Y * (int) TileHeight, (int) TileWidth, (int) TileHeight), (int) TileWidth, (int) TileHeight);
				}
			}
		} else {
			tiles = new RedSprite[2];
			tiles[0] = new RedSprite(0, 0, TileWidth, TileHeight);
			tiles[0].allowCollisions = 0;
			tiles[1] = new RedSprite(0, 0, TileWidth, TileHeight);
		}
	}

	public void loadTilemap(String DataPath, int WidthInTiles, int HeightInTiles, RedImage TilesImage, double TileWidth, double TileHeight) {
		width = WidthInTiles * TileWidth;
		height = HeightInTiles * TileHeight;
		widthInTiles = WidthInTiles;
		heightInTiles = HeightInTiles;
		tileWidth = TileWidth;
		tileHeight = TileHeight;
		data = new int[WidthInTiles][HeightInTiles];
		byte helper;

		InputStream dataStream = getClass().getResourceAsStream(DataPath);

		for (int Y = 0; Y < HeightInTiles; Y++) {
			for (int X = 0; X < WidthInTiles; X++) {
				helper = 0;
				try {
					while (helper >= 0 && helper <= 9) {
						helper = (byte) (dataStream.read() - '0');
						if (helper >= 0 && helper <= 9) {
							data[X][Y] = data[X][Y] * 10 + helper;
						}
					}
				} catch (IOException Exception) {
					System.err.println(Exception);
				}
			}
		}
		if (TilesImage != null) {
			tiles = new RedSprite[(int) ((TilesImage.width / TileWidth) * (TilesImage.height / TileHeight))];
			for (int Y = 0; Y < TilesImage.height / TileHeight; Y++) {
				for (int X = 0; X < TilesImage.width / TileWidth; X++) {

					tiles[(int) (Y * TilesImage.height / TileHeight + X)] = new RedSprite(0, 0, TileWidth, TileHeight);
					tiles[(int) (Y * TilesImage.height / TileHeight + X)].loadImage(TilesImage.getSubImage(X * (int) TileWidth, Y * (int) TileHeight, (int) TileWidth, (int) TileHeight), (int) TileWidth, (int) TileHeight);
				}
			}
		} else {
			tiles = new RedSprite[2];
			tiles[0] = new RedSprite(0, 0, TileWidth, TileHeight);
			tiles[0].allowCollisions = 0;
			tiles[1] = new RedSprite(0, 0, TileWidth, TileHeight);
		}
	}

	@Override
	public void draw(RedCanvas Canvas) {
		if (visible) {
			if(dirty) {
				for( RedSprite tile : tiles ) {
					tile.dirty = true;
				}
				dirty = false;
			}
			for (int X = 0; X < data.length; X++) {
				for (int Y = 0; Y < data[0].length; Y++) {
					tiles[data[X][Y]].teleport(x + X * tileWidth, y + Y * tileHeight);
					tiles[data[X][Y]].draw(Canvas);
				}
			}
		}
	}

	@Override
	public void update() {
		super.update();
		for (RedSprite tile : tiles) {
			tile.update();
		}
	}

	public boolean collide(RedObject Object) {
		boolean collided = false;
		RedObject virtualTile = new RedObject(0, 0, tileWidth, tileHeight);
		virtualTile.immovable = immovable;

		RedPoint start, finish;
		start = new RedPoint((Math.min(Object.x, Object.last.x) - Math.max(x, last.x)) / tileWidth, (Math.min(Object.y, Object.last.y) - Math.max(y, last.y)) / tileHeight);
		finish = new RedPoint((Math.max(Object.x, Object.last.x) + Object.width - Math.min(x, last.x)) / tileWidth, (Math.max(Object.y, Object.last.y) + Object.height - Math.min(y, last.y)) / tileHeight);
		if (start.x < 0) {
			start.x = 0;
		}
		if (start.y < 0) {
			start.y = 0;
		}
		if (finish.x > data.length) {
			finish.x = data.length;
		}
		if (finish.y > data[0].length) {
			finish.y = data[0].length;
		}

		for (int Y = (int) start.y; Y < finish.y; Y++) {
			for (int X = (int) start.x; X < finish.x; X++) {
				virtualTile.x = X * tileWidth + x;
				virtualTile.y = Y * tileHeight + y;
				virtualTile.last.x = X * tileWidth + last.x;
				virtualTile.last.y = Y * tileHeight + last.y;

				virtualTile.allowCollisions = tiles[ data[X][Y]].allowCollisions;
				virtualTile.velocity.x = velocity.x;
				virtualTile.velocity.y = velocity.y;
				collided = RedG.collide(virtualTile, Object) || collided;
				velocity.x = virtualTile.velocity.x;
				velocity.y = virtualTile.velocity.y;
			}
		}
		return collided;
	}

	public boolean overlap(RedObject Object) {
		boolean overlapFound = false;
		RedObject virtualTile = new RedObject(0, 0, tileWidth, tileHeight);
		virtualTile.immovable = immovable;

		RedPoint start, finish;
		start = new RedPoint((Math.min(Object.x, Object.last.x) - Math.max(x, last.x)) / tileWidth, (Math.min(Object.y, Object.last.y) - Math.max(y, last.y)) / tileHeight);
		finish = new RedPoint((Math.max(Object.x, Object.last.x) + Object.width - Math.min(x, last.x)) / tileWidth, (Math.max(Object.y, Object.last.y) + Object.height - Math.min(y, last.y)) / tileHeight);
		if (start.x < 0) {
			start.x = 0;
		}
		if (start.y < 0) {
			start.y = 0;
		}
		if (finish.x > data.length) {
			finish.x = data.length;
		}
		if (finish.y > data[0].length) {
			finish.y = data[0].length;
		}

		for (int Y = (int) start.y; Y < finish.y; Y++) {
			for (int X = (int) start.x; X < finish.x; X++) {
				virtualTile.x = X * tileWidth + x;
				virtualTile.y = Y * tileHeight + y;
				virtualTile.last.x = X * tileWidth + last.x;
				virtualTile.last.y = Y * tileHeight + last.y;

				if (tiles[ data[X][Y]].allowCollisions != 0) {
					virtualTile.velocity.x = velocity.x;
					virtualTile.velocity.y = velocity.y;
					overlapFound = RedG.overlap(virtualTile, Object) || overlapFound;
					velocity.x = virtualTile.velocity.x;
					velocity.y = virtualTile.velocity.y;
				}
			}
		}
		return overlapFound;
	}

	public static int[][] imageToData(RedImage Image) {
		int[][] result = new int[Image.width][Image.height];

		for (int X = 0; X < Image.width; X++) {
			for (int Y = 0; Y < Image.height; Y++) {
				if (Image.getRGB(X, Y) == 0xff000000) {
					result[X][Y] = 1;
				}
			}
		}

		return result;
	}
}
