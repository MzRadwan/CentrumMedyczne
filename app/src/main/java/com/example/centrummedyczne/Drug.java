package com.example.centrummedyczne;

public class Drug {
    private String active_substance, dose, trade_name, form;

    public Drug(){}

    public Drug(String active_substance, String dose, String trade_name, String form){
        this.active_substance = active_substance;
        this.dose = dose;
        this.trade_name = trade_name;
        this.form = form;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getTrade_name() {
        return trade_name;
    }

    public void setTrade_name(String trade_name) {
        this.trade_name = trade_name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getActive_substance() {
        return active_substance;
    }

    public void setActive_substance(String active_substance) {
        this.active_substance = active_substance;
    }
}
