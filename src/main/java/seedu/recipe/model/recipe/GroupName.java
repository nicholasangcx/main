//@@author hoangduong1607
package seedu.recipe.model.recipe;

/**
 * Represents a recipe group's name in the recipe book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class GroupName extends Name {
    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Recipe group names should only contain alphanumeric characters and spaces, and it should not be blank";

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public GroupName(String name) {
        super(name);
    }
}
