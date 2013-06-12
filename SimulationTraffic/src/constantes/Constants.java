package constantes;
import java.awt.Color;

import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;


public class Constants {

	//constantes buyer
	public static final int MAX_DEGROGUAGE = 0;
	public static final int MIN_DEDROGUAGE = 0;
	public static final int MAX_PERCEPTION = 0;
	public static final int MAX_CONSO_CANABIS = 0;
	public static final int MIN_CONSO_CANABIS = 0;
	public static final int PUISSANCE_CANABIS = 0;
	public static final int PUISSANCE_METH = 0;
	public static final int MAX_CONSO_METH = 0;
	public static final int MIN_CONSO_METH = 0;
	
	
	public static int GRID_SIZE = 800;
	public static int GANG_NUMBER = 3;
	public static int PROD_MAX;
	public static int CHARGE_MAX;
	public static int MOVEMENT_MAX;
	public static int INIT_SIZE_GANG = 100000;
	public SparseGrid2D yard;
	public IntGrid2D territory;
	public static Color[] GANG_COLOR;
	public static int MIN_DISTANCE_BETWEEN_GANG = 400;
	public static int[][] GRID;
	public int[][] GANG_CENTER = new int[GANG_NUMBER][2];
	
	//constantes cops
	public static final int HAUTEUR_ZONE_COP = 0;
	public static final int LARGEUR_ZONE_COP = 0;

	//constantes beings
	public static int DISTANCE_DEPLACEMENT = 1;
	public static final int DEFAULT_PV = 0;
	public static final int DEFAULT_ATTAQUE = 0;
	public static final int DEFAULT_SHOOT_DISTANCE = 0;

	//constantes suppliers
	public static int CHARGE_MAX_SUPPLIER = 30;
	
	//constantes farmer
	public static int CHARGE_MAX_FARMER = 30;
	public static int MIN_PRODUCTION_FARMER = 1;
	public static int MAX_PRODUCTION_FARMER = 10;

	//constantes shops
	public static int PRIX_FOURNITURES;

	//constantes boss
	public static int PREVISION_ACHAT_FOURNITURE_INITIALE = 1;
	public static int RICHESSE_INITIALE = 1000;
	public static int CANABIS_INITIALE = 0;
	public static int FOURNITURE_INITIALE = 0;
	public static int METH_INITIALE = 0;

	//constantes coocker
	public static int FOURNITURE_TO_METH = 2;
	public static int FOURNITURE_INITIALE_COOCKER = 2;
	public static int METH_INITIALE_COOCKER = 2;

	//constantes dealer
	public static int LARGEUR_ZONE_DEALER;
	public static int HAUTEUR_ZONE_DEALER;

	//constantes Being Agent
	public static int PERIMETRE_PERCEPTION = 3;

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
		yard = new SparseGrid2D(GRID_SIZE, GRID_SIZE);
		GRID = new int[GRID_SIZE][GRID_SIZE];
		GANG_COLOR = new Color[GANG_NUMBER];
	}

	private void _initialisation() {
		yard = new SparseGrid2D(GRID_SIZE, GRID_SIZE);
		territory = new IntGrid2D(GRID_SIZE, GRID_SIZE, 0);
		
		for(int i = 0; i < GANG_NUMBER; i++) {
			int x, y;
			do {
				x = (int)(Math.random()*GRID_SIZE);
				y = (int)(Math.random()*GRID_SIZE);
			} while (!checkGangCenter(x, y));
			GANG_CENTER[i][0] = x;
			GANG_CENTER[i][1] = y;
			int value = (int)Math.pow(2, i);
			territory.set(x, y, value);
			for (int j = 0; j < 4; j++) {
				int current_x = x;
				int current_y = y;
				int allocation = INIT_SIZE_GANG/4;
				while (allocation > 0) {
					float ponderation_x = (float)Math.pow((double)Math.abs(current_x - x), 1) ;
					float ponderation_y = (float)Math.pow((double)Math.abs(current_y - y), 1);
					float ponderation;
					if ((ponderation_x + ponderation_y) == 0) {
						ponderation = (float)0;
					} else {
						ponderation = (float)((ponderation_x - ponderation_y)/(GRID_SIZE*2)*0.5);
					}
					//System.out.println(ponderation);
					if (Math.random() > (0.5 /*- ponderation*/)) {
						current_x += getSignedValue(1, ponderation_x/GRID_SIZE, (current_x - x));
						if (current_x < 0) {
							current_x = 0;
						}
						if (current_x > (GRID_SIZE-1)) {
							current_x = GRID_SIZE-1;
						}
					} else {
						current_y += getSignedValue(1, ponderation_y/GRID_SIZE, (current_y - y));
						if (current_y < 0) {
							current_y = 0;
						}
						if (current_y > (GRID_SIZE-1)) {
							current_y = GRID_SIZE-1;
						}
					}
					String res = getBinary(territory.get(current_x, current_y));
					if (i+1 > res.length() || res.charAt(res.length()-i-1) == '0') {
					//if (territory.get(current_x, current_y) == 0) {
						//System.out.println("test");
						
						allocation--;
						int oldValue = territory.get(current_x, current_y);
						territory.set(current_x, current_y, (value + oldValue));
					}
				}
			}
		}
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

	public static int getINIT_SIZE_GANG() {
		return INIT_SIZE_GANG;
	}

	public static void setINIT_SIZE_GANG(int iNIT_SIZE_GANG) {
		INIT_SIZE_GANG = iNIT_SIZE_GANG;
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

	public static int[][] getGRID() {
		return GRID;
	}

	public static void setGRID(int[][] gRID) {
		GRID = gRID;
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
