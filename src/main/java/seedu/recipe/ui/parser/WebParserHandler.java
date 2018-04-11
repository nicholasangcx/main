//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

import javafx.scene.web.WebView;

/**
 * Handles a WebParseRequestEvent
 */
public class WebParserHandler {

    private WebView browser;

    public WebParserHandler(WebView browser) {
        requireNonNull(browser);
        this.browser = browser;
    }

    /**
     * Returns the according WebParser to the currently loaded page in the BrowserPanel
     */
    public WebParser getWebParser() {
        String url = browser.getEngine().getLocation();
        Document document = browser.getEngine().getDocument();
        W3CDom w3CDom = new W3CDom();
        String documentString = w3CDom.asString(document);
        if (browser.getEngine().getTitle() != null) {
            return WebParserHandler.getWebParser(url, documentString);
        } else {
            return null;
        }
    }

    /**
     * Reads the {@code url}, returns the according WebParser loaded with {@code documentString}
     */
    public static WebParser getWebParser(String url, String documentString) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            switch (domain) {
            case WikiaParser.DOMAIN:
                /*
                Try to pre-parse to see if the page is mobile wikia or wikia.
                 */
                org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(documentString);
                if (jsoupDocument.getElementById("mw-content-text") == null) {
                    return new MobileWikiaParser(jsoupDocument);
                } else {
                    return new WikiaParser(jsoupDocument);
                }

            default:
                return null;

            }
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
