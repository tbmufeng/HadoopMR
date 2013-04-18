package indi;

import java.util.Map;

public class BizOrder {
	String[] arr;
	String content;

	public BizOrder(String str) {

		if (str.startsWith(":"))
			str = str.substring(1);
		String[] temp = str.split(Utils.SPLIT);
		if (temp.length == 2) {
			str = temp[1];
		}
		if (str != null)
			arr = str.split("\t");
		content = str;
		attMap = Utils.getAttr(getAttributes());
	}

	public boolean isValid() {
		if (arr != null && arr.length == 56)
			return true;
		return false;
	}

	public String getAttribute(String code) {
		return attMap.get(code);
	}

	public String getRealRootCat() {
		return getAttribute("realRootCat");
	}

	public String getBizOrderId() {

		return arr[0];
	}

	public String getBuyerId() {
		return arr[6];
	}

	public String getSellerId() {
		return arr[7];
	}

	public String getBizType() {
		return arr[13];
	}

	public String getPayStatus() {
		return arr[15];
	}

	public String getAttributes() {
		if (arr.length > 31)
			return arr[31];
		return "";
	}

	public String getBuyerIp() {
		return arr[33];
	}

	public boolean isSubOrder() {
		return "1".equals(arr[37]);
	}

	public String get() {
		return arr[-1];
	}

	private Map<String, String> attMap;

	public String toString() {
		return Utils.BIZORDER + Utils.SPLIT + content;
	}
}
