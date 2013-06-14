package main;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import agents.BeingAbstractAgent;
import agents.BossAgent;
import agents.BuyerAgent;
import agents.CookerAgent;
import agents.CopsAgent;
import agents.DealerAgent;
import agents.FarmerAgent;
import agents.GangMemberAbstractAgent;
import agents.ShopAgent;
import agents.SupplierAgent;
import constantes.Constants;


public class Beings extends SimState {

	private static constantes.Constants constantes;
	
	public Beings(long seed) {
		super(seed);
		constantes = new constantes.Constants();
	}
	
	public void start() {
		super.start();
		constantes.getYard().clear();
		_initialisation();
	}

	/**
	 *	FONCTIONS D'AJOUT D'AGENTS
	 */
	public void _initialisation() {
		for(int i = 0; i < Constants.GANG_NUMBER; i++) {
			int x, y, boss_x, boss_y;
			do {
				boss_x = (int)(Math.random()*Constants.GRID_SIZE);
				boss_y = (int)(Math.random()*Constants.GRID_SIZE);
			} while (!constantes.checkGangCenter(boss_x, boss_y));
			constantes.GANG_CENTER[i][0] = boss_x;
			constantes.GANG_CENTER[i][1] = boss_y;
			
			//Initialisation du boss
			BossAgent ba = new BossAgent(this, boss_x, boss_y, 15, 10, i);
			ba.setStoppable(schedule.scheduleRepeating(ba));
			constantes.yard.setObjectLocation(ba, boss_x, boss_y);
			
			//Initialisation des 4 premiers dealers autour
			x = boss_x - (constantes.LARGEUR_ZONE_DEALER / 2);
			y = boss_y - (constantes.HAUTEUR_ZONE_DEALER / 2);
			DealerAgent da;
			if (x >= 0 && x < constantes.GRID_SIZE && y >= 0 && y < constantes.GRID_SIZE) {
				da = new DealerAgent(x, y, ba, (x - constantes.LARGEUR_ZONE_DEALER / 2), (y - constantes.HAUTEUR_ZONE_DEALER / 2), i);
				da.setStoppable(schedule.scheduleRepeating(da));
				constantes.yard.setObjectLocation(da, x, y);
			}
			x += constantes.LARGEUR_ZONE_DEALER;
			if (x >= 0 && x < constantes.GRID_SIZE && y >= 0 && y < constantes.GRID_SIZE) {
				da = new DealerAgent(x, y, ba, (x - constantes.LARGEUR_ZONE_DEALER / 2), (y - constantes.HAUTEUR_ZONE_DEALER / 2), i);
				da.setStoppable(schedule.scheduleRepeating(da));
				constantes.yard.setObjectLocation(da, x, y);
			}
			y += constantes.HAUTEUR_ZONE_DEALER;
			if (x >= 0 && x < constantes.GRID_SIZE && y >= 0 && y < constantes.GRID_SIZE) {
				da = new DealerAgent(x, y, ba, (x - constantes.LARGEUR_ZONE_DEALER / 2), (y - constantes.HAUTEUR_ZONE_DEALER / 2), i);
				da.setStoppable(schedule.scheduleRepeating(da));
				constantes.yard.setObjectLocation(da, x, y);
			}
			x -= constantes.LARGEUR_ZONE_DEALER;
			if (x >= 0 && x < constantes.GRID_SIZE && y >= 0 && y < constantes.GRID_SIZE) {
				da = new DealerAgent(x, y, ba, (x - constantes.LARGEUR_ZONE_DEALER / 2), (y - constantes.HAUTEUR_ZONE_DEALER / 2), i);
				da.setStoppable(schedule.scheduleRepeating(da));
				constantes.yard.setObjectLocation(da, x, y);
			}
			
			//Initialisation dealer
			int current_x;
			int current_y;
			for (int j = 0; j < constantes.INITIAL_DEALER_NUMBER - 4; j++) {
				do {
					current_x = x;
					current_y = y;
					//DŽplacement sur x
					if (Math.random() > 0.5) {
						//DŽplacement positif
						if (Math.random() > 0.5) {
							x += constantes.LARGEUR_ZONE_DEALER;
						//DŽplacement nŽgatif
						} else {
							x -= constantes.LARGEUR_ZONE_DEALER;
						}
						if (x > constantes.GRID_SIZE || x < 0) {
							x = current_x;
						}
					//DŽplacement sur y	
					} else {
						//DŽplacement positif
						if (Math.random() > 0.5) {
							y += constantes.HAUTEUR_ZONE_DEALER;
						//DŽplacement nŽgatif
						} else {
							y -= constantes.HAUTEUR_ZONE_DEALER;
						}
						if (y > constantes.GRID_SIZE || y < 0) {
							y = current_y;
						}
					}
				} while (!checkPosition(x, y, i));
				da = new DealerAgent(x, y, ba, (x - constantes.LARGEUR_ZONE_DEALER / 2), (y - constantes.HAUTEUR_ZONE_DEALER / 2), i);
				da.setStoppable(schedule.scheduleRepeating(da));
				constantes.yard.setObjectLocation(da, x, y);
			}
			
			//Initialisation farmer
			for (int j = 0; j < constantes.INITIAL_FARMER_NUMBER; j++) {
				do {
					x = (int)(Math.random()*Constants.GRID_SIZE);
					y = (int)(Math.random()*Constants.GRID_SIZE);
				} while (!constantes.checkFarmerDistance(x, y, boss_x, boss_y, i));
				FarmerAgent fa = new FarmerAgent(x, y, ba, x, y, i);
				fa.setStoppable(schedule.scheduleRepeating(fa));
				constantes.yard.setObjectLocation(fa, x, y);
			}
			
			//Initialisation cooker
			for (int j = 0; j < constantes.INITIAL_FARMER_NUMBER; j++) {
				do {
					x = (int)(Math.random()*Constants.GRID_SIZE);
					y = (int)(Math.random()*Constants.GRID_SIZE);
				} while (!constantes.checkCookerDistance(x, y, boss_x, boss_y, i));
				CookerAgent ca = new CookerAgent(x, y, ba, x, y, i);
				ca.setStoppable(schedule.scheduleRepeating(ca));
				constantes.yard.setObjectLocation(ca, x, y);
			}
			
			//Initialisation supplier
			for (int j = 0; j < constantes.INITIAL_SUPPLIER_NUMBER; j++) {
				do {
					x = (int)(Math.random()*Constants.GRID_SIZE);
					y = (int)(Math.random()*Constants.GRID_SIZE);
				} while (!constantes.checkSupplierDistance(x, y, boss_x, boss_y, i));
				SupplierAgent sa = new SupplierAgent(x, y, ba, i);
				sa.setStoppable(schedule.scheduleRepeating(sa));
				constantes.yard.setObjectLocation(sa, x, y);
			}
			
			//Initialisation protector
			for (int j = 0; j < constantes.INITIAL_PROTECTOR_NUMBER; j++) {
				do {
					x = (int)(Math.random()*Constants.GRID_SIZE);
					y = (int)(Math.random()*Constants.GRID_SIZE);
				} while (!constantes.checkProtectorDistance(x, y, boss_x, boss_y, i));
				SupplierAgent sa = new SupplierAgent(x, y, ba, i);
				sa.setStoppable(schedule.scheduleRepeating(sa));
				constantes.yard.setObjectLocation(sa, x, y);
			}
		}
		
		//Initialisation cop
		int y = Constants.GRID_SIZE / 6;
		int x;
		for (int j = 0; j < 3; j++) {
			x = Constants.GRID_SIZE / 6;
			for (int k = 0; k < 3; k++) {
				CopsAgent ca = new CopsAgent(x, y, (x - Constants.GRID_SIZE / 6), (y- Constants.GRID_SIZE/6));
				ca.setStoppable(schedule.scheduleRepeating(ca));
				constantes.yard.setObjectLocation(ca, x, y);
				x += Constants.GRID_SIZE / 3;
			}
			y += Constants.GRID_SIZE / 3;
		}
		
		//Initialisation buyer
		for (int j = 0; j < constantes.INITIAL_BUYER_NUMBER; j++) {
			x = (int) (Math.random() * Constants.GRID_SIZE);
			y = (int) (Math.random() * Constants.GRID_SIZE);
			BuyerAgent ba = new BuyerAgent(x, y);
			ba.setStoppable(schedule.scheduleRepeating(ba));
			constantes.yard.setObjectLocation(ba, x, y);
		}
		
		//Initialisation shop
		for (int j = 0; j < constantes.INITIAL_SHOP_NUMBER; j++) {
			do {
				x = (int) (Math.random() * Constants.GRID_SIZE);
				y = (int) (Math.random() * Constants.GRID_SIZE);
			} while (!constantes.checkShopDistance(x, y));
			System.out.println(x);
			System.out.println(y);
			ShopAgent sa = new ShopAgent(x, y);
			schedule.scheduleRepeating(sa);
			constantes.yard.setObjectLocation(sa, x, y);
		}
	}
	
	/**
	 *	FONCTION DE SERVICE
	 */
	private boolean checkPosition(int x, int y, int i) {
		Bag b = constantes.getYard().getObjectsAtLocation(x, y);
		if (b != null) {
			for(int k = 0; k < b.size(); k++) {
				Steppable agent = (Steppable) b.get(k);
				if (agent instanceof GangMemberAbstractAgent) {
					GangMemberAbstractAgent agent_tmp = (GangMemberAbstractAgent) agent;
					if (agent_tmp.gang_number == i) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void addBeingsAbstractAgent(BeingAbstractAgent baa) {
		baa.setStoppable(schedule.scheduleRepeating(baa));
	}

	/**
	 *	Retourne l'agent magasin le plus proche de la position indiquée.
	 *	/!\ Retourne null si aucun magasin trouvé.
	 * TODO
	 */
	public ShopAgent getClosestShop(int x, int y){
		return null;
	}

	/**
	 * Retourne la position d'un Boss pour un gang donné.
	 * /!\ Retourne null si le boss n'existe pas.
	 * TODO
	 */
	public BossAgent getBossGang(int gang_number){
		return null;
	}

	public static Constants getConstants() {
		return constantes;
	}
}
