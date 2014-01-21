package com.zzqiltw.tank_war;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	
	public static final int X_SPEED = 10;
	public static final int Y_SPEED = 10;
	
	public static final int WIDTH  = 10;
	public static final int HEIGHT = 10;
	
	private int x;
	private int y;
	private Direction dir;
	
	private boolean good;
	private boolean live = true;
	
	private TankClient tc;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	/*
	 * 反射
	 */
	private static Image[] missileImgs = null;
	private static Map<String,Image> imgs = new HashMap<String,Image>();
	static {
		missileImgs = new Image[] {
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
		};
		
		imgs.put("L", missileImgs[0]);
		imgs.put("LU", missileImgs[1]);
		imgs.put("R", missileImgs[2]);
		imgs.put("RU", missileImgs[3]);
		imgs.put("LD", missileImgs[4]);
		imgs.put("RD", missileImgs[5]);
		imgs.put("D", missileImgs[6]);
		imgs.put("U", missileImgs[7]);
		
	}
	
	
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Direction dir, boolean good, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;			//死了就不花了
		}
		switch (dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null );
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null );
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null );
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null );
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null );
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null );
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null );
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null );
			break;
		}
		move();
	}
	
	public void move() {
		switch (dir) {
		case L:
			x -= X_SPEED;
			break;
		case LU:
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
		case U:
			y -= Y_SPEED;
			break;
		case RU:
			x += X_SPEED;
			y -= Y_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case RD:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case LD:
			x -= X_SPEED;
			y += Y_SPEED;
			break;
		}
		
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT ) {
			live = false;
			tc.missiles.remove(this);
		}
	}
	
	
	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}			
	
	public boolean hitTank(Tank t) {			//碰撞检测
		if (this.getRect().intersects(t.getRect()) && t.isLive() && this.live && this.good != t.isGood()) {
			
			if (t.isGood()) {
				t.setLife(t.getLife() - 1);
				if (t.getLife() <= 0) {
					t.setLive(false);
				} 
			} else {
				t.setLive(false);
				tc.setKillCount(tc.getKillCount()+1);
			}
			
			this.live = false;
			
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			
			return true;
		} 
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); ++i) {
			if (hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if (this.isLive() && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
			
	}
	
}
