//@@author kokonguyen191
package seedu.recipe.ui.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A WebParser for recipes.wikia.com mobile version
 */
public class MobileWikiaParser extends WikiaParser {

    public static final String DOMAIN = "recipes.wikia.com";

    /**
     * Constructs from a Jsoup Document.
     */
    public MobileWikiaParser(Document document) {
        super(document);
    }

    /**
     * Constructs from a HTML string and a URL.
     */
    public MobileWikiaParser(String html, String url) {
        super(html, url);
    }

    @Override
    protected void getMainBody() {
        contentText = this.document.selectFirst(".article-content.mw-content");
    }

    @Override
    protected void getCategories() {
        categories = document.select(".mw-content.collapsible-menu.ember-view ul li");
    }

    @Override
    public String getName() {
        return document.selectFirst(".wiki-page-header__title").text();
    }

    @Override
    public String getImageUrl() {
        Element image = contentText.selectFirst(".article-media-placeholder");
        if (image == null) {
            return "";
        } else {
            return image.attr("data-src");
        }
    }

    @Override
    public String getUrl() {
        return document.selectFirst("[rel=\"canonical\"]").attr("href") + "?useskin=wikiamobile";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MobileWikiaParser // instanceof handles nulls
                && document.html().equals(((MobileWikiaParser) other).document.html()));
    }
}
