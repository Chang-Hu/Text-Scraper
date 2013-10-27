Text-Scraper
============

Design and build a robust text scraper that will connect to a page on www.shopping.com and return results about a given keyword. There are two queries that will be performed:

Query 1: Total number of results

Given a keyword, such as "digital camera", return the total number of results found.

Query 2: Result Object

Given a keyword (e.g. "digital cameras") and page number (e.g. "1"), return the results in a result object and then print results on screen. For each result, return the following information:

Title/Product Name (e.g. "Samsung TL100 Digital Camera")

Price of the product

Shipping Price (e.g. "Free Shipping", "$3.50")

Vendor (e.g. "Amazon", "5 stores")

For "digital cameras", there should be either 40 or 80 results that return for page 1.

How to run/execute the program:

Encapsulate your assignment inside an executable jar (e.g. java -jar Assignment.jar ...)

Handle the two queries above:

Query 1: (requires a single argument)

java -jar Assignment.jar <keyword> (e.g. java -jar Assignment.jar "baby strollers")

Query 2: (requires two arguments)

java -jar Assignment.jar <keyword> <page number> (e.g. java -jar Assignment.jar "baby strollers" 2)
