package ia.hsm;

import java.util.LinkedList;

public class SubMaquinaEstado extends Estado {
	
	public LinkedList<Estado> states = new LinkedList<Estado>();
	
	public Estado initialState;
	public Estado currentState;
	
	// Estados conseguidos por nuestra ia
	@Override
	public LinkedList<Estado> getStates() {
		LinkedList<Estado> result = new LinkedList<Estado>();
		result.add(this);
		
		if (currentState != null) {
			result.addAll(currentState.getStates());
		}
		
		return result;
	}
	
	@Override
	public UpdateResult update() {
		UpdateResult result;
		
		if (initialState == null) {
			return super.update();
		}
		
		
		// Si no hay ningún estado, usamos el primero
		if (currentState == null) {
			currentState = initialState;
			result = new UpdateResult();
			result.actions = currentState.getEntryAction();
			return result;
		}
		
		//Encuentra una transición en el estado
		Transicion triggeredTransition = null;
		for (Transicion transition : currentState.transitions) {
			if (transition.isTriggered()) {
				triggeredTransition = transition;
				break;
			}
		}		
		
		// Si se encuentra una transicion
		if (triggeredTransition != null) {
			
			result = new UpdateResult();
			result.actions = new LinkedList<Accion>();
			result.transition = triggeredTransition;
			result.level = triggeredTransition.getLevel();
		} 
		// otherwise recurse down for a result
		else {
		
			return currentState.update();
		}
		
		// Verifica si el resultado contiene una transición
		if (result.transition != null) {
			// Actua basado en el nivel
			if (result.level == 0) {
				
				//System.out.println("Same level");
				
				Estado targetState = result.transition.getTargetState();
				result.actions.addAll(currentState.getExitAction());
				result.actions.addAll(result.transition.getAction());
				result.actions.addAll(targetState.getEntryAction());
				
				//incializa el estado actual
				currentState = targetState;
				
				// agrega acciones puede que sea un estado 
				result.actions.addAll(getAction());
			} else if (result.level > 0) {
				
				// Designado para un nivel mayor
				// Final del estado actual
				result.actions.addAll(currentState.getExitAction());
				currentState = null;
				
				// Decrementa el numero de niveles a ir
				result.level--;
			} else {
				
				// Necesita ser pasado desde abajo
				Estado targetState = result.transition.getTargetState();
				SubMaquinaEstado targetMachine = targetState.parent;
				result.actions.addAll(result.transition.getAction());
				result.actions.addAll(targetMachine.updateDown(targetState, -result.level));
				
				// Elimina transiciones
				result.transition = null;
			}
		}
		
		// Si no se puede conseguir la transicion
		else {
			
			// simplemente usa la accion normal
			result.actions.addAll(getAction());
		}
		
		// regresa resultados
		return result;
	}
	
	
	// cada estado en turno por el numero de nivel dado
	private LinkedList<Accion> updateDown(Estado state, int level) {
		LinkedList<Accion> actions;
		// si no esta al top del nivel, sigue recursando
		if (level > 0) {
			// Pasa atraves de las transiciones y las acciones
			actions = parent.updateDown(this, level-1);
		} 
		
		// De otra forma no hay acciones que agregar
		else {
			actions = new LinkedList<Accion>();
		}
		
		// Si hay un estado actual, se sale
		if (currentState != null) {
			actions.addAll(currentState.getExitAction());
		}
		
		// Movimiento al siguiente estado
		currentState = state;
		
		return null;
	}
	
	public void addState(SubMaquinaEstado state) {
		states.add(state);
		state.parent = this;
	}
}
