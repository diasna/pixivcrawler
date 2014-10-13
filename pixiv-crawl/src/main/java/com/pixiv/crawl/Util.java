package com.pixiv.crawl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class Util {
	public static void evaluateIds(TagNode initTagNode, List<String> ids) throws XPatherException {
		Object[] foundList = initTagNode.evaluateXPath("//li[@class='image-item']/a[@class]");
		for (Object obj : foundList) {
			TagNode spanNode = (TagNode) obj;
			ids.add(spanNode.getAttributeByName("href").replaceAll("[^0-9]", ""));
		}
	}
	
	public static TagNode extractTagNode(String url, String ref) throws ClientProtocolException, IOException{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:32.0) Gecko/20100101 Firefox/32.0"));
		getRequest.setHeader(new BasicHeader("Cookie", "p_ab_id=3; __utma=235335808.1984870330.1409795642.1412073715.1413193270.3; __utmz=235335808.1409795642.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=12563162=1; _ga=GA1.2.1984870330.1409795642; module_orders_mypage=%5B%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; login_ever=yes; bookmark_tag_type=count; bookmark_tag_order=desc; auto_view_enabled=1; __utmb=235335808.14.10.1413193270; __utmc=235335808; GCSCU_89993436389_H3=C=89993436389.apps.googleusercontent.com:S=5a47ccf2551d5a7389a1ddf756a90bc6dbf8e8f4.1ZpDrg04UljOCQh3.80f7:I=1413194763:X=1413281163; device_token=a9bda9755ec7721a20fdbfec90b22d86; __utmt=1; visit_ever=yes; PHPSESSID=12563162_7bb9dd55b70ef4efc504529c6eb7ef1b"));
		if(!"".equalsIgnoreCase(ref))
			getRequest.setHeader(new BasicHeader(HttpHeaders.REFERER, ref));
		HttpResponse response = httpClient.execute(getRequest);
		CleanerProperties props = new CleanerProperties();
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		return new HtmlCleaner(props).clean(response.getEntity().getContent());
	}
	
	public static void saveImage(String imageUrl, String destinationFile, String ref) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(imageUrl);
		getRequest.setHeader(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:32.0) Gecko/20100101 Firefox/32.0"));
		getRequest.setHeader(new BasicHeader("Cookie", "p_ab_id=3; __utma=235335808.1984870330.1409795642.1412073715.1413193270.3; __utmz=235335808.1409795642.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=12563162=1; _ga=GA1.2.1984870330.1409795642; module_orders_mypage=%5B%7B%22name%22%3A%22everyone_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22spotlight%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22featured_tags%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22contests%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22following_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22mypixiv_new_illusts%22%2C%22visible%22%3Atrue%7D%2C%7B%22name%22%3A%22booth_follow_items%22%2C%22visible%22%3Atrue%7D%5D; login_ever=yes; bookmark_tag_type=count; bookmark_tag_order=desc; auto_view_enabled=1; __utmb=235335808.14.10.1413193270; __utmc=235335808; GCSCU_89993436389_H3=C=89993436389.apps.googleusercontent.com:S=5a47ccf2551d5a7389a1ddf756a90bc6dbf8e8f4.1ZpDrg04UljOCQh3.80f7:I=1413194763:X=1413281163; device_token=a9bda9755ec7721a20fdbfec90b22d86; __utmt=1; visit_ever=yes; PHPSESSID=12563162_7bb9dd55b70ef4efc504529c6eb7ef1b"));
		if(!"".equalsIgnoreCase(ref))
			getRequest.setHeader(new BasicHeader(HttpHeaders.REFERER, ref));
		HttpResponse response = httpClient.execute(getRequest);
		InputStream is = response.getEntity().getContent();
		File f = new File(destinationFile);
		if (!f.getParentFile().exists())
		    f.getParentFile().mkdirs();
		if (!f.exists())
		    f.createNewFile();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
