package com.koconr.smspam.params;

public enum Params {
    BASE_URL("https://smsspamaway.ew.r.appspot.com/"),
    CHECK_IF_SPAM("isspam"),
    SEND_TO_DATABASE("isspam/feedback/");

    public final String label;

    private Params(String label) {
        this.label = label;
    }

    public static String getUrl(Params suffix) throws Exception {
        if (suffix == Params.BASE_URL) {
            throw new Exception("You can't create url with suffix same as Params.BASE_URL!");
        }
        return Params.BASE_URL.label + suffix.label;
    }
}
