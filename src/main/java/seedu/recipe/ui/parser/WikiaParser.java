//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A WebParser for recipes.wikia.com
 */
public class WikiaParser extends WebParser {

    public static final String DOMAIN = "recipes.wikia.com";

    protected static final String MAIN_BODY_ID = "mw-content-text";
    protected static final String CATEGORY_CLASS_NAME = "category";
    protected static final String PAGE_TITLE_CLASS_NAME = ".page-header__title";

    protected static final String HTML_HEADING_TWO_TAG_NAME = "h2";
    protected static final String HTML_HEADING_THREE_TAG_NAME = "h3";
    protected static final String HTML_LIST_TAG_NAME = "li";
    protected static final String HTML_LINK_TAG_NAME = "a";
    protected static final String HTML_LINK_ATTRIBUTE = "href";

    protected static final String INGREDIENT_SELECTOR = "h2,ul";
    protected static final String INGREDIENT_HEADING = "Ingredient";
    protected static final String INGREDIENT_DELIMITER = ", ";

    protected static final String INSTRUCTION_LIST_SELECTOR = "ol li";
    protected static final String INSTRUCTION_GENERIC_SELECTOR = "h2,p,h3";
    protected static final String INSTRUCTION_HEADING = "Directions";
    protected static final String INSTRUCTION_DELIMITER = "\n";

    protected static final String IMAGE_SELECTOR = ".image img";
    protected static final String IMAGE_SOURCE_ATTRIBUTE = "src";

    protected static final String URL_SELECTOR = "[rel=\"canonical\"]";

    protected static final String DISHES_SUFFIX = "ishes";
    protected static final String DISHES_STRING_REGEX = "[Dd]ishes";
    protected static final String RECIPES_SUFFIX = "ecipes";
    protected static final String RECIPES_STRING_REGEX = "[Rr]ecipes";

    protected static final String EMPTY_STRING = "";
    protected static final String WHITE_SPACE = " ";
    protected static final String NON_ALPHANUMERIC_REGEX = "[^A-Za-z0-9]";

    protected Document document;
    protected Element contentText;
    protected Elements categories;
    protected Elements elementsWithIngredientWithLink;
    protected Elements elementsWithIngredient;
    protected Elements elementsWithInstruction;

    /**
     * Constructs from a Jsoup Document.
     */
    public WikiaParser(Document document) {
        requireNonNull(document);
        this.document = document;
        getMainBody();
        getCategories();
        initializeEmptyFields();
    }

    /**
     * Constructs from a HTML string and a URL.
     */
    public WikiaParser(String html, String url) {
        requireNonNull(html);
        this.document = Jsoup.parse(html, url);
        getMainBody();
        getCategories();
        initializeEmptyFields();
    }

    /**
     * Assigns {@code contentText} to the Element that contains the article body.
     */
    protected void getMainBody() {
        contentText = document.getElementById(MAIN_BODY_ID);
    }

    /**
     * Assigns {@code categories} to the Elements that contains the categories.
     */
    protected void getCategories() {
        categories = document.getElementsByClass(CATEGORY_CLASS_NAME);
    }

    private void initializeEmptyFields() {
        elementsWithIngredientWithLink = new Elements();
        elementsWithIngredient = new Elements();
        elementsWithInstruction = new Elements();
    }

    @Override
    public String getName() {
        return document.selectFirst(PAGE_TITLE_CLASS_NAME).text();
    }

    @Override
    public String getIngredient() {
        populateIngredient();
        return getIngredientString();
    }

