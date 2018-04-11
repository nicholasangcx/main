//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_TAGS;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_CHICKEN_RICE_IMAGE_URL;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_CHICKEN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_RECIPE_URL_CHICKEN;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_RECIPE_URL_UGANDAN;
import static seedu.recipe.testutil.WikiaRecipes.MOBILE_WIKIA_UGANDAN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_TAGS;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.ui.GuiUnitTest;

public class MobileWikiaParserTest extends GuiUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MobileWikiaParser wikiaParserChicken;
    private MobileWikiaParser wikiaParserUgandan;

    @Before
    public void setUp() throws IOException {
        wikiaParserChicken = new MobileWikiaParser(Jsoup.connect(MOBILE_WIKIA_RECIPE_URL_CHICKEN).get());
        wikiaParserUgandan = new MobileWikiaParser(Jsoup.connect(MOBILE_WIKIA_RECIPE_URL_UGANDAN).get());
    }

    @Test
    public void constructor_nullArgument_throwsException() {
        thrown.expect(NullPointerException.class);
        new MobileWikiaParser(null);
        new MobileWikiaParser(null, "");
    }

    @Test
    public void equals() {
        String testDocumentString = "<html>Test</html>";
        String testUrl = "127.0.0.1";
        MobileWikiaParser wikiaParserA = new MobileWikiaParser(testDocumentString, testUrl);
        MobileWikiaParser wikiaParserB = new MobileWikiaParser(Jsoup.parse(testDocumentString, testUrl));
        assertEquals(wikiaParserA, wikiaParserB);
    }

    @Test
    public void getName_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getName(), CHICKEN_NAME);
        assertEquals(wikiaParserUgandan.getName(), UGANDAN_NAME);
    }

    @Test
    public void getIngredient_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getIngredient(), CHICKEN_INGREDIENT);
        assertEquals(wikiaParserUgandan.getIngredient(), UGANDAN_INGREDIENT);
    }

    @Test
    public void getInstruction_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getInstruction(), CHICKEN_INSTRUCTION);
        assertEquals(wikiaParserUgandan.getInstruction(), UGANDAN_INSTRUCTION);
    }

    @Test
    public void getImageUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getImageUrl(), MOBILE_CHICKEN_RICE_IMAGE_URL);
    }

    @Test
    public void getUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(wikiaParserChicken.getUrl(), MOBILE_WIKIA_RECIPE_URL_CHICKEN);
        assertEquals(wikiaParserUgandan.getUrl(), MOBILE_WIKIA_RECIPE_URL_UGANDAN);
    }

    @Test
    public void getTags_validRecipes_returnsResult() throws Exception {
        assertArrayEquals(wikiaParserChicken.getTags(), CHICKEN_TAGS);
        assertArrayEquals(wikiaParserUgandan.getTags(), UGANDAN_TAGS);
    }

    @Test
    public void parseRecipe_validRecipe_returnsValidCommand() throws Exception {
        assertEquals(wikiaParserChicken.parseRecipe(), MOBILE_WIKIA_CHICKEN_ADD_COMMAND);
        assertEquals(wikiaParserUgandan.parseRecipe(), MOBILE_WIKIA_UGANDAN_ADD_COMMAND);
    }
}
