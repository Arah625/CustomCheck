package org.oneapp.annotation;

import com.puppycrawl.tools.checkstyle.StatelessCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checkstyle check that ensures {@code @WithTimeout} annotations are followed by a trailing comment.
 * <p>
 * This check is useful for enforcing documentation standards for timeout annotations,
 * ensuring each usage of {@code @WithTimeout} is accompanied by a comment explaining modification of default timeout.
 * </p>
 *
 * @author Arah625
 */
@StatelessCheck
public class WithTimeoutTrailingCommentCheck extends AbstractCheck {

    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    private static final String MSG_KEY = "with.timeout.missing.comment";

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.ANNOTATION};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    /**
     * Visits a token and checks if it is an {@code @WithTimeout} annotation without a trailing comment.
     *
     * @param ast the AST node being visited
     */
    @Override
    public void visitToken(DetailAST ast) {
        // Check if this is a @WithTimeout annotation
        String annotationName = ast.findFirstToken(TokenTypes.IDENT).getText();
        if ("WithTimeout".equals(annotationName)) {
            int lineNo = ast.getLineNo();
            String line = getLines()[lineNo - 1]; // Line numbers are 1-based in Checkstyle
            String[] parts = line.split("//");
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                log(ast.getLineNo(), MSG_KEY);
            }
        }
    }
}