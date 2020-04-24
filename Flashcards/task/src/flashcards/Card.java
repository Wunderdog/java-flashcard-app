package flashcards;

public class Card implements Comparable<Card>{
    private String desc;
    private String def;

    Card(String card, String def) {
        this.desc = card;
        this.def = def;

    }
    public String getCard() {
        return desc;
    }
    public String getDesc() { return desc; }
    public String getDef() {
        return def;
    }
    public String toString() {
        String output = String.format("Description:\n"
                        + "%s\n"
                        + "Definition:\n"
                        + "%s", desc, def);
        return output;
    }
    @Override
    public boolean equals(Object obj) {
        Card other = (Card) obj;
        if (getDesc() != other.getDesc())
            return false;
        return true;
    }
    @Override
    public int compareTo(Card c) {
        if (getDesc() == c.getDesc()) {
            return 0;
        } else {
            return getDesc().compareTo(c.getDesc());
        }
    }
}
