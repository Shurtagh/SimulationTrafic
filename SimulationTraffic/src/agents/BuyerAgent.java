package agents;

import jade.core.CaseInsensitiveString;
import main.Beings;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.IntBag;
import constantes.Constants;


public class BuyerAgent extends BeingAbstractAgent {

	public int onDrug;
	public boolean isVisible;
	
	
	public BuyerAgent(int x, int y, Stoppable stoppable) {
		super(x, y, stoppable);
	}

	public void step(SimState state) {
		if (isOnDrug()) {
			onDrug -= Math.random()*(Constants.MAX_DEGROGUAGE - Constants.MIN_DEDROGUAGE) + Constants.MIN_DEDROGUAGE;
		} else {
			if (isVisible) {
				DealerAgent dealer = searchDealer();
				if (dealer != null) {
					deplacement(dealer.x, dealer.y);
					if (Constants.casesAdjacentes(x, y, dealer.x, dealer.y)) {
						double typeAchat = Math.random();
						if (typeAchat < 0.6) {
							onDrug += Constants.PUISSANCE_CANABIS * dealer.sellCanabis((int)(Math.random()*(Constants.MAX_CONSO_CANABIS - Constants.MIN_CONSO_CANABIS) + Constants.MAX_CONSO_CANABIS));
						} else if (typeAchat > 0.85) {
							onDrug += Constants.PUISSANCE_METH * dealer.sellMeth((int)(Math.random()*(Constants.MAX_CONSO_METH - Constants.MIN_CONSO_METH) + Constants.MAX_CONSO_METH));
							onDrug += Constants.PUISSANCE_CANABIS * dealer.sellCanabis((int)(Math.random()*(Constants.MAX_CONSO_CANABIS - Constants.MIN_CONSO_CANABIS) + Constants.MAX_CONSO_CANABIS));
						} else {
							onDrug += Constants.PUISSANCE_METH * dealer.sellMeth((int)(Math.random()*(Constants.MAX_CONSO_METH - Constants.MIN_CONSO_METH) + Constants.MAX_CONSO_METH));
						}
						isVisible = false;
						Beings.getConstants().getYard().remove(this);
					}
				} else {
					deplacementAleatoire();
				}
			} else {
				onDrug = 0;
				this.x = (int) (Math.random()*Constants.GRID_SIZE);
				this.y = (int) (Math.random()*Constants.GRID_SIZE);
				Beings.getConstants().getYard().setObjectLocation(this, x, y);
				isVisible = false;
			}
		}
	}
	
	public boolean isOnDrug() {
		return (onDrug > 0);
	}
	
	public DealerAgent searchDealer() {
		IntBag xPos = new IntBag();
		IntBag yPos = new IntBag();
		Beings.getConstants().getYard().getNeighborsHamiltonianDistance(x, y, Constants.MAX_PERCEPTION, false, xPos, yPos);
		for(int i = 0; i < xPos.size(); i++) {
			Bag res = Beings.getConstants().getYard().getObjectsAtLocation(xPos.get(i), yPos.get(i));
			for(Object x: res) {
				if (x instanceof DealerAgent) {
					return (DealerAgent) x;
				}
			}
		}
		return null;
	}
}
