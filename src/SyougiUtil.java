import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

class SyougiUtil {

	// 文字から動的に駒インスタンス作る
	static Koma makeKoma(String komaName, String owner)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {

		// 駒の名前からクラス生成
		Class komaClass = Class.forName(komaName);

		// コンストラクタ生成
		Class<?>[] types = { String.class };
		Constructor<String> constructor;
		constructor = komaClass.getConstructor(types);

		// 引数
		Object[] args = { owner };

		Object obj;
		// インスタンス生成
		obj = constructor.newInstance(args);

		return (Koma) obj;
	}

	static Koma searchKoma(List<Koma> motigoma, String komaName) {
		Koma searchKoma = null;
		Koma koma;
		for (int i = 0; i < motigoma.size(); i++) {
			koma = motigoma.get(i);
			if (komaName.equals(koma.komaType)) {
				searchKoma = koma;
				break;
			}
		}
		return searchKoma;
	}

	static boolean contain(List<Point> pointLst, Point point) {
		// リストがある座標をもっているならtrueを返す
		for (int i = 0; i < pointLst.size(); i++) {
			if (point.isEqual(pointLst.get(i))) {
				return true;
			}
		}

		return false;
	}

	static boolean contain(List<Koma> motigomaLst, Koma selectKoma) {
		// 持ち駒リストがある選択した駒をもっているならtrueを返す
		for (int i = 0; i < motigomaLst.size(); i++) {
			// 名前が同じならtrue
			if (selectKoma.komaType.equals(motigomaLst.get(i).komaType)) {
				return true;
			}
		}

		return false;
	}

	static class KomaInfo {
		public Koma koma;
		public Point point;

		public KomaInfo(Koma koma, Point point) {
			this.koma = koma;
			this.point = point;
		}
	}

	// ownerの駒を集める
	static List<KomaInfo> collectKoma(Koma[][] field, String owner) {
		List<KomaInfo> komaInfoLst = new ArrayList<KomaInfo>();
		Koma koma;
		KomaInfo komaInfo;
		for (int i = 1; i < 10; i++) {
			for (int j = 1; j < 10; j++) {
				koma = field[i][j];
				if (koma != null) {
					if (owner.equals(koma.owner)) {
						komaInfo = new KomaInfo(koma, new Point(j, i));
						komaInfoLst.add(komaInfo);
					}
				}

			}
		}

		return komaInfoLst;
	}
    //randomThinkよう
	static List<Koma> collectKoma(List<Koma> motigomaLst) {
		List<Koma> komaLst = new ArrayList<Koma>();
		Koma koma;
		for (int i = 1; i < motigomaLst.size(); i++) {
			koma = motigomaLst.get(i);
			if (koma != null) {
				komaLst.add(koma);
			}

		}

		return komaLst;
	}

}