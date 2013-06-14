package constantes;
import java.awt.Color;

import agents.BossAgent;

import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;


public class Constants {

	//constantes buyer
	public static final int MAX_DEGROGUAGE = 15;
	public static final int MIN_DEDROGUAGE = 5;
	public static final int MAX_PERCEPTION = 25;
	
		//consommation canabis
	public static final int MAX_CONSO_CANABIS = 50;
	public static final int MIN_CONSO_CANABIS = 25;		
	public static final int PUISSANCE_CANABIS = 1;
		//consommation meth
	public static final int MAX_CONSO_METH = 50;
	public static final int MIN_CONSO_METH = 25;
	public static final int PUISSANCE_METH = 2;
	
	//configuration
	public static int GRID_SIZE = 500;		//taille de la grille
	public static int GANG_NUMBER = 2;		//numéro de gang
	public static int MOVEMENT_MAX = 1;		//?
	public static int PROD_MAX = 1;			//?
	public static int CHARGE_MAX = 1;		//?
	
	//distances initiales (/gang sauf shop et buyer)
	public static int INITIAL_SHOP_NUMBER = 5;
	public static int INITIAL_BUYER_NUMBER = 500;
	
	public static int INITIAL_DEALER_NUMBER = 200;
	public static int INITIAL_FARMER_NUMBER = 1;
	public static int INITIAL_COOKER_NUMBER = 1;
	public static int INITIAL_SUPPLIER_NUMBER = 1;
	public static int INITIAL_PROTECTOR_NUMBER = 1;
	
	//avancé
	public SparseGrid2D yard;
	public IntGrid2D territory;
	public static Color[] GANG_COLOR;
	public static int MIN_DISTANCE_BETWEEN_GANG = 100;
	public int[][] GANG_CENTER = new int[GANG_NUMBER][2];
	public int[][] SHOP_CENTER = new int[INITIAL_SHOP_NUMBER][2];
	
	//distance d'apparition
	public static int MAX_DISTANCE_BOSS_FARMER = 10;
	public static int MAX_DISTANCE_BOSS_SUPPLIER = 10;
	public static int MAX_DISTANCE_BOSS_COOKER = 10;
	public static int MAX_DISTANCE_BOSS_MANAGER = 10;
	public static final int MAX_DISTANCE_BOSS_PROTECTOR = 10;
	
	//constantes cops
	public static final int HAUTEUR_ZONE_COP = 0;
	public static final int LARGEUR_ZONE_COP = 0;

	//constantes beings
	public static int DISTANCE_DEPLACEMENT = 5;
	public static final int DEFAULT_PV = 100;
	public static final int DEFAULT_ATTAQUE = 20;
	public static final int DEFAULT_SHOOT_DISTANCE = 15;

	//constantes suppliers
	public static int CHARGE_MAX_SUPPLIER = 30;
	
	//constantes farmer
	public static int CHARGE_MAX_FARMER = 30;
	public static int MIN_PRODUCTION_FARMER = 1;
	public static int MAX_PRODUCTION_FARMER = 10;

	//constantes shops
	public static int PRIX_FOURNITURES = 1;
	public static int MIN_DISTANCE_BETWEEN_SHOP = 1;
	
	//constantes boss
	public static int PREVISION_ACHAT_FOURNITURE_INITIALE = 1;
	public static int RICHESSE_INITIALE = 1000;
	public static int CANABIS_INITIALE = 1000;
	public static int FOURNITURE_INITIALE = 100;
	public static int METH_INITIALE = 100;
	public static int PREVISION_DONNER_FOURNITURE = 1;
	public static int PREVISION_PORTE_METH_MANAGER = 10;
	public static int PREVISION_PORTE_CANABIS_MANAGER = 10;
	public static int SEUIL_RICHESSE_DEALER = 1000;				//seuil à partir duquel on achète des dealers
	
	public static int PRIX_FARMER = 10;
	public static int PRIX_SUPPLIER = 10;
	public static int PRIX_COOKER = 10;
	public static int PRIX_PROTECTOR = 10;
	public static int PRIX_MANAGER = 10;
	public static int PRIX_DEALER = 10;
	

	//constantes coocker
	public static int FOURNITURE_TO_METH = 2;
	public static int FOURNITURE_INITIALE_COOCKER = 2;
	public static int METH_INITIALE_COOCKER = 2;

