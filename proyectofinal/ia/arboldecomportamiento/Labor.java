package ia.arboldecomportamiento;

import java.util.LinkedList;

public abstract class Labor {
	public LinkedList<Labor> children = new LinkedList<Labor>();
	
	public abstract boolean run();
}
