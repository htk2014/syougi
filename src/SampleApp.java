import java.awt.*;
import java.awt.event.*;

public class SampleApp extends Frame {
	Image img;

	public SampleApp() {
		this.setSize(new Dimension(300, 200));
		this.addWindowListener(new WindowMyAdapter());
		img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./img/board2.jpg"));
	}

	public void paint(Graphics g) {
		if (img != null) {
			System.out.println("success");
			g.drawImage(img, 0, 0, this);
		}
	}

	public static void main(String[] args) {
		new SampleApp().setVisible(true);
	}

	class WindowMyAdapter extends WindowAdapter {

		public void windowClosing(WindowEvent arg0) {
			System.exit(0);
		}
	}
}