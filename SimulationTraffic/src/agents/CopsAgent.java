package agents;

import main.Beings;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.IntBag;
import constantes.Constants;


public class CopsAgent extends BeingAbstractAgent {

	public int zone_x;
	public int zone_y;
	public int largeur;
	public int hauteur;
	
	public CopsAgent(int x, int y, int zone_x, int zone_y) {
		super(x, y);
		//attributs caractéristiques
		this.zone_x = zone_x;
		this.zone_y = zone_y;
		this.hauteur = Constants.HAUTEUR_ZONE_COP;
		this.largeur = Constants.LARGEUR_ZONE_COP;
	}
	
	public void step(SimState state) {
		GangMemberAbstractAgent gma = searchGangMember();
		if (gma != null) {
			deplacement(gma.x, gma.y);
			if (canShoot(gma)) {
				attaquer(gma);
			}
		} else {
			zonerAleatoire(zone_x, zone_y, largeur, hauteur);
		}
	}
	
	public GangMemberAbstractAgent searchGangMember() {
		IntBag xPos = new IntBag();
		IntBag yPos = new IntBag();
		Beings.getConstants().getYard().getNeighborsHamiltonianDistance(x, y, Constants.MAX_PERCEPTION, false, xPos, yPos);
		for(int i = 0; i < xPos.size(); i++) {
			Bag res = Beings.getConstants().getYard().getObjectsAtLocation(xPos.get(i), yPos.get(i));
			if (res != null) {
				for(Object x: res) {
					if (x instanceof GangMemberAbstractAgent) {
						return (GangMemberAbstractAgent) x;
					}
				}
			}
		}
		return null;
	}

}
