package de.nerogar.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.nerogar.game.entity.EntityBone;

public class MapLoader {

	private static int getID(int color) {
		switch (color & 0xffffff) {
		case 0xffffff:
			return Map.FLOOR.id;
		case 0x969696:
			return Map.WALL_V.id;// |
		case 0x828282:
			return Map.WALL_H.id;// -
		case 0xb4b4b4:
			return Map.WALL_TR.id;// |-
		case 0xf0f0f0:
			return Map.WALL_TU.id;// -^-
		case 0xdcdcdc:
			return Map.WALL_TD.id;// -,-
		case 0xc8c8c8:
			return Map.WALL_TL.id;// -|
		case 0x3c3c3c:
			return Map.WALL_RU.id;// L
		case 0x646464:
			return Map.WALL_RD.id;// ,-
		case 0x282828:
			return Map.WALL_LU.id;// -^
		case 0x505050:
			return Map.WALL_LD.id;// -,
		case 0x0a0a0a:
			return Map.WALL_CROSS.id;// +

		case 0x007800:
			return Map.FENCE_V.id;
		case 0x006400:
			return Map.FENCE_H.id;
		case 0x00b400:
			return Map.FENCE_TD.id;
		case 0x00c800:
			return Map.FENCE_TU.id;
		case 0x00dc00:
			return Map.FENCE_TR.id;
		case 0x000a00:
			return Map.FENCE_DOOR_UL.id;
		case 0x001e00:
			return Map.FENCE_DOOR_UR.id;
		case 0x001400:
			return Map.FENCE_DOOR_DL.id;
		case 0x002800:
			return Map.FENCE_DOOR_DR.id;
		case 0x003200:
			return Map.FENCE_DOOR_RU.id;
		case 0x004600:
			return Map.FENCE_DOOR_RD.id;
		case 0x005000:
			return Map.FENCE_DOOR_LD.id;
		case 0x003c00:
			return Map.FENCE_DOOR_LU.id;

		case 0xf00000:
			return Map.FLOOR.id;//bones

		case 0x000000:
			return Map.TREE.id;
			
		case 0x00000a:
			return Map.STONES.id;
			
		case 0x969600:
			return Map.TORCH.id;
			//old

			/*case 0x00a000:
				return Map.FLOOR.id;
			case 0x000000:
				return Map.ROCK.id;
			case 0x006000:
				return Map.TREE.id;
			case 0x008080:
				return Map.TORCH.id;
			case 0x000080:
				return Map.CHEST.id;
			case 0x0000ff:
				return Map.DOOR.id;
			case 0x00ffff:
				return Map.FLOOR.id;//spawn
			case 0xffaa00:
				return Map.FLOOR.id;
			*/
		default:
			return Map.FLOOR.id;
		}
	}

	private static boolean isSpawn(int color) {
		return (color & 0xffffff) == 0xffff;
	}

	private static boolean isBone(int color) {
		return (color & 0xffffff) == 0xf00000;
	}

	private static boolean isEnemySpawn(int color) {
		return (color & 0xffffff) == 0xffff00;
	}

	public static Map loadMap(int worldType, String filename) {
		BufferedImage image;
		int[] pixels;
		int width;
		int height;

		Vector playerPos = new Vector();
		ArrayList<Vector> enemySpawnLocationsList = new ArrayList<Vector>();

		try {
			image = ImageIO.read(new File("res/" + filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);

		Map map;
		if (worldType == Map.SERVER_WORLD) {
			map = new Map(worldType);
		} else {
			map = new Map(worldType);
		}

		int mapSize = width;
		int[] tileIDs = new int[mapSize * mapSize];

		for (int i = 0; i < pixels.length; i++) {
			tileIDs[i] = getID(pixels[i]);

			if (isSpawn(pixels[i])) {
				playerPos.setX(i % mapSize).setY(i / mapSize);
			} else if (isBone(pixels[i])) {
				map.spawnEntity(new EntityBone(map, new Vector(i % mapSize, i / mapSize)));
			} else if (isEnemySpawn(pixels[i])) {
				enemySpawnLocationsList.add(new Vector(i % mapSize, i / mapSize));
			}
		}

		Vector[] enemySpawnLocations = new Vector[enemySpawnLocationsList.size()];
		enemySpawnLocationsList.toArray(enemySpawnLocations);

		map.load(tileIDs, mapSize, playerPos, enemySpawnLocations);
		return map;
	}
}
