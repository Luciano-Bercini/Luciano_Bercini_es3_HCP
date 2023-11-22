// Parser top-down a discesa ricorsiva.
public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(String filePath) {
        lexer = new Lexer();
        lexer.initialize(filePath);
        try {
            currentToken = lexer.nextToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    // Encoding the S (initial symbol) production.
    public boolean parse() {
        if (program()) {
            return currentToken.getName().equals("EOF");
        }
        return false;
    }


    private boolean program() {
        if (stmt()) {
            while (match(";")) {
                if (!stmt()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean stmt() {
        if (match(Token.IF)) {
            if (expr()) {
                if (match(Token.THEN)) {
                    if (stmt()) {
                        if (match(Token.END_IF)) {
                            return true;
                        }
                        if (match(Token.ELSE)) {
                            if (stmt()) {
                                if (match(Token.END_IF)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (match(Token.ID)) {
            if (match(Token.ASSIGN)) {
                return expr();
            }
            return false;
        }
        if (match(Token.WHILE)) {
            if (expr()) {
                if (match(Token.LOOP)) {
                    if (stmt()) {
                        return match(Token.END_LOOP);
                    }
                }
            }
            return false;
        }
        return false;
    }

    private boolean expr() {
        // Implementing lookahead by checking everything from FIRST(alpha).
        if (match(Token.ID)) {
            if (match(Token.RELOP)) {
                return expr();
            }
            return true;
        }
        if (match(Token.NUMBER)) {
            if (match(Token.RELOP)) {
                return expr();
            }
            return true;
        }
        return false;
    }

    // Returns whether there's a match between the expected terminal token and the currently scanned token.
    // If there's a match, we also move to the next token as we're doing fine with the current non-terminal.
    private boolean match(String expectedTerminalToken) {
        String currentTokenName = currentToken.getName();
        if (!currentTokenName.equals(expectedTerminalToken)) {
            return false;
        }
        try {
            if (!currentTokenName.equals("EOF")) {
                currentToken = lexer.nextToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}