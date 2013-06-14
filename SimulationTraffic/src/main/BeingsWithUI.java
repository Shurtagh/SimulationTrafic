package main;
import java.awt.Color;

import javax.swing.JFrame;


import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.gui.SimpleColorMap;
import agents.BossAgent;
import agents.BuyerAgent;
import agents.DealerAgent;
import agents.ShopAgent;


public class BeingsWithUI extends GUIState {

	public static int FRAME_SIZE = 700;
	public Display2D display;
	public JFrame displayFrame;
	public SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();
	public FastValueGridPortrayal2D backPortrayal = new FastValueGridPortrayal2D();
	
	public BeingsWithUI(SimState state) {
		super(state);
	}
	
	public void start() {
		super.start();
		setupPortrayal();
	}
	
	public void load(SimState state) {
		
	}
	
	public OvalPortrayal2D getBossPortrayal() {
		OvalPortrayal2D o = new OvalPortrayal2D();
		o.scale = 6;
		o.filled = true;
		o.paint = Color.black;
		return o;
	}
	
	public RectanglePortrayal2D getDealerPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.scale = 2;
		r.filled = true;
		r.paint = Color.black;
		return r;
	}
	
	public RectanglePortrayal2D getShopPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.scale = 8;
		r.filled = true;
		r.paint = Color.white;
		return r;
	}
	
	public RectanglePortrayal2D getBuyerPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.scale = 2;
		r.filled = true;
		r.paint = Color.red;
		return r;
	}
	
	public void setupPortrayal() {
		yardPortrayal.setField(Beings.getConstants().getYard());
		yardPortrayal.setPortrayalForClass(BossAgent.class, getBossPortrayal());
		yardPortrayal.setPortrayalForClass(DealerAgent.class, getDealerPortrayal());
		yardPortrayal.setPortrayalForClass(ShopAgent.class, getShopPortrayal());
		yardPortrayal.setPortrayalForClass(BuyerAgent.class,  getBuyerPortrayal());

		
		backPortrayal.setField(Beings.getConstants().getTerritory());
		Color[] color = new Color[(int)Math.pow(2, Beings.getConstants().GANG_NUMBER)];
		for(int i = 0; i < Beings.getConstants().GANG_NUMBER; i++) {
			int value = (int)Math.pow(2, i);
			color[value] = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)); 
		}
		color[0] = new Color(150, 150, 150);
		for (int j = 1; j < (int)Math.pow(2, Beings.getConstants().GANG_NUMBER); j++) {
			double i = Math.log(j)/Math.log(2);
			if (i != Math.round(i)) {
				String binary = getBinary(j);
				int[] value = new int[binary.length()];
				int z = 0, x = 0;
				for (int k = binary.length()-1; k > -1; k--) {
					if (binary.charAt(k) == '1') {
						value[x] = (int) Math.pow(2, z);
						x++;
					}
					z++;
				}
				Color[] c = new Color[value.length];
				int y = 0;
				for(int v: value) {
					c[y++] = color[v];
				}
				color[j] = getMixedColor(c);
			}
		}
		backPortrayal.setMap(new SimpleColorMap(color));
		display.reset();
		display.repaint();
	}
	
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(FRAME_SIZE,FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Simulation d'un traffic de drogue");
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(backPortrayal, "Background");
		display.attach(yardPortrayal, "Yard");
		
	}
	
	public String getBinary (int number) {
		double rest = number;
		String res = "";
		while (rest >= 2) {
			double dividende = rest / 2;
			if (dividende == Math.floor(dividende)) {
				res = 0 + res;
			} else {
				res = 1 + res;
			}
			rest = (int) dividende;
		}
		if  (rest == 1) {
			return 1 + res;
		} else {
			return 0 + res;
		}
	}
	
	public Color getMixedColor(Color[] colors) {
		int red = 0, blue = 0, green = 0;
		for(Color c: colors) {
			red += c.getRed();
			blue += c.getBlue();
			green += c.getGreen();
		}
		return new Color(red/colors.length, green/colors.length, blue/colors.length);
	}

}
