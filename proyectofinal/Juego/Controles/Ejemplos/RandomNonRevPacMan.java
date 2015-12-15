package Juego.Controles.Ejemplos;

import Juego.Controles.ControlPacMan;
import Juego.core.G;
import Juego.core.Game;

public final class RandomNonRevPacMan implements ControlPacMan
{	
	public int getAction(Game game,long timeDue)
	{			
		int[] directions=game.getPossiblePacManDirs(false);		//set flag as false to prevent reversals	
		return directions[G.rnd.nextInt(directions.length)];		
	}
}