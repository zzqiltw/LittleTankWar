package com.zzqiltw.tank_war;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class Tank {

	public static final int X_SPEED = 5;
	public static final int Y_SPEED = 5;

	
	
	TankClient tc = null;			//持有对象引用
	
	private static Random rand = new Random();  //AI
	private int step = rand.nextInt(10) + 5;		//AI走step步后随机转向
	
	private int x;
	private int y;

	private int oldX;
	private int oldY;

	private int life = 5;
	private BloodBar blood = new BloodBar();
	
	private boolean bL = false;
	private boolean bU = false;
	private boolean bR = false;
	private boolean bD = false;

	private boolean live = true;
	private boolean good;

	private Direction dir = Direction.STOP; // 默认停止
	private Direction ptDir = Direction.D;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	/*
	 * 反射
	 */
	private static Image[] tankImgs = null;
	private static Map<String,Image> imgs = new HashMap<String,Image>();
	static {
		tankImgs = new Image[] {
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
		};
		
		imgs.put("L", tankImgs[0]);
		imgs.put("LU", tankImgs[1]);
		imgs.put("R", tankImgs[2]);
		imgs.put("RU", tankImgs[3]);
		imgs.put("LD", tankImgs[4]);
		imgs.put("RD", tankImgs[5]);
		imgs.put("D", tankImgs[6]);
		imgs.put("U", tankImgs[7]);
		
	}
	
	public static final int WIDTH  = 30;
	public static final int HEIGHT = 30;
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	private void stay() {
		this.x = this.oldX;
		this.y = this.oldY;
	}

	public void draw(Graphics g) {
		if (!live) {
			if (!good)
				tc.tanks.remove(this);
			return;
		}
		
		if (good) {
			blood.draw(g);
		}
				
		switch (ptDir) {
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
		
		this.oldX = x;
		this.oldY = y;
		
		switch (dir) {
		case L:
			x -= X_SPEED;
			break;

		case U:
			y -= Y_SPEED;
			break;
		
		case R:
			x += X_SPEED;
			break;
		
		case D:
			y += Y_SPEED;
			break;
			
		case LD:
			x -= X_SPEED;
			y += Y_SPEED;
			break;
			
		case RD:
			x += X_SPEED;
			y += Y_SPEED;
			break;
			
		case RU:
			x += X_SPEED;
			y -= Y_SPEED;
			break;
			
		case LU:
			x -= X_SPEED;
			y -= Y_SPEED;
			break;
			
		case STOP:
			break;
		}
		
		if (this.dir != Direction.STOP)
			this.ptDir = this.dir;
		
		if (x < 0)  x = 0;
		if (y < 30) y = 30;
		if (x + Tank.WIDTH  > TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH  - Tank.WIDTH;
		if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if (!good) {
			Direction[] dirs = Direction.values();
			if (step == 0) {
				step = rand.nextInt(10) + 5;
				int randomNumber = rand.nextInt(dirs.length);
				dir = dirs[randomNumber];
			}
			--step;
			
			if (rand.nextInt(100) > 97)		//减少炮弹密集程度
				this.fire();
		}
		
		
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		this.locateDirection();
	}

	public void locateDirection() {
		if (bL && !bU && !bR && !bD)       dir = Direction.L;
		else if (bL && bU && !bR && !bD)   dir = Direction.LU;
		else if (!bL && bU && !bR && !bD)  dir = Direction.U;
		else if (!bL && bU && bR && !bD)   dir = Direction.RU;
		else if (!bL && !bU && bR && !bD)  dir = Direction.R;
		else if (!bL && !bU && bR && bD)   dir = Direction.RD;
		else if (!bL && !bU && !bR && bD)  dir = Direction.D;
		else if (bL && !bU && !bR && bD)   dir = Direction.LD;
		else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_R:
			tc.tanks.clear();
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_F2:
			if (!this.live && this.good) {
				this.live = true;
				this.life = 5;
			}
		
		}
		this.locateDirection();//需要重新定向一次！！！
	}

	public Missile fire() {
		if (!live) 
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir, good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir) {
		if (!live) 
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, dir, good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire() {
		Direction[] dirs = Direction.values();
		
		for (int i = 0; i < 8; ++i) {
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public boolean isGood() {
		return good;
	}
	
	public boolean hitWall(Wall w) {
		if (this.isLive() && this.getRect().intersects(w.getRect())) {
			this.stay();			//回到上一个位置，否则永远和墙撞到。
			return true;
		}
		return false;
	}
	
	public boolean hitTank(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); ++i) {
			Tank t = tanks.get(i);
			if (t != this) {
				if (this.isLive() && t.isLive() && this.getRect().intersects(t.getRect())) {
					t.stay();
					this.stay();
					return true;
				}
					
			}
		}
		return false;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean eat(Free f) {
		if (this.isLive() && f.isLive() && this.getRect().intersects(f.getRect())) {
			if (this.getLife() != 5) {
				this.setLife(life + 1);
			}
			f.setLive(false);
			return true;
		}
		return false;
	}
	
	
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.GREEN);
			
			g.drawRect(x, y-10, WIDTH, 10);
			int w = life*WIDTH/5;
			g.fillRect(x, y-10, w, 10);
			
			g.setColor(c);
		}
	}
	
}
