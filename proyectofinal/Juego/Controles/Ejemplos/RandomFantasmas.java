package Juego.Controles.Ejemplos;

import Juego.Controles.ControlFantasmas;
import Juego.core.G;
import Juego.core.Game;

public final class RandomFantasmas implements ControlFantasmas
{	
	public int[] getActions(Game game,long timeDue)
	{	
		int[] directions=new int[Game.NUM_GHOSTS];
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i=0;i<directions.length;i++)
			if(game.ghostRequiresAction(i))
			{			
				int[] possibleDirs=game.getPossibleGhostDirs(i);			
				directions[i]=possibleDirs[G.rnd.nextInt(possibleDirs.length)];
			}
		
		return directions;
	}
}