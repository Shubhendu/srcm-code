package org.srcm.gems.regapp.web.util;


public enum CustomFieldType {
    TEXT_FIELD("Text Field"), 
    TEXT_AREA("Text Area"), 
    DROP_DOWN("Drop Down"), 
    RADIO_BUTTONS("Select One - Radio Button"), 
    CHECK_BOXES("Select Many - Check Boxes");
    
    private String label;
    

    private CustomFieldType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
    public int getValue() {
        return this.ordinal();
    }

}