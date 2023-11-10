import java.io.FileNotFoundException;

public class Tester {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Tester <input_file>");
            return;
        }
        String filePath = args[0]; // Get the input file path from the command line argument.
        try {
            Parser parser = new Parser(filePath);
            parser.parse();
            Token token = parser.getCurrentToken();
            if (token.getName().equals("EOF")) {
                System.out.println("Parsing completed successfully.");
            } else {
                System.err.println("Parsing error: Unexpected token " + token);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        // LexicalAnalysis(filePath);
    }

    private static void LexicalAnalysis(String filePath) {
        Lexer lexicalAnalyzer = new Lexer();
        if (lexicalAnalyzer.initialize(filePath)) {
            Token token;
            try {
                while (!(token = lexicalAnalyzer.nextToken()).toString().equals("EOF")) {
                    System.out.println(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}