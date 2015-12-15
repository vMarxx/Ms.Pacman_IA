package ia.funciones;

public class EstadoMaquina {
	
	public EstadoMaquinaEstado initialState;
	
	public EstadoMaquinaEstado currentState;
	
	@SuppressWarnings("unused")
	public Accion update() {
		Accion actions = null;

        // Primer caso - no tenemos estado actual
        if (currentState == null)
        {
            // En este caso usamos la acción de entrada para el estado inicial.
            if (initialState != null) {

                // 
            	//Transición en el primer estado
                currentState = initialState;

                // vuelve en el estado inicial
                actions = currentState.getEntryActions();

            } else {

                // No tenemos nada que hacer
                actions = null;
            }
        }

        // De otra forma tenemos un estado actual
        else {
            // Empieza sin ninguna transición
            Transicion transition = null;

            // Checa a través de cada transicion
            BaseTransicion testTransition = currentState.firstTransition;
            while (testTransition != null) {
                if (testTransition.isTriggered()) {
                    transition = (Transicion)testTransition;
                    break;
                }
                testTransition = testTransition.next;
            }

            // checamos si encontramos alguna transición 
            if (transition != null) {
                // Encontramos el destino
                EstadoMaquinaEstado nextState = transition.getTargetState();

                // Acumulacion de lista de estados
                Accion tempList = null;
                Accion last = null;

                // Agregamos cada elemento en nuestra lista 
                actions = currentState.getExitActions();
                if (actions != null) {
                	last = actions.getLast();
                }

                tempList = transition.getActions();
                
                if (actions == null) {
                	actions = tempList;
                	last = actions.getLast();
                } else if (tempList != null) {
                	last.next = tempList;
                	last = tempList.getLast();
                }
                
                tempList = nextState.getEntryActions();
                if (actions == null) {
                	actions = tempList;
                	last = actions.getLast();
                } else if (tempList != null) {
                	last.next = tempList;
                }

                // Actualizar lista de estados
                currentState = nextState;
            }

            // Si no otra forma nuestras acciones son las del estado actual
           
            else {

                actions = currentState.getActions();
            }
        }

        return actions;
	}
	
}
