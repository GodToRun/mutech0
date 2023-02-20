package mutech0;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import mutech0.bufutils.IntBuffer;
import mutech0.shader.Shader;
import mutech0.shader.ShaderObject;
import mutech0.shader.ShaderObjectPool;

public class Mutech0 {
	public int[] key = new int[500];
	public Renderer renderer;
	public BufferedImage screenBuffer;
    public int[] screenPixels;
    public Player player;
	public Window window;
	private int[] zbuffer;
	private float offx = 0, offy = 0, offz = 0;
	private static final int ZBUFFER_INITIAL_VALUE = 2147483647;
	private Shader shader = null;
	private long curTime = System.currentTimeMillis();
    private long lastTime = curTime;
    private long totalTime;
    private double frames;
    private double fps = 100;
    public int clearColor = 0x0088FF;
    private IntBuffer textureBuffer;
    private int textureWidth, textureHeight;
    private int frameBufferWidth, frameBufferHeight;
    private int[] vbuffer;
    private ArrayList<Integer> intersects = new ArrayList<Integer>();
    private ArrayList<Float> vertices = new ArrayList<Float>();
    private ArrayList<UVCoords> uvCoords = new ArrayList<UVCoords>();
    private ArrayList<Color> colors = new ArrayList<Color>();
    UVCoords cuv = null;
    Color col = null;
    
