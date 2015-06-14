import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum MoveType {
	RIGHT, LEFT, UP, DOWN, RIGHTUP, // 右斜め上
	RIGHTDOWN, // 右斜めした
	LEFTUP, // 左斜め上
	LEFTDOWN, // 左斜めした
	// 桂馬の動き
	KEIRIGHTJUMP, KEILEFTJUMP,
	// JUMPは複数移動の意
	RIGHTJUMP, LEFTJUMP, UPJUMP, DOWNJUMP, RIGHTUPJUMP, RIGHTDOWNJUMP, LEFTUPJUMP, LEFTDOWNJUMP
};

abstract class Koma {
	public String komaType;// 駒の種類
	public List<MoveType> moveTypeLst;// 動ける方向
	public String owner;// 所有者
	public Boolean promote = false;// 成れるか
	public Boolean promoted = false;// 成ったか

	// 成駒インスタンスを返す
	public Koma getPromoteKoma(String owner) {
		return null;
	}

	public Koma getOldState(String owner) {
		return null;
	}
}

// 歩
class Fu extends Koma {
	public Fu(String owner) {
		this.komaType = "Fu";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP));// 歩の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Tokin(owner);
	}
}

// と金
class Tokin extends Koma {
	public Tokin(String owner) {
		this.komaType = "Tokin";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN));// と金の動き
	}

	public Koma getOldState(String owner) {
		return new Fu(owner);
	}
}

// 飛車
class Hisya extends Koma {
	public Hisya(String owner) {
		this.komaType = "Hisya";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(
				MoveType.UPJUMP, MoveType.LEFTJUMP, MoveType.RIGHTJUMP,
				MoveType.DOWNJUMP));// 飛車の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Ryuu(owner);
	}
}

// 竜
class Ryuu extends Koma {
	public Ryuu(String owner) {
		this.komaType = "Ryuu";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(
				MoveType.UPJUMP, MoveType.LEFTJUMP, MoveType.RIGHTJUMP,
				MoveType.DOWNJUMP, MoveType.LEFTUP, MoveType.RIGHTUP,
				MoveType.LEFTDOWN, MoveType.RIGHTDOWN));// 竜の動き
	}

	public Koma getOldState(String owner) {
		return new Hisya(owner);
	}
}

// 角
class Kaku extends Koma {
	public Kaku(String owner) {
		this.komaType = "Kaku";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(
				MoveType.RIGHTUPJUMP, MoveType.LEFTUPJUMP,
				MoveType.RIGHTDOWNJUMP, MoveType.LEFTDOWNJUMP));// 角の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Uma(owner);
	}
}

// 馬
class Uma extends Koma {
	public Uma(String owner) {
		this.komaType = "Uma";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(
				MoveType.RIGHTUPJUMP, MoveType.LEFTUPJUMP,
				MoveType.RIGHTDOWNJUMP, MoveType.LEFTDOWNJUMP, MoveType.UP,
				MoveType.LEFT, MoveType.RIGHT, MoveType.DOWN));// 馬の動き
	}

	public Koma getOldState(String owner) {
		return new Kaku(owner);
	}
}

// 香
class Kyou extends Koma {
	public Kyou(String owner) {
		this.komaType = "Kyou";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(
				Arrays.asList(MoveType.UPJUMP));// 香の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Narikyou(owner);
	}
}

// 成香
class Narikyou extends Koma {
	public Narikyou(String owner) {
		this.komaType = "Narikyou";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN));// 成香の動き
	}

	public Koma getOldState(String owner) {
		return new Kyou(owner);
	}
}

// 桂
class Kei extends Koma {
	public Kei(String owner) {
		this.komaType = "Kei";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(
				MoveType.KEILEFTJUMP, MoveType.KEIRIGHTJUMP));// 桂の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Narikei(owner);
	}
}

// 成桂
class Narikei extends Koma {
	public Narikei(String owner) {
		this.komaType = "Narikei";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN));// 成桂の動き
	}

	public Koma getOldState(String owner) {
		return new Kei(owner);
	}
}

// 銀
class Gin extends Koma {
	public Gin(String owner) {
		this.komaType = "Gin";
		this.owner = owner;
		this.promote = true;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFTDOWN,
				MoveType.RIGHTDOWN));// 銀の動き
	}

	public Koma getPromoteKoma(String owner) {
		return new Narigin(owner);
	}
}

// 成銀
class Narigin extends Koma {
	public Narigin(String owner) {
		this.komaType = "Narigin";
		this.owner = owner;
		this.promoted = true;// 成ったか
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN));// 成銀の動き
	}

	public Koma getOldState(String owner) {
		return new Gin(owner);
	}
}

// 金
class Kin extends Koma {
	public Kin(String owner) {
		this.komaType = "Kin";
		this.owner = owner;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN));// 金の動き
	}
}

// 王
class Ou extends Koma {
	public Ou(String owner) {
		this.komaType = "Ou";
		this.owner = owner;
		this.moveTypeLst = new ArrayList<MoveType>(Arrays.asList(MoveType.UP,
				MoveType.LEFTUP, MoveType.RIGHTUP, MoveType.LEFT,
				MoveType.RIGHT, MoveType.DOWN, MoveType.LEFTDOWN,
				MoveType.RIGHTDOWN));// 王の動き
	}
}
