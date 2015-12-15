package ia.game.fanstasmas;

import Juego.core.Game;

class IsBlue extends Decision {
	public DecisionNode getBranch (Game game) {
		int i;
		for(i = 0; i < Game.NUM_GHOSTS; i++){
			if (game.isEdible(i) == true)
				return this.trueNode;
		}
		return this.falseNode;
	}
}
