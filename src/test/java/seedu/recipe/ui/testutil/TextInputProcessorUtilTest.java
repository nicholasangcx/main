//@@author hoangduong1607
package seedu.recipe.ui.testutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.recipe.ui.util.TextInputProcessorUtil;

public class TextInputProcessorUtilTest {

    private static final char WHITESPACE = ' ';
    private static final char LF = '\n';
    private static final String EMPTY_STRING = "";

    private static final String FIRST_WORD = "HELLO";
    private static final String SECOND_WORD = "WORLD";
    private static final String THIRD_WORD = "MY";
    private static final String FOURTH_WORD = "NAME";
    private static final String FIFTH_WORD = "IS";
    private static final String FIRST_SINGLE_LINE_SENTENCE = FIRST_WORD + WHITESPACE + SECOND_WORD;
    private static final String SECOND_SINGLE_LINE_SENTENCE = WHITESPACE + THIRD_WORD + WHITESPACE + FOURTH_WORD
            + WHITESPACE + FIFTH_WORD + WHITESPACE;
    private static final String MULTIPLE_LINES_SENTENCE = FIRST_SINGLE_LINE_SENTENCE + LF + SECOND_SINGLE_LINE_SENTENCE;

    private static TextInputProcessorUtil textInputProcessor = new TextInputProcessorUtil();

    @Test
    public void getLastWord_success() {
        textInputProcessor.setContent(FIRST_SINGLE_LINE_SENTENCE);
        assertEquals(SECOND_WORD, textInputProcessor.getLastWord());

        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(EMPTY_STRING, textInputProcessor.getLastWord());
    }

    @Test
    public void getFirstWord_success() {
        textInputProcessor.setContent(FIRST_SINGLE_LINE_SENTENCE);
        assertEquals(FIRST_WORD, textInputProcessor.getFirstWord());

        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(FIRST_WORD, textInputProcessor.getFirstWord());
    }

    @Test
    public void getLastLine_success() {
        textInputProcessor.setContent(MULTIPLE_LINES_SENTENCE);
        assertEquals(SECOND_SINGLE_LINE_SENTENCE, textInputProcessor.getLastLine());
    }
}
