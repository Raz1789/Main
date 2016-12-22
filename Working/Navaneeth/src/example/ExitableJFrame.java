package example;

import javax.swing.JFrame;

public class ExitableJFrame extends JFrame {

	private static final long serialVersionUID = 1196137015920634040L;
	
	public ExitableJFrame() {
		
	}
	public ExitableJFrame(String title) {
		super(title);
	}
	protected void frameInit() {
		super.frameInit();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

}