	public Mutech0(int xsiz, int ysiz) {
		player = new Player();
		window = new Window(this, xsiz, ysiz);
		renderer = new Renderer(this);
		screenBuffer = new BufferedImage(xsiz, ysiz, BufferedImage.TYPE_INT_RGB);
		frameBufferWidth = xsiz;
		frameBufferHeight = ysiz;
		screenPixels = ((DataBufferInt)screenBuffer.getRaster().getDataBuffer()).getData();
		zbuffer = new int[screenPixels.length];
		vbuffer = new int[screenPixels.length];
		for (int i = 0; i < zbuffer.length; i++) {
			zbuffer[i] = ZBUFFER_INITIAL_VALUE;
		}
	}
	public void setBufferStretch(float Xs, float Ys) {
		this.renderer.XStretch = Xs;
		this.renderer.YStretch = Ys;
	}
	public void setFrameBufferSize(int width, int height) {
		screenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.frameBufferWidth = width;
		this.frameBufferHeight = height;
		
		screenPixels = ((DataBufferInt)screenBuffer.getRaster().getDataBuffer()).getData();
		zbuffer = new int[width * height];
		vbuffer = new int[width * height];
	}
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	public Shader getShader() {
		return this.shader;
	}
	private int []clipBehindPlayer(int x1, int y1, int z1, int x2, int y2, int z2) {
		int[] arr = new int[3];
		float da=y1;
		float db=y2;
		float d=da-db; if (d<0) {d=1;}
		float s = da/(da-db);
		arr[0] = (int)(x1 + s*(x2-x1));
		arr[1] = (int)(y1 + s*(y2-y1)); if (arr[1]<1) arr[1]=1;
		arr[2] = (int)(z1 + s*(z2-z1));
		return arr;
	}
	public float dist(float x1, float y1, float x2, float y2) {
		float distance = (float)Math.sqrt((double)((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)));
		return distance;
	}
	public float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
		float distance = (float)Math.sqrt((double)((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)) + (z2-z1)*(z2-z1));
		return distance;
	}
	void drawWall(int x1, int x2, int b1, int b2, int t1, int t2, int c, int s, int w) {
		/*int x, y;
		
		int dyb = b2 - b1;
		int dyt = t2-t1;
		int dx = x2-x1; if (dx==0) dx=1;
		int xs=x1;
		
		int tx1 = x1;
		int tx2 = x2;
		
		if(x1<0) {x1=0;}
		if(x2<0) {x2=0;}
		if(x1>=frameBufferWidth) {x1=frameBufferWidth-1;}
		if(x2>=frameBufferWidth) {x2=frameBufferWidth-1;}
		Texture tex = textures[walls[w].sd_textur];
		for (x=x1;x<x2;x++) {
			int y1 = (int)(dyb*(x-xs+0.5)/dx+b1);
			int y2 = (int)(dyt*(x-xs+0.5)/dx+t1);
			
			int ty1 = y1;
			int ty2 = y2;
			if(y1<0) {y1=0;}
			if(y2<0) {y2=0;}
			if(y1>=frameBufferHeight) {y1=frameBufferHeight-1;}
			if(y2>=frameBufferHeight) {y2=frameBufferHeight-1;}
			if (sectors[s].surface==1) {sectors[s].surf[x]=y1; continue;}
			else if (sectors[s].surface==2) {sectors[s].surf[x]=y2; continue;}
			else if (sectors[s].surface==-1) {
				for(y=sectors[s].surf[x];y<y1;y++) {
					int tx = (int)(((float)(x-tx1)/(float)(tx2-tx1)) * (float)tex.width);
					int ty = (int)(((float)y*5/(float)y1) * (float)tex.height) + (int)player.y;
					
					tx %= tex.width;
					ty %= tex.height;
					pixel(x,y, sectors[s].c1);
				}
			}
			else if (sectors[s].surface==-2) {for(y=y2;y<sectors[s].surf[x];y++) {pixel(x,y,sectors[s].c2);}}
			for (y=y1;y<y2;y++) {
				int len = x2-x1;
				int tx = (int)(((float)(x-x1)/(float)((len))) * (float)tex.width);
				int ty = (int)(((float)(y-ty1)/(float)(ty2-ty1)) * (float)tex.height);
				tx %= tex.width;
				ty %= tex.height;
				if (tx < 0) tx = 0;
				if (ty < 0) ty = 0;
				pixel(x, y, tex.texture.get(tx + ty * tex.width).getRGB());
			}
		}
		*/
	}
	public void plotVPixel(int x0, int y0, int x1, int y1, int decide) {
		int blockSize = 1;
        int scaledX0 = x0 / blockSize;
        int scaledY0 = y0 / blockSize;
        int scaledX1 = x1 / blockSize;
        int scaledY1 = y1 / blockSize;
        int dx = scaledX1 - scaledX0;
        int dy = scaledY1 - scaledY0;
        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        int dx2 = dx << 1;
        int dy2 = dy << 1;
        int x = scaledX0;
        int y = scaledY0;
        int error;
        if (dx >= dy) {
            error = dy2 - dx;
            do {
            	vpixel(x, y, -2147483647);
                if (error > 0) {
                    y += stepY;
                    error -= dx2;
                }
                error += dy2;
                x += stepX;
            } while (x != scaledX1);
        } else {
            error = dx2 - dy;
            do {
                vpixel(x, y, -2147483647);
                if (error > 0) {
                    x += stepX;
                    error -= dy2;
                }
                error += dx2;
                y += stepY;
            } while (y != scaledY1);
        }
    }
	public int[] calcOnPoint(float playerx, float playery, float playerz, float yaw, float pitch, float sx, float sy, float sz, float CS, float SN) {
		playerx += offx;
		playery += offy;
		playerz += offz;
		float x1=sx-playerx;
		float y1=sy-playery;
		
		int[] wx = new int[4];
		int[] wy = new int[4];
		int[] wz = new int[4];
		
		wx[0]=(int)(x1*CS-y1*SN);
		wx[2]=wx[0];
		
		wy[0]=(int)(y1*CS+x1*SN);
		wy[2]=wy[0];
		
		wz[0]=(int)(sz-playerz+((pitch*wy[0])/32.0));
		
		if (wy[0]<=0) {
			int[] arr = clipBehindPlayer(wx[0], wy[0], wz[0], wx[1], wy[1], wz[1]);
			wx[0]=arr[0];
			wy[0]=arr[1];
			wz[0]=arr[2];
		}
		
		
		int res=2;
		wx[0]=wx[0]*200/wy[0]+frameBufferWidth/(res); wy[0]=wz[0]*200/wy[0]+frameBufferHeight/(res);
		return new int[] { wx[0], wy[0] };
	}
	public void begin() {
		vertices.clear();
		uvCoords.clear();
		colors.clear();
		intersects.clear();
	}
	public void bindTexture(IntBuffer tex, int width, int height) {
		this.textureBuffer = tex;
		this.textureWidth = width;
		this.textureHeight = height;
	}
	private ShaderObject shaderFrag(Shader shader, Color col, int fcol) {
		if (shader != null) {
			ShaderObject in = ShaderObjectPool.push();
			in.out((byte)0, fcol);
			in.out((byte)1, col.getRGB());
			ShaderObject out = shader.frag(in);
			ShaderObjectPool.pop();
			return out;
		}
		else return null;
	}
	public void identity() {
		vertices.clear();
		uvCoords.clear();
		colors.clear();
		intersects.clear();
		offx = 0;
		offy = 0;
		offz = 0;
		textureBuffer = null;
	}
	public void translate(float x, float y, float z) {
		offx += x;
		offy += y;
		offz += z;
	}
	public void drawArrays(List<Float> vertices, int cut, int snum) {
		if (vertices.size() == 0) return;
		if (vertices.size() / 3 > cut) {
			List<Float> vlist1 = vertices.subList(0, cut * 3);
			if (vertices.size() / 3 > cut) {
				List<Float> vlist2 = vertices.subList(cut * 3, vertices.size());
				drawArrays(vlist2, cut, snum + cut);
			}
			if (vlist1.size() >= 3) {
				
				vertices = vlist1;
			}
		}
		UVCoords uv = new UVCoords(0f, 0f, 1f, 1f);
		Color col = new Color(255, 255, 255);
		if (snum >= 0 && snum < uvCoords.size())
			uv = uvCoords.get(snum);
		if (snum >= 0 && snum < colors.size() && colors.get(snum) != null)
			col = colors.get(snum);
		//System.out.println(uvCoords.size());
		
		float SN = (float)Math.sin(player.a * (Math.PI/180));
		float CS = (float)Math.cos(player.a * (Math.PI/180));
		
		int ix = 2147483647, iy = 2147483647, ax = -2147483647, ay = -2147483647;
		int nz = 0;
		int zi = 0;
		// vertex rendering
		for (int i = 0; i < vertices.size(); i += 3) {
			
			float sx = vertices.get(i), sy = vertices.get(i+1), sz = vertices.get(i+2);
			
			int[] p1 = calcOnPoint(player.x, player.y, player.z, player.a, player.l, sx, sy, sz, CS, SN);
			nz += dist(sx, sy, sz, player.x+offx, player.y+offy, player.z+offz);
			
			ix = (int)Math.min(ix, p1[0]);
			iy = (int)Math.min(iy, p1[1]);
			
			ax = (int)Math.max(ax, p1[0]);
			ay = (int)Math.max(ay, p1[1]);
			if (i >= vertices.size()-3) {
				break;
			}
			int[] p2 = calcOnPoint(player.x, player.y, player.z, player.a, player.l, vertices.get(i+3), vertices.get(i+4), vertices.get(i+5), CS, SN);
			plotVPixel(p1[0], p1[1], p2[0], p2[1], 0);
		}
		if (zi != 0)
			nz /= zi;
		int[] p1 = calcOnPoint(player.x, player.y, player.z, player.a, player.l, vertices.get(vertices.size()-3), vertices.get(vertices.size()-2), vertices.get(vertices.size()-1), CS, SN);
		
		int[] p2 = calcOnPoint(player.x, player.y, player.z, player.a, player.l, vertices.get(0), vertices.get(1), vertices.get(2), CS, SN);
		plotVPixel(p1[0], p1[1], p2[0], p2[1], 0);
		intersects.clear();
		// fill polygon algorithm - scan lines and find intersect points p0 and p1.
		for (int y = iy; y <= ay; y++) {
			for (int x = ix; x <= ax; x++) {
				if (x >= 0 && y >= 0 && x < frameBufferWidth && y < frameBufferHeight && vbuffer[x + y * frameBufferWidth] == -2147483647) {
					intersects.add(x);
				}
			}
			if (intersects.size() > 1) {
				for (int i = 0; i < intersects.size()-1; i++) {
					for (int x = intersects.get(i); x < intersects.get(i+1); x++) {
						if (zbuffer[x + y * frameBufferWidth] > nz) {
							if (textureBuffer != null) {
								
								int tx, ty;
								float dx = x-ix;
								float mx = ax-ix+1;
								float dy = y-iy;
								float my = ay-iy+1;
								tx = (int)(((dx/mx)+uv.x)*((float)textureWidth/uv.scalex));
								ty = (int)(((dy/my)+uv.y)*((float)textureHeight/uv.scaley));
								tx %= textureWidth;
								ty %= textureHeight;
								int fcol = textureBuffer.get(tx + ty * textureWidth);
								ShaderObject out = shaderFrag(shader, col, fcol);
								if (!(boolean)out.in(ShaderObject.DISCARD_FLAG)) {
									zbuffer[x + y * frameBufferWidth] = nz;
									vpixel(x, y, (int)out.in(ShaderObject.OUTCOLOR_FRAG));
									//vbuffer[x + y * frameBufferWidth] = (int)out.in(ShaderObject.COLOR_FRAG);
								}
								ShaderObjectPool.pop();
							}
							else {
								ShaderObject out = shaderFrag(shader, col, 0xFFFFFF);
								if (!(boolean)out.in(ShaderObject.DISCARD_FLAG)) {
									zbuffer[x + y * frameBufferWidth] = nz;
									vpixel(x, y, (int)out.in(ShaderObject.OUTCOLOR_FRAG));
									//vbuffer[x + y * frameBufferWidth] = (int)out.in(ShaderObject.COLOR_FRAG);
								}
								ShaderObjectPool.pop();
							}
							
						}
						
					}
				}
			}
			intersects.clear();
		}
		for (int x = ix; x <= ax; x++) {
			for (int y = iy; y <= ay; y++) {
				if (x >= 0 && y >= 0 && x < frameBufferWidth && y < frameBufferHeight) {
					if (vbuffer[x + y * frameBufferWidth] != 2147483647 && vbuffer[x + y * frameBufferWidth] != -2147483647)
						screenPixels[x + y * frameBufferWidth] = vbuffer[x + y * frameBufferWidth];
					vbuffer[x + y * frameBufferWidth] = 2147483647;
				}
			}
		}
	}
	public void end(int cut) {
		drawArrays(vertices, cut, 0);
	}
	public double dt() {
		return 1D / fps;
	}
	public double fps() {
		return fps;
	}
	public void vertex(float x, float y, float z) {
		vertices.add(x);
		vertices.add(y);
		vertices.add(z);
		uvCoords.add(cuv);
		colors.add(col);
	}
	public void color(int r, int g, int b) {
		col = new Color(r, g, b);
	}
	public void uvside(float x, float y, float scalex, float scaley) {
		cuv = new UVCoords(x, y, scalex, scaley);
	}
	public void uvside(int r, int g, int b) {
		col = new Color(r, g, b);
	}
	public void uvside(UVCoords coord) {
		cuv = coord;
	}
	public void update() {
		if (screenPixels == null) return;
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		
		totalTime += curTime - lastTime;
		if (totalTime > 1000) {
			totalTime -= 1000;
			fps = frames;
			frames = 0;
		}
		frames++;
		
		for (int x = 0; x < frameBufferWidth; x++) {
			for (int y = 0; y < frameBufferHeight; y++) {
				screenPixels[x + y * frameBufferWidth] = clearColor;
				zbuffer[x + y * frameBufferWidth] = ZBUFFER_INITIAL_VALUE;
			}
		}
	}
	public void pixel(int x, int y, int c) {
		if (x >= 0 && x < frameBufferWidth && y >= 0 && y < frameBufferHeight)
			screenPixels[x + y * frameBufferWidth] = c;
	}
	public void vpixel(int x, int y, int c) {
		if (x >= 0 && x < frameBufferWidth && y >= 0 && y < frameBufferHeight)
			vbuffer[x + y * frameBufferWidth] = c;
	}
	public void onKey(KeyEvent e) {
		key[e.getKeyCode()] = 1;
	}
	public void onKeyUp(KeyEvent e) {
		if (key[e.getKeyCode()] == 1)
			key[e.getKeyCode()] = 0;
	}
}
