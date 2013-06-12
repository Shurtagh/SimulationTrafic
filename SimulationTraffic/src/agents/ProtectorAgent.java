package agents;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;

public class ProtectorAgent extends GangMemberAbstractAgent{

	public BeingAbstractAgent order = null;
	
	public ProtectorAgent(int x, int y, Stoppable stoppable, BossAgent boss) {
		super(x, y, stoppable, boss);
	}
		
	@Override
	public void step(SimState state) {
		//On signale tous les ennemis
		this.boss.signalEnnemies(this.regarderAgresseursProche());
		//Si on n'a pas d'ordre, on se déplace pseudo-aléatoirement
		if (order == null) {
			DealerAgent dealer = this.boss.getRandomDealer();
			deplacement(dealer.x, dealer.y);
		//Sinon on se déplace vers notre cible et on tire
		} else {
			deplacement(order.x, order.y);
			if (canShoot(order)) {
				attaquer(order);
				if (order.isDead()) {
					order = null;
				}
			}
		}
	}

}
