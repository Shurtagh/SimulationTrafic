package agents;

import constantes.Constants;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;

public class ProtectorAgent extends GangMemberAbstractAgent{

	//mission
	public BeingAbstractAgent order = null;
	private DealerAgent tmp_target = null;
	
	public ProtectorAgent(int x, int y,  BossAgent boss, int gang_number) {
		super(x, y, boss,gang_number);
		
		//ajout ‡ la liste du gang
		this.boss.protectors.add(this);
	}
		
	@Override
	public void step(SimState state) {
		
		//si il a une mission il la fait (et rien d'autre)
		if(isOccuped()){
			
				
				//se diriger vers la cible
				dirigerVersLaCible();
				
				//essaye de tirer
				if (canShoot(order)) {
					attaquer(order);
					if (order.isDead()) {
						order = null;
					}
				}
			
		}else{
			
			//si pas de mission se rapprocher d'un dealer quelconque
			if(this.tmp_target == null){
				tmp_target = boss.getRandomDealer();
			}
			
			//se rapproche au maximum du tmp dealer
			dirigerVersLeTmpDealer();
			
			//atteind sa cible, se rapproche d'une autre
			if(procheTmpDealer()){
				tmp_target = null;
			}
		}
		
//		//On signale tous les ennemis
//		this.boss.signalEnnemies(this.regarderAgresseursProche());
//		
//		//Si on n'a pas d'ordre, on se déplace pseudo-aléatoirement
//		if (order == null) {
//			DealerAgent dealer = this.boss.getRandomDealer();
//			deplacement(dealer.x, dealer.y);
//			
//		//Sinon on se déplace vers notre cible et on tire
//		} else {
//			
//			deplacement(order.x, order.y);
//			if (canShoot(order)) {
//				attaquer(order);
//				if (order.isDead()) {
//					order = null;
//				}
//			}
//		}
	}
	
	/**
	 *	D√©place l'agent en direction du dealer.
	 *	TOTEST
	 */
	private void dirigerVersLeTmpDealer(){
		if(this.tmp_target != null){			
			//d√©placement vers cette case
			this.deplacement(this.tmp_target.x,this.tmp_target.y);
		}
	}
	
	private void dirigerVersLaCible(){
		if(this.order != null){			
			//d√©placement vers cette case
			this.deplacement(this.order.x,this.order.y);
		}
	}
	
	private boolean procheTmpDealer(){
		if(this.tmp_target != null){
			return Constants.casesAdjacentes(this.x, this.y, this.tmp_target.x, this.tmp_target.y);
		}
		return false;
	}
	
	private boolean procheCible(){
		if(this.order != null){
			return Constants.casesAdjacentes(this.x, this.y, this.order.x, this.order.y);
		}
		return false;
	}
	
	/**
	 * libre ou pas
	 */
	public boolean isOccuped(){
		return this.order != null;
	}
	
	/**
	 * visiter dealer indiquÈ
	 */
	public void setMission(BeingAbstractAgent cible){
		this.order = cible;
	}
	
	/**
	 * Surcharge de la fonction die pour se retirer de la liste.
	 */
	public void die(){
		super.die();
		this.boss.protectors.remove(this);
	}

}
