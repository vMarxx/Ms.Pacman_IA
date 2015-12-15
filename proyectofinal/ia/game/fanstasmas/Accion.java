package ia.game.fanstasmas;

import Juego.core.*;

public class Accion extends DecisionNode{
	public Accion makeDecision(Game game){
		return this;
	}
	public int[] execute(Game game){
		int[] result = new int[4];
		result[0] = -1;
		result[1] = -1;
		result[2] = -1;
		result[3] = -1;
		return result;
	}
}