	//constantes dealer
	public static int LARGEUR_ZONE_DEALER = 10;
	public static int HAUTEUR_ZONE_DEALER = 10;
	public static int CHARGE_CANABIS_MAX_DEALER = 200;
	public static int CHARGE_METH_MAX_DEALER = 200;
	
	//constantes manager
	public static int CHARGE_CANABIS_MAX_MANAGER = CHARGE_CANABIS_MAX_DEALER * 3;
	public static int CHARGE_METH_MAX_MANAGER = CHARGE_METH_MAX_DEALER * 3;

	//constantes Being Agent
	public static int PERIMETRE_PERCEPTION = 25;

	/**
	 *	FONCTION TOOLS
	 */

	/**
	 *	Retourne vrai si deux cases sont adjacentes.
	 *	Diagonale aussi.
	 *	TOTEST
	 */
	public static boolean casesAdjacentes(int x1, int y1, int x2, int y2){
		int xRes = Math.abs(x1 - x2);
		int yRes = Math.abs(y1 - y2);
		if((xRes == 0) || (xRes == 1)){
			if((yRes == 0) || (yRes == 1)){
				return true;
			}
		}
		return false;
	}

	public Constants() {
		_initialisation();
	}

	private void _initialisation() {
		yard = new SparseGrid2D(GRID_SIZE, GRID_SIZE);
		territory = new IntGrid2D(GRID_SIZE, GRID_SIZE, 0);
		GANG_COLOR = new Color[GANG_NUMBER];
	}

	public boolean checkGangCenter(int x, int y) {
		boolean flag = true;
		if (GANG_CENTER != null) {
			for(int[] i: GANG_CENTER) {
				int distance = Math.abs(i[0] - x) + Math.abs(i[1] - y);
				if (distance < MIN_DISTANCE_BETWEEN_GANG) {
					flag = false;
				}
			}
		}
		return flag;
	}
	
	public void setColor() {
		for(int i = 0; i < GANG_NUMBER; i++) {
			int red = (int) (Math.random() * 200 + 55);
			int green = (int) (Math.random() * 200 + 55);
			int blue = (int) (Math.random() * 200 + 55);
			GANG_COLOR[i] = new Color(red, green, blue);
		}
	}
	
	public static int getGRID_SIZE() {
		return GRID_SIZE;
	}

	public static void setGRID_SIZE(int gRID_SIZE) {
		GRID_SIZE = gRID_SIZE;
	}

	public static int getGANG_NUMBER() {
		return GANG_NUMBER;
	}

	public static void setGANG_NUMBER(int gANG_NUMBER) {
		GANG_NUMBER = gANG_NUMBER;
	}

	public static int getPROD_MAX() {
		return PROD_MAX;
	}

	public static void setPROD_MAX(int pROD_MAX) {
		PROD_MAX = pROD_MAX;
	}

	public static int getCHARGE_MAX() {
		return CHARGE_MAX;
	}

	public static void setCHARGE_MAX(int cHARGE_MAX) {
		CHARGE_MAX = cHARGE_MAX;
	}

	public static int getMOVEMENT_MAX() {
		return MOVEMENT_MAX;
	}

	public static void setMOVEMENT_MAX(int mOVEMENT_MAX) {
		MOVEMENT_MAX = mOVEMENT_MAX;
	}

	public SparseGrid2D getYard() {
		return yard;
	}

	public void setYard(SparseGrid2D yard) {
		this.yard = yard;
	}

	public IntGrid2D getTerritory() {
		return territory;
	}

	public void setTerritory(IntGrid2D territory) {
		this.territory = territory;
	}
	
	private int getSignedValue(int val, double ponderation, int sign) {
		double pond;
		if (sign == 0) {
			pond = ponderation;
		} else {
			pond = ponderation * sign / Math.abs(sign);
		}
		double rand = Math.random();
		if (rand > (0.5 /*- pond*/)) {
			val = -1 * val;
		}
		return val;
	}
	
