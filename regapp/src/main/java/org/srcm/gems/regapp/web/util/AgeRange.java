package org.srcm.gems.regapp.web.util;


public enum AgeRange {
    UNDER_5("0 - 5"), 
    BETWEEN_6AND11("6 - 11"), 
    BETWEEN_12AND16("12 - 16"), 
    BETWEEN_17AND30("17 - 30"), 
    BETWEEN_31AND44("31 - 44"),
    BETWEEN_45AND59("45 - 59"),
    ABOVE_60("60 + Yrs");
    
    private String label;
    

    private AgeRange(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public int getValue() {
        return this.ordinal();
    }

}