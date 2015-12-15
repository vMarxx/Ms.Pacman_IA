package ia.arboldecomportamiento;

public class Selector extends Labor {

	@Override
	public boolean run() {
		for (Labor t : children) {
			if (t.run()) return true;
		}
		return false;
	}

}
