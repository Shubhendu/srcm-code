package org.srcm.gems.regapp.web.util;

public enum DonationAccountType {
	 NO_PAYMENT("No Payment"), 
	    SRCM("SRCM"), 
	    SMSF("SMSF");
	    
	    private String label;
	    

	    private DonationAccountType(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }
	    
	    public int getValue() {
	        return this.ordinal();
	    }

}
