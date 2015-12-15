/**
 * 
 */
package Juego.Entradas.Fantasmas;
import Juego.core.*;

/**
 * @author user
 *
 */

abstract class DecisionNode {
	public abstract Accion makeDecision(Game game); 
}


 public abstract class Decision extends DecisionNode{
	public DecisionNode trueNode;
	public DecisionNode falseNode;
	
	public DecisionNode getBranch(Game game){
		System.out.println("Warning!");
		return null;
	}
		
	


	public Accion makeDecision(Game game) {
		  DecisionNode branch = this.getBranch(game);
		  return branch.makeDecision(game); 
	}

 }




