package com.example.paintthetown491;

public class ProfileSearchItem {
    private String usr_fn, usr_ln, usr_ph;

    public ProfileSearchItem (String fn, String ln, String ph)
    {
        usr_fn = fn;
        usr_ln = ln;
        usr_ph = ph;
    }

    public String getUsr_fn() { return usr_fn; }

    public String getUsr_ln() { return usr_ln; }

    public String getUsr_ph() { return usr_ph; }
}
