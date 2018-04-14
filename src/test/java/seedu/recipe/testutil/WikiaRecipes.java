//@@author kokonguyen191
package seedu.recipe.testutil;

import static seedu.recipe.logic.commands.AddCommand.COMMAND_WORD;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INSTRUCTION;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_URL;

import java.util.Arrays;
import java.util.stream.Collectors;

import seedu.recipe.model.recipe.Recipe;

/**
 * A utility class containing a list of {@code Recipe} objects parsed from Wikia to be used in tests.
 */
public class WikiaRecipes {
    public static final String LF = "\n";

    public static final String WIKIA_NOT_RECIPE = "http://recipes.wikia.com/d/f?sort=latest";

    public static final String CHICKEN_NAME = "Hainanese Chicken Rice";
    public static final String CHICKEN_INGREDIENT = "chicken, salt, spring onion, pandan leaves, "
            + "ginger, ginger, garlic, cinnamon, cloves, star anise, chicken broth, pandan leaves, salt,"
            + " light soy sauce, sesame oil, cucumber, tomatoes, coriander, lettuce, pineapple, fresh "
            + "chillies, ginger, garlic, vinegar, fish sauce, sugar, sweet soy sauce";
    public static final String CHICKEN_INSTRUCTION = "Boil water with spring Onion, ginger and pandan leaves, "
            + "put in Chicken and cook till done,"
            + " do not over cook. briefly dip in cold water and set aside to cool. Keep broth heated.\n"
            + "Wash rice and drain. Finely shred ginger and garlic, fry in oil with cloves, cinammon and "
            + "star anise till fragrant, add in rice and fry for several minutes. Transfer into rice "
            + "cooker, add chicken broth, pinch of salt, pandan leaves and start cooking.\n"
            + "Put all chili sauce ingredient in a mixer and grind till fine.\n"
            + "Slice and arrange tomatoes and cucumbers on a big plate, cut Chicken into small pieces and p"
            + "ut on top. Splash some light soy sauce and sesame oil over, throw a bunch of coriander "
            + "on top.\nNext, Put broth in a bowl with lettuce, get ready chili sauce and sweet "
            + "soy sauce. #Serve rice on a plate with spoon and folk.";
    public static final String WIKIA_RECIPE_URL_CHICKEN = "http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice";
    public static final String CHICKEN_RICE_IMAGE_URL = "https://vignette.wikia.nocookie.net/recipes/images/d/d3"
            + "/Chickenrice2.jpg/revision/latest/scale-to-width-down/180?cb=20080516004325";
    public static final String MOBILE_WIKIA_RECIPE_URL_CHICKEN =
            "http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice?useskin=wikiamobile";
    public static final String MOBILE_CHICKEN_RICE_IMAGE_URL = "https://vignette.wikia.nocookie.net/recipes/images/d/d3"
            + "/Chickenrice2.jpg/revision/latest/scale-to-width-down/340?cb=20080516004325";
    public static final String[] CHICKEN_TAGS = {"SingaporeanMeat", "ScrewPineLeaf",
        "Chicken", "Cucumber", "Lettuce", "MainDishPoultry", "Pineapple", "Rice"};
    public static final Recipe HAINANESE_CHICKEN_RICE = new RecipeBuilder()
            .withName(CHICKEN_NAME)
            .withIngredient(CHICKEN_INGREDIENT)
            .withInstruction(CHICKEN_INSTRUCTION)
            .withCookingTime("-")
            .withPreparationTime("-")
            .withCalories("-")
            .withServings("-")
            .withUrl(WIKIA_RECIPE_URL_CHICKEN)
            .withImage("-")
            .withTags(CHICKEN_TAGS).build();

    public static final String UGANDAN_NAME = "Ugandan Chicken Stew";
    public static final String UGANDAN_INGREDIENT = "chicken, oil, onion, tomatoes, potatoes, salt, pepper";
    public static final String UGANDAN_INSTRUCTION = "In a heavy stewing pan, sauté chicken pieces "
            + "in hot oil until nicely browned.\n"
            + "Add onion, tomatoes, potatoes, salt, pepper, and enough water to just cover.\n"
            + "Cover pan and simmer until chicken is cooked, 45 minutes to 1 hour.";
    public static final String WIKIA_RECIPE_URL_UGANDAN = "http://recipes.wikia.com/wiki/Ugandan_Chicken_Stew";
    public static final String MOBILE_WIKIA_RECIPE_URL_UGANDAN =
            "http://recipes.wikia.com/wiki/Ugandan_Chicken_Stew?useskin=wikiamobile";
    public static final String[] UGANDAN_TAGS = {"UgandanMeat", "Potato", "MainDishPoultry", "Tomato", "Stew",
        "Chicken", "RecipesThatNeedPhotos"};
    public static final Recipe UGANDAN_CHICKEN_STEW = new RecipeBuilder()
            .withName(UGANDAN_NAME)
            .withIngredient(UGANDAN_INGREDIENT)
            .withInstruction(UGANDAN_INSTRUCTION)
            .withCookingTime("-")
            .withPreparationTime("-")
            .withCalories("-")
            .withServings("-")
            .withUrl(MOBILE_WIKIA_RECIPE_URL_UGANDAN)
            .withImage("-")
            .withTags(UGANDAN_TAGS).build();

    public static final String WIKIA_RECIPE_URL_DEVIL = "http://recipes.wikia.com/wiki/Devil_Chicken";
    public static final String DEVIL_INSTRUCTION = "Clean and cut the Chicken into medium sized pieces. "
            + "Grind all the ingredients together into a fine thick gravy. "
            + "Apply this on the Chicken pieces and let it marinate for an hour. "
            + "Afterwards cook the Chicken pieces along with the gravy till "
            + "it gets cooked and it becomes dry. "
            + "Take the Chicken pieces and cool it and then deep-fry the same. "
            + "If you want the Chicken pieces to be crisp, roll over the Chicken pieces "
            + "in corn flour before frying. This can be served as a starter or "
            + "as an accompaniment in cocktail parties.";

    public static final String WIKIA_RECIPE_URL_BEEF =
            "http://recipes.wikia.com/wiki/Beef_Tenderloin_with_Madeira_Sauce";
    public static final String BEEF_INGREDIENT = "1 cup of garlic, 2 cups of mustard, 3 tbs chopped rosemary, 1 cup "
            + "chopped thyme, 2 tsp garlic, 2 tsp vegtebale oil, 4 tsp salt, 1 tsp pepper, 3 cups of water, 4 "
            + "tbs butter, 2 cups of red wine, 1 cup of garlic, 2 1/2 cups of corn, 4 cup of water, 2 tomatoes,"
            + " 1 tsp chopped thyme, 1/2 tsp each sea salt and pepper";

    public static final String WIKIA_CHICKEN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + CHICKEN_NAME + LF + PREFIX_INGREDIENT + CHICKEN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + CHICKEN_INSTRUCTION + LF + PREFIX_IMG + CHICKEN_RICE_IMAGE_URL + LF
                    + PREFIX_URL + WIKIA_RECIPE_URL_CHICKEN + LF + joinTags(CHICKEN_TAGS);
    public static final String WIKIA_UGANDAN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + UGANDAN_NAME + LF + PREFIX_INGREDIENT + UGANDAN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + UGANDAN_INSTRUCTION + LF + PREFIX_URL + WIKIA_RECIPE_URL_UGANDAN
                    + LF + joinTags(UGANDAN_TAGS);
    public static final String MOBILE_WIKIA_CHICKEN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + CHICKEN_NAME + LF + PREFIX_INGREDIENT + CHICKEN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + CHICKEN_INSTRUCTION + LF + PREFIX_IMG + MOBILE_CHICKEN_RICE_IMAGE_URL + LF
                    + PREFIX_URL + MOBILE_WIKIA_RECIPE_URL_CHICKEN + LF + joinTags(CHICKEN_TAGS);
    public static final String MOBILE_WIKIA_UGANDAN_ADD_COMMAND =
            COMMAND_WORD + LF + PREFIX_NAME + UGANDAN_NAME + LF + PREFIX_INGREDIENT + UGANDAN_INGREDIENT + LF
                    + PREFIX_INSTRUCTION + UGANDAN_INSTRUCTION + LF + PREFIX_URL + MOBILE_WIKIA_RECIPE_URL_UGANDAN
                    + LF + joinTags(UGANDAN_TAGS);

    private WikiaRecipes() {
    } // prevents instantiation

    /**
     * Takes in an array of tag strings and returns a string that can be passed to an add or edit command.
     */
    private static String joinTags(String[] tags) {
        return Arrays.stream(tags)
                .map(tag -> PREFIX_TAG + tag + " ")
                .collect(Collectors.joining());
    }
}
