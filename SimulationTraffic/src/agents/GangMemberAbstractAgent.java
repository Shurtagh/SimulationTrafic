package agents;


import main.Beings;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import constantes.Constants;





public abstract class GangMemberAbstractAgent extends BeingAbstractAgent {
	
	
	/*** attributs ***/
	public BossAgent boss;

	/**
	 * 	Constructeur
	 */
	public GangMemberAbstractAgent(int x , int y, Stoppable stoppable, BossAgent boss) {

		//appel classe mÃ¨re
		super(x,y,stoppable);

		//attributs
		this.boss = boss;
	}

	/**
	 * Action appelÃ©e Ã  chaque itÃ©ration.
	 */
	@Override
	public void step(SimState state) {
	}

	/**
	 *	retoure l'ensemble des agents emenies + agents flics
	 *	TOTEST
	 */
	public Bag regarderAgresseursProche(){
		
		//grille du modèle
		SparseGrid2D map = Beings.getConstants().getYard();
		
		//calcul y de la zone de perception
		int y_min_zone = y - perimetrePerception;
		int y_max_zone = y + perimetrePerception;
		//borné par la bordure
		if (y_min_zone < 0) {y_min_zone = 0;}
		if (y_max_zone > map.getHeight()) {y_max_zone = map.getHeight();}
		
		//calcul x de la zone de perception
		int x_min_zone = x - perimetrePerception;
		int x_max_zone = x + perimetrePerception;
		//borné par la bordure
		if (x_min_zone < 0) {x_min_zone = 0;}
		if (x_max_zone > map.getWidth()) {x_max_zone = map.getWidth();}
		
		
		
		//résultat des agents
		Bag objects = new Bag();
		
		//parcourir la zone de perception
		for (int i = y_min_zone; i <= y_max_zone; i++) {
			for(int j = x_min_zone; j <= x_max_zone; j++){
				
				//reggarde si un ou des éléments sont présents
				Bag objects_location = map.getObjectsAtLocation(j, i);
				//si des éléments présents
				if (objects_location != null) {
					
					//pour chacun des élements présents sur cette case
					for (int k = 0; k < objects_location.size(); k++) {
						
						//on regarde le type
						Object instance_agent = objects_location.get(k);
						
						//agent gang
						if (instance_agent instanceof GangMemberAbstractAgent) {
							
							//on vérifie que son gang est différent
							GangMemberAbstractAgent agent = (GangMemberAbstractAgent)instance_agent;
							if(agent.boss != this.boss){
								objects.push(agent);
							}
						}
						
						//agent policier
						if (instance_agent instanceof CopsAgent) {
							objects.push((CopsAgent)instance_agent);
						}
						
					}
				}
				
			}
		}
		return null;
	}

}
