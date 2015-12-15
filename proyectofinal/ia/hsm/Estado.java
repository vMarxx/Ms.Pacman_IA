package ia.hsm;

import java.util.LinkedList;

public class Estado extends HSMBase {
	LinkedList<Transicion> transitions = new LinkedList<Transicion>();
	public SubMaquinaEstado parent;
	
	public LinkedList<Accion> getAction() {
		return new LinkedList<Accion>();
	}
	
	public LinkedList<Accion> getEntryAction() {
		return new LinkedList<Accion>();
	}
	
	public LinkedList<Accion> getExitAction() {
		return new LinkedList<Accion>();
	}

	@Override
	public LinkedList<Estado> getStates() {
		LinkedList<Estado> result = new LinkedList<Estado>();
		result.add(this);
		return result;
	}
	
	public int getDepth() {
		if (parent == null) {
			return 0;
		}
		
		return 1 + parent.getDepth();
	}
	
	public void addTransition(Transicion transition) {
		transitions.add(transition);
	}
}
