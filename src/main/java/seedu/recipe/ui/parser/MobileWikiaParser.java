//@@author kokonguyen191
package seedu.recipe.ui.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A WebParser for recipes.wikia.com mobile version
 */
public class MobileWikiaParser extends WikiaParser {

    public static final String DOMAIN = "recipes.wikia.com";

    protected static final String MAIN_BODY_CLASS_NAME = ".article-content.mw-content";
    protected static final String CATEGORY_SELECTOR = ".mw-content.collapsible-menu.ember-view ul li";
    protected static final String PAGE_TITLE_CLASS_NAME = ".wiki-page-header__title";
    protected static final String MOBILE_URL_SUFFIX = "?useskin=wikiamobile";

    protected static final String IMAGE_SELECTOR = ".article-media-placeholder";
    protected static final String IMAGE_SOURCE_ATTRIBUTE = "data-src";

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
        contentText = this.document.selectFirst(MAIN_BODY_CLASS_NAME);
    }

    @Override
    protected void getCategories() {
        categories = document.select(CATEGORY_SELECTOR);
    }

    @Override
    public String getName() {
        return document.selectFirst(PAGE_TITLE_CLASS_NAME).text();
    }

    @Override
    public String getImageUrl() {
        Element image = contentText.selectFirst(IMAGE_SELECTOR);
        if (image == null) {
            return EMPTY_STRING;
        } else {
            return image.attr(IMAGE_SOURCE_ATTRIBUTE);
        }
    }

    @Override
    public String getUrl() {
        return super.getUrl() + MOBILE_URL_SUFFIX;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MobileWikiaParser // instanceof handles nulls
                && document.html().equals(((MobileWikiaParser) other).document.html()));
    }
}
