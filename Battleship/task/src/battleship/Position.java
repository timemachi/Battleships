package battleship;

public class Position {
    private char ROW;
    private int COL;

    Position(char ROW, int COL) {
        this.ROW = ROW;
        this.COL = COL;
    }

    public int getRow() {
        return ROW - 'A';
    }

    public int getCOL() {
        return COL - 1;
    }

    public static Position toPoint(String s) {
        assert s.length() >= 2;
        char c = s.charAt(0);
        int i = Integer.parseInt(s.substring(1));
        return new Position(c, i);
    }
}
