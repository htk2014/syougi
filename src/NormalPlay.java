import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class NormalPlay {
	// プレイヤ用座標表(プレイヤからみた）
	static Map<MoveType, Point> humanPointMap = new HashMap<MoveType, Point>() {
		{
			put(MoveType.RIGHT, new Point(1, 0));// 右

			put(MoveType.LEFT, new Point(-1, 0));// 左

			put(MoveType.UP, new Point(0, -1));// 上

			put(MoveType.DOWN, new Point(0, 1));// 下

			put(MoveType.RIGHTUP, new Point(1, -1));// 右斜め上

			put(MoveType.LEFTUP, new Point(-1, -1));// 左斜め下

			put(MoveType.RIGHTDOWN, new Point(1, 1));// 右斜め下

			put(MoveType.LEFTDOWN, new Point(-1, 1));// 左斜め下

			put(MoveType.KEIRIGHTJUMP, new Point(1, -2));// 桂馬の右ジャンプ

			put(MoveType.KEILEFTJUMP, new Point(-1, -2));// 桂馬の左ジャンプ

			put(MoveType.RIGHTJUMP, new Point(1, 0));// 右複数移動

			put(MoveType.LEFTJUMP, new Point(-1, 0));// 左複数移動

			put(MoveType.UPJUMP, new Point(0, -1));// 上複数移動

			put(MoveType.DOWNJUMP, new Point(0, 1));// 下複数移動

			put(MoveType.RIGHTUPJUMP, new Point(1, -1));// 右斜め上複数移動

			put(MoveType.LEFTUPJUMP, new Point(-1, -1));// 左斜め下複数移動

			put(MoveType.RIGHTDOWNJUMP, new Point(1, 1));// 右斜め下複数移動

			put(MoveType.LEFTDOWNJUMP, new Point(-1, 1));// 左斜め下複数移動

		}
	};

	// cpu用座標表(プレイヤからみた）
	static Map<MoveType, Point> cpuPointMap = new HashMap<MoveType, Point>() {
		{
			put(MoveType.RIGHT, new Point(-1, 0));// 右

			put(MoveType.LEFT, new Point(1, 0));// 左

			put(MoveType.UP, new Point(0, 1));// 上

			put(MoveType.DOWN, new Point(0, -1));// 下

			put(MoveType.RIGHTUP, new Point(-1, 1));// 右斜め上

			put(MoveType.LEFTUP, new Point(1, 1));// 左斜め下

			put(MoveType.RIGHTDOWN, new Point(-1, -1));// 右斜め下

			put(MoveType.LEFTDOWN, new Point(1, -1));// 左斜め下

			put(MoveType.KEIRIGHTJUMP, new Point(-1, 2));// 桂馬の右ジャンプ

			put(MoveType.KEILEFTJUMP, new Point(1, 2));// 桂馬の左ジャンプ

			put(MoveType.RIGHTJUMP, new Point(-1, 0));// 右複数移動

			put(MoveType.LEFTJUMP, new Point(1, 0));// 左複数移動

			put(MoveType.UPJUMP, new Point(0, 1));// 上複数移動

			put(MoveType.DOWNJUMP, new Point(0, -1));// 下複数移動

			put(MoveType.RIGHTUPJUMP, new Point(-1, 1));// 右斜め上複数移動

			put(MoveType.LEFTUPJUMP, new Point(1, 1));// 左斜め下複数移動

			put(MoveType.RIGHTDOWNJUMP, new Point(-1, -1));// 右斜め下複数移動

			put(MoveType.LEFTDOWNJUMP, new Point(1, -1));// 左斜め下複数移動

		}
	};
	static Map<String, Integer> promotePointMap = new HashMap<String, Integer>() {
		{
			put("Human", 3);// 人間の駒が成れるｙ座標
			put("Cpu", 7);// cpuの駒が成れるｙ座標
		}
	};

	// cpuがランダムで指してくる
	static void normalPlay2(Koma[][] field, List<Koma> motigoma, String owner,
			Point nowPoint, Point newPoint) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException {
		Koma selectKoma = field[nowPoint.y][nowPoint.x];

		if (selectKoma != null) {
			System.out.println(selectKoma.komaType);
		}

		if (field[nowPoint.y][nowPoint.x] != null) {
			// 入力したプレイヤーの駒か？
			if (owner.equals(field[nowPoint.y][nowPoint.x].owner)) {
				// 移動可能座標を集める
				List<Point> pointLst = collectMoveablePoint(field, selectKoma,
						nowPoint);
				if (pointLst != null) {
					// 移動可能座標表示
					if (pointLst.size() > 0) {
						for (int i = 0; i < pointLst.size(); i++) {
							System.out.println(pointLst.get(i).toString());
						}
					} else
						System.out.println("list is null");
					// 移動可能
					if (SyougiUtil.contain(pointLst, newPoint)) {
						// 敵の駒が移動する座標にあるか
						if (field[newPoint.y][newPoint.x] != null
								&& field[newPoint.y][newPoint.x].owner != owner) {
							Koma getKoma = field[newPoint.y][newPoint.x];
							// 成っている駒なら通常状態に戻す
							if (getKoma.promoted) {
								getKoma = getKoma.getOldState(owner);
							}

							String getKomaName = getKoma.komaType;

							motigoma.add(SyougiUtil
									.makeKoma(getKomaName, owner));

							System.out.println("koma get");
							System.out.println(getKoma.komaType);
						}

						field[nowPoint.y][nowPoint.x] = null;

						// 成れる位置にいるか
						boolean promote = false;
						int promotePointY = promotePointMap.get(owner);

						if (owner.equals("Human")
								&& promotePointY >= newPoint.y) {
							promote = true;
						} else if (owner.equals("Cpu")
								&& promotePointY <= newPoint.y) {
							promote = true;
						}
						// 成れるか?
						if (promote) {
							// 成る
							field[newPoint.y][newPoint.x] = selectKoma
									.getPromoteKoma(owner);
						} else {
							// 移動
							field[newPoint.y][newPoint.x] = selectKoma;
						}

						System.out.println();
						// 盤情報表示
						for (int i = 0; i < 11; i++) {
							System.out.println(Arrays.toString(field[i]));
						}

						System.out.println(owner + "motigoma");
						if (0 < motigoma.size()) {
							// 持ち駒表示
							for (int i = 0; i < motigoma.size(); i++) {
								System.out.println(motigoma.get(i).komaType);
							}
						} else
							System.out.println("motigoma null");

					} else
						System.out.println("can't move");

				}

			} else
				System.out.println("owner is eleagal");
		} else
			System.out.println("it is null");
	}

	static void normalPlay(Koma[][] field, List<Koma> motigoma, String owner)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		Scanner scan = new Scanner(System.in);
		System.out.println("please type x point");
		int pointX = Integer.parseInt(scan.next());
		System.out.println("please type y point");
		int pointY = Integer.parseInt(scan.next());
		// 移動したい座標
		System.out.println("please type new x point");
		int newPointX = Integer.parseInt(scan.next());
		System.out.println("please type new y point");
		int newPointY = Integer.parseInt(scan.next());

		Point nowPoint = new Point(pointX, pointY);
		Point newPoint = new Point(newPointX, newPointY);
		Koma selectKoma = field[pointY][pointX];

		if (selectKoma != null) {
			System.out.println(selectKoma.komaType);
		}

		if (field[pointY][pointX] != null) {
			// 入力したプレイヤーの駒か？
			if (owner.equals(field[pointY][pointX].owner)) {
				// 移動可能座標を集める
				List<Point> pointLst = collectMoveablePoint(field, selectKoma,
						nowPoint);
				if (pointLst != null) {
					// 移動可能座標表示
					if (pointLst.size() > 0) {
						for (int i = 0; i < pointLst.size(); i++) {
							System.out.println(pointLst.get(i).toString());
						}
					} else
						System.out.println("list is null");
					// 移動可能
					if (SyougiUtil.contain(pointLst, newPoint)) {
						// 敵の駒が移動する座標にあるか
						if (field[newPoint.y][newPoint.x] != null
								&& field[newPoint.y][newPoint.x].owner != owner) {
							Koma getKoma = field[newPoint.y][newPoint.x];
							// 成っている駒なら通常状態に戻す
							if (getKoma.promoted) {
								getKoma = getKoma.getOldState(owner);
							}

							String getKomaName = getKoma.komaType;

							motigoma.add(SyougiUtil
									.makeKoma(getKomaName, owner));

							System.out.println("koma get");
							System.out.println(getKoma.komaType);
						}

						field[nowPoint.y][nowPoint.x] = null;

						// 成れる位置にいるか
						boolean promote = false;
						int promotePointY = promotePointMap.get(owner);

						if (owner.equals("Human")
								&& promotePointY >= newPoint.y) {
							promote = true;
						} else if (owner.equals("Cpu")
								&& promotePointY <= newPoint.y) {
							promote = true;
						}
						// 成れるか?
						if (promote) {
							// 成る
							field[newPoint.y][newPoint.x] = selectKoma
									.getPromoteKoma(owner);
						} else {
							// 移動
							field[newPoint.y][newPoint.x] = selectKoma;
						}

						System.out.println();
						// 盤情報表示
						for (int i = 0; i < 11; i++) {
							System.out.println(Arrays.toString(field[i]));
						}

						System.out.println(owner + "motigoma");
						if (0 < motigoma.size()) {
							// 持ち駒表示
							for (int i = 0; i < motigoma.size(); i++) {
								System.out.println(motigoma.get(i).komaType);
							}
						} else
							System.out.println("motigoma null");

					} else
						System.out.println("can't move");

				}

			} else
				System.out.println("owner is eleagal");
		} else
			System.out.println("it is null");
	}

	// 移動可能座標を集める
	static List<Point> collectMoveablePoint(Koma[][] field, Koma selectKoma,
			Point nowPoint) {
		List<Point> MoveablePointLst = new ArrayList<Point>();
		MoveType moveType;
		Point addingPoint;
		Point collectedPoint;
		List<Point> collectedPointLst;

		for (int i = 0; i < selectKoma.moveTypeLst.size(); i++) {
			moveType = selectKoma.moveTypeLst.get(i);
			addingPoint = getPointMapVal(selectKoma.owner, moveType);// ジャンプ系以外の動きはmoveTypeをそのまま,ジャンプ系は一回一回の動き(RIGHTJUMPのmoveType=RIGHT)
			collectedPoint = null;
			collectedPointLst = null;

			// 座標を集める
			switch (moveType) {
			// 複数マス動かない場合
			case RIGHT:
			case LEFT:
			case UP:
			case DOWN:
			case RIGHTUP:
			case LEFTUP:
			case RIGHTDOWN:
			case LEFTDOWN:
			case KEIRIGHTJUMP:
			case KEILEFTJUMP:
				// 座標単体を集める
				collectedPoint = getPoint(field, nowPoint, addingPoint,
						selectKoma.owner);
				break;
			// ジャンプ系の駒の動きで座標を集める
			case RIGHTJUMP:
			case LEFTJUMP:
			case UPJUMP:
			case DOWNJUMP:
			case RIGHTUPJUMP:
			case LEFTUPJUMP:
			case RIGHTDOWNJUMP:
			case LEFTDOWNJUMP:
				// 座標をリストで集める
				collectedPointLst = getFewPoint(field, nowPoint, addingPoint,
						selectKoma.owner);
				break;
			}
			// 座標を集めているなら移動可能リストに追加
			if (collectedPoint != null) {
				MoveablePointLst.add(collectedPoint);
			}

			// リストに座標を集めているなら移動可能リストに追加
			if (collectedPointLst != null) {
				MoveablePointLst.addAll(collectedPointLst);
			}

		}

		return MoveablePointLst;
	}

	static Point getPointMapVal(String owner, MoveType moveType) {
		Point point = null;

		if (owner == "Human") {
			point = humanPointMap.get(moveType);
		} else if (owner == "Cpu") {
			point = cpuPointMap.get(moveType);
		}
		return point;
	}

	// 移動可能な座標を一つ集め、その座標で返す
	static Point getPoint(Koma[][] field, Point nowPoint, Point addingPoint,
			String owner) {
		Point searchPoint = nowPoint.add(addingPoint);
		Koma fieldVal = field[searchPoint.y][searchPoint.x];
		// 9*9以内かつ調べた座標の値がnullか調べた座標の駒が相手の物なら、その場所は移動可能
		if (searchPoint.pointInRange()
				&& (fieldVal == null || isEnemyKoma(fieldVal, owner))) {
			return searchPoint;
		} else
			return null;
	}

	// 移動可能な座標を複数集め、その座標をリストで返す
	static List<Point> getFewPoint(Koma[][] field, Point nowPoint,
			Point addingPoint, String owner) {
		List<Point> pointLst = new ArrayList<Point>();
		while (true) {
			Point searchPoint = getPoint(field, nowPoint, addingPoint, owner);
			// 座標を集めれる間、繰り返す
			if (searchPoint != null) {
				pointLst.add(searchPoint);
				// 調べた座標の駒が相手の物ならそれ以上行けないので,繰り返し終了
				if (isEnemyKoma(field[searchPoint.y][searchPoint.x], owner)) {
					break;
				}
				// 調べる座標を更新するためnowPointを更新
				nowPoint = nowPoint.add(addingPoint);
			} else
				break;
		}

		return pointLst;
	}

	// komaがnullじゃなく、ownerの物じゃないならtrue,違うならfalse
	static boolean isEnemyKoma(Koma koma, String owner) {
		if (koma != null) {
			if (koma.owner != owner) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	static class PointInfo {
		Point nowPoint;
		Point newPoint;

		public PointInfo(Point nowPoint, Point newPoint) {
			this.nowPoint = nowPoint;
			this.newPoint = newPoint;

		}
	}

	static PointInfo fieldRandomThink(Koma[][] field) {
		String owner = "Cpu";
		Random rnd = new Random();
		int ran;
		Point nowPoint;
		Point newPoint;
		SyougiUtil.KomaInfo selectKomaInfo;
		List<SyougiUtil.KomaInfo> komaInfoLst = null;

		// ownerの駒を集める
		komaInfoLst = SyougiUtil.collectKoma(field, owner);
		// 動けない駒をフィルター
		komaInfoLst = filterMoveableKoma(field, komaInfoLst);

		System.out.println(komaInfoLst.size());
		ran = rnd.nextInt(komaInfoLst.size());
		// ランダムでownerの駒を選択
		selectKomaInfo = komaInfoLst.get(ran);
		// 選択した駒の現在の座標
		nowPoint = selectKomaInfo.point;
		System.out.println(selectKomaInfo.koma.owner);
		// 移動可能座標を集める
		List<Point> moveablePointLst = collectMoveablePoint(field,
				selectKomaInfo.koma, nowPoint);
		System.out.println(moveablePointLst.size());
		ran = rnd.nextInt(moveablePointLst.size());
		// 移動先座標ランダムで取得
		newPoint = moveablePointLst.get(ran);

		return new PointInfo(nowPoint, newPoint);

	}

	static PointInfo randomThink(Koma[][] field, List<Koma> motigomaLst) {
		String owner = "Cpu";
		Random rnd = new Random();
		int ran;
		Point nowPoint;
		Point newPoint;
		SyougiUtil.KomaInfo selectKomaInfo;
		List<SyougiUtil.KomaInfo> komaInfoLst = null;

		// 持ち駒があるか
		if (motigomaLst.size() > 0) {
			// フィールドか持ち駒かのどちらか
			ran = ran = rnd.nextInt(1);
			if (ran == 0) {
				// フィールドの駒でランダム
				//komaInfoLst = fieldRandomThink(field);
			} else if (ran == 1) {
				// 持ち駒でランダム

				// komaInfoLst = SyougiUtil.collectKoma(motigomaLst, owner);
				// todo 持ち駒よう関数つくる
			}

		} else {
			// フィールドでランダム
			// ownerの駒を集める
			komaInfoLst = SyougiUtil.collectKoma(field, owner);
		}
		// 動けない駒をフィルター
		komaInfoLst = filterMoveableKoma(field, komaInfoLst);

		System.out.println(komaInfoLst.size());
		ran = rnd.nextInt(komaInfoLst.size());
		// ランダムでownerの駒を選択
		selectKomaInfo = komaInfoLst.get(ran);
		// 選択した駒の現在の座標
		nowPoint = selectKomaInfo.point;
		System.out.println(selectKomaInfo.koma.owner);
		// 移動可能座標を集める
		List<Point> moveablePointLst = collectMoveablePoint(field,
				selectKomaInfo.koma, nowPoint);
		System.out.println(moveablePointLst.size());
		ran = rnd.nextInt(moveablePointLst.size());
		// 移動先座標ランダムで取得
		newPoint = moveablePointLst.get(ran);

		return new PointInfo(nowPoint, newPoint);

	}

	static List<SyougiUtil.KomaInfo> filterMoveableKoma(Koma[][] field,
			List<SyougiUtil.KomaInfo> komaInfoLst) {
		SyougiUtil.KomaInfo komaInfo;
		Point point;
		Koma koma;
		for (int i = 0; i < komaInfoLst.size(); i++) {
			komaInfo = komaInfoLst.get(i);
			koma = komaInfo.koma;
			point = komaInfo.point;
			// 移動可能な座標の個数が0か
			if (collectMoveablePoint(field, koma, point).size() <= 0) {
				komaInfoLst.remove(komaInfo);

			}
		}

		return komaInfoLst;
	}
}