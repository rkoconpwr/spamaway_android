package com.koconr.smspam.model;

public class SpamProbabilityModel {
    private float spamPropability;

    public SpamProbabilityModel() {}

    public boolean isSpam() {
        return  spamPropability > 0.5;
    }
}
