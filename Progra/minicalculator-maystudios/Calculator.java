import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Calculator {

    private static class Operation {
        BiFunction<Double, Double, Double> function;
        String errorMessage;

        Operation(BiFunction<Double, Double, Double> function, String errorMessage) {
            this.function = function;
            this.errorMessage = errorMessage;
        }
    }

    private static final Map<String, Operation> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put("+", new Operation((a, b) -> a + b, null));
        OPERATORS.put("-", new Operation((a, b) -> a - b, null));
        OPERATORS.put("*", new Operation((a, b) -> a * b, null));
        OPERATORS.put("/", new Operation((a, b) -> b == 0 ? null : a / b, "division by zero"));
        // Add more operators here as needed
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("too few arguments, expected: operand operator operand");
            return;
        } else if (args.length > 3) {
            System.out.println("too many arguments, expected: operand operator operand");
            return;
        }

        String operator = args[1];
        Double operand1 = parseOperand(args[0], "first");
        Double operand2 = parseOperand(args[2], "second");

        if (operand1 == null || operand2 == null) {
            return;
        }

        calculateAndPrintResult(operand1, operand2, operator);
    }

    private static Double parseOperand(String operand, String position) {
        try {
            return Double.parseDouble(operand);
        } catch (NumberFormatException e) {
            System.out.println(position + " operand is no number: " + operand);
            return null;
        }
    }

    private static void calculateAndPrintResult(double operand1, double operand2, String operator) {
        if (!OPERATORS.containsKey(operator)) {
            System.out.println("unknown operator " + operator + ", supported operators: " + OPERATORS.keySet());
            return;
        }

        Operation operation = OPERATORS.get(operator);
        Double result = operation.function.apply(operand1, operand2);

        if (result == null) {
            System.out.println(operation.errorMessage != null ? operation.errorMessage : "Error in calculation");
        } else {
            System.out.println(result);
        }
    }
}
