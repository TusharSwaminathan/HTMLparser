package iOS;


import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class clsVectorArticle {


	public static void main(String[] args) throws Exception {
		try {
			URL oracle = new URL("URL to Parse");
			HttpURLConnection httpcon = (HttpURLConnection) oracle.openConnection();
			//Valid URL checker
			int responseCode = httpcon.getResponseCode();
			httpcon.disconnect();
			if (responseCode != 404) {
				httpcon = (HttpURLConnection) oracle.openConnection();
				httpcon.addRequestProperty("User-Agent", "Mozilla/4.76"); 

				BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
				String inputLine;
				String safe = null; 
				while ((inputLine = in.readLine()) != null)
					safe+=inputLine;


				Whitelist whitelist = Whitelist.none();
				whitelist.removeTags(new String[]{"span"});				   
				whitelist.addTags(new String[]{"p"});
				String safe2 = Jsoup.clean(safe, whitelist);

				List<String> lines = extractText(new StringReader(safe2));
				String content="";
				for (String line : lines) {
					content+=line;
				}
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	//PDF Extractor
	public static String parsePdf(InputStream is) throws IOException {
		String strPdf="";
		PdfReader reader = new PdfReader(is);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			strPdf+=strategy.getResultantText();
		}
		reader.close();
		return strPdf;
	}

	//HTML Extractor
	public static List<String> extractText(Reader reader) throws IOException {
		final ArrayList<String> list = new ArrayList<String>();

		ParserDelegator parserDelegator = new ParserDelegator();
		ParserCallback parserCallback = new ParserCallback() {
			public void handleText(final char[] data, final int pos) {
				list.add(new String(data));
			}
			public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) { }
			public void handleEndTag(Tag t, final int pos) {  }
			public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) { }
			public void handleComment(final char[] data, final int pos) { }
			public void handleError(final java.lang.String errMsg, final int pos) { }
		};
		parserDelegator.parse(reader, parserCallback, true);
		return list;
	}
}