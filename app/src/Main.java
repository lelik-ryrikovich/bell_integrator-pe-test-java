import logic.Logic;

public class Main {
    public static void main(String[] args) {
        Logic.processTestFile( 1, "input1.testfile", "checkOutput1.testfile");
        Logic.processTestFile( 2, "input2.testfile", "checkOutput2.testfile");
    }
}