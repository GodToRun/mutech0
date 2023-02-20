package techtest;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mutech0.Renderer;
import mutech0.Mutech0;
import mutech0.Window;
import mutech0.bufutils.IntBuffer;

public class Game {
	public Mutech0 engine;
	public Renderer renderer;
	public Window window;
	public float vx = 0, vy = 0, vz = 0;
	public float dtick = 0;
	public boolean xcoll = false, ycoll = false, zcoll = true;
	public float cx, cy, cz, px, py, pz;
	public static final int width = 5, height = 5, depth = 5;
	int[] blocks = new int[width * height * depth];
	IntBuffer btex;
	IntBuffer loadTex(int texNum, String pictureName, int sx, int sy, int x1, int y1, int texWidth, int texHeight) {
		try {
			BufferedImage img = ImageIO.read(new File(pictureName));
			IntBuffer b = IntBuffer.alloc(texWidth * texHeight);
			for (int x = sx; x < x1+sx; x++) {
				for (int y = sy; y < y1+sy; y++) {
					int col = img.getRGB(x, y);
					Color color = new Color((col>>16)&0xFF, (col>>8)&0xFF, (col)&0xFF);
					b.seek((x-sx) + (y-sy) * (texWidth));
					b.put(color.getRGB());
				}
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Game(String filePath) {
		engine = new Mutech0(640, 400);
		engine.setFrameBufferSize(320, 200);
		engine.setBufferStretch(2, 2);
		px = -188;
		py = -248;
		pz = -140;
		engine.setShader(new TestShader());
		btex = loadTex(0, "resources/textures/btop.png", 0, 0, 128, 16, 128, 16);
		//tex.put(RED).put(YELLOW).put(RED).put(RED).put(RED).put(RED).put(RED).put(YELLOW).put(YELLOW).put(YELLOW).put(RED).put(YELLOW).put(YELLOW).put(RED).put(RED).put(YELLOW);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					if (Math.random() > 0.2f) {
						int id = 0;
						if (z == depth-1) id = 1; 
						else if (z > depth-4) id = 2;
						else id = 3;
						blocks[(z * width * height) + (y * width) + x] = id;
					}
				}
			}
		}
		while(true) {
			update();
			
			engine.update();
			render();
			engine.window.update();
		}
	}
	public void render() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					if (getTile(x, y, z) > 0)
						renderBlock(x, y, z, getTile(x, y, z));
				}
			}
		}
	}
	int getTile(int x, int y, int z) {
		return blocks[(z * width * height) + (y * width) + x];
	}
	public void renderBlock(int x, int y, int z, int id) {
		engine.identity();
		engine.bindTexture(btex, 128, 16);
		Block b = Block.tiles[getTile(x, y, z)];
		engine.translate(x * 20.0f, y * 20.0f, z * 20.0f);
		
		if ((y > 0 && getTile(x, y-1, z) == 0) || (y == 0)) {
			engine.color(175, 175, 175);
			engine.uvside(b.sidecoord);
	        engine.vertex( 10.0f, 10.0f,-10.0f);        
	        engine.vertex(-10.0f, 10.0f,-10.0f);        
	        engine.vertex(-10.0f, 10.0f, 10.0f);
	        engine.vertex( 10.0f, 10.0f, 10.0f);
		}
		if ((y < height-1 && getTile(x, y+1, z) == 0) || (y == height-1)) {
			engine.color(175, 175, 175);
			engine.uvside(b.sidecoord);        
	        engine.vertex( 10.0f,-10.0f, 10.0f);
	        engine.vertex(-10.0f,-10.0f, 10.0f);
	        engine.vertex(-10.0f,-10.0f,-10.0f);
	        engine.vertex( 10.0f,-10.0f,-10.0f);
		}
		if ((z > 0 && getTile(x, y, z-1) == 0) || (z == 0)) {
			engine.color(155, 155, 155);
			engine.uvside(b.bottomcoord);
	        engine.vertex( 10.0f, 10.0f, 10.0f);
	        engine.vertex(-10.0f, 10.0f, 10.0f);
	        engine.vertex(-10.0f,-10.0f, 10.0f);
	        engine.vertex( 10.0f,-10.0f, 10.0f);
		}
		if ((z < depth-1 && getTile(x, y, z+1) == 0) || (z == depth-1)) {
			engine.color(255, 255, 255);
			engine.uvside(b.topcoord);
	        engine.vertex( 10.0f,-10.0f,-10.0f);
	        engine.vertex(-10.0f,-10.0f,-10.0f);
	        engine.vertex(-10.0f, 10.0f,-10.0f);
	        engine.vertex( 10.0f, 10.0f,-10.0f);
		}
		if ((x < width-1 && getTile(x+1, y, z) == 0) || (x == width-1)) {
			engine.color(200, 200, 200);
			engine.uvside(b.sidecoord);
	        engine.vertex(-10.0f, 10.0f, 10.0f);
	        engine.vertex(-10.0f, 10.0f,-10.0f);
	        engine.vertex(-10.0f,-10.0f,-10.0f);
	        engine.vertex(-10.0f,-10.0f, 10.0f);
		}
		if ((x > 0 && getTile(x-1, y, z) == 0) || (x == 0)) {
			engine.color(200, 200, 200);
			engine.uvside(b.sidecoord);
	        engine.vertex( 10.0f, 10.0f,-10.0f);
	        engine.vertex( 10.0f, 10.0f, 10.0f);
	        engine.vertex( 10.0f,-10.0f, 10.0f);
	        engine.vertex( 10.0f,-10.0f,-10.0f);
		}
        engine.end(4);
	}
	public void update() {
		
		engine.player.x = px + cx;
		engine.player.y = py + cy;
		engine.player.z = pz + cz;
		
		px += vx;
		py += vy;
		pz += vz;
		
		//vz += 0.003f;
		
		float acc = 1 + (float)engine.dt() * 4;
		vx /= acc;
		vy /= acc;
		vz /= acc;
		
		cz /= acc;
		if (engine.key[KeyEvent.VK_RIGHT] == 1) {
			engine.player.rotate((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_W] == 1) {
			
			engine.player.l -= ((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_S] == 1) {
			
			engine.player.l += ((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_LEFT] == 1) {
			engine.player.rotate((float)(-80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_SPACE] == 1) {
			//zcoll = false;
			pz -= engine.dt() * 80;
		}
		if (engine.key[KeyEvent.VK_SHIFT] == 1) {
			//zcoll = false;
			pz += engine.dt() * 80;
		}
		float dx=(float)Math.sin(engine.player.a * (Math.PI/180));
		float dy=(float)Math.cos(engine.player.a * (Math.PI/180));
		float speed = 120;
		if (engine.key[KeyEvent.VK_UP] == 1) {
			//bobbing();
			px += dx*speed*engine.dt();
			py += dy*speed*engine.dt();
		}
		else if (engine.key[KeyEvent.VK_DOWN] == 1) {
			//bobbing();
			px += -dx*speed*engine.dt();
			py += -dy*speed*engine.dt();
		}
	}
	public static void main(String[] args) {
		new Game(args[0]);
	}
}
