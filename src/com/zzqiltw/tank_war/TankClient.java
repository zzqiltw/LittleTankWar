package com.zzqiltw.tank_war;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

/**
 * 
 * @author zzq
 * 主类。TankWar主窗口
 * 各类都有这个类的引用
 * 
 *
 */

public class TankClient extends Frame {
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;	
	public static final Color GAME_COLOR = Color.GRAY;
	
	private int killCount = 0;
	
	Tank myTank = new Tank(500, 500, true, Direction.STOP, this);
	
	Wall w1 = new Wall(300,300,20,150,this);
	Wall w2 = new Wall(500,100,200,10,this);
	
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Free f = new Free();
	
	//int maxHit = 0;
	
	//Image offScreenImage = tk.getImage(TankClient.class.getClassLoader().getResource("images/Kanade.gif")) ;

	Image offScreenImage = null;
	
	public void paint(Graphics g) {
		
		if (!myTank.isLive()) {
			g.drawString("Game Over!!!你可以向春哥祈祷让他帮你满血复活", GAME_WIDTH/2, GAME_HEIGHT/2);
			//return;
		}
		
		if (killCount >= 20) {
			myTank.draw(g);
			g.drawString("Holy Shit!!!你已经成为真男人！！", GAME_WIDTH/2, GAME_HEIGHT/2);
			return;
		}
		
		
		if (tanks.size() == 0) {
			for (int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("recallTankCount")); ++i) {
				tanks.add(new Tank(200 + 40*(i+1), 50, false, Direction.D, this));
			}
		}
		
		g.drawString("一共有"+missiles.size()+"发炮弹", 10, 40);
		g.drawString("一共有"+tanks.size()+"辆坦克", 300, 40);
		g.drawString("你还有"+(myTank.getLife()*20)+"%的生命值", 500,40);
		
		for (int i = 0; i < missiles.size(); ++i) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		for (int i = 0; i < explodes.size(); ++i) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for (int i = 0; i < tanks.size(); ++i) {
			Tank t = tanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			t.hitTank(tanks);
			t.draw(g);
		}
		myTank.hitWall(w1);
		myTank.hitWall(w2);
		
		myTank.eat(f);
		
		myTank.draw(g);
		w1.draw(g);
		w2.draw(g);

		f.draw(g);
	}

	public void update(Graphics g) {
		
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = g.getColor();
		gOffScreen.setColor(GAME_COLOR);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		
		paint(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);
		
	}

	/**
	 * 本方法显示主窗口
	 */
	
	public void launchFrame() {
		
		/*
		 * 配置文件中读入。
		 */
		
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		
		for (int i = 0; i < initTankCount; ++i) {
			tanks.add(new Tank(200 + 40*(i+1), 50, false, Direction.D, this));
		}
		
		this.setBounds(100,100,GAME_WIDTH,GAME_HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setTitle("坦克大战精简版");
		this.setBackground(GAME_COLOR);
		
		this.addKeyListener(new KeyMonitor());
		
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	/**
	 * 
	 * @param killCount 击毁Tank数
	 */

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	private class PaintThread implements Runnable {

		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

}
