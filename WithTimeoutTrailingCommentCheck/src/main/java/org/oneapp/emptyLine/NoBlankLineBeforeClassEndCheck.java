package org.oneapp.emptyLine;


import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class NoBlankLineBeforeClassEndCheck extends AbstractCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    private static final String MSG_KEY = "no.blank.line.before.class.end";

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.ENUM_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
        final DetailAST closingBrace = objBlock.getLastChild();

        // Ensuring we are dealing with the closing brace of the class.
        if (closingBrace.getType() == TokenTypes.RCURLY) {
            DetailAST lastTokenBeforeClosingBrace = closingBrace.getPreviousSibling();
            // Skip over any trailing semicolons or single line comments just before the closing brace
            while (lastTokenBeforeClosingBrace != null &&
                    (lastTokenBeforeClosingBrace.getType() == TokenTypes.SEMI ||
                            lastTokenBeforeClosingBrace.getType() == TokenTypes.SINGLE_LINE_COMMENT ||
                            lastTokenBeforeClosingBrace.getLineNo() == closingBrace.getLineNo() - 1)) {
                lastTokenBeforeClosingBrace = lastTokenBeforeClosingBrace.getPreviousSibling();
            }

            if (lastTokenBeforeClosingBrace != null) {
                // Here, lastTokenBeforeClosingBrace.getLineNo() gives the line number of the last significant token.
                // You need to inspect if there's an actual non-blank line between this and the closing brace.
                int blankLineCount = 0;
                for (int i = lastTokenBeforeClosingBrace.getLineNo() + 1; i < closingBrace.getLineNo(); i++) {
                    String line = getLines()[i - 1].trim(); // Adjust for zero-based indexing
                    if (line.isEmpty()) {
                        blankLineCount++;
                    }
                }

                if (blankLineCount > 0) {
                    log(closingBrace, MSG_KEY);
                }
            }
        }
    }
}