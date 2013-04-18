package indi;

public class PayOrder {
	private String[] arr;

	private String content;

	public PayOrder(String str) {
		if (str.startsWith(":"))
			str = str.substring(1);
		String[] temp = str.split(Utils.SPLIT);
		if (temp.length == 2) {
			str = temp[1];
		}
		if (str != null)
			arr = str.split("\t");
		content = str;
	}

	public double getTotalFee() {
		return Double.parseDouble(arr[1]);
	}

	public String getPayOrderId() {
		return arr[0];
	}

	public String get() {
		return arr[-1];
	}

	public boolean isValid() {
		if (arr != null && arr.length == 56)
			return true;
		return false;
	}

	public String toString() {

		return Utils.PAYORDER + Utils.SPLIT + content;
	}
}
