package ia.hsm;

import java.util.LinkedList;

public class Transicion {
	public Estado source;
	public Estado target;
	public Condicion condition;
	public Accion action;
	
	public Transicion(Accion action, Condicion condition) {
		this.action = action;
		this.condition = condition;
	}
	
	public Transicion() {}

	public boolean isTriggered() {
		return condition.test();
	}

	public int getLevel() {
		return source.getDepth() - target.getDepth();
	}

	public Estado getTargetState() {
		return target;
	}

	public LinkedList<Accion> getAction() {
		LinkedList<Accion> result = new LinkedList<Accion>();
		result.add(action);
		return result;
	}

}
