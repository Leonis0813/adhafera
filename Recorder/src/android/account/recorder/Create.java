package android.account.recorder;

public class Create extends State {
	
	public Create(Controller ctrl) {
		super(ctrl);
	}
	
	public void putAccount(String[] column) {
		ctrl.createAccount(column);
	}
	
	public void clickCancel() {
		ctrl.cancelInput();
	}
}
