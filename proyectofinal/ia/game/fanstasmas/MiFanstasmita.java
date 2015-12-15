package ia.game.fanstasmas;

import Juego.Controles.ControlFantasmas;
import Juego.core.Game;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MiFanstasmita implements ControlFantasmas{

	Decision rootIsBlue;

	public MiFanstasmita(){
		Accion chasePacMan = new AccionPersecucion();
		Accion chaseNewDir = new AccionPersecucuionNuevaRuta();
		Accion runAway = new AccionHuir();
		Decision ate = new EsComida();
		rootIsBlue = new IsBlue();
		rootIsBlue.trueNode = ate;
		rootIsBlue.falseNode = chasePacMan;
		ate.falseNode = runAway;
		ate.trueNode = chaseNewDir;

	}
	//Place your game logic here to play the game as the ghosts
	public int[] getActions(Game game,long timeDue)
	{
		Accion nextAction = (Accion) rootIsBlue.makeDecision(game); //exception here
		return nextAction.execute(game);		

	}
}
