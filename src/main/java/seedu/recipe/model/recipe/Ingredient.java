package seedu.recipe.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.commons.util.AppUtil.checkArgument;

/**
 * Represents a Recipe's ingredient in the recipe book.
 * Guarantees: immutable; is valid as declared in {@link #isValidIngredient(String)}
 */
public class Ingredient {

    public static final String NULL_INGREDIENT_REFERENCE = "-";
    public static final String MESSAGE_INGREDIENT_CONSTRAINTS = "Recipe ingredients should be a list of "
            + "ingredients, delimited by commas.";
    public static  final String INGREDIENT_VALIDATION_REGEX = "^(([a-zA-Z0-9]|[^\\x00-\\x7F]|-)+,?\\s*)+$";

    public final String value;

    /**
     * Constructs an {@code Ingredient}.
     *
     * @param ingredient A valid ingredient recipe.
     */
    public Ingredient(String ingredient) {
        requireNonNull(ingredient);
        checkArgument(isValidIngredient(ingredient), MESSAGE_INGREDIENT_CONSTRAINTS);
        this.value = ingredient;
    }

    /**
     * Returns true if a given string is a valid recipe ingredient.
     */
    public static boolean isValidIngredient(String test) {
        return test.equals(NULL_INGREDIENT_REFERENCE) || test.matches(INGREDIENT_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Ingredient // instanceof handles nulls
                && this.value.equals(((Ingredient) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
