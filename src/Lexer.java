import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.PushbackReader;

// Implementation of a lexer through transition diagrams; keywords do not use a transition diagram.
public class Lexer {

    private static HashMap<String, Token> keywordTable;
    private ArrayList<String> symbolTable;
    private PushbackReader reader;
    private int state;
    private int relopState;

    public Lexer() {
        keywordTable = new HashMap<>();
        symbolTable = new ArrayList<>();
        initializeKeywordTable();
        state = 0;
        relopState = 0;
    }

    private void initializeKeywordTable() {
        keywordTable.put("end if", new Token("END IF"));
        keywordTable.put("end loop", new Token("END LOOP"));
        keywordTable.put("if", new Token("IF"));
        keywordTable.put("then", new Token("THEN"));
        keywordTable.put("else", new Token("ELSE"));
        keywordTable.put("while", new Token("WHILE"));
        keywordTable.put("int", new Token("INT"));
        keywordTable.put("float", new Token("FLOAT"));
        keywordTable.put("loop", new Token("LOOP"));
    }

    public Boolean initialize(String filePath) {
        try {
            reader = new PushbackReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found while reading file from path: " + filePath);
            return false;
        }
        return true;
    }

    public Token nextToken() throws Exception {
        // Reset the state whenever we call nextToken() as we want to start fresh.
        state = 0;
        StringBuilder lexeme = new StringBuilder();
        int currentChar = -1;
        while (true) {
            try {
                currentChar = reader.read();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
            char c = (char) currentChar;
            switch (state) {
                case 0: // Case 0 handles the initial "routing" to the different diagrams.
                    if (Character.isSpaceChar(c)) {
//                        System.out.println(lexeme);
//                        if (lexeme.toString().equals("end")) {
//          
//                            state = 1;
//                            lexeme.append(c);
//                        }
                        break; // Skip space characters.
                    } else if (c == ';') {
                        return new Token(";");
                    } else if (Character.isLetter(c)) {
                        state = 1; // Identifiers.
                        lexeme.append(c);
                    } else if (Character.isDigit(c)) {
                        state = 2; // Numbers.
                        lexeme.append(c);
                    } else if (c == '<') {
                        lexeme.append(c);
                        state = 3; // Operators starting with '<'.
                    } else if (c == '>') {
                        lexeme.append(c);
                        state = 4; // Operators starting with '>'.
                    }
                    break;

                case 1: // Case 1 handles identifiers.
                    if (Character.isLetterOrDigit(c)) {
                        lexeme.append(c);
                    } else {
                        unread(c);
                        return installId(lexeme.toString());
                    }
                    break;

                case 2: // Case 2 handles numbers.
                    if (Character.isDigit(c) || c == '.') {
                        lexeme.append(c);
                    } else {
                        unread(c);
                        String tokenValue = lexeme.toString();
                        return new Token("NUMBER", tokenValue);
                    }
                    break;

                case 3: // Case 3 handles operators starting with '<'.
                    lexeme.append(c);
                    if (c == '=') {
                        relopState = 1;
                    } else if (c == '>') {
                        relopState = 2;
                    } else if (c == '-') {
                        int lexemeLength = lexeme.length();
                        int previousCharIndex = lexemeLength - 2;
                        if (previousCharIndex > 0 && lexeme.charAt(previousCharIndex) == '-') {
                            return new Token("ASSIGN");
                        }
                        lexeme.append(c);
                    } else {
                        unread(c);
                        return new Token(Token.Operator, "LT");
                    }
                    break;

                case 4: // Case 4 handles operators starting with '>'.
                    lexeme.append(c);
                    if (c == '=') {
                        relopState = 3;
                    } else {
                        unread(c);
                        return new Token(Token.Operator, "GT");
                    }
                    break;

                case 5: // Case 5 handles operators starting with '=' (for EQ).
                    lexeme.append(c);
                    if (c == '=') {
                        relopState = 4;
                    } else {
                        unread(c);
                        return new Token("ASSIGN");
                    }
                    break;
                    
                default:
                    break;
            }

            if (relopState > 0) {
                if (relopState == 1) {
                    return new Token(Token.Operator, "LE");
                } else if (relopState == 2) {
                    return new Token(Token.Operator, "NE");
                } else if (relopState == 3) {
                    return new Token(Token.Operator, "GE");
                } else if (relopState == 4) {
                    return new Token(Token.Operator, "EQ");
                }
            }
            
            boolean hasReachedEOF = currentChar == -1;
            if (hasReachedEOF) {
                reader.close();
                return new Token("EOF");
            }
        }
    }

    private Boolean isVoidChar(int c) {
        return c == -1 || Character.isSpaceChar(c);
    }

    private void unread(int character) {
        // Unreading void characters is pointless, as they cannot form a token.
        if (!isVoidChar(character)) {
            try {
                reader.unread(character);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Token installId(String lexeme) {
        if (keywordTable.containsKey(lexeme)) {
            System.out.println(lexeme);
            return keywordTable.get(lexeme);
        }
        else {
            symbolTable.add(lexeme);
            return new Token("ID", lexeme);
        }
    }
}