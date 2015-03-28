package android.account.recorder;

public abstract class State {
	protected Controller ctrl;
	
	public State(Controller ctrl) {
		this.ctrl = ctrl;
	}
	
	public abstract void putAccount(String[] column);
}