    @Override
    public String getInstruction() {
        populateInstruction();
        return getInstructionString();
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
    public String[] getTags() {
        LinkedList<String> tags = new LinkedList<>();

        for (Element category : categories) {
            String rawText = category.text();
            tags.add(trimTag(rawText));
        }

        return tags.toArray(new String[tags.size()]);
    }

    @Override
    public String getUrl() {
        return document.selectFirst(URL_SELECTOR).attr(HTML_LINK_ATTRIBUTE);
    }

    /**
     * Fills {@code elementsWithIngredientWithLink} and {@code elementsWithIngredient} with the
     * relevant ingredient Element's.
     */
    private void populateIngredient() {
        Iterator<Element> elementIterator = getIteratorForSelection(INGREDIENT_SELECTOR);
        Function<Element, Boolean> isNextIngredientHeading = (
                element) -> (element.text().startsWith(INGREDIENT_HEADING));
        skipUntil(elementIterator, isNextIngredientHeading);
        populateIngredient(elementIterator);
    }

    /**
     * Iterates Elements using an Iterator, adds relevant ingredient Element to {@code elementsWithIngredientWithLink}
     * and {@code elementsWithIngredient}.
     */
    private void populateIngredient(Iterator<Element> elementIterator) {
        while (elementIterator.hasNext()) {
            Element nextElement = elementIterator.next();
            if (nextElement.tagName().contains(HTML_HEADING_TWO_TAG_NAME)) {
                break;
            }
            elementsWithIngredient.addAll(nextElement.select(HTML_LIST_TAG_NAME));
            elementsWithIngredientWithLink.addAll(nextElement.select(HTML_LINK_TAG_NAME));
        }
    }

    /**
     * Returns a String of Ingredient that can be used for an add/edit command.
     */
    private String getIngredientString() {
        List<String> ingredientList;
        if (elementsWithIngredientWithLink.isEmpty()) {
            ingredientList = elementsWithIngredient.eachText();
        } else {
            ingredientList = elementsWithIngredientWithLink.eachText();
        }
        return String.join(INGREDIENT_DELIMITER, ingredientList);
    }

    /**
     * Fills {@code elementsWithInstruction} with the relevant instruction Element's.
     */
    private void populateInstruction() {
        elementsWithInstruction = contentText.select(INSTRUCTION_LIST_SELECTOR);
        if (elementsWithInstruction.isEmpty()) {
            Iterator<Element> elementIterator = getIteratorForSelection(INSTRUCTION_GENERIC_SELECTOR);
            Function<Element, Boolean> isNextInstructionHeading = (
                    element) -> (element.text().startsWith(INSTRUCTION_HEADING));
            skipUntil(elementIterator, isNextInstructionHeading);
            populateInstruction(elementIterator);
        }
    }

    /**
     * Iterates Elements using an Iterator, adds relevant instruction Element to {@code elementsWithInstruction}.
     */
    private void populateInstruction(Iterator<Element> elementIterator) {
        while (elementIterator.hasNext()) {
            Element nextElement = elementIterator.next();
            if (nextElement.tagName().equals(HTML_HEADING_THREE_TAG_NAME)
                    || nextElement.tagName().equals(HTML_HEADING_TWO_TAG_NAME)) {
                break;
            }
            elementsWithInstruction.add(nextElement);
        }
    }

    /**
     * Returns a String of Instruction that can be used for an add/edit command.
     */
    private String getInstructionString() {
        List<String> instructionList = elementsWithInstruction.eachText();
        return String.join(INSTRUCTION_DELIMITER, instructionList);
    }

    /**
     * Returns an Iterator of Element for a list of Elements selected from {@code contentText} using
     * {@code cssSelector}.
     */
    private Iterator<Element> getIteratorForSelection(String cssSelector) {
        Elements elements = contentText.select(cssSelector);
        return elements.iterator();
    }

    /**
     * Iterates through an Iterator, at each iteration, evaluates the {@code booleanEvaluator}, if it returns
     * true, breaks the loop and exits the functions, else, continues to the next iteration.
     */
    private void skipUntil(Iterator<Element> elementIterator, Function<Element, Boolean> booleanEvaluator) {
        while (elementIterator.hasNext()) {
            Element nextElement = elementIterator.next();
            if (booleanEvaluator.apply(nextElement)) {
                break;
            }
        }
    }

    /**
     * Trims unnecessary words to make tags shorter and more generic, removes all non-alphanumeric characters.
     * @return the trimmed tag
     */
    private String trimTag(String tag) {
        if (tag.endsWith(DISHES_SUFFIX)) {
            tag = tag.replaceAll(DISHES_STRING_REGEX, EMPTY_STRING);
        }
        if (tag.endsWith(RECIPES_SUFFIX)) {
            tag = tag.replaceAll(RECIPES_STRING_REGEX, EMPTY_STRING);
        }
        tag.replaceAll(NON_ALPHANUMERIC_REGEX, EMPTY_STRING);
        return Arrays.stream(tag.split(WHITE_SPACE))
                .map(word -> Character.toTitleCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WikiaParser // instanceof handles nulls
                && document.html().equals(((WikiaParser) other).document.html()));
    }
}
