import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShoppingScraper {

	public String RetrieveContent(String url) {
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;
		String jsp = null;
		URL serverAddress = null;

		try {
			serverAddress = new URL(url);
			/* set up out communications stuff */
			connection = null;

			/* Set up the initial connection */
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			/* establish connection */
			connection.connect();

			/* read the result from the server */
			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				if (!line.trim().equals("")) {
					sb.append(line + '\n');
				}
			}

			jsp = sb.toString().trim();

			return jsp;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("MalformedURLException!");
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			System.out.println("ProtocolException!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
			return null;
		} finally {
			/* close the connection, set all objects to null */
			connection.disconnect();
			rd = null;
			sb = null;
			connection = null;

		}
	}

	public int ResultNumber(String keywords) {
		/* replace any space between keywords by "%20" */
		keywords = keywords.replace(" ", "%20");
		String url = "http://www.shopping.com/products?KW="
				+ keywords;
		String jsp = RetrieveContent(url);

		/* failure */
		if (jsp == null) {
			System.out.println("Failed to retrieve web page.");
			return 0;
		}

		/* Jsoup
		 * Convert Java String to DOM document
		 */
		Document doc = Jsoup.parse(jsp);
		/* Find the element containing number of results */
		Element content = doc.getElementById("sortFiltersBox");

		if (content == null) {
			return 0;
		}

		Elements num_results = content.getElementsByClass("numTotalResults");
		for (Element el : num_results) {
			/* get the text content of element */
			String text = el.text();
			int start_index = text.indexOf("of ");
			start_index += 3;
			System.out.println(text);
			String number = text.substring(start_index);
			number = number.replace(",", "");
			/* convert number of results from String to int */
			return Integer.parseInt(number);

		}

		return 0;
	}

	public void showResultInfo(String url) {
		String jsp = RetrieveContent(url);
		if (jsp == null) {
			System.out.println("Failed to retrieve web page.");
			return ;
		}

		/* Jsoup
		 * Convert Java String to DOM document
		 */
		Document doc = Jsoup.parse(jsp);

		int num = doc.getElementsByClass("gridBox").size();
		for (int i = 1; i <= num; i++) {
			
			String price;
			try {
				price = doc
					.getElementById("priceClickableQA" + i)
					.getElementsByClass("productPrice").text();
			} catch (NullPointerException e) {
				price = doc
						.getElementById("priceProductQA" + i)
						.getElementsByClass("productPrice").text();	
			}

			String title = doc.getElementById("nameQA" + i).text();
			String shippingPrice = doc
					.getElementById("quickLookItem-" + i)
					.getElementsByClass("taxShippingArea").text();
			
			if(!shippingPrice.equals("Free Shipping")) {
				String[] temp = shippingPrice.split(" ");
				if (temp.length == 3 && temp[2].equals("shipping")) {
					shippingPrice = temp[1];
				}				
			}
			
			String vendor = doc
					.getElementById("quickLookItem-" + i)
					.getElementsByClass("newMerchantName").text();
			
			System.out.println("Product " + i + " title: " + title);
			System.out.println("Product " + i + " price: "
					+ price);
			System.out.println("Product " + i + " shipping price: " + shippingPrice);
			System.out.println("Product " + i + " vendor: " + vendor);
			System.out.println("******************************************");			

		}

	}

	public String findPage(String keywords, int number) {
		/* replace any space between keywords by "%20" */
		keywords = keywords.replace(" ", "%20");
		String link = "http://www.shopping.com/products~PG-" + number +"?KW="
				+ keywords;
		return link;
	}

	public static void main(String[] args) {
		/* check parameters */
		if (args.length == 0 || args.length > 2) {
			System.out.println("Please input valid parameters.");
			return;
		}

		ShoppingScraper scraper = new ShoppingScraper();
		/* only a single argument is given, return
		 * the total number of results found
		 */
		if (args.length == 1) {
			int num_results = scraper.ResultNumber(args[0]);
			if (num_results == 0) {
				System.out.println("No results found.");
			} else {
				System.out.println("Number of results is : " + num_results);
			}
		}

		/* when two arguments are provided,
		 * return list of product information
		 */
		if (args.length == 2) {
			try {
				int number = Integer.parseInt(args[1]);
				if (number <= 0) {
					System.out.println("You should give a valid page value.");
				}
				
				String page_link = scraper.findPage(args[0], number);
				if (page_link == null) {
					System.out.println("This page is not found.");
				} else {
					scraper.showResultInfo(page_link);
				}
			
			} catch (NumberFormatException nfe){
				System.out.println("You should give an integer value to the second parameter.");
				return ;
			}
		}

	}
}