	public static String getBinary (int number) {
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
	
	public boolean checkFarmerDistance(int x, int y, int boss_x, int boss_y, int gang_number) {
		if ((Math.abs(x - boss_y) + Math.abs(y - boss_y)) < MAX_DISTANCE_BOSS_FARMER) {
			String binary = getBinary(territory.get(x, y));
			if (binary.length() < gang_number) {
				return false;
			}
			if (binary.charAt(binary.length() - 1 - gang_number) == '1') {				
				return true;
			}
		}
		return false;
	}

	public boolean checkSupplierDistance(int x, int y, int boss_x, int boss_y, int gang_number) {
		if ((Math.abs(x - boss_y) + Math.abs(y - boss_y)) < MAX_DISTANCE_BOSS_SUPPLIER) {
			String binary = getBinary(territory.get(x, y));
			if (binary.charAt(binary.length() - 1 - gang_number) == '1') {				
				return true;
			}
		}
		return false;
	}
	
	public boolean checkCookerDistance(int x, int y, int boss_x, int boss_y, int gang_number) {
		if ((Math.abs(x - boss_y) + Math.abs(y - boss_y)) < MAX_DISTANCE_BOSS_COOKER) {
			String binary = getBinary(territory.get(x, y));
			if (binary.charAt(binary.length() - 1 - gang_number) == '1') {				
				return true;
			}
		}
		return false;
	}
	
	public boolean checkProtectorDistance(int x, int y, int boss_x, int boss_y, int gang_number) {
		if ((Math.abs(x - boss_y) + Math.abs(y - boss_y)) < MAX_DISTANCE_BOSS_PROTECTOR) {
			String binary = getBinary(territory.get(x, y));
			if (binary.charAt(binary.length() - 1 - gang_number) == '1') {				
				return true;
			}
		}
		return false;
	}
	
	public boolean checkShopDistance(int x, int y) {
		if (territory.get(x, y) == 0) {
			boolean flag = true;
			if (SHOP_CENTER != null) {
				for(int[] i: SHOP_CENTER) {
					int distance = Math.abs(i[0] - x) + Math.abs(i[1] - y);
					if (distance < MIN_DISTANCE_BETWEEN_SHOP) {
						flag = false;
					}
				}
			}
			return flag;
		}
		return false;
	}
	
	public static int getINITIAL_DEALER_NUMBER() {
		return INITIAL_DEALER_NUMBER;
	}

	public static void setINITIAL_DEALER_NUMBER(int iNITIAL_DEALER_NUMBER) {
		INITIAL_DEALER_NUMBER = iNITIAL_DEALER_NUMBER;
	}

	public static Color[] getGANG_COLOR() {
		return GANG_COLOR;
	}

	public static void setGANG_COLOR(Color[] gANG_COLOR) {
		GANG_COLOR = gANG_COLOR;
	}

	public static int getMIN_DISTANCE_BETWEEN_GANG() {
		return MIN_DISTANCE_BETWEEN_GANG;
	}

	public static void setMIN_DISTANCE_BETWEEN_GANG(int mIN_DISTANCE_BETWEEN_GANG) {
		MIN_DISTANCE_BETWEEN_GANG = mIN_DISTANCE_BETWEEN_GANG;
	}

	

	public int[][] getGANG_CENTER() {
		return GANG_CENTER;
	}

	public void setGANG_CENTER(int[][] gANG_CENTER) {
		GANG_CENTER = gANG_CENTER;
	}

	public static int getDISTANCE_DEPLACEMENT() {
		return DISTANCE_DEPLACEMENT;
	}

	public static void setDISTANCE_DEPLACEMENT(int dISTANCE_DEPLACEMENT) {
		DISTANCE_DEPLACEMENT = dISTANCE_DEPLACEMENT;
	}

	public static int getCHARGE_MAX_SUPPLIER() {
		return CHARGE_MAX_SUPPLIER;
	}

	public static void setCHARGE_MAX_SUPPLIER(int cHARGE_MAX_SUPPLIER) {
		CHARGE_MAX_SUPPLIER = cHARGE_MAX_SUPPLIER;
	}

	public static int getPRIX_FOURNITURES() {
		return PRIX_FOURNITURES;
	}

	public static void setPRIX_FOURNITURES(int pRIX_FOURNITURES) {
		PRIX_FOURNITURES = pRIX_FOURNITURES;
	}

	public static int getPREVISION_ACHAT_FOURNITURE_INITIALE() {
		return PREVISION_ACHAT_FOURNITURE_INITIALE;
	}

	public static void setPREVISION_ACHAT_FOURNITURE_INITIALE(
			int pREVISION_ACHAT_FOURNITURE_INITIALE) {
		PREVISION_ACHAT_FOURNITURE_INITIALE = pREVISION_ACHAT_FOURNITURE_INITIALE;
	}

	public static int getRICHESSE_INITIALE() {
		return RICHESSE_INITIALE;
	}

	public static void setRICHESSE_INITIALE(int rICHESSE_INITIALE) {
		RICHESSE_INITIALE = rICHESSE_INITIALE;
	}
}
