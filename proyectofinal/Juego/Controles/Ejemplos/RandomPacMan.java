package Juego.Controles.Ejemplos;

import Juego.Controles.ControlPacMan;
import Juego.core.G;
import Juego.core.Game;

public final class RandomPacMan implements ControlPacMan
{
	public int getAction(Game game,long timeDue)
	{
		int[] directions=game.getPossiblePacManDirs(true);		//set flag as true to include reversals		
		return directions[G.rnd.nextInt(directions.length)];
	}
}