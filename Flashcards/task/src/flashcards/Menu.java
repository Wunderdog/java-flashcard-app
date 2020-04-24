package flashcards;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import static flashcards.CardParser.*;

public class Menu {
    final String utf8 = StandardCharsets.UTF_8.name();
    CardBank cards = new CardBank();
    Scanner scan;

    public Menu(Scanner scan) {
        this.scan = scan;
    }
    public void add(int num_cards) {
        String card = "";
        String def = "";
        boolean exists;
        for (int i = 0; i < num_cards; i++) {
            exists = false;
            System.out.println(String.format("The card"));

            card = scan.nextLine().trim().replaceAll("[ +|\n]", " ");
            if (cards.containsDesc(card)) {
                exists = true;
                System.out.println(String.format("The card \"%s\" already exists.", card));
            }
            if (exists == false) {
                System.out.println(String.format("The definition of the card:"));
                def = scan.nextLine().trim().replaceAll("[ +|\n]", " ");
                if (cards.containsDef(def)) {
                    exists = true;
                    System.out.println(String.format("The definition \"%s\" already exists.", def));
                }
            }
            if (exists == false) {
                cards.addCard(new Card(card, def));
                System.out.println(String.format("The pair (\"%s\":\"%s\") has been added.\n", card, def));
            }
        }
    }
    public void add() {add(1);}
    public void remove() {
        System.out.println("The card:");
        remove(scan.nextLine().trim().replaceAll("[ +|\n]", " "));
    }
    public void remove(String desc) {
        if (cards.containsDesc(desc)) {
            cards.removeCard(desc);
            System.out.println("The card has been removed.\n");
        } else {
            System.out.println(String.format("Can't remove \"%s\": there is no such card.\n", desc));
        }
    }
    public void ask() {
        boolean valid = false;
        int numQuestions = 0;
        do {
        try {
            System.out.println("How many times to ask?");
            numQuestions = Integer.parseInt(scan.nextLine().trim());
            valid = true;
        } catch (NumberFormatException nme) {
            System.out.println("Invalid number. Try again:\n");
        }
        } while(valid == false);
        ask(numQuestions);
    }
    public void ask(int numQuestions) {
        if (numQuestions <= 0) {
            return;
        }
        String ans;
        String end;
        Card card;
        for (int i = 0; i < numQuestions; i++) {
            card = cards.getRandom();
            System.out.println(String.format("Print the definition of \"%s\":", card.getCard()));
            ans = scan.nextLine().trim().replaceAll("[ +|\n]", " ");
            if (card.getDef().toLowerCase().equals(ans.toLowerCase())) {
                System.out.println("Correct answer.\n");
            } else {
                cards.addMiss(card.getDesc());
                end = cards.containsDef(ans) ?
                        String.format(", you\'ve just written the definition of \"%s\".", cards.getByDef(ans))
                        : ".";
                System.out.println(String.format("Wrong answer. The correct one is \"%s\"%s\n", card.getDef(), end));
            }
        }

    }
    public void exportCards() {
        System.out.println("File name:");
        exportCards(scan.nextLine());
    }
    public void exportCards(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            int numSaved = 0;
            for (Card card : cards) {
                writer.printf("\"%s\",\"%s\",%d\n",
                        escapeCsv(card.getDesc()),
                        escapeCsv(card.getDef()),
                        cards.getMisses(card.getDesc()));
                numSaved++;
            }
            System.out.println(String.format("%d cards have been saved.\n", numSaved));
        } catch (IOException ioe) {
            System.out.println("Error: Unable to  write to file.\n");
        }
    }
    public void importCards() {
        System.out.println("File name:");
        importCards(scan.nextLine());
    }
    public void importCards(String fileName) {

        try (Scanner reader = new Scanner(new File(fileName))) {
            int numAdded = 0;
            Card importedCard;
            String next;
            while (reader.hasNext()) {
                next = reader.nextLine();
                importedCard = parseCard(next, cards);
                if (importedCard != null) {
                    cards.addCard(importedCard);
                    numAdded += 1;
                }
            }
            System.out.println(String.format("%d cards have been loaded.\n", numAdded));
        } catch (FileNotFoundException ioe) {
            System.out.println("File not found.\n");
        }
    }
    public void log(ByteArrayOutputStream baos) {
        System.out.println("File name:");
        String fileName = scan.nextLine();
        String output = "";
        try (PrintWriter writer = new PrintWriter(fileName)) {
            output = baos.toString(utf8);
            int numSaved = 0;
            writer.printf(output);
            System.out.println(String.format("The log has been saved.\n"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Error: Unable to  write to file.\n");
        }
    }
    public void printStats() {
        String[] mostMissed = cards.getMostMissed();
        if (mostMissed.length == 0) {
            System.out.println("There are no cards with errors.\n");
        } else {
            String miss_string = Arrays.asList(mostMissed).stream().collect(Collectors.joining("\", \"", "\"", "\""));
            String out = String.format("The hardest card%s %s. You have %d errors answering %s.\n",
                    mostMissed.length > 1 ? "s are" : " is",
                    miss_string, cards.getMaxMisses(),
                    mostMissed.length > 1 ? "them": "it");
            System.out.println(out);
        }
    }
    public void resetStats() {
        cards.resetStats();
        System.out.println("Card statistics have been reset.\n");
    }
}
