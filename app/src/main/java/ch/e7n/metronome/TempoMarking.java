package ch.e7n.metronome;

public enum TempoMarking {
    larghissimo("Larghissimo", 1, 20, 20),
    grave("Grave", 20, 40, 40),
    lento("Lento", 40, 45, 45),
    largo("Largo", 45, 50, 50),
    larghetto("Larghetto", 50, 55, 55),
    adagio("Adagio", 55, 60, 65),
    adagietto("Adagietto", 65, 65, 69),
    andantino("Andantino", 78, 80, 83),
    andante("Andante", 84, 90, 90),
    andateModerato("Andante moderato", 90, 100, 100),
    marciaModerato("Marcia moderato", 83, 85, 85),
    moderato("Moderato", 100, 110, 112),
    allegroModerato("Allegro Moderato", 112, 115, 116),
    allegretto("Allegretto", 116, 120, 120),
    allegro("Allegro", 120, 140, 160),
    vivace("Vivace", 132, 140, 140),
    vivacissimo("Vivacissimo", 140, 150, 150),
    allegrissimo("Allegrissimo", 168, 170, 177),
    presto("Presto", 180, 180, 200),
    prestissimo("Prestissimo", 200, 200, 480),
    unknown("---", 1, 90, 480);

    private final String name;
    private final int minBeat;
    private final int averageBeat;
    private final int maxBeat;

    public String getName() {
        return name;
    }

    public int getMinBeat() {
        return minBeat;
    }

    public int getAverageBeat() {
        return averageBeat;
    }

    public int getMaxBeat() {
        return maxBeat;
    }

    TempoMarking(String name, int minBeat, int averageBeat, int maxBeat) {
        this.name = name;
        this.minBeat = minBeat;
        this.averageBeat = averageBeat;
        this.maxBeat = maxBeat;
    }

    @Override public String toString() {
        return name;
    }
}
