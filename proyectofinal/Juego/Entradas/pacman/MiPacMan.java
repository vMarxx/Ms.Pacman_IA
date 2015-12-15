package Juego.Entradas.pacman;

import java.util.LinkedList;

import Juego.Controles.ControlPacMan;
import Juego.core.Game;
import ia.hsm.Accion;
import ia.hsm.Condicion;
import ia.hsm.SubMaquinaEstado;
import ia.hsm.Transicion;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MiPacMan implements ControlPacMan
{
	static final LinkedList<Accion> NO_ACTION = new LinkedList<Accion>();
	
	class PacManTransition extends Transicion {
		String message;
		
		public PacManTransition(Condicion condition) {
			this("", condition);
		}
		
		public PacManTransition(String message, Condicion condition) {
			this.condition = condition;
			this.message = message;
		}
		
		@Override
		public LinkedList<Accion> getAction() {
			//System.out.println(message);
			return NO_ACTION;
		}
	}
	
	class PacManState extends SubMaquinaEstado {
		public LinkedList<Accion> action;
		public LinkedList<Accion> entryAction = NO_ACTION;
		public LinkedList<Accion> exitAction = NO_ACTION;
		
		public String message;
		
		public PacManState(AccionPacMan action, String message) {
			this.action = new LinkedList<Accion>();
			this.action.add(action);
			this.message = message;
		}
		
		public PacManState(AccionPacMan action) {
			this(action, "");
		}
		
		@Override
		public LinkedList<Accion> getAction() {
			return action;
		}
		
		@Override
		public LinkedList<Accion> getEntryAction() {
			//System.out.println("\tEntering: " + message);
			return entryAction;
		}
		
		@Override
		public LinkedList<Accion> getExitAction() {
			return exitAction;
		}
	}
	
	/*
	// reckoning
	public static int CLOSE_DIST = 80;
	public static int CLOSE_BLUE_DIST = 92;
	*/
	
	public static int CLOSE_DIST = 35;
	public static int CLOSE_BLUE_DIST = 50;
	public static int POWER_DIST = 25;
	public static int JUNC_DIST = 5;
	
	public static int[] ghostDist = new int[4];
	//public static int closestGhost = -1;
	public static int closestBlueGhost = -1;
	public static int closestNonBlueGhost = -1;
	public static int currentLoc = -1;
	
	SubMaquinaEstado root;
	LinkedList<AccionPacMan> pacmanActions = new LinkedList<AccionPacMan>();
	LinkedList<Accion> resultActions = new LinkedList<Accion>();
	
	public MiPacMan() {
		root = new SubMaquinaEstado();
		
		SubMaquinaEstado normalState = new SubMaquinaEstado();
		root.addState(normalState);
		root.initialState = normalState;
		
		PacManState eatPillState = new PacManState(new CercaPillAvoidPowerAction(), "eat pill");
		normalState.addState(eatPillState);		
		normalState.initialState = eatPillState;
		
		PacManState runAwayState = new PacManState(new AccionHuir(), "run away");
		normalState.addState(runAwayState);
		
		PacManState eatGhostState = new PacManState(new AccionComerFanstasmas(), "eat ghost");
		root.addState(eatGhostState);
		
		final Condicion closeCondition = new Condicion() {
			@Override
			public boolean test() {
				if (closestNonBlueGhost < 0) {
					return false;
				}
				return (ghostDist[closestNonBlueGhost] < CLOSE_DIST);
			}
		};
		
		// Build Transitions
		PacManTransition close = new PacManTransition("close transition", closeCondition);
		
		PacManTransition far = new PacManTransition("far transition", new Condicion() {

			@Override
			public boolean test() {
				return !closeCondition.test();
			}
			
		});
		
		
		final Condicion nearBlueCondition = new Condicion() {
			@Override
			public boolean test() {
				// if closest blue doesn't exist
				// then can be near blue
				if (closestBlueGhost < 0) {
					return false;
				} 
				
				// if closest non-blue doesn't exist
				// then we can't be near non blue
				else if (closestNonBlueGhost < 0) {
					return true;
				} 
				
				// return true if blue is closer than non-blue
				else {
					return !((ghostDist[closestNonBlueGhost] < ghostDist[closestBlueGhost]) && 
						   (ghostDist[closestNonBlueGhost] < CLOSE_DIST));
				}
			}
		};
		
		PacManTransition nearBlue = new PacManTransition("near blue", nearBlueCondition);
		
		PacManTransition nearNonBlue = new PacManTransition("near non-blue", new Condicion() {

			@Override
			public boolean test() {
				return !nearBlueCondition.test();
			}
			
		});
		
		// Join states with transitions
		bindTransition(eatPillState, runAwayState, close);
		bindTransition(runAwayState, eatPillState, far);
		
		bindTransition(normalState, eatGhostState, nearBlue);
		bindTransition(eatGhostState, normalState, nearNonBlue);
	}
	
	//Place your game logic here to play the game as Ms Pac-Man
	public int getAction(Game game, long timeDue)
	{		
		currentLoc = game.getCurPacManLoc();
		
		
		//closestGhost = -1;
		closestBlueGhost = -1;
		closestNonBlueGhost = -1;
		//int dist = Integer.MAX_VALUE;
		int distBlue = Integer.MAX_VALUE;
		int distNonBlue = Integer.MAX_VALUE;
		
		for (int i = 0; i < Game.NUM_GHOSTS; i++) {
			ghostDist[i] = game.getPathDistance(currentLoc, game.getCurGhostLoc(i));
			
			// if the ghost is not in the cell
			if (ghostDist[i] >= 0) { 
				
				// find closest ghost
				//if (ghostDist[i] < dist) {
				//	dist = ghostDist[i];
				//	closestGhost = i;
				//}
				
				// find closest blue ghost
				if (game.isEdible(i) && ghostDist[i] < distBlue) {
					distBlue = ghostDist[i];
					closestBlueGhost = i;
				}
				
				// find closest non-blue ghost
				if (!game.isEdible(i) && ghostDist[i] < distNonBlue) {
					distNonBlue = ghostDist[i];
					closestNonBlueGhost = i;
				}
			}
		}
		
		while (resultActions.isEmpty()) {
			resultActions.addAll(root.update().actions);
		}
		
		
		for (Accion action : resultActions) {
			pacmanActions.add((AccionPacMan)action);
		}
		
		// reset result actions
		resultActions.clear();
		
		// perform PacMan action
		return pacmanActions.removeFirst().act(game);
	}
	
	static void bindTransition(SubMaquinaEstado src, SubMaquinaEstado target, Transicion transition) {
		src.addTransition(transition);
		transition.source = src;
		transition.target = target;
	}
}