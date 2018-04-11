//@@author kokonguyen191
package seedu.recipe.ui.parser;

import static seedu.recipe.logic.commands.AddCommand.COMMAND_WORD;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INGREDIENT;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_INSTRUCTION;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_URL;

import org.jsoup.nodes.Document;

/**
 * The API set of a web parser
 */
public abstract class WebParser {

    private static final String LF = "\n";

    protected Document document;

    /**
     * Returns the parsed Recipe object from the page.
     */
    public String parseRecipe() {
        String name = getName();
        String ingredient = getIngredient();
        String instruction = getInstruction();
        String imageUrl = getImageUrl();
        String url = getUrl();
        String[] tags = getTags();

        if (!name.equals("")) {
            StringBuilder commandBuilder = new StringBuilder();
            commandBuilder.append(COMMAND_WORD)
                    .append(LF)
                    .append(PREFIX_NAME)
                    .append(name);
            if (!ingredient.equals("")) {
                commandBuilder.append(LF)
                        .append(PREFIX_INGREDIENT)
                        .append(ingredient);
            }
            if (!instruction.equals("")) {
                commandBuilder.append(LF)
                        .append(PREFIX_INSTRUCTION)
                        .append(instruction);
            }
            if (!imageUrl.equals("")) {
                commandBuilder.append(LF)
                        .append(PREFIX_IMG)
                        .append(imageUrl);
            }
            commandBuilder.append(LF)
                    .append(PREFIX_URL)
                    .append(url);
            if (tags.length > 0) {
                commandBuilder.append(LF);
                for (String tag : tags) {
                    commandBuilder
                            .append(PREFIX_TAG)
                            .append(tag)
                            .append(" ");
                }
            }
            return commandBuilder.toString();
        }
        return null;
    }

    public abstract String getName();

    public abstract String getIngredient();

    public abstract String getInstruction();

    public abstract String getImageUrl();

    public abstract String getUrl();

    public abstract String[] getTags();
}
