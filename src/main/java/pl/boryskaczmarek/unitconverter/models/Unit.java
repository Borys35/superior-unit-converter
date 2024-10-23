package pl.boryskaczmarek.unitconverter.models;

public class Unit {
    private String abbr;
    private String measure;
    private String system;
    private String singular;
    private String plural;

    // Constructors
    public Unit() {}
    public Unit(String abbr, String measure, String system, String singular, String plural) {
        this.abbr = abbr;
        this.measure = measure;
        this.system = system;
        this.singular = singular;
        this.plural = plural;
    }

    // Getters and Setters
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "abbr='" + abbr + '\'' +
                ", measure='" + measure + '\'' +
                ", system='" + system + '\'' +
                ", singular='" + singular + '\'' +
                ", plural='" + plural + '\'' +
                '}';
    }
}
