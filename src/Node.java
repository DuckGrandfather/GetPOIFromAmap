public class Node {
	double _lux, _luy, _rdx, _rdy;
	Node[] children;
	/**
	 * 创建结点 结点值为覆盖的矩形范围  孩子结点设置为空
	 * @param lux 左上角点X坐标
	 * @param luy 左上角点Y坐标
	 * @param rdx 右下角点X坐标
	 * @param rdy 右下角点Y坐标
	 */
	public Node(double lux, double luy, double rdx, double rdy) {
		this._lux = lux;
		this._luy = luy;
		this._rdx = rdx;
		this._rdy = rdy;
		this.children = new Node[4];
	}
	
}
