import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.PushbackReader;

public class Parser {
    private Lexer lexer;
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

    public void parse() {
        program();
        match("EOF");
    }

    private void program() {
        stmt();
        while (currentToken.getName().equals(";")) {
            match(";");
            stmt();
        }
    }

    private void stmt() {
        switch (currentToken.getName()) {
            case "IF" -> {
                match("IF");
                expr();
                match("THEN");
                stmt();
                if (currentToken.getName().equals("ELSE")) {
                    match("ELSE");
                    stmt();
                }
                match("END IF");
            }
            case "ID" -> {
                String id = currentToken.getAttribute();
                match("ID");
                if (currentToken.getName().equals("ASSIGN")) {
                    match("ASSIGN");
                    expr();
                    // Perform assignment of the ID
                    System.out.println("Assigning " + id);
                }
            }
            case "WHILE" -> {
                match("WHILE");
                expr();
                match("LOOP");
                stmt();
                match("END LOOP");
            }
        }
    }

    private void expr() {
        term();
        while (currentToken.getName().equals("RELOP")) {
            match("RELOP");
            term();
        }
    }

    private void term() {
        if (currentToken.getName().equals("ID")) {
            match("ID");
        } else if (currentToken.getName().equals("NUMBER")) {
            match("NUMBER");
        } else {
            // Handle error
            System.err.println("Syntax error: ID or NUMBER expected, but got " + currentToken.getName());
            System.exit(1);
        }
    }

    private void match(String expectedTokenType) {
        String currentTokenName = currentToken.getName();
        if (currentTokenName.equals(expectedTokenType)) {
            if (currentTokenName.equals("EOF")) {
                return;
            }
            try {
                currentToken = lexer.nextToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Syntax error: " + expectedTokenType + " expected, but got " + currentToken.getName());
            System.exit(1);
        }
    }
}