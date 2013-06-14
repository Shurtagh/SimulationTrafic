package agents;

import constantes.Constants;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.util.Bag;

public class ManagerAgent extends GangMemberAbstractAgent{
	
	//attributs
	public int charge_meth_max; 			//max charge qu'il peut porter
	public int charge_meth_courante;		//charge qu'il porte actuellement en meth
	public int charge_canabis_max; 			//max charge qu'il peut porter
	public int charge_canabis_courante;		//charge qu'il porte actuellement en canabis
	public int montant_courant;				//montant courant de l'agent
	
	//mission
	private DealerAgent target = null;
	private DealerAgent tmp_target = null;
	
	public ManagerAgent(int x, int y, BossAgent boss, int gang_number) {
		super(x, y, boss, gang_number);
		
		//initialisatino des attributs
		charge_meth_max = Constants.CHARGE_METH_MAX_MANAGER;
		charge_meth_courante = 0;
		charge_canabis_max = Constants.CHARGE_CANABIS_MAX_MANAGER;
		charge_canabis_courante = 0;
		montant_courant = 0;
		
		//ajout ‡ la liste du gang
		this.boss.managers.add(this);
	}
		
	@Override
	public void step(SimState state) {
		
		//dois je aller voir un dealer 
		if (allerVoirDealer()) {
			
			//suis je ‡ cÙtÈ du dealer
			if ( procheDealer() ){
				
				//donner de la drogue au dealer
				this.donnerMeth(target);
				this.donnerCanabis(target);
				
				//prendre l'argent
				this.montant_courant += this.target.donnerArgent();
				
				//objectif aller voir le boss
				this.target = null;
				
			}else{
				
				//se diriger vers le dealer
				dirigerVersLeDealer();
			}

		}else{
			
			//si j'ai de l'argent => aller vers le boss
			if(this.montant_courant > 0){
				
				//suis je ‡ cÙtÈ du boss
				if( procheDuBoss() ){
					
					//donner toute mon argent au boss
					if(this.montant_courant > 0){
						this.boss.recevoirArgent(this.montant_courant);
						this.montant_courant = 0;
					}
					
					//prendre la drogue nÈcessaire pour une prochaine livraison ‡ un dealer
					this.boss.donnerMethManager(this);
					this.boss.donnerCanabisManager(this);
									
				}else{
					
					//se diriger vers le boss
					this.dirigerVersLeBoss();
				}
				
			//sinon dÈplacement alÈtoire de dealer en dealer
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
			
		}
			
	}
	
	/**
	 * libre ou pas
	 */
	public boolean isOccuped(){
		return this.target == null;
	}
	
	/**
	 * visiter dealer indiquÈ
	 */
	public void setMission(DealerAgent dealer){
		this.target = dealer;
	}
	
	/**
	 * Surcharge de la fonction die pour se retirer de la liste.
	 */
	public void die(){
		super.die();
		this.boss.managers.remove(this);
	}
	
	/**
	 * Donne du canabis au dealer.
	 * @param target2
	 */
	private void donnerCanabis(DealerAgent target2) {
		if(target2 != null){
			
			//rÈcupËre le manque
			int manque = this.target.charge_canabis_max - this.target.charge_canabis_courante;
			if(manque > 0){				
				//ce que l'on peut donner
				if(manque > this.charge_canabis_courante){
					manque = this.charge_canabis_courante;
				}
				//donne le manque
				this.target.charge_canabis_courante += manque;
				//dÈcrÈmente
				this.charge_canabis_courante -= manque;
			}
		}
	}

	/**
	 * Donne de la meth au dealer.
	 * @param target2
	 */
	private void donnerMeth(DealerAgent target2) {
		if(target2 != null){
			
			//rÈcupËre le manque
			int manque = this.target.charge_meth_max - this.target.charge_meth_courante;
			if(manque > 0){				
				//ce que l'on peut donner
				if(manque > this.charge_meth_courante){
					manque = this.charge_meth_courante;
				}
				//donne le manque
				this.target.charge_meth_courante += manque;
				//dÈcrÈmente
				this.charge_meth_courante -= manque;
			}
		}
	}

	private boolean allerVoirDealer(){
		return !(this.target == null);
	}
	
	private boolean procheDealer(){
		if(this.target != null){
			return Constants.casesAdjacentes(this.x, this.y, this.target.x, this.target.y);
		}
		return false;
	}
	
	private boolean procheTmpDealer(){
		if(this.tmp_target != null){
			return Constants.casesAdjacentes(this.x, this.y, this.tmp_target.x, this.tmp_target.y);
		}
		return false;
	}
	
	/**
	 *	D√©place l'agent en direction du dealer.
	 *	TOTEST
	 */
	private void dirigerVersLeDealer(){
		if(this.target != null){			
			//d√©placement vers cette case
			this.deplacement(this.target.x,this.target.y);
		}
	}
	
	/**
	 *	D√©place l'agent en direction du dealer tmp.
	 *	TOTEST
	 */
	private void dirigerVersLeTmpDealer(){
		if(this.tmp_target != null){			
			//d√©placement vers cette case
			this.deplacement(this.tmp_target.x,this.tmp_target.y);
		}
	}
	
	/**
	 *	Retourne vrai si l'agent est adjacent au boss
	 *	TOTEST
	 */
	private boolean procheDuBoss(){
		return Constants.casesAdjacentes(this.x, this.y, this.boss.x, this.boss.y);
	}

	/**
	 *	D√©place l'agent en direction du boss.
	 *	TOTEST
	 */
	private void dirigerVersLeBoss(){
		//d√©placement vers cette case
		this.deplacement(this.boss.x,this.boss.y);
	}

}
