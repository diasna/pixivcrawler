package com.pixiv.crawl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws XPatherException {
		try {
			String memberId = "4872213";
			String url = "http://www.pixiv.net/member_illust.php?id="+memberId;
			TagNode initTagNode = Util.extractTagNode(url,"");
			Object[] numObj = initTagNode.evaluateXPath("//div/span[@class='count-badge']");
			String numImageText = ((TagNode) numObj[0]).getText().toString();
			
			double numImage = Double.valueOf(numImageText.replaceAll("[^0-9]", ""));
			double totalPage = Math.ceil(numImage / 20);
			
			System.err.println("Found Image: " + numImage);
			System.err.println("Total Page: " + totalPage);
			
			List<String> ids = new ArrayList<String>();
			Util.evaluateIds(initTagNode, ids);
			
			for (int i = 2; i <= totalPage; i++) {
				url = url+"&p="+i;
				TagNode tagNode = Util.extractTagNode(url,"");
				Util.evaluateIds(tagNode, ids);
			}
			System.err.println("Data: "+Arrays.toString(ids.toArray()));
			
			ExecutorService executor = Executors.newFixedThreadPool(3);
			for (String id : ids) {
				Runnable worker = new Worker(id, memberId);
				executor.execute(worker);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
				
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
