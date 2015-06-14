import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

class SyougiTest extends Frame implements Runnable, MouseListener {
	// 駒名から画像パスを得るためのhuman用マップ
	static Map<String, String> humanKomaImagePathMap = new HashMap<String, String>() {
		{
			put("Ou", "./img/koma/sgl01.png");// 王
			put("Hisya", "./img/koma/sgl02.png");// 飛車
			put("Kaku", "./img/koma/sgl03.png");// 角
			put("Kin", "./img/koma/sgl04.png");// 金
			put("Gin", "./img/koma/sgl05.png");// 銀
			put("Kei", "./img/koma/sgl06.png");// 桂
			put("Kyou", "./img/koma/sgl07.png");// 香
			put("Fu", "./img/koma/sgl08.png");// 歩
			put("Ryuu", "./img/koma/sgl22.png");// 龍
			put("Uma", "./img/koma/sgl23.png");// 馬
			put("Narigin", "./img/koma/sgl25.png");// 成り銀
			put("Narikei", "./img/koma/sgl26.png");// 成り桂馬
			put("Narikyou", "./img/koma/sgl27.png");// 成り香
			put("Tokin", "./img/koma/sgl28.png");// と金
		}
	};

	// 駒名から画像パスを得るためのcpu用マップ
	static Map<String, String> cpuKomaImagePathMap = new HashMap<String, String>() {
		{
			put("Ou", "./img/koma/sgl41.png");// 王
			put("Hisya", "./img/koma/sgl32.png");// 飛車
			put("Kaku", "./img/koma/sgl33.png");// 角
			put("Kin", "./img/koma/sgl34.png");// 金
			put("Gin", "./img/koma/sgl35.png");// 銀
			put("Kei", "./img/koma/sgl36.png");// 桂
			put("Kyou", "./img/koma/sgl37.png");// 香
			put("Fu", "./img/koma/sgl38.png");// 歩
			put("Ryuu", "./img/koma/sgl51.png");// 龍
			put("Uma", "./img/koma/sgl53.png");// 馬
			put("Narigin", "./img/koma/sgl55.png");// 成り銀
			put("Narikei", "./img/koma/sgl56.png");// 成り桂馬
			put("Narikyou", "./img/koma/sgl57.png");// 成り香
			put("Tokin", "./img/koma/sgl58.png");// と金
		}
	};

	static Koma field[][] = {
			{ null, null, null, null, null, null, null, null, null, null, null },
			{ null, new Kyou("Cpu"), new Kei("Cpu"), new Gin("Cpu"),
					new Kin("Cpu"), new Ou("Cpu"), new Kin("Cpu"),
					new Gin("Cpu"), new Kei("Cpu"), new Kyou("Cpu"), null },
			{ null, null, new Hisya("Cpu"), null, null, null, null, null,
					new Kaku("Cpu"), null, null },
			{ null, new Fu("Cpu"), new Fu("Cpu"), new Fu("Cpu"), new Fu("Cpu"),
					new Fu("Cpu"), new Fu("Cpu"), new Fu("Cpu"), new Fu("Cpu"),
					new Fu("Cpu"), null },
			{ null, null, null, null, null, null, null, null, null, null, null },
			{ null, null, null, null, null, null, null, null, null, null, null },
			{ null, null, null, null, null, null, null, null, null, null, null },
			{ null, new Fu("Human"), new Fu("Human"), new Fu("Human"),
					new Fu("Human"), new Fu("Human"), new Fu("Human"),
					new Fu("Human"), new Fu("Human"), new Fu("Human"), null },
			{ null, null, new Kaku("Human"), null, null, null, null, null,
					new Hisya("Human"), null, null },
			{ null, new Kyou("Human"), new Kei("Human"), new Gin("Human"),
					new Kin("Human"), new Ou("Human"), new Kin("Human"),
					new Gin("Human"), new Kei("Human"), new Kyou("Human"), null },
			{ null, null, null, null, null, null, null, null, null, null, null } };

	public List<Koma> humanMotigomaLst;
	public List<Koma> cpuMotigomaLst;

	static class MotigomaPlaceInfo {
		int startX;
		int startY;
		int addX;
		int addY;

		public MotigomaPlaceInfo(int startX, int startY, int addX, int addY) {
			this.startX = startX;
			this.startY = startY;
			this.addX = addX;
			this.addY = addY;
		}
	}

	public MotigomaPlaceInfo humanMotigomaPlaceInfo;
	public MotigomaPlaceInfo cpuMotigomaPlaceInfo;

