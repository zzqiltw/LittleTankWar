package com.zzqiltw.tank_war;
import java.awt.*;
import java.awt.Image;

public class Explode {

	int x;
	int y;
	
	//int[] dm = {4,7,12,18,26,32,49};
	
	private static boolean init = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	/*
	 * 反射
	 */
	private static Image[] imgs = {
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
	};
	
	int step = 0;

	private boolean live = true;
	
	private TankClient tc = null;
	
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		/*
		 * 解决第一张图片没显示的问题
		 */
		if (!init) {
			for (int i = 0; i < imgs.length; i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
			init = true;
		}
		
		if (!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if (step == imgs.length) {
			this.live = false;
			step = 0;
			return;
		} 
		
		g.drawImage(imgs[step], x, y, null);
		
		++step;
	}

}
