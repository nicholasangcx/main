//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
    private static final String LF = "\n";

    protected Document document;
    protected Element contentText;
    protected Elements categories;

    /**
     * Constructs from a Jsoup Document.
     */
    public WikiaParser(Document document) {
        requireNonNull(document);
        this.document = document;
        getMainBody();
        getCategories();
    }

    /**
     * Constructs from a HTML string and a URL.
     */
    public WikiaParser(String html, String url) {
        requireNonNull(html);
        this.document = Jsoup.parse(html, url);
        getMainBody();
        getCategories();
    }

    /**
     * Assigns {@code contentText} to the Element that contains the article body.
     */
    protected void getMainBody() {
        contentText = document.getElementById("mw-content-text");
    }

    /**
     * Assigns {@code categories} to the ElementS that contains the categories.
     */
    protected void getCategories() {
        categories = document.getElementsByClass("category");
    }

    @Override
    public String getName() {
        return document.selectFirst(".page-header__title").text();
    }

    @Override
    public String getIngredient() {
        Elements[] arrayOfElementsOfIngredients = getElementsOfIngredient();
        return getIngredientString(arrayOfElementsOfIngredients);
    }

    /**
     * Returns an array of size 2 of Elements. The first contains all Element with class "a", which contain a link.
     * The second contains all Element with class "li", which contain the whole ingredient line.
     */
    private Elements[] getElementsOfIngredient() {
        Elements elements = contentText.select("h2,ul");
        Iterator<Element> eleIte = elements.iterator();
        while (eleIte.hasNext()) {
            String nextText = eleIte.next().text();
            if (nextText.startsWith("Ingredient")) {
                break;
            }
        }

        Elements elementsWithIngredientWithLink = new Elements();
        Elements elementsWithIngredient = new Elements();
        while (eleIte.hasNext()) {
            Element nextElement = eleIte.next();
            if (nextElement.tagName().contains("h2")) {
                break;
            }
            elementsWithIngredient.addAll(nextElement.select("li"));
            elementsWithIngredientWithLink.addAll(nextElement.select("a"));
        }

        return new Elements[] {elementsWithIngredientWithLink, elementsWithIngredient};
    }

    /**
     * Returns a Ingredient string that can be used in an add command from the list of ingredients.
     */
    private String getIngredientString(Elements[] arrayOfElementsOfIngredients) {
        Elements elementsWithIngredientWithLink = arrayOfElementsOfIngredients[0];
        Elements elementsWithIngredient = arrayOfElementsOfIngredients[1];

        List<String> ingredientList;
        if (elementsWithIngredientWithLink.isEmpty()) {
            ingredientList = elementsWithIngredient.eachText();
        } else {
            ingredientList = elementsWithIngredientWithLink.eachText();
        }
        return String.join(", ", ingredientList);
    }

    @Override
    public String getInstruction() {
        Elements elementsWithInstruction = contentText.select("ol li");
        if (elementsWithInstruction.isEmpty()) {
            Elements elements = contentText.select("h2,p,h3");
            Iterator<Element> eleIte = elements.iterator();
            while (eleIte.hasNext()) {
                String nextText = eleIte.next().text();
                if (nextText.startsWith("Directions")) {
                    break;
                }
            }
            while (eleIte.hasNext()) {
                Element nextElement = eleIte.next();
                if (nextElement.tagName().equals("h3")) {
                    break;
                } else {
                    elementsWithInstruction.add(nextElement);
                }
            }
        }
        List<String> instructionList = elementsWithInstruction.eachText();
        return String.join("\n", instructionList);
    }

    @Override
    public String getImageUrl() {
        Element image = contentText.selectFirst(".image img");
        if (image == null) {
            return "";
        } else {
            return image.attr("src");
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

    /**
     * Trims a tag, removes generic keywords to make tag shorter
     */
    protected String trimTag(String tag) {
        if (tag.endsWith("ishes")) {
            tag = tag.replace("Dishes", "").replace("dishes", "");
        }
        if (tag.endsWith("ecipes")) {
            tag = tag.replace("Recipes", "").replace("recipes", "");
        }
        return Arrays.stream(tag.split(" "))
                .map(word -> Character.toTitleCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining());
    }

    @Override
    public String getUrl() {
        return document.selectFirst("[rel=\"canonical\"]").attr("href");
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof WikiaParser // instanceof handles nulls
                && document.html().equals(((WikiaParser) other).document.html()));
    }
}