	public SyougiTest() {
		this.setSize(new Dimension(1200, 740));
		this.addMouseListener(this);
		this.addWindowListener(new WindowMyAdapter());
		// 持ち駒表示よう情報
		humanMotigomaPlaceInfo = new MotigomaPlaceInfo((300 + 600),
				((6 - 1) * 64 + 82), 45, 64);
		cpuMotigomaPlaceInfo = new MotigomaPlaceInfo((300 - 60),
				((4 - 1) * 64 + 82), -45, -64);
		// 持ち駒のリスト
		humanMotigomaLst = new ArrayList<Koma>();
		cpuMotigomaLst = new ArrayList<Koma>();

		humanMotigomaLst.add(new Fu("Human"));
		/*
		 * humanMotigomaLst.add(new Fu("Human")); humanMotigomaLst.add(new
		 * Fu("Human")); humanMotigomaLst.add(new Fu("Human"));
		 * humanMotigomaLst.add(new Fu("Human")); humanMotigomaLst.add(new
		 * Fu("Human")); humanMotigomaLst.add(new Kyou("Human"));
		 * humanMotigomaLst.add(new Fu("Human")); humanMotigomaLst.add(new
		 * Kei("Human"));
		 * 
		 * cpuMotigomaLst.add(new Fu("Cpu")); cpuMotigomaLst.add(new
		 * Kei("Cpu")); cpuMotigomaLst.add(new Kei("Cpu"));
		 * cpuMotigomaLst.add(new Kei("Cpu")); cpuMotigomaLst.add(new
		 * Kei("Cpu")); cpuMotigomaLst.add(new Kei("Cpu"));
		 * cpuMotigomaLst.add(new Kei("Cpu")); cpuMotigomaLst.add(new
		 * Kei("Cpu")); cpuMotigomaLst.add(new Kei("Cpu"));
		 * cpuMotigomaLst.add(new Kei("Cpu")); cpuMotigomaLst.add(new
		 * Kei("Cpu")); cpuMotigomaLst.add(new Kei("Cpu"));
		 */

	}

	public static void main(String args[]) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException {
		SyougiTest syougi = new SyougiTest();
		syougi.setVisible(true);
		// thread作成
		Thread th;
		th = new Thread(syougi);
		th.start();

	}

	class WindowMyAdapter extends WindowAdapter {

		public void windowClosing(WindowEvent arg0) {
			System.exit(0);
		}
	}

	@Override
	public void run() { // TODO Auto-generated
						// method stub
		Scanner scan = new Scanner(System.in);
		List<Koma> motigoma;
		while (true) {
			System.out.println("please type owner type:Human or Cpu");
			String owner = scan.next();

			if (owner.equals("Human")) {
				motigoma = humanMotigomaLst;
			} else if (owner.equals("Cpu")) {
				motigoma = cpuMotigomaLst;
			} else
				continue;

			System.out.println("please type play type:normal or motigoma");
			String playType = scan.next();

			if (playType.equals("normal")) {
				// 盤の中の駒を選択してプレイ
				try {
					NormalPlay.normalPlay(field, motigoma, owner);
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException | NoSuchMethodException
						| SecurityException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (playType.equals("motigoma")) {
				// 持ち駒の中の駒を選択してプレイ
				MotigomaPlay.motigomaPlay(field, motigoma, owner);
			}
			// paint実行
			this.repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
	}

	String boardImagePath = "./img/board2.jpg";

	public void paint(Graphics g) {
		String imagePath = null;
		String komaName = null;
		String owner = null;
		Image komaImage;
		Image boardImage;

		boardImage = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(boardImagePath));
		g.drawImage(boardImage, 300, 50, this);
		// 盤上の駒を表示
		for (int i = 1; i < 10; i++) {
			for (int j = 1; j < 10; j++) {
				if (field[i][j] != null) {
					komaName = field[i][j].komaType;
					owner = field[i][j].owner; // 駒名から画像パス取得
					komaImage = createKomaImage(komaName, owner);
					// 画像表示
					g.drawImage(komaImage, 332 + ((j - 1) * 60),
							((i - 1) * 64) + 82, this);
				}
			}
		}
		// 持ち駒表示
		this.viewMotigoma(g, humanMotigomaPlaceInfo, humanMotigomaLst);
		this.viewMotigoma(g, cpuMotigomaPlaceInfo, cpuMotigomaLst);
	}

	// 持ち駒画像を表示
	public void viewMotigoma(Graphics g, MotigomaPlaceInfo placeInfo,
			List<Koma> motigomaLst) {
		Koma motigoma;
		Image motigomaImage = null;

		for (int i = 0; i < motigomaLst.size(); i++) {
			motigoma = motigomaLst.get(i);
			motigomaImage = createKomaImage(motigoma.komaType, motigoma.owner);
			// ５個で折り返す
			g.drawImage(motigomaImage, placeInfo.startX
					+ ((i % 5) * placeInfo.addX), placeInfo.startY
					+ ((i / 5) * placeInfo.addY), this);
		}
	}

	public Image createKomaImage(String komaName, String owner) {
		Image komaImage;
		String imagePath = null;
		// 画像パス取得
		if (owner.equals("Human")) {
			imagePath = humanKomaImagePathMap.get(komaName);
		} else if (owner.equals("Cpu")) {
			imagePath = cpuKomaImagePathMap.get(komaName);
		}

		// 画像生成
		komaImage = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(imagePath));

		return komaImage;

	}

	public void mouseClicked(MouseEvent e) {
		java.awt.Point point = e.getPoint();
		System.out.println("X座標:" + point.x);
		System.out.println("Y座標:" + point.y);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
