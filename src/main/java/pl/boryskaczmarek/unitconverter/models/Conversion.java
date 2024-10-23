package pl.boryskaczmarek.unitconverter.models;

public class Conversion {
    private String measure;
    private Unit from;
    private Unit to;
    private double value;
    private String result;

    // Constructors
    public Conversion() {
        this.from = new Unit();
        this.to = new Unit();
    }
    public Conversion(String measure, Unit from, Unit to, double value, String result) {
        this.measure = measure;
        this.from = from;
        this.to = to;
        this.value = value;
        this.result = result;
    }

    // Getters and Setters
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Unit getFrom() {
        return from;
    }

    public void setFrom(Unit from) {
        this.from = from;
    }

    public Unit getTo() {
        return to;
    }

    public void setTo(Unit to) {
        this.to = to;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Conversion{" +
                "measure='" + measure + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", value=" + value +
                ", result='" + result + '\'' +
                '}';
    }
}
