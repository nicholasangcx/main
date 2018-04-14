//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static seedu.recipe.testutil.WikiaRecipes.BEEF_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_RICE_IMAGE_URL;
import static seedu.recipe.testutil.WikiaRecipes.CHICKEN_TAGS;
import static seedu.recipe.testutil.WikiaRecipes.DEVIL_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INGREDIENT;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_INSTRUCTION;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_NAME;
import static seedu.recipe.testutil.WikiaRecipes.UGANDAN_TAGS;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_CHICKEN_ADD_COMMAND;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_NOT_RECIPE;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_BEEF;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_CHICKEN;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_DEVIL;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_RECIPE_URL_UGANDAN;
import static seedu.recipe.testutil.WikiaRecipes.WIKIA_UGANDAN_ADD_COMMAND;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recipe.ui.GuiUnitTest;

public class WikiaParserTest extends GuiUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WikiaParser wikiaParserChicken;
    private WikiaParser wikiaParserBeef;
    private WikiaParser wikiaParserUgandan;
    private WikiaParser wikiaParserDevil;
    private WikiaParser wikiaParserNotRecipe;

    @Before
    public void setUp() throws IOException {
        wikiaParserChicken = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_CHICKEN).get());
        wikiaParserUgandan = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_UGANDAN).get());
        wikiaParserBeef = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_BEEF).get());
        wikiaParserDevil = new WikiaParser(Jsoup.connect(WIKIA_RECIPE_URL_DEVIL).get());
        wikiaParserNotRecipe = new WikiaParser(Jsoup.connect(WIKIA_NOT_RECIPE).get());
    }

    @Test
    public void constructor_nullArgument_throwsException() {
        thrown.expect(NullPointerException.class);
        new WikiaParser(null);
        new WikiaParser(null, "");
    }

    @Test
    public void equals() {
        String testDocumentString = "<html>Test</html>";
        String testUrl = "127.0.0.1";
        WikiaParser wikiaParserA = new WikiaParser(testDocumentString, testUrl);
        WikiaParser wikiaParserB = new WikiaParser(Jsoup.parse(testDocumentString, testUrl));
        assertEquals(wikiaParserA, wikiaParserB);
    }

    @Test
    public void getName_validRecipes_returnsResult() throws Exception {
        assertEquals(CHICKEN_NAME, wikiaParserChicken.getName());
        assertEquals(UGANDAN_NAME, wikiaParserUgandan.getName());
    }

    @Test
    public void getIngredient_validRecipes_returnsResult() throws Exception {
        assertEquals(BEEF_INGREDIENT, wikiaParserBeef.getIngredient());
        assertEquals(CHICKEN_INGREDIENT, wikiaParserChicken.getIngredient());
        assertEquals(UGANDAN_INGREDIENT, wikiaParserUgandan.getIngredient());
    }

    @Test
    public void getInstruction_validRecipes_returnsResult() throws Exception {
        assertEquals(CHICKEN_INSTRUCTION, wikiaParserChicken.getInstruction());
        assertEquals(UGANDAN_INSTRUCTION, wikiaParserUgandan.getInstruction());
        assertEquals(DEVIL_INSTRUCTION, wikiaParserDevil.getInstruction());
    }

    @Test
    public void getImageUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(CHICKEN_RICE_IMAGE_URL, wikiaParserChicken.getImageUrl());
    }

    @Test
    public void getUrl_validRecipes_returnsResult() throws Exception {
        assertEquals(WIKIA_RECIPE_URL_CHICKEN, wikiaParserChicken.getUrl());
        assertEquals(WIKIA_RECIPE_URL_UGANDAN, wikiaParserUgandan.getUrl());
    }

    @Test
    public void getTags_validRecipes_returnsResult() throws Exception {
        assertArrayEquals(CHICKEN_TAGS, wikiaParserChicken.getTags());
        assertArrayEquals(UGANDAN_TAGS, wikiaParserUgandan.getTags());
    }

    @Test
    public void parseRecipe_validRecipe_returnsValidCommand() throws Exception {
        String a = WIKIA_CHICKEN_ADD_COMMAND;
        assertEquals(WIKIA_CHICKEN_ADD_COMMAND, wikiaParserChicken.parseRecipe());
        assertEquals(WIKIA_UGANDAN_ADD_COMMAND, wikiaParserUgandan.parseRecipe());
    }

    @Test
    public void getName_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getName();
    }

    @Test
    public void getIngredient_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getIngredient();
    }

    @Test
    public void getInstruction_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getInstruction();
    }

    @Test
    public void getImageUrl_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.getImageUrl();
    }

    @Test
    public void parseRecipe_notRecipe_throwsNullPointer() throws Exception {
        thrown.expect(NullPointerException.class);
        wikiaParserNotRecipe.parseRecipe();
    }
}
