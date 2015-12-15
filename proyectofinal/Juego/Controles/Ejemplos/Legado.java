package Juego.Controles.Ejemplos;

import Juego.Controles.ControlFantasmas;
import Juego.core.Game;
import Juego.core.Game.DM;

public class Legado implements ControlFantasmas
{
	public int[] getActions(Game game,long timeDue)
	{
		int[] directions=new int[Game.NUM_GHOSTS];
		DM[] dms=Game.DM.values();
		
		for(int i=0;i<directions.length-1;i++)
			if(game.ghostRequiresAction(i))
				directions[i]=game.getNextGhostDir(i,game.getCurPacManLoc(),true,dms[i]);	//approach Ms Pac-Man using a different distance measure
																							//for each ghost; last ghost takes random action
		directions[3]=Game.rnd.nextInt(4);
		
		return directions;
	}
}