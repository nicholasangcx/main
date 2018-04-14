//@@author hoangduong1607
package seedu.recipe.ui.util;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Provides basic functions for processing text input
 */
public class TextInputProcessorUtil {

    private static final String EMPTY_STRING = "";
    private static final char LF = '\n';
    private static final char SPACE = ' ';
    private static final int LINE_HEIGHT = 26;
    private static final int MAX_LENGTH = 60;
    private static final int MAX_LINES = 9;

    private String content;
    private Font font;

    public TextInputProcessorUtil() {
        content = new String();
    }

    /**
     * Gets X-coordinate of caret
     */
    public double getCaretPositionX() {
        Text lastLine = new Text(getTextWithoutLastWord(getLastLine()));
        lastLine.setFont(font);
        return lastLine.prefWidth(-1);
    }

    /**
     * Gets Y-coordinate of caret
     */
    public double getCaretPositionY() {
        return getRow() * LINE_HEIGHT;
    }

    /**
     * Checks whether text is too long in
     */
    public boolean isTextTooLong() {
        return (content.length() >= MAX_LENGTH || getRow() >= MAX_LINES);
    }

    /**
     * Gets row index (1-indexed) of caret
     */
    public int getRow() {
        int row = 1;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == LF) {
                row++;
            }
        }
        return row;
    }

    /**
     * Gets last word (character(s) between the last whitespace and end of string) from {@code content}
     */
    public String getLastWord() {
        String lastWord = EMPTY_STRING;

        for (int i = content.length() - 1; i >= 0; i--) {
            if (isWordSeparator(content.charAt(i))) {
                break;
            }
            lastWord = content.charAt(i) + lastWord;
        }

        return lastWord;
    }

    /**
     * Gets first word from {@code content}
     */
    public String getFirstWord() {
        String firstWord = EMPTY_STRING;

        String trimmedContent = content.trim();
        for (int i = 0; i < trimmedContent.length(); i++) {
            if (isWordSeparator(trimmedContent.charAt(i))) {
                break;
            }
            firstWord = firstWord + trimmedContent.charAt(i);
        }

        return firstWord;
    }

    /**
     * Checks whether {@code inputChar} is a word separator
     */
    private boolean isWordSeparator(char inputChar) {
        return (inputChar == LF || inputChar == SPACE);
    }

    /**
     * Gets last line from {@code content}
     */
    public String getLastLine() {
        String lastLine = new String("");

        for (int i = content.length() - 1; i >= 0; i--) {
            if (isLineSeparator(content.charAt(i))) {
                break;
            }
            lastLine = content.charAt(i) + lastLine;
        }

        return lastLine;
    }

    /**
     * Checks whether {@code inputChar} is a line separator
     */
    private boolean isLineSeparator(char inputChar) {
        return (inputChar == LF);
    }

    /**
     * Gets the resulting text after replacing last word of {@code content} with {@code newLastWord}
     */
    public String replaceLastWord(String newLastWord) {
        return getTextWithoutLastWord() + newLastWord;
    }

    /**
     * Gets the resulting text after removing last word from {@code content}
     */
    public String getTextWithoutLastWord() {
        int newLength = content.length() - getLastWord().length();
        return content.substring(0, newLength);
    }

    /**
     * Gets the resulting text after removing last word from {@code inputText}
     */
    public String getTextWithoutLastWord(String inputText) {
        int newLength = inputText.length() - getLastWord().length();
        return inputText.substring(0, newLength);
    }

    /**
     * Sets content in TextInputProcessor to {@code inputText}
     */
    public void setContent(String inputText) {
        content = inputText;
    }

    /**
     * Sets font of content in TextInputProcessor to {@code font}
     */
    public void setFont(Font inputFont) {
        font = inputFont;
    }
}
