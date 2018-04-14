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
        assertEquals(CHICKEN_NAME, wikiaParserChicken.getName());
        assertEquals(UGANDAN_NAME, wikiaParserUgandan.getName());
    }

    @Test
    public void getIngredient_validRecipes_returnsResult() throws Exception {
        assertEquals(CHICKEN_INGREDIENT, wikiaParserChicken.getIngredient());
        assertEquals(UGANDAN_INGREDIENT, wikiaParserUgandan.getIngredient());
    }

    @Test
    public void getInstruction_validRecipes_returnsResult() throws Exception {
        assertEquals(CHICKEN_INSTRUCTION, wikiaParserChicken.getInstruction());
        assertEquals(UGANDAN_INSTRUCTION, wikiaParserUgandan.getInstruction());
    }

    @Test
    public void getImageUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(MOBILE_CHICKEN_RICE_IMAGE_URL, wikiaParserChicken.getImageUrl());
    }

    @Test
    public void getUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(MOBILE_WIKIA_RECIPE_URL_CHICKEN, wikiaParserChicken.getUrl());
        assertEquals(MOBILE_WIKIA_RECIPE_URL_UGANDAN, wikiaParserUgandan.getUrl());
    }

    @Test
    public void getTags_validRecipes_returnsResult() throws Exception {
        assertArrayEquals(CHICKEN_TAGS, wikiaParserChicken.getTags());
        assertArrayEquals(UGANDAN_TAGS, wikiaParserUgandan.getTags());
    }

    @Test
    public void parseRecipe_validRecipe_returnsValidCommand() throws Exception {
        assertEquals(MOBILE_WIKIA_CHICKEN_ADD_COMMAND, wikiaParserChicken.parseRecipe());
        assertEquals(MOBILE_WIKIA_UGANDAN_ADD_COMMAND, wikiaParserUgandan.parseRecipe());
    }
}
