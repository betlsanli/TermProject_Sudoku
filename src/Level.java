public enum Level {
    EASY(20),
    MEDIUM(40),
    HARD(60);

    public final int level; // Number of empty boxes
    Level(int level){
        this.level =level;
    }
}
