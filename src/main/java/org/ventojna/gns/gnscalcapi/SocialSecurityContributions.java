package org.ventojna.gns.gnscalcapi;

public class SocialSecurityContributions {

    //insurance calculation percentages
    private final double pensionInsurancePercent = 0.0935;
    private final double unemploymentInsurancePercent = 0.015;
    private final double healthInsurancePercent = 0.073;
    private final double careInsurancePercent = 0.01275 + 0.0025;
    private double healthInsuranceCompletePercent;

    // assessment ceilings
    private final double pensionInsuranceMax = 78000.00;
    private final double unemploymentInsuranceAssessmentCeiling = 78000.00;
    private final double healthInsurancePercentAssessmentCeiling = 53100.00;
    private final double careInsurancePercentAssessmentCeiling = 53100.00;

    //insurance amounts
    private double pensionInsuranceAmount;
    private double unemploymentInsuranceAmount;
    private double healthInsuranceAmount;
    private double careInsuranceAmount;
    private double socialInsuranceCompleteAmount;


    public double getPensionInsuranceAmount() {
        return pensionInsuranceAmount;
    }

    public double getUnemploymentInsuranceAmount() {
        return unemploymentInsuranceAmount;
    }

    public double getHealthInsuranceAmount() {
        return healthInsuranceAmount;
    }

    public double getCareInsuranceAmount() {
        return careInsuranceAmount;
    }

    public double getSocialInsuranceCompleteAmount() {
        return socialInsuranceCompleteAmount;
    }

    SocialSecurityContributions(double gross, double healthInsurenceCoPaymentPercent) {
        this.healthInsuranceCompletePercent = healthInsurancePercent + healthInsurenceCoPaymentPercent;
        calcAllContribution(gross);
    }

    private void calcAllContribution(double gross) {
        this.pensionInsuranceAmount =
                calcGenericInsurance(gross, pensionInsurancePercent, pensionInsuranceMax);
        this.unemploymentInsuranceAmount =
                calcGenericInsurance(gross, unemploymentInsurancePercent, unemploymentInsuranceAssessmentCeiling);
        this.healthInsuranceAmount =
                calcGenericInsurance(gross, healthInsuranceCompletePercent, healthInsurancePercentAssessmentCeiling);
        this.careInsuranceAmount =
                calcGenericInsurance(gross, careInsurancePercent, careInsurancePercentAssessmentCeiling);

        this.socialInsuranceCompleteAmount =
                pensionInsuranceAmount + unemploymentInsuranceAmount + healthInsuranceAmount + careInsuranceAmount;
    }

    private double calcGenericInsurance(double gross, double percentage, double assessmentCeiling) {
        double amount = gross;
        if (gross > assessmentCeiling) amount = assessmentCeiling;
        amount = amount * percentage;
        return amount;
    }
}
