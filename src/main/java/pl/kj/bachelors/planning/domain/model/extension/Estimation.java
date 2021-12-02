package pl.kj.bachelors.planning.domain.model.extension;

public enum Estimation {
    S(1),
    M(2),
    L(3),
    XL(5),
    XXL(8),
    XXXL(13),
    XXXXL(21),
    OMIT(0),
    BREAK(0);

    public final int value;

    private Estimation(int value) {
        this.value = value;
    }
}
