package ia.hsm;

import java.util.LinkedList;

import ia.hsm.HSMBase.UpdateResult;

public class HSMTest {
	static final LinkedList<Accion> NO_ACTION = new LinkedList<Accion>();
	
	static class TestAction extends Accion {
		public String actionText;
		
		public TestAction(String actionText) {
			this.actionText = actionText;
		}
		
		@Override 
		public void act() {
			System.out.println(actionText);
		}
	}
	
	static class TestState extends SubMaquinaEstado {
		public Accion action;
		
		public TestState(String actionText) {
			this.action = new TestAction(actionText);
		}
		
		@Override
		public LinkedList<Accion> getAction() {
			LinkedList<Accion> result = new LinkedList<Accion>();
			result.add(this.action);
			return result;
		}
		
		@Override
		public LinkedList<Accion> getEntryAction() {
			return NO_ACTION;
		}
		
		@Override
		public LinkedList<Accion> getExitAction() {
			return NO_ACTION;
		}
	}
	
	static int TEST_VALUE = 0;
	
	public static void main(String[] args) {
		SubMaquinaEstado root = new SubMaquinaEstado();
		
		SubMaquinaEstado state1 = new SubMaquinaEstado();
		TestState state2 = new TestState("State 2");
		
		Transicion transition1 = new Transicion(new TestAction("Transition 1"), new Condicion() {
			@Override
			public boolean test() {
				return TEST_VALUE == 1;
			}
		});
		
		Transicion transition3 = new Transicion(new TestAction("Transition 3"), new Condicion() {

			@Override
			public boolean test() {
				return TEST_VALUE == 3;
			}
			
		});
		
		bindTransition(state1, state2, transition1);
		bindTransition(state2, state1, transition3);
		
		root.addState(state1);
		root.addState(state2);
		
		root.initialState = root.currentState = state1;
		
		TestState internal1 = new TestState("Internal 1");
		TestState internal2 = new TestState("Internal 2");
		
		Transicion transition2 = new Transicion(new TestAction("Transition 2"), new Condicion() {
			@Override
			public boolean test() {
				return TEST_VALUE == 2;
			}
		});
		
		bindTransition(internal1, internal2, transition2);
		
		state1.addState(internal1);
		state2.addState(internal2);
		
		state1.initialState = state1.currentState = internal1;
		
		
		UpdateResult result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		TEST_VALUE = 2;
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		TEST_VALUE = 1;
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		TEST_VALUE = 3;
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
		result = root.update();
		
		for (Accion action : result.actions) {
			action.act();
		}
		
	}
	
	static void bindTransition(SubMaquinaEstado src, SubMaquinaEstado target, Transicion transition) {
		src.addTransition(transition);
		transition.source = src;
		transition.target = target;
	}

}
