package com.pixiv.crawl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;


public class Worker implements Runnable {
	String imgId;
	String memberId;
	public Worker(String imgId, String memberId) {
		this.imgId = imgId;
		this.memberId = memberId;
	}

	public void run() {
		try {
			String url = "http://www.pixiv.net/member_illust.php?mode=big&illust_id="+imgId;
			TagNode node = Util.extractTagNode(url, "http://www.pixiv.net/member_illust.php?mode=medium&illust_id="+imgId);
			Object[] foundList = node.evaluateXPath("//img");
			String imgUrl = ((TagNode)foundList[0]).getAttributeByName("src");
			Util.saveImage(imgUrl, "/Users/miku/Pictures/pixiv/"+memberId+"/"+imgId+".jpg", url);
		} catch (ClientProtocolException e) {
			System.err.println("Exception image ["+imgId+"]");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Exception image ["+imgId+"]");
			e.printStackTrace();
		} catch (XPatherException e) {
			System.err.println("Exception image ["+imgId+"]");
			e.printStackTrace();
		}
	}
	
	
}
