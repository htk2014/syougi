import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class MotigomaPlay {

	static void motigomaPlay(Koma[][] field, List<Koma> motigoma, String owner) {
		Scanner scan = new Scanner(System.in);

		System.out.println("please type koma name you want put");
		String komaName = scan.next();
		System.out.println("please type x point you want put");
		int x = Integer.parseInt(scan.next());
		System.out.println("please type y point you want put");
		int y = Integer.parseInt(scan.next());
		Koma selectKoma = SyougiUtil.searchKoma(motigoma, komaName);

		// 選んだ駒が持ち駒内にあるか
		if (SyougiUtil.contain(motigoma, selectKoma)) {
			List<Point> nullPointLst = collectNullPoint(field);

			// 空白座標が存在しないか？
			if (nullPointLst == null) {
				System.out.println("putable point don't excist");
				// 終了
				return;
			}
			Point newPoint = new Point(x, y);

			// ここにフィルターを入れる（二歩だとかおけないところをフィルター）
			switch (selectKoma.komaType) {
			case "Fu":
				nullPointLst = fileteringFu(nullPointLst, owner, field);
				break;
			case "Kyou":
				nullPointLst = fileteringKyou(nullPointLst, owner);
				break;
			case "Kei":
				nullPointLst = fileteringKei(nullPointLst, owner);
				break;
			}
			// 置くことが可能な座標リストの中に、選択した座標がそんざいすればそこに駒を置く
			if (SyougiUtil.contain(nullPointLst, newPoint)) {
				field[newPoint.y][newPoint.x] = selectKoma;
				// 持ち駒から消去
				motigoma.remove(selectKoma);

				// 盤情報表示
				for (int i = 0; i < 11; i++) {
					System.out.println(Arrays.toString(field[i]));
				}
			} else
				System.out.println("can't put this koma to field");

		} else
			System.out.println("selectKoma don't excist in motigoma");
	}

	static List<Point> collectKomaPoint(Koma[][] field, String komaName,
			String owner) {
		List<Point> komaPointLst = new ArrayList<Point>();
		// 歩の座標を集める
		for (int i = 1; i < 10; i++) {
			for (int j = 1; j < 10; j++) {
				if (field[i][j] != null) {
					if (field[i][j].komaType.equals(komaName)
							&& field[i][j].owner.equals(owner)) {
						komaPointLst.add(new Point(j, i));
					}
				}
			}
		}
		return komaPointLst;

	}

	// 空白座標を集める持ち駒ようの関数
	static List<Point> collectNullPoint(Koma[][] field) {
		List<Point> nullPointLst = new ArrayList<Point>();
		for (int i = 1; i < 10; i++) {
			for (int j = 1; j < 10; j++) {
				if (field[i][j] == null) {
					nullPointLst.add(new Point(j, i));
				}
			}
		}
		// 空白の座標を一つ以上回収しているならリストを返す
		if (0 < nullPointLst.size()) {
			return nullPointLst;
		} else
			return null;
	}

	static List<Point> filteringForbiddenPointY(List<Point> nullPointLst,
			int forbiddenPointY) {
		for (int i = 0; i < nullPointLst.size(); i++) {
			Point point = nullPointLst.get(i);
			if (point.y == forbiddenPointY) {
				nullPointLst.remove(point);
			}
		}
		return nullPointLst;
	}

	static List<Point> filteringForbiddenPointX(List<Point> nullPointLst,
			int forbiddenPointX) {
		for (int i = 0; i < nullPointLst.size(); i++) {
			Point point = nullPointLst.get(i);
			if (point.x == forbiddenPointX) {
				nullPointLst.remove(point);
			}
		}
		return nullPointLst;
	}

	static List<Point> fileteringKyou(List<Point> nullPointLst, String owner) {
		int forbiddenPointY = -1;
		if (owner.equals("Human")) {
			forbiddenPointY = 1;
		} else if (owner.equals("Cpu")) {
			forbiddenPointY = 9;
		}

		if (forbiddenPointY != -1) {
			return filteringForbiddenPointY(nullPointLst, forbiddenPointY);
		}
		return nullPointLst;
	}

	static List<Point> fileteringKei(List<Point> nullPointLst, String owner) {
		// 桂馬は８，９(Human)　２，１(Cpu)においてはいけない
		int forbiddenPointY[] = new int[2];
		if (owner.equals("Human")) {
			forbiddenPointY[0] = 2;
			forbiddenPointY[1] = 1;
		} else if (owner.equals("Cpu")) {
			forbiddenPointY[0] = 8;
			forbiddenPointY[1] = 9;
		}

		for (int i = 0; i < forbiddenPointY.length; i++) {
			nullPointLst = filteringForbiddenPointY(nullPointLst,
					forbiddenPointY[i]);
		}

		return nullPointLst;
	}

	static List<Point> fileteringFu(List<Point> nullPointLst, String owner,
			Koma[][] field) {
		int forbiddenPointY = -1;
		if (owner.equals("Human")) {
			forbiddenPointY = 2;
		} else if (owner.equals("Cpu")) {
			forbiddenPointY = 8;
		}

		if (forbiddenPointY != -1) {
			nullPointLst = filteringForbiddenPointY(nullPointLst,
					forbiddenPointY);
		}

		// ２歩
		// ownerの歩の座標を集める
		int forbiddenPointX;
		List<Point> fuPointLst = collectKomaPoint(field, "Fu", owner);
		for (int i = 0; i < fuPointLst.size(); i++) {
			// 歩の座標xをつかってフィルタリング
			forbiddenPointX = fuPointLst.get(i).x;
			nullPointLst = filteringForbiddenPointX(nullPointLst,
					forbiddenPointX);
		}

		return nullPointLst;
	}
}