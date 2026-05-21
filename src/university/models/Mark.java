package university.models;

import java.io.Serializable;
import java.util.Objects;

public class Mark implements Serializable, Comparable<Mark> {

    private static final long serialVersionUID = 1L;

    public static final double MAX_ATTEST  = 30.0;
    public static final double MAX_FINAL   = 40.0;

    private double attest1;
    private double attest2;
    private double finalExam;

    public Mark(double attest1, double attest2, double finalExam) {
        this.attest1   = clampAttest(attest1);
        this.attest2   = clampAttest(attest2);
        this.finalExam = clampFinal(finalExam);
    }

    // ── helpers ──────────────────────────────────────────────────────────────
    private static double clampAttest(double v) {
        if (v < 0 || v > MAX_ATTEST)
            throw new IllegalArgumentException(
                    "Attestation score must be between 0 and " + (int) MAX_ATTEST + ", got: " + v);
        return v;
    }

    private static double clampFinal(double v) {
        if (v < 0 || v > MAX_FINAL)
            throw new IllegalArgumentException(
                    "Final exam score must be between 0 and " + (int) MAX_FINAL + ", got: " + v);
        return v;
    }

    
    public double getTotal() {
        return attest1 + attest2 + finalExam;
    }

    public String getGrade() {
        double total = getTotal();
        if (total >= 90) return "A";
        if (total >= 75) return "B";
        if (total >= 60) return "C";
        if (total >= 50) return "D";
        return "F";
    }

    public double getGpaPoints() {
        return switch (getGrade()) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default  -> 0.0;
        };
    }

    public boolean isFail() { return "F".equals(getGrade()); }

    @Override
    public int compareTo(Mark other) {
        return Double.compare(other.getTotal(), this.getTotal());
    }

    public double getAttest1()              { return attest1; }
    public void   setAttest1(double v)      { this.attest1   = clampAttest(v); }
    public double getAttest2()              { return attest2; }
    public void   setAttest2(double v)      { this.attest2   = clampAttest(v); }
    public double getFinalExam()            { return finalExam; }
    public void   setFinalExam(double v)    { this.finalExam = clampFinal(v); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        Mark mark = (Mark) o;
        return Double.compare(mark.attest1,   attest1)   == 0
            && Double.compare(mark.attest2,   attest2)   == 0
            && Double.compare(mark.finalExam, finalExam) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attest1, attest2, finalExam);
    }

    @Override
    public String toString() {
        return String.format("Mark{A1=%.1f, A2=%.1f, Final=%.1f → Total=%.1f (%s)}",
                attest1, attest2, finalExam, getTotal(), getGrade());
    }
}