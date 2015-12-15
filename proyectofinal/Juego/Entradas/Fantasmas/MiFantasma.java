package Juego.Entradas.Fantasmas;

import Juego.Controles.ControlFantasmas;
import Juego.core.Game;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MiFantasma implements ControlFantasmas{
	
	 Decision rootIsBlue;
	 	 
	 public MiFantasma(){
		 Accion chasePacMan = new AccionPersecusion();
		 Accion runAway = new AccionHuir();
		 Decision powerPill = new PowerPillClose();
		 rootIsBlue = new IsBlue();
		 rootIsBlue.trueNode = runAway;
		 rootIsBlue.falseNode = powerPill;
		 powerPill.falseNode = chasePacMan;
		 powerPill.trueNode = runAway;

		 
	 }
	//Place your game logic here to play the game as the ghosts
	public int[] getActions(Game game,long timeDue)
	{
		Accion nextAction = (Accion) rootIsBlue.makeDecision(game); //exception here
		return nextAction.execute(game);		
		
	}
}