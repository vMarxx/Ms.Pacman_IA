package ia.hsm;

import java.util.LinkedList;

public abstract class HSMBase {
	public static final class UpdateResult {
		public LinkedList<Accion> actions = new LinkedList<Accion>();
		public Transicion transition;
		public int level;
	}
	
	public LinkedList<Accion> getAction() {
		return new LinkedList<Accion>();
	}
	
	public UpdateResult update() {
		UpdateResult result = new UpdateResult();
		result.actions = getAction();
		result.transition = null;
		result.level = 0;
		return result;
	}
	
	public abstract LinkedList<Estado> getStates();
}
