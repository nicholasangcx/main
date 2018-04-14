package seedu.recipe.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.recipe.commons.core.index.Index;
import seedu.recipe.commons.exceptions.IllegalValueException;
import seedu.recipe.commons.util.StringUtil;
import seedu.recipe.model.file.Filename;
import seedu.recipe.model.recipe.Calories;
import seedu.recipe.model.recipe.CookingTime;
import seedu.recipe.model.recipe.GroupName;
import seedu.recipe.model.recipe.Image;
import seedu.recipe.model.recipe.Ingredient;
import seedu.recipe.model.recipe.Instruction;
import seedu.recipe.model.recipe.Name;
import seedu.recipe.model.recipe.PreparationTime;
import seedu.recipe.model.recipe.Servings;
import seedu.recipe.model.recipe.Url;
import seedu.recipe.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    //@@author kokonguyen191

    /**
     * Returns a null {@code Ingredient} object to use as the default value if no value is given.
     */
    public static Ingredient getNullReferenceIngredient() throws IllegalValueException {
        return new Ingredient(Ingredient.NULL_INGREDIENT_REFERENCE);
    }

    /**
     * Parses a {@code String ingredient} into an {@code Ingredient}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code ingredient} is invalid.
     */
    public static Ingredient parseIngredient(String ingredient) throws IllegalValueException {
        requireNonNull(ingredient);
        String trimmedIngredient = ingredient.trim();
        if (!Ingredient.isValidIngredient(trimmedIngredient)) {
            throw new IllegalValueException(Ingredient.MESSAGE_INGREDIENT_CONSTRAINTS);
        }
        return new Ingredient(trimmedIngredient);
    }

    /**
     * Parses a {@code Optional<String> ingredient} into an {@code Optional<Ingredient>}
     * if {@code ingredient} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Ingredient> parseIngredient(Optional<String> ingredient) throws IllegalValueException {
        requireNonNull(ingredient);
        return ingredient.isPresent() ? Optional.of(parseIngredient(ingredient.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> ingredient} into an {@code Optional<Ingredient>}
     * if {@code ingredient} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Ingredient> parseIngredientOnInitialAdd(Optional<String> ingredient)
            throws IllegalValueException {
        requireNonNull(ingredient);
        return ingredient.isPresent()
                ? Optional.of(parseIngredient(ingredient.get())) : Optional.of(getNullReferenceIngredient());
    }

    /**
     * Returns a null {@code Instruction} object to use as the default value if no value is given.
     */
    public static Instruction getNullReferenceInstruction() throws IllegalValueException {
        return new Instruction(Instruction.NULL_INSTRUCTION_REFERENCE);
    }

    /**
     * Parses a {@code String recipe} into an {@code Instruction}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code recipe} is invalid.
     */
    public static Instruction parseInstruction(String instruction) throws IllegalValueException {
        requireNonNull(instruction);
        String trimmedInstruction = instruction.trim();
        if (!Instruction.isValidInstuction(trimmedInstruction)) {
            throw new IllegalValueException(Instruction.MESSAGE_INSTRUCTION_CONSTRAINTS);
        }
        return new Instruction(trimmedInstruction);
    }

    /**
     * Parses a {@code Optional<String> recipe} into an {@code Optional<Instruction>} if {@code recipe} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Instruction> parseInstruction(Optional<String> instruction) throws IllegalValueException {
        requireNonNull(instruction);
        return instruction.isPresent() ? Optional.of(parseInstruction(instruction.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> instruction} into an {@code Optional<Instruction>}
     * if {@code instruction} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Instruction> parseInstructionOnInitialAdd(Optional<String> instruction)
            throws IllegalValueException {
        requireNonNull(instruction);
        return instruction.isPresent()
                ? Optional.of(parseInstruction(instruction.get())) : Optional.of(getNullReferenceInstruction());
    }

    /**
     * Returns a null {@code CookingTime} object to use as the default value if no value is given.
     */
    public static CookingTime getNullReferenceCookingTime() throws IllegalValueException {
        return new CookingTime(CookingTime.NULL_COOKING_TIME_REFERENCE);
    }

    /**
     * Parses a {@code String cookingTime} into a {@code CookingTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code cookingTime} is invalid.
     */
    public static CookingTime parseCookingTime(String cookingTime) throws IllegalValueException {
        requireNonNull(cookingTime);
        String trimmedCookingTime = cookingTime.trim();
        if (!CookingTime.isValidCookingTime(trimmedCookingTime)) {
            throw new IllegalValueException(CookingTime.MESSAGE_COOKING_TIME_CONSTRAINTS);
        }
        return new CookingTime(trimmedCookingTime);
    }

    /**
     * Parses a {@code Optional<String> cookingTime} into an {@code Optional<CookingTime>}
     * if {@code cookingTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<CookingTime> parseCookingTime(Optional<String> cookingTime) throws IllegalValueException {
        requireNonNull(cookingTime);
        return cookingTime.isPresent()
                ? Optional.of(parseCookingTime(cookingTime.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> cookingTime} into an {@code Optional<CookingTime>}
     * if {@code cookingTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<CookingTime> parseCookingTimeOnInitialAdd(Optional<String> cookingTime)
            throws IllegalValueException {
        requireNonNull(cookingTime);
        return cookingTime.isPresent()
                ? Optional.of(parseCookingTime(cookingTime.get())) : Optional.of(getNullReferenceCookingTime());
    }

    /**
     * Returns a null {@code PreparationTime} object to use as the default value if no value is given.
     */
    public static PreparationTime getNullReferencePreparationTime() throws IllegalValueException {
        return new PreparationTime(PreparationTime.NULL_PREPARATION_TIME_REFERENCE);
    }

    /**
     * Parses a {@code String preparationTime} into a {@code PreparationTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code preparationTime} is invalid.
     */
    public static PreparationTime parsePreparationTime(String preparationTime) throws IllegalValueException {
        requireNonNull(preparationTime);
        String trimmedPreparationTime = preparationTime.trim();
        if (!PreparationTime.isValidPreparationTime(trimmedPreparationTime)) {
            throw new IllegalValueException(PreparationTime.MESSAGE_PREPARATION_TIME_CONSTRAINTS);
        }
        return new PreparationTime(trimmedPreparationTime);
    }

    /**
     * Parses a {@code Optional<String> preparationTime} into an {@code Optional<PreparationTime>}
     * if {@code preparationTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PreparationTime> parsePreparationTime(Optional<String> preparationTime)
            throws IllegalValueException {
        requireNonNull(preparationTime);
        return preparationTime.isPresent()
                ? Optional.of(parsePreparationTime(preparationTime.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> preparationTime} into an {@code Optional<PreparationTime>}
     * if {@code preparationTime} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<PreparationTime> parsePreparationTimeOnInitialAdd(Optional<String> preparationTime)
            throws IllegalValueException {
        requireNonNull(preparationTime);
        return preparationTime.isPresent() ? Optional.of(parsePreparationTime(preparationTime.get()))
                : Optional.of(getNullReferencePreparationTime());
    }

    /**
     * Returns a null {@code Calories} object to use as the default value if no value is given.
     */
    public static Calories getNullReferenceCalories() throws IllegalValueException {
        return new Calories(Calories.NULL_CALORIES_REFERENCE);
    }

    /**
     * Parses a {@code String calories} into a {@code Calories}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code calories} is invalid.
     */
    public static Calories parseCalories(String calories) throws IllegalValueException {
        requireNonNull(calories);
        String trimmedCalories = calories.trim();
        if (!Calories.isValidCalories(trimmedCalories)) {
            throw new IllegalValueException(Calories.MESSAGE_CALORIES_CONSTRAINTS);
        }
        return new Calories(trimmedCalories);
    }

    /**
     * Parses a {@code Optional<String> calories} into an {@code Optional<Calories>}
     * if {@code calories} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Calories> parseCalories(Optional<String> calories) throws IllegalValueException {
        requireNonNull(calories);
        return calories.isPresent()
                ? Optional.of(parseCalories(calories.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> calories} into an {@code Optional<Calories>}
     * if {@code calories} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Calories> parseCaloriesOnInitialAdd(Optional<String> calories) throws IllegalValueException {
        requireNonNull(calories);
        return calories.isPresent()
                ? Optional.of(parseCalories(calories.get())) : Optional.of(getNullReferenceCalories());
    }

    /**
     * Returns a null {@code Servings} object to use as the default value if no value is given.
     */
    public static Servings getNullReferenceServings() throws IllegalValueException {
        return new Servings(Servings.NULL_SERVINGS_REFERENCE);
    }

    /**
     * Parses a {@code String servings} into a {@code Servings}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code servings} is invalid.
     */
    public static Servings parseServings(String servings) throws IllegalValueException {
        requireNonNull(servings);
        String trimmedServings = servings.trim();
        if (!Servings.isValidServings(trimmedServings)) {
            throw new IllegalValueException(Servings.MESSAGE_SERVINGS_CONSTRAINTS);
        }
        return new Servings(trimmedServings);
    }

    /**
     * Parses a {@code Optional<String> servings} into an {@code Optional<Servings>}
     * if {@code servings} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Servings> parseServings(Optional<String> servings) throws IllegalValueException {
        requireNonNull(servings);
        return servings.isPresent()
                ? Optional.of(parseServings(servings.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> servings} into an {@code Optional<Servings>}
     * if {@code servings} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Servings> parseServingsOnInitialAdd(Optional<String> servings) throws IllegalValueException {
        requireNonNull(servings);
        return servings.isPresent()
                ? Optional.of(parseServings(servings.get())) : Optional.of(getNullReferenceServings());
    }

    //@@author RyanAngJY

    /**
     * Parses a {@code String url} into an {@code Url}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code url} is invalid.
     */
    public static Url parseUrl(String url) throws IllegalValueException {
        requireNonNull(url);
        String trimmedUrl = url.trim();
        if (!Url.isValidUrl(trimmedUrl)) {
            throw new IllegalValueException(Url.MESSAGE_URL_CONSTRAINTS);
        }
        return new Url(trimmedUrl);
    }

    /**
     * Parses a {@code Optional<String> url} into an {@code Optional<Url>} if {@code url} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Url> parseUrl(Optional<String> url) throws IllegalValueException {
        requireNonNull(url);
        return url.isPresent() ? Optional.of(parseUrl(url.get())) : Optional.empty();
    }

    public static Url getNullReferenceUrl() {
        return new Url(Url.NULL_URL_REFERENCE);
    }

    /**
     * Parses a {@code Optional<String> url} into an {@code Optional<Url>} if {@code url} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Url> parseUrlOnInitialAdd(Optional<String> url) throws IllegalValueException {
        requireNonNull(url);
        return url.isPresent() ? Optional.of(parseUrl(url.get())) : Optional.of(getNullReferenceUrl());
    }

    /**
     * Parses a {@code String image} into an {@code image}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code image} is invalid.
     */
    public static Image parseImage(String image) throws IllegalValueException {
        requireNonNull(image);
        String trimmedImage = image.trim();
        if (!Image.isValidImage(trimmedImage)) {
            throw new IllegalValueException(Image.MESSAGE_IMAGE_CONSTRAINTS);
        }
        return new Image(trimmedImage);
    }

    /**
     * Parses a {@code Optional<String> url} into an {@code Optional<Url>} if {@code url} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Image> parseImage(Optional<String> image) throws IllegalValueException {
        requireNonNull(image);
        return image.isPresent() ? Optional.of(parseImage(image.get())) : Optional.empty();
    }

    public static Image getNullReferenceImage() {
        return new Image(Image.NULL_IMAGE_REFERENCE);
    }

    /**
     * Parses a {@code Optional<String> image} into an {@code Optional<Image>} if {@code image} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Image> parseImageOnInitialAdd(Optional<String> image) throws IllegalValueException {
        requireNonNull(image);
        return image.isPresent() ? Optional.of(parseImage(image.get())) : Optional.of(getNullReferenceImage());
    }
    //@@author

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    //@@author hoangduong1607
    /**
     * Parses {@code Collection<String> indices} into a {@code Set<Index>}.
     */
    public static Set<Index> parseIndices(Collection<String> indices) throws IllegalValueException {
        requireNonNull(indices);
        final Set<Index> indexSet = new HashSet<>();
        for (String index : indices) {
            indexSet.add(parseIndex(index));
        }
        return indexSet;
    }
    //@@author

    //@@author nicholasangcx

    /**
     * Parses {@code String filename} into a {@code String XmlExtensionFilename}.
     * A .xml extension will be added to the original filename.
     *
     * @throws IllegalValueException if the give {@code filename} is invalid.
     */
    public static String parseFilename(String filename) throws IllegalValueException {
        requireNonNull(filename);
        if (!Filename.isValidFilename(filename)) {
            throw new IllegalValueException(Filename.MESSAGE_FILENAME_CONSTRAINTS);
        }
        return filename + ".xml";
    }
    //@@author

    //@@author hoangduong1607
    /**
     * Parses a {@code String groupName} into a {@code GroupName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code groupName} is invalid.
     */
    public static GroupName parseGroupName(String groupName) throws IllegalValueException {
        requireNonNull(groupName);
        String trimmedName = groupName.trim();
        if (!GroupName.isValidName(trimmedName)) {
            throw new IllegalValueException(GroupName.MESSAGE_NAME_CONSTRAINTS);
        }
        return new GroupName(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> groupName} into an {@code Optional<GroupName>} if {@code groupName} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<GroupName> parseGroupName(Optional<String> groupName) throws IllegalValueException {
        requireNonNull(groupName);
        return groupName.isPresent() ? Optional.of(parseGroupName(groupName.get())) : Optional.empty();
    }
    //@@author
}
