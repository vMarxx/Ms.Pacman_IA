package Juego.Entradas.pacman;

import java.awt.Color;

import Juego.core.Game;
import Juego.core.GameView;

public class AccionComerFanstasmas extends AccionPacMan {
	@Override
	int act(Game game) {
		AccionHuir.visitedJunctions.clear();
		int current = game.getCurPacManLoc();
		int target = game.getCurGhostLoc(MiPacMan.closestBlueGhost);
		GameView.addPoints(game, Color.ORANGE, game.getPath(current, target));
		int dir = game.getNextPacManDir(target, true, Game.DM.PATH);
		return dir;
	}
}
