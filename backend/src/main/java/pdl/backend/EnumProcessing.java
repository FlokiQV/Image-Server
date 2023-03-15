package pdl.backend;


public enum EnumProcessing {

    LUMI(0),
    FLOUMOY(1),
    FLOUGAUSS(2),
    HISTO(3),
    COLOR(4),
    SOBEL(5);
    private final int code;

    EnumProcessing(int code) {
        this.code = code;
    }
}