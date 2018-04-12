package seedu.recipe.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.recipe.model.ReadOnlyRecipeBook;
import seedu.recipe.model.RecipeBook;
import seedu.recipe.model.recipe.Calories;
import seedu.recipe.model.recipe.CookingTime;
import seedu.recipe.model.recipe.Image;
import seedu.recipe.model.recipe.Ingredient;
import seedu.recipe.model.recipe.Instruction;
import seedu.recipe.model.recipe.Name;
import seedu.recipe.model.recipe.PreparationTime;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.model.recipe.Servings;
import seedu.recipe.model.recipe.Url;
import seedu.recipe.model.recipe.exceptions.DuplicateRecipeException;
import seedu.recipe.model.tag.Tag;

/**
 * Contains utility methods for populating {@code RecipeBook} with sample data.
 */
public class SampleDataUtil {

    //@@author kokonguyen191
    public static Recipe[] getSampleRecipes() {
        return new Recipe[] {
            new Recipe(
                        new Name("Mee Goreng"),
                        new Ingredient(
                                "green chillies, red chili paste, hot chili sauce,"
                                        + " tomato sauce, tomatoes, potatoes, mutton,"
                                        + " onion, bean sprouts, cabbage, yellow noodle,"
                                        + " oil noodle, eggs, msg, salt, sugar"),
                        new Instruction(
                                "Heat oil and fry onion well, add minced "
                                        + "mutton, tomatoes, potatoes and cabbage."
                                        + "Next, throw in noodles and bean sprouts and "
                                        + "fry for a short while.Throw in green"
                                        + " chillies, red chile and fry briefly."
                                        + "In the center of the wok, heat oil, and put"
                                        + " in the eggs, scramble and mix with "
                                        + "noodles thoroughly.Season with msg, "
                                        + "salt, sugar, tomato sauce and chile "
                                        + "sauce.Served with sliced cucumber and tomato sauce."
                                        + "Best eaten with mama teh!! enjoy!."),
                        new CookingTime("5m"),
                        new PreparationTime("10m"),
                        new Calories("750"),
                        new Servings("1"),
                        new Url("http://recipes.wikia.com/wiki/Mee_Goreng?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images/c/ca/"
                                        + "Meegoreng1.jpg/revision/latest/scale-to-width-down/"
                                        + "340?cb=20080516004609"),
                        getTagSet("Potato", "HokkienNoodle", "MungBeanSprout", "Cabbage", "Mutton",
                                "SingaporeanAppetizers", "FreshChilePepper", "Tomato", "Cucumber")
                ),
            new Recipe(
                        new Name("Hainanese Chicken Rice"),
                        new Ingredient(
                                "ginger, garlic, cinnamon, cloves, star anise,"
                                        + " chicken broth, pandan leaves, salt, "
                                        + "light soy sauce, sesame oil, cucumber, "
                                        + "tomatoes, coriander, lettuce, pineapple,"
                                        + " fresh chillies, ginger, garlic, vinegar"
                                        + ", fish sauce, sugar, sweet soy sauce"),
                        new Instruction(
                                "Boil water with spring Onion, ginger and pandan l"
                                        + "eaves, put in Chicken and cook till done, do not over cook."
                                        + "Briefly dip in cold water and set aside to cool. "
                                        + "Keep broth heated.Wash rice and drain. Fin"
                                        + "ely shred ginger and garlic, fry in oil wit"
                                        + "h cloves, cinammon and star anise till frag"
                                        + "rant, add in rice and fry for several minutes."
                                        + "Transfer into rice cooker, add chicken broth, pin"
                                        + "ch of salt, pandan leaves and start cooking"
                                        + ".Put all chili sauce ingredient in a mixer and grind till fine."
                                        + "Slice and arrange tomatoes and cucumbers on a big p"
                                        + "late, cut Chicken into small pieces and put"
                                        + " on top. Splash some light soy sauce and se"
                                        + "same oil over, throw a bunch of coriander on top."
                                        + "Next, Put broth in a bowl with lettuce, get ready c"
                                        + "hili sauce and sweet soy sauce. Serve rice "
                                        + "on a plate with spoon and fork."),
                        new CookingTime("7m"),
                        new PreparationTime("15m"),
                        new Calories("750"),
                        new Servings("2"),
                        new Url("http://recipes.wikia.com/wiki/Hainanese_Chicken_Rice?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images"
                                        + "/d/d3/Chickenrice2.jpg/revision/latest/scale-to-width-down"
                                        + "/340?cb=20080516004325"),
                        getTagSet("MainDishPoultry", "ScrewPineLeaf", "Lettuce", "SingaporeanMeat", "Chicken",
                                "Pineapple", "Cucumber", "Rice")
                ),
            new Recipe(
                        new Name("Breakfast Pizza"),
                        new Ingredient("bacon, sausage, green onions, green pepper, eggs, cheddar"),
                        new Instruction("Mix dough according to package."
                                + "While dough is rising cook bacon and sausage."
                                + "Slice vegetables.Cook scrambled eggs in the same skillet as the meat."
                                + "Spread dough thinly on pizza pan."
                                + "Cook for 5 minutes at 450°F remove from oven and add meat,"
                                + " eggs, veggies, and cheese."
                                + "Return to oven until cheese is melted.Enjoy!"),
                        new CookingTime("5m"),
                        new PreparationTime("15m"),
                        new Calories("1000"),
                        new Servings("3"),
                        new Url("http://recipes.wikia.com/wiki/Breakfast_Pizza?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/"
                                        + "images/3/31/164_1breakfast_pizza.jpg/revision/late"
                                        + "st/scale-to-width-down/340?cb=20130610170317"),
                        getTagSet("Pizza", "Breakfast", "Sausage", "Brunch", "Egg", "Cheddar", "Shallot",
                                "ReadyMadeDough", "GreenBellPepper", "Bacon")
                ),
            new Recipe(
                        new Name("Veggie Taco"),
                        new Ingredient(
                                "tortilla, refried beans, cheddar, avocado, lettuce, cucumber,"
                                        + " tomato, radishes, scallions"),
                        new Instruction("Spread refried beans onto tortilla."
                                + "Place on paper plate."
                                + "Microwave on high 30–45 seconds until beans are hot.Sprinkle with cheese."
                                + "Fold tortilla in half and top with avocado and chopped salad vegetables."
                                + "Serve with salsa."),
                        new CookingTime("1m"),
                        new PreparationTime("4m"),
                        new Calories("600"),
                        new Servings("1"),
                        new Url("http://recipes.wikia.com/wiki/Veggie_Taco?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes"
                                        + "/images/0/0b/Veggie_Taco.jpg/revision/latest/scal"
                                        + "e-to-width-down/340?cb=20080516004531"),
                        getTagSet("Lettuce", "RefriedBean", "Cheddar", "Avocado", "Radish", "MexicanVegetarian",
                                "Tomato", "Cucumber", "Taco")
                ),
            new Recipe(
                        new Name("Pho Bo"),
                        new Ingredient(
                                "rice noodles, bean sprouts, shallots, coriander, beef, beef stock, "
                                        + "consommé, fresh ginger, cinnamon, coriander seeds, star anis"
                                        + "e, caster sugar, salt, black pepper, fish sauce"),
                        new Instruction("Boil stock, add the ginger, cinnamon, coriander seeds and s"
                                + "tar anise.After 15 minutes, add the sugar, salt, peppe"
                                + "r and fish sauce.Cook the noodles in water, make them al dente."
                                + "Add bean sprouts, shallots and coriander."),
                        new CookingTime("15m"),
                        new PreparationTime("20m"),
                        new Calories("900"),
                        new Servings("2"),
                        new Url("http://recipes.wikia.com/wiki/Pho_Bo?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images/e"
                                        + "/e1/Pho_bo.jpg/revision/latest/scale-to-width-down/340?c"
                                        + "b=20080516004830"),
                        getTagSet("VietnameseSoups", "RiceNoodle", "StarAnise", "BeanSprout", "VietnameseNoodle",
                                "Beef", "BeefStockAndBroth")
                ),
            new Recipe(
                        new Name("Hiyashi Chuka"),
                        new Ingredient("water, rice wine vinegar, soy sauce, sugar, oil, water, sug"
                                + "ar, soy sauce, rice wine vinegar, sesame seeds, sesa"
                                + "me oil, Chinese egg noodles, chuka soba, ramen, eggs"
                                + ", ham, chicken breasts, cucumbers, carrots, bean spro"
                                + "uts, tomatoes, ginger, shoga"),
                        new Instruction("All ingredients should be as cold as possible for ma"
                                + "ximum body-chilling benefit."
                                + "Divide chilled noodles among serving plates."
                                + "Add toppings of your choice."
                                + "My personal favorite is ham, omelette, cucumber, ca"
                                + "rrot, bean sprouts and ginger."
                                + "Add dressing of your choice just before eating."),
                        new CookingTime("10m"),
                        new PreparationTime("10m"),
                        new Calories("700"),
                        new Servings("2"),
                        new Url("http://recipes.wikia.com/wiki/Hiyashi_Chuka?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/i"
                                        + "mages/4/4d/Hiyashi_Chuka_2.jpg/revision/latest/scal"
                                        + "e-to-width-down/340?cb=20080516004300"),
                        getTagSet("ChineseEggNoodle", "Carrot", "Egg", "Ham", "RiceVinegar", "Ramen", "BeanSprout",
                                "JapaneseSalads", "ChickenBreast", "Cucumber")
                ),
            new Recipe(
                        new Name("Bulgogi I"),
                        new Ingredient(
                                "beef sirloin, soy sauce, water, scallions, garlic, soy s"
                                        + "auce, sesame oil, black bean paste, Shaoxing wine,"
                                        + " sugar, cayenne pepper, ginger, sugar, sesame seed"
                                        + ", oil, Tabasco, salt, garlic, sesame seed, scallions, oil"),
                        new Instruction("Cut beef into very thin strips and pound to flatten; the"
                                + "n cut into medium size squares."
                                + "Combine all the other ingredients."
                                + "The marinade, as the name of the dish implies, should b"
                                + "e quite fiery."
                                + "Mix meat and marinade and set aside for 4 to 5 hours, o"
                                + "r longer if refrigerated."
                                + "Broil very quickly over hot charcoal, dip in Bulgogi sau"
                                + "ce and serve immediately with white rice."),
                        new CookingTime("15m"),
                        new PreparationTime("6h"),
                        new Calories("1500"),
                        new Servings("6"),
                        new Url("http://recipes.wikia.com/wiki/Bulgogi_I?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/"
                                        + "images/3/32/Cooking.jpg/revision/latest/scale-to-widt"
                                        + "h-down/340?cb=20050413221745"),
                        getTagSet("BeefSirloin", "KoreanMeat")
                ),
            new Recipe(
                        new Name("Rassolnik"),
                        new Ingredient(
                                "veal, beef, kidneys, chicken, giblets, carrot, parsley ro"
                                        + "ot, celery root, onion, salt, salt, black pepper"
                                        + "corns, bay leaves, potatoes, long-grain rice, c"
                                        + "ucumbers, sour cream, parsley"),
                        new Instruction("While the kidneys are soaking, cut the carrot, parsley "
                                + "and celery roots, and onion into julienne strips."
                                + "In 4-quart pot, bring 2 quarters of water to a boil."
                                + "Add the kidneys , julienned vegetables, 1 teaspoon salt, a"
                                + "nd the peppercorns and bay leaves, and bring to a boil again."
                                + "Lower the heat and simmer, partially covered, for 30 minutes."
                                + "Meanwhile, peel the potatoes and cut into 1-inch cubes."
                                + "Strain the stock, discarding the vegetables."
                                + "Cut the kidneys into ¼-inch slices and return to the stoc"
                                + "k, adding the potatoes and rice."
                                + "Cook slowly, partially covered, for 20 minutes, then add "
                                + "the pickles and simmer 5 minutes more."
                                + "Turn off the heat, cover completely, and allow the flavor"
                                + "s to mingle for 5 minutes."
                                + "Blend the sour cream with 1 cup of soup and stir it back i"
                                + "nto the pot, then taste the seasoning."),
                        new CookingTime("45m"),
                        new PreparationTime("25m"),
                        new Calories("900"),
                        new Servings("4"),
                        new Url("http://recipes.wikia.com/wiki/Rassolnik?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/image"
                                        + "s/3/3f/460.jpg/revision/latest/scale-to-width-down/"
                                        + "340?cb=20080516004855"),
                        getTagSet("Celeriac", "SourCream", "Potato", "RussianSoups", "Carrot", "RussianMeat", "Pickle",
                                "LongGrainRice", "Giblet", "ParsleyRoot", "Kidney")
                ),
            new Recipe(
                        new Name("Sausage Rolls"),
                        new Ingredient("shortcrust pastry, sausage, plain flour, milk"),
                        new Instruction("Roll the pastry out thinly into a rectangle, then cut it "
                                + "lengthwise into 2 strips."
                                + "Divide the sausage meat into 2 pieces; dust with flour an"
                                + "d form into 2 rolls the length of the pastry."
                                + "Lay a roll of sausage meat down the center of each strip;"
                                + " just brush down the edges of the pastry with a little milk."
                                + "Fold one side of the pastry over the sausage meat and pres"
                                + "s the two edges firmly together."
                                + "Seal the long edges together by flaking."
                                + "Brush the length of the two rolls with milk; then cut each"
                                + " into slices 4 cm (1 inch) to 5 cm (2 inches) long."
                                + "Place on a baking sheet and bake in a moderately hot oven "
                                + "(200°C / 400°F / Gas 6) for 15 minutes; to cook "
                                + "the meat thoroughly, reduce the temperature to mode"
                                + "ate (180°C / 350°F / Gas 4) and cook for a further 15 minutes."
                                + "Cover and brown the top of the dish under a hot grill. Ser"
                                + "ve straight from the pan."),
                        new CookingTime("30m"),
                        new PreparationTime("15m"),
                        new Calories("900"),
                        new Servings("4"),
                        new Url("http://recipes.wikia.com/wiki/Sausage_Rolls?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images/8"
                                        + "/8b/Sausage_rolls.jpg/revision/latest/scale-to-width"
                                        + "-down/340?cb=20130725232626"),
                        getTagSet("Sausage", "British", "AsianAppetizers", "Appetizer", "AustralianAppetizers",
                                "EuropeanAppetizers", "European", "World", "Asian", "Meat", "Oceanian", "SideDishMeat",
                                "SideDish", "Australian", "MeatAppetizer", "BritishAppetizers", "OceanianAppetizers",
                                "SavoryPastryAppetizer")
                ),
            new Recipe(
                        new Name("Traditional Banoffee Pie"),
                        new Ingredient("butter, brown sugar, condensed milk, bananas"),
                        new Instruction("Have a baking tin, bowl, non-stick pan and wooden spoon ready."
                                + "Make sure the digestive biscuits are crushed to breadcrumbs."
                                + "Tip all the breadcrumbs into the baking tin, then "
                                + "use a spoon to create a pie shell across th"
                                + "e bottom and around the sides of the tin."
                                + "Chill this in your fridge/freezer for ten minutes a"
                                + "nd continue to the caramel."
                                + "Melt the butter and sugar into a non-stick pan over low heat."
                                + "Stir this continuously until all the sugar has dissolved."
                                + "Add the condensed milk and bring this to a boil fo"
                                + "r about a minute. Stir this until a thick golden caramel forms."
                                + "Spread the caramel over the now firm base, and the"
                                + "n leave to chill for an hour."
                                + "After the hour chilling, remove the pie from the "
                                + "tin carefully and place it on your serving plate."
                                + "Slice the bananas into small chunks."
                                + "Create a layer of bananas on top of the caramel."
                                + "Spread the whipped cream on top so it covers the layer of bananas."),
                        new CookingTime("3h"),
                        new PreparationTime("15m"),
                        new Calories("800"),
                        new Servings("6"),
                        new Url("http://recipes.wikia.com/wiki/Traditional_Banoffee_Pie?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images/b/b2"
                                        + "/Banoffeepiewithpecan_86926_16x9.jpg/revision/latest/"
                                        + "scale-to-width-down/340?cb=20130711152627"),
                        getTagSet("BritishDesserts", "British", "Pie", "Dessert")
                ),
            new Recipe(
                        new Name("Traditional Welsh Rarebit"),
                        new Ingredient("-"),
                        new Instruction("Put the cheese, flour, mustard, Worcestershire sauce, "
                                + "butter and pepper into a saucepan."
                                + "Mix well and then add the beer or milk to moisten. Be "
                                + "careful not to make it too wet as you'll never "
                                + "get it to stick to the bread."
                                + "Stir the mixture over a low heat until it's melted."
                                + "Once your mixture has the consistency of a thick paste,"
                                + " remove it from the heat and allow to cool slightly."
                                + "While the cheese mixture is cooling, take four slices o"
                                + "f bread and toast them on one side only."
                                + "Once done, divide the mixture between the four slices of"
                                + " toast. Pop this back under the grill to brown."
                                + "Serve immediately. This dish makes a great lunchtime sna"
                                + "ck, or for a more substantial meal, try serving "
                                + "it alongside a bowl of leek and potato soup."),
                        new CookingTime("25m"),
                        new PreparationTime("30m"),
                        new Calories("1000"),
                        new Servings("4"),
                        new Url("http://recipes.wikia.com/wiki/Traditional_Welsh_Rarebit?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipes/images/9/9b/"
                                        + "WelshRarebit4_big.jpg/revision/latest/"
                                        + "scale-to-width-down/340?cb=20110728151258"),
                        getTagSet("British", "Welsh", "Snack", "Lunch")
                ),
            new Recipe(
                        new Name("Egg Curry"),
                        new Ingredient(
                                "eggs, onions, tomatoes, tomato, garlic, fresh ginger, garlic, cumin, chili powder, "
                                        + "turmeric, coriander powder, cumin, salt, yoghurt, coriander leaves, oil"),
                        new Instruction(
                                "Add cumin seeds in hot oil till it begins to sizzle. Add ginger-garli"
                                        + "c paste and Onion paste, and fry for 3 – 5 minutes till slightly browned."
                                        + "Add salt, chili powder, coriander powder, cumin powder and tur"
                                        + "meric powder and cook for another minute till fragrant. Add"
                                        + " tomatoe paste and let cook for a few minutes till all th"
                                        + "e spices blend in."
                                        + "Add in the youghurt and stir constantly to avoid getting lumps."
                                        + "Put in the boiled egg halves abd cover cook for another 5 minutes."
                                        + "Garnish with chopped coriander leaves and serve "
                                        + "warm with fresh rotis or rice."),
                        new CookingTime("15m"),
                        new PreparationTime("45m"),
                        new Calories("900"),
                        new Servings("4"),
                        new Url("http://recipes.wikia.com/wiki/Egg_Curry?useskin=wikiamobile"),
                        new Image(
                                "https://vignette.wikia.nocookie.net/recipe"
                                        + "s/images/1/1b/Egg_Curry.jpg/revision/latest/scal"
                                        + "e-to-width-down/340?cb=20080516004839"),
                        getTagSet("Curry", "Egg", "HookedOnHeat", "IndianVegetarian", "ChiliPowder", "Tomato", "Rice")
                )
        };
    }

    //@@author
    public static ReadOnlyRecipeBook getSampleRecipeBook() {
        try {
            RecipeBook sampleAb = new RecipeBook();
            for (Recipe sampleRecipe : getSampleRecipes()) {
                sampleAb.addRecipe(sampleRecipe);
            }
            return sampleAb;
        } catch (DuplicateRecipeException e) {
            throw new AssertionError("sample data cannot contain duplicate recipes", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
