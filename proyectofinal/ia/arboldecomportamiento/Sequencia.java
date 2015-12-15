package ia.arboldecomportamiento;

public class Sequencia extends Labor {

	@Override
	public boolean run() {
		for (Labor t : children) {
			if (!t.run()) return false;
		}
		return true;
	}

}
