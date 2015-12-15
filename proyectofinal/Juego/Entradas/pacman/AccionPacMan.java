package Juego.Entradas.pacman;

import Juego.core.Game;
import ia.hsm.Accion;

abstract class AccionPacMan extends Accion {		
	abstract int act(Game game);
}
