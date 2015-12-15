package ia.funciones;

public abstract class Transicion extends BaseTransicion {
	
	public EstadoMaquinaEstado target;
	
	public Condicion condition;
	
	public EstadoMaquinaEstado getTargetState() {
		return target;
	}
	
	public boolean isTriggered() {
		return condition.test();
	}
	
}
