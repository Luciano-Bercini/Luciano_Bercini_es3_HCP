public class Token {
    
    public static String EOF = "EOF";
    public static String IF = "IF";
    public static String THEN = "THEN";
    public static String ELSE = "ELSE";
    public static String END_IF = "END IF";
    public static String END_LOOP = "END LOOP";
    public static String ID = "ID";
    public static String ASSIGN = "ASSIGN";
    public static String WHILE = "WHILE";
    public static String LOOP = "LOOP";
    public static String RELOP = "RELOP";
    public static String NUMBER = "NUMBER";
    public static String INT = "INT";
    public static String FLOAT = "FLOAT";
    
    private String name;
    private String attribute;

    public Token(String name, String attribute) {
        this.name = name;
        this.attribute = attribute;
    }

    public Token(String name) {
        this.name = name;
        this.attribute = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String toString() {
        return attribute == null ? name : "(" + name + ", \"" + attribute + "\")";
    }
}
