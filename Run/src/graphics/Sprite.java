package graphics;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class Sprite {

//======================================================================
//
//--------------------------------Fields--------------------------------
//
//======================================================================	

	private Image image;
	
//	private static final String RES_PATH = "file:C:/Users/Caleb/Documents/AHA Backup/AHA/AHA 15-16/AP Computer Science/Run/res/";
	private static final String RES_PATH = "file:D:/Users/Caleb/OneDrive - UW-Madison/eclipse-workspace/Run/res/";
	private static final String SPRITE_SHEET = RES_PATH + "SpriteSheet3.png";
	
	public static final Sprite[][] NULL = {{new Sprite(new Image(RES_PATH + "NULL.jpg"))}};
	
	public static final Sprite[][] NULL_PLATFORM = {{new Sprite(new Image(RES_PATH + "NULL_PLATFORM.jpg"))}};
	
	public static final Sprite[][] PLATFORM1 = {{new Sprite(SPRITE_SHEET, 0, 0, 128, 32)}};
	public static final Sprite[][] PLATFORM2 = {{new Sprite(SPRITE_SHEET, 0, 32, 128, 32)}};
	public static final Sprite[][] PLATFORM3 = {{new Sprite(SPRITE_SHEET, 0, 64, 128, 32)}};
	public static final Sprite[][][] PLATFORMS = {PLATFORM1, PLATFORM2, PLATFORM3};
	
	public static final Sprite[][] ENEMY = {{new Sprite(SPRITE_SHEET, 256 + 2, 0 + 6, 128 - 18, 96 - 6 - 16)}};
	public static final Sprite[][][] ENEMIES = {ENEMY};
	
	public static final Sprite[][] PLAYER = {
			//moving
			{new Sprite(SPRITE_SHEET, 128 + 6, 0, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 64, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 128, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 192, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 256, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 320, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 384, 64 - 6 - 4, 64 - 4),
			new Sprite(SPRITE_SHEET, 128 + 6, 448, 64 - 6 - 4, 64 - 4)},
			//jumping
			{new Sprite(SPRITE_SHEET, 192 + 6, 0, 64 - 6 - 4, 64 - 4)},
			//dead
			{new Sprite(SPRITE_SHEET, 192 + 6, 64, 64 - 6 - 4, 64 - 4)}
		};
	
//======================================================================
//
//-----------------------------Constructors-----------------------------
//
//======================================================================	

	public Sprite(Image image) {
		this.image = image;
	}
	
	public Sprite(String path, int x, int y, int width, int height) {
		try {
			Image spriteSheet = new Image(path);
			PixelReader reader = spriteSheet.getPixelReader();
			image = new WritableImage(reader, x, y, width, height);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("ERROR: SPRITE DIMENSIONS OUTSIDE SPRITESHEET");
		}
	}
	
//======================================================================
//
//-------------------------------Methods--------------------------------
//
//======================================================================
	
	public int height() {
		return (int) image.getHeight();
	}
	
	public int width() {
		return (int) image.getWidth();
	}
	
//======================================================================
//
//---------------------------Getters/Setters----------------------------
//
//======================================================================

	public Image getImage() {
		return image;
	}
	
}
