public class Node {
	double _lux, _luy, _rdx, _rdy;
	Node[] children;
	/**
	 * ������� ���ֵΪ���ǵľ��η�Χ  ���ӽ������Ϊ��
	 * @param lux ���Ͻǵ�X����
	 * @param luy ���Ͻǵ�Y����
	 * @param rdx ���½ǵ�X����
	 * @param rdy ���½ǵ�Y����
	 */
	public Node(double lux, double luy, double rdx, double rdy) {
		this._lux = lux;
		this._luy = luy;
		this._rdx = rdx;
		this._rdy = rdy;
		this.children = new Node[4];
	}
	
}
