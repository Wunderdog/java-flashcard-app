package flashcards;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CardParser {

    public static String escapeCsv(String input) {
        return input.replaceAll(",", "\\,")
                .replaceAll(" +", " ")
                .replaceAll("\"", "\\\"");
    }
    public static String unescapeCsv(String output) {
        return output.trim().replaceAll("^( *\")|(\" *)$", "")
                .replaceAll("\\\\,",",")
                .replaceAll("\\\\\"", "\"").trim();
    }
    public static Card parseCard(String line, CardBank cards) {
        String matchStr = "((?<=((?<!\\\\)\")) *, *)";
        String matchStr2 = "((?<!(\")) *, *)";
        String matchStr3 = "(?:(?<!\\\\),)\\s*(?:\")?(\\d+)(?:\")?$";
        String desc = "";
        String def = "";
        int misses = 0;

        Matcher match3 = Pattern.compile(matchStr3).matcher(line);

        if (line.isEmpty()) {return null;}
        if (match3.find()) {
            try {
                misses = Integer.parseInt(match3.group(1));
            } catch(NumberFormatException nfe) {
                System.out.println("Error reading number of misses.");
            }
            line = line.substring(0, match3.start());
        }
        Matcher match = Pattern.compile(matchStr).matcher(line);
        Matcher match2 = Pattern.compile(matchStr2).matcher(line);
        if (match.find()) {
            desc = unescapeCsv(line.substring(0, match.start()));
            def = unescapeCsv(line.substring(match.end()));
        } else if (match2.find()) {
            desc = unescapeCsv(line.substring(0, match2.start()));
            def = unescapeCsv(line.substring(match2.end()));
        } else {
            return null;
        }
        cards.appendToMisses(desc, misses);
        return new Card(desc, def);
    }
}
