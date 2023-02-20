package mutech0;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Renderer {
	public Mutech0 engine;
	float XStretch = 1, YStretch = 1;
	public Renderer(Mutech0 engine) {
		this.engine = engine;
	}
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (engine != null && engine.screenBuffer != null)
			g2d.drawImage(engine.screenBuffer, 0, 0, (int)(engine.screenBuffer.getWidth() * XStretch), (int)(engine.screenBuffer.getHeight() * YStretch), null);
	}
}
