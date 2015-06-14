//座標クラス
class Point {
	public int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// 加算
	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}

	@Override
	public String toString() {
		return "Point [x=" + this.x + ", y=" + this.y + "]";
	}

	// 座標比較
	public boolean isEqual(Point p) {
		return (this.x == p.x && this.y == p.y);
	}

	// 9*9の範囲内か？
	public boolean pointInRange() {
		return (this.x > 0 && this.y > 0 && this.x < 10 && this.y < 10);
	}
}