package mutech0;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	Mutech0 engine;
	Mutech0Panel panel;
	public int width = 640;
	public int height = 480;
	public Window(Mutech0 engine, int xsiz, int ysiz) {
		this.engine = engine;
		this.width = xsiz;
		this.height = ysiz;
		setTitle("mutech0");
		setSize(xsiz, ysiz);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Mutech0Panel();
        this.add(panel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(new KeyAdapter() { //키 이벤트
			@Override
			public void keyPressed(KeyEvent e) { //키 눌렀을때
				engine.onKey(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				engine.onKeyUp(e);
			}
		});
	}
	public void update() {
		//panel.repaint();
		if (engine.renderer != null) {
			engine.renderer.render(panel.getGraphics());
		}
	}
	class Mutech0Panel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if (engine.renderer != null) {
    			engine.renderer.render(g);
    		}
        }
    }
}
