import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sample {
    public static void main(String[] args) {
        String regex = "^\\d{9}$";

        Pattern pattern = Pattern.compile(regex);
        String input="939811422";
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.out.println("Input matches the regex.");
        } else {
            System.out.println("Input does not match the regex.");
        }


    }
}