package android.account.recorder;

public class Update extends State {
	
	public Update(Controller ctrl) {
		super(ctrl);
	}

	public void putAccount(String[] column) {
		ctrl.updateAccount(column);
	}
	
	public void clickCancel() {
		ctrl.cancelEdit();
	}
}
