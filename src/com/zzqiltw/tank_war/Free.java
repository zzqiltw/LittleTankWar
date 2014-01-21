package com.zzqiltw.tank_war;
import java.awt.*;
import java.util.Random;

public class Free {
	private int x,y,w,h;
	private TankClient tc = null;
	
	private boolean live = true;
	
	private int step = 0;
	
	private int[][] points = new int[80][2];
	
	public Free() {
		x = points[0][0];
		y = points[0][1];
		w = h = 20;
		
		for (int i = 0; i < 80; ++i) {
			points[i][0] = i * 10;
			points[i][1] = 270;
		}
	}
	
	public void move() {
		++step;
		if (step == points.length) {
			step = 0;
		}
		
		x = points[step][0];
		y = points[step][1];
	}
	
	
	public void draw(Graphics g) {
		if (!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		move();
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean b) {
		this.live = b;
	}
}
