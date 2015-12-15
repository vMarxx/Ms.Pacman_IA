package ia.funciones;

public class Accion {
	
	public Accion next;
	
	public final Accion getLast() {
		// Si estamos en la final, y entonces terminar
        if (next == null) return this;

        // De lo contrario, encontrar el final iterativa
        Accion thisAction = this;
        Accion nextAction = next;
        while(nextAction != null) {
        	thisAction = nextAction;
            nextAction = nextAction.next;
        }

        // El elemento final es en thisAction.
        return thisAction;
	}
	
	public void act() {}
}
