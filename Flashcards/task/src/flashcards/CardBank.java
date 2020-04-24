package flashcards;

import java.util.*;

public class CardBank implements Iterable<Card> {
    private ArrayList<Card> cards = new ArrayList<>();
    private Map<String, String> descs = new HashMap<>();
    private Map<String, String> defs = new HashMap<>();
    private Map<String, Integer> misses = new HashMap<>();
    private ArrayList<String> questions = new ArrayList<>();
    private Random rand = new Random(new Date().getTime());
    private int mostMisses = 0;

    CardBank() {}
    CardBank(Card[] cards) {
        addAll(cards);
    }
    private int addAll(Card[] cards) {
        int cardsAdded = 0;
        for (Card card : cards) {
            cardsAdded += addCard(card);
        }
        return cardsAdded;
    }
    public boolean containsDesc(String desc) {
        return descs.containsKey(desc);
    }
    public boolean containsDef(String def) {
        return defs.containsKey(def);
    }
    public String getByDef(String def) {
        return defs.get(def);
    }
    public String getByDesc(String desc) {
        return descs.get(desc);
    }
    public int addCard(Card newCard) {
        String desc = newCard.getCard();
        String def = newCard.getDef();
        if (containsDesc(desc) && !containsDef(def)) {
            removeCard(desc);
        }
        if (!containsDesc(desc) && !containsDef(def)) {
            descs.put(desc, def);
            defs.put(def, desc);
            cards.add(newCard);
            questions.add(desc);
            return 1;
        }
        return 0;
    }
    public int removeCard(String desc) {
        if (containsDesc(desc)) {
            for (Card card : cards) {
                if(card.getDesc().equals(desc)) {
                    cards.remove(card);
                    break;
                }
            }
            defs.remove(descs.get(desc));
            descs.remove(desc);
            questions.remove(desc);
            misses.remove(desc);
            return 1;
        }
        return 0;
    }
    public Card getRandom() {
        if (questions.size() == 0) {
            questions.addAll(descs.keySet());
        }
        int index = rand.nextInt(questions.size());
        String desc = questions.get(index);
        Card result = new Card(desc, descs.get(desc));
        questions.remove(index);
        return result;
    }
    public int appendToMisses(String key, int value) {
        misses.merge(key, value, (a, b) -> a + b);
        mostMisses = misses.get(key) > mostMisses ? misses.get(key) : mostMisses;
        return misses.get(key);
    }
    public int addMiss(String key) {
        return appendToMisses(key, 1);
    }
    public int getMisses(String key) {
        return misses.get(key) == null ? 0 : misses.get(key);
    }
    public int setMisses(String key, int value) {
        misses.put(key, value);
        mostMisses = misses.get(key) > mostMisses ? misses.get(key) : mostMisses;
        return misses.get(key);
    }
    public String[] getMostMissed() {
        return misses.keySet().stream().filter((val) -> misses.get(val) == mostMisses).toArray(String[]::new);
    }
    public int getMaxMisses() {
        return mostMisses;
    }
    public void resetStats() {
        misses.clear();
        mostMisses = 0;
    }
    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
