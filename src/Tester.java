public class Tester {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Tester <input_file>");
            return;
        }
        String filePath = args[0]; // Get the input file path from the command line argument.
        try {
            Parser parser = new Parser(filePath);
            if (parser.parse()) {
                System.out.println("Parsing completed successfully.");
            } else {
                System.err.println("Parsing error at token: " + parser.getCurrentToken());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}