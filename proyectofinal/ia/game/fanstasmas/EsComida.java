package ia.game.fanstasmas;
import Juego.core.*;

class EsComida extends Decision{
	public DecisionNode getBranch(Game game){
		int i;
		for(i = 0; i < Game.NUM_GHOSTS; i++){
			if (game.getLairTime(i) > 2 && game.getLairTime(i) < 3)
				return this.trueNode;
		}
		return this.falseNode;
	}
}
