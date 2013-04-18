package indi;

public class LogiOrder {
	private String[] arr;

	private String content;

	public LogiOrder(String str) {
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

	public String getAddress() {
		return arr[2];
	}

	public String getLogisticsOrderId() {

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
		if (content.startsWith(":")) {
			return Utils.LOGIORDER + Utils.SPLIT + content;
		}
		return Utils.LOGIORDER + Utils.SPLIT + content;
	}
}
