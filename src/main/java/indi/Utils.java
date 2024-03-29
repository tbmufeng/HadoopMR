package indi;

import java.util.HashMap;
import java.util.Map;

import com.taobao.cdnnet.tbip.IpInfoEntity;
import com.taobao.cdnnet.tbip.TbipImpl;

public class Utils {

	static TbipImpl tbip = new TbipImpl();

	public static String getProv(String ip) {
		IpInfoEntity entry = tbip.getIpInfo(ip);
		if (entry != null) {
			return entry.getArea();
		}
		return null;
	}

	public static String getCity(String ip) {
		IpInfoEntity entry = tbip.getIpInfo(ip);
		if (entry != null) {
			return entry.getCity();
		}
		return null;
	}

	public static final String BIZORDER = "biz_order";
	public static final String PAYORDER = "pay_order";
	public static final String LOGIORDER = "lgi_order";
	public static final String SPLIT = "\u0030:";

	public String checkType(String content) {
		String[] arr = content.split("\t");
		switch (arr.length) {
		case 30:
			return LOGIORDER;
		case 56:
			return BIZORDER;
		case 25:
			return LOGIORDER;
		default:
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println("prov:" + Utils.getProv("10.1.167.130"));
	}

	public static Map<String, String> getAttr(String attrs) {
		Map<String, String> attrMap = new HashMap<String, String>();
		for (String kv : attrs.split(";")) {
			String[] arr = kv.split(":");
			if (arr.length == 2) {
				attrMap.put(arr[0], arr[1]);
			}

		}
		return attrMap;
	}

}
