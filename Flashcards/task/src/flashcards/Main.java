package flashcards;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String utf8 = StandardCharsets.UTF_8.name();
        boolean exportIsSet = false;
        String exportFilename = "";

        try {
            PrintStream logStream = new PrintStream(baos, true, utf8);
            TeePrintStream tps = new TeePrintStream(logStream, System.out, System.in);
            CustomInputFilter dbis = new CustomInputFilter(System.in, logStream);
            System.setOut(tps);
            System.setIn(dbis);
            Scanner scan = new Scanner(dbis);
            Menu menu = new Menu(scan);
            if (args.length > 1) {
                for (int i = 0; i < args.length - args.length % 2; i += 2) {
                    if ("-import".equals(args[i])) {
                        menu.importCards(args[i + 1]);
                    }
                    if ("-export".equals(args[i])) {
                        exportIsSet = true;
                        exportFilename = args[i + 1];
                    }
                }
            }
            String menuOptions = "Input the action (add, remove, import, export, ask, exit, "
                    + "log, hardest card, reset stats):";
            System.out.println(menuOptions);
            String input = scan.nextLine().toLowerCase().trim();
            while (!"exit".equals(input)) {
                if ("exit".equals(input)) {
                    break;
                } else if ("add".equals(input)) {
                    menu.add();
                } else if ("remove".equals(input)) {
                    menu.remove();
                } else if ("import".equals(input)) {
                    menu.importCards();
                } else if ("export".equals(input)) {
                    menu.exportCards();
                } else if ("ask".equals(input)) {
                    menu.ask();
                } else if ("log".equals(input)) {
                    menu.log(baos);
                } else if ("hardest card".equals(input)) {
                    menu.printStats();
                } else if ("reset stats".equals(input)) {
                    menu.resetStats();
                } else {
                    System.out.println("That's not a valid option. Try again.");
                }
                System.out.println(menuOptions);
                input = scan.nextLine();
            }
            System.out.println("Bye bye!");
            if (exportIsSet) {
                menu.exportCards(exportFilename);
            }
            logStream.close();
            tps.close();
        } catch (UnsupportedEncodingException uee) {
            System.out.println("Unsupported Encoding Exception");
        } catch (IOException ioe) {
            System.out.println("IOException");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
