package ia.game.fanstasmas;
import Juego.core.Game;


public class AccionHuir extends Accion{
	public int[] execute(Game game){
		int[] directions = new int[Game.NUM_GHOSTS];
		for(int i=0;i<directions.length;i++){		//for each ghost
			if(game.ghostRequiresAction(i))			//if it requires an action
				directions[i] = game.getNextGhostDir(i,game.getCurPacManLoc(),false,Game.DM.PATH);
		}
		return directions;
	}
}
