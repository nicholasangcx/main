package seedu.recipe.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.recipe.commons.core.LogsCenter;
import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.commons.exceptions.NoInternetConnectionException;
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
import seedu.recipe.model.tag.Tag;

/**
 * JAXB-friendly version of the Recipe.
 */
public class XmlAdaptedRecipe {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Recipe's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String ingredient;
    @XmlElement(required = true)
    private String instruction;
    @XmlElement(required = true)
    private String cookingTime;
    @XmlElement(required = true)
    private String preparationTime;
    @XmlElement(required = true)
    private String calories;
    @XmlElement(required = true)
    private String servings;
    @XmlElement(required = true)
    private String url;
    @XmlElement(required = true)
    private String image;


    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedRecipe.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedRecipe() {
    }

    /**
     * Constructs an {@code XmlAdaptedRecipe} with the given recipe details.
     */
    public XmlAdaptedRecipe(String name, String ingredient, String instruction, String cookingTime,
                            String preparationTime, String calories, String servings, String url, String image,
                            List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.ingredient = ingredient;
        this.instruction = instruction;
        this.cookingTime = cookingTime;
        this.preparationTime = preparationTime;
        this.calories = calories;
        this.servings = servings;
        this.url = url;
        this.image = image;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Recipe into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedRecipe
     */
    public XmlAdaptedRecipe(Recipe source) {
        name = source.getName().fullName;
        ingredient = source.getIngredient().value;
        instruction = source.getInstruction().value;
        cookingTime = source.getCookingTime().value;
        preparationTime = source.getPreparationTime().value;
        calories = source.getCalories().value;
        servings = source.getServings().value;
        url = source.getUrl().value;
        image = source.getImage().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted recipe object into the model's Recipe object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted recipe
     */
    public Recipe toModelType() throws IllegalValueException {
        final List<Tag> recipeTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            recipeTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        //@@author kokonguyen191
        if (this.ingredient == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Ingredient.class.getSimpleName()));
        }
        if (!Ingredient.isValidIngredient(this.ingredient)) {
            throw new IllegalValueException(Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);
        }
        final Ingredient ingredient = new Ingredient(this.ingredient);

        if (this.instruction == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Instruction.class.getSimpleName()));
        }
        if (!Instruction.isValidInstuction(this.instruction)) {
            throw new IllegalValueException(Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);
        }
        final Instruction instruction = new Instruction(this.instruction);

        if (this.preparationTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PreparationTime.class.getSimpleName()));
        }
        if (!PreparationTime.isValidPreparationTime(this.preparationTime)) {
            throw new IllegalValueException(PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);
        }
        final PreparationTime preparationTime = new PreparationTime(this.preparationTime);

        if (this.cookingTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CookingTime.class.getSimpleName()));
        }
        if (!CookingTime.isValidCookingTime(this.cookingTime)) {
            throw new IllegalValueException(CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS);
        }
        final CookingTime cookingTime = new CookingTime(this.cookingTime);

        if (this.calories == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Calories.class.getSimpleName()));
        }
        if (!Calories.isValidCalories(this.calories)) {
            throw new IllegalValueException(Calories.MESSAGE_CALORIES_CONSTRAINTS);
        }
        final Calories calories = new Calories(this.calories);

        if (this.servings == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Servings.class.getSimpleName()));
        }
        if (!Servings.isValidServings(this.servings)) {
            throw new IllegalValueException(Servings.MESSAGE_SERVINGS_CONSTRAINTS);
        }
        final Servings servings = new Servings(this.servings);

        //@@author RyanAngJY
        if (this.url == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Url.class.getSimpleName()));
        }
        if (!Url.isValidUrl(this.url)) {
            throw new IllegalValueException(Url.MESSAGE_URL_CONSTRAINTS);
        }
        final Url url = new Url(this.url);

        if (this.image == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Image.class.getSimpleName()));
        }
        try {
            if (!Image.isValidImage(this.image)) {
                this.image = Image.NULL_IMAGE_REFERENCE;
            }
        } catch (NoInternetConnectionException e) {
            LogsCenter.getLogger(XmlAdaptedRecipe.class)
                    .warning("No Internet connection while trying to get Recipe object from XmlAdaptedRecipe.");
        }
        final Image image = new Image(this.image);
        //@@author

        final Set<Tag> tags = new HashSet<>(recipeTags);
        return new Recipe(name, ingredient, instruction, cookingTime, preparationTime, calories, servings, url,
                image, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedRecipe)) {
            return false;
        }

        XmlAdaptedRecipe otherRecipe = (XmlAdaptedRecipe) other;
        return Objects.equals(name, otherRecipe.name)
                && Objects.equals(ingredient, otherRecipe.ingredient)
                && Objects.equals(instruction, otherRecipe.instruction)
                && Objects.equals(cookingTime, otherRecipe.cookingTime)
                && Objects.equals(preparationTime, otherRecipe.preparationTime)
                && Objects.equals(calories, otherRecipe.calories)
                && Objects.equals(servings, otherRecipe.servings)
                && Objects.equals(url, otherRecipe.url)
                && Objects.equals(image, otherRecipe.image)
                && tagged.equals(otherRecipe.tagged);
    }
}
