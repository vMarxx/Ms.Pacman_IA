package ia.funciones;

public abstract class BaseTransicion {
	
	public abstract boolean isTriggered();
	
	public Accion getActions() {
		return null;
	}
	
	public BaseTransicion next;
}
