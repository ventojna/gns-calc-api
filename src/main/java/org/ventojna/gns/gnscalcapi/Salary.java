package org.ventojna.gns.gnscalcapi;

import de.powerproject.lohnpap.pap.Lohnsteuer;
import de.powerproject.lohnpap.pap.LohnsteuerInterface;

import java.math.BigDecimal;

public class Salary {

/*
  Basic calculation 
  Bruttolohn
  - MINUS Lohnsteuer (14 - 45 %)
  - MINUS Kirchensteuer (8 - 9 % des Lohnsteuerbetrags)
  - MINUS Solidaritätszuschlag (5,5 % des Lohnsteuerbetrags)
  - MINUS Krankenkassenbeitrag (7,3 % + Zusatzbeitrag)
  - MINUS Beitrag zur Pflegeversicherung (1,175 % + evtl. Zuschlag)
  - MINUS Arbeitslosenversicherung (1,5 %)
  - MINUS Rentenversicherung (9,35 %)
  = Nettolohn
*/

    private long id;

    private final double netSalary;
    private final int grossSalaryPerYer;
    private final double churchTax;
    private final double incomeTax;
    private final double soliTax;
    private final int age;
    private final int taxClass;

    private final double healthInsuranceAddContribution;
    private final boolean privateHealthInsurance;
    private final boolean privtePensionEnsrance;

    private final double religionTaxPercent;
    private final int annualTaxAllowance;

    private final LohnsteuerInterface payrollTax;
    //private final SalaryTax2019 payrollTax;

    private final double socialSecurityContribution;
    private final double healthInsurance;
    private final double pensionEnsrance;
    private final double unemploymentEnsurance;
    private final double careEnsurance;

    public long getId() {
        return id;
    }

    public int getGrossSalaryPerYer() {
        return grossSalaryPerYer;
    }

    public double getChurchTax() {
        return churchTax;
    }

    public double getIncomeTax() {
        return incomeTax;
    }

    public double getSoliTax() {
        return soliTax;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public int getAge() {
        return age;
    }

    public int getTaxClass() {
        return taxClass;
    }

    public double getHealthInsuranceAddContribution() {
        return healthInsuranceAddContribution;
    }

    public boolean getPrivateHealthInsurance() {
        return privateHealthInsurance;
    }

    public boolean getPrivtePensionEnsrance() {
        return privtePensionEnsrance;
    }


    public int getAnnualTaxAllowance() {
        return annualTaxAllowance;
    }

    public double getsocialSecurityContribution() {
        return socialSecurityContribution;
    }

    public double getHealthInsurance() {
        return healthInsurance;
    }

    public double getPensionEnsrance() {
        return pensionEnsrance;
    }

    public double getUnemploymentEnsurance() {
        return unemploymentEnsurance;
    }

    public double getCareEnsurance() {
        return careEnsurance;
    }

    public Salary(
            long id,
            int grossSalaryPerYer,
            int age,
            int taxClass,
            double healthInsuranceAddContribution,
            boolean privateHealthInsurance,
            boolean privatePensionInsurance,
            double religionTaxPercent,
            int annualTaxAllowance) {

        //set constructor parameters
        this.id = id;
        this.grossSalaryPerYer = grossSalaryPerYer;
        this.age = age;
        this.taxClass = taxClass;
        this.healthInsuranceAddContribution = healthInsuranceAddContribution;
        this.privateHealthInsurance = privateHealthInsurance;
        this.privtePensionEnsrance = privatePensionInsurance;
        this.religionTaxPercent = religionTaxPercent;
        this.annualTaxAllowance = annualTaxAllowance;

        //calculate taxes
        this.payrollTax = Lohnsteuer.getInstance();
        this.calculateTaxes();

        //set taxes
        this.incomeTax = this.payrollTax.getLstlzz().doubleValue() / 100;
        this.soliTax = this.payrollTax.getSolzlzz().doubleValue() / 100;
        this.churchTax = calcChurchTax();

        //set social contributions
        SocialSecurityContributions socialSecContribution =
                new SocialSecurityContributions(this.grossSalaryPerYer, this.healthInsuranceAddContribution / 100);
        this.socialSecurityContribution = socialSecContribution.getSocialInsuranceCompleteAmount();
        this.healthInsurance = socialSecContribution.getHealthInsuranceAmount();
        this.pensionEnsrance = socialSecContribution.getPensionInsuranceAmount();
        this.unemploymentEnsurance = socialSecContribution.getUnemploymentInsuranceAmount();
        this.careEnsurance = socialSecContribution.getCareInsuranceAmount();

        //calculate net salary
        this.netSalary =
                this.grossSalaryPerYer
                        - this.incomeTax
                        - this.soliTax
                        - this.churchTax
                        - this.socialSecurityContribution;
    }

    private double calcChurchTax() {
        if (this.religionTaxPercent > 0 && this.religionTaxPercent < 100)
            return this.payrollTax.getBk().doubleValue() * religionTaxPercent /100 / 100;
        else
            return 0.0;
    }

    public void calculateTaxes() {

        //info:
        //https://github.com/MarcelLehmann/Lohnsteuer
        //https://www.bmf-steuerrechner.de/index.xhtml

        this.payrollTax.setJre4(new BigDecimal(this.grossSalaryPerYer * 100)); // Jahresgehalt Cent -> Euro
        this.payrollTax.setKrv(privateHealthInsurance ? 0 : 1);                    // Nicht privat Rentenversichert = 0
        this.payrollTax.setKvz(new BigDecimal(this.healthInsuranceAddContribution));
        this.payrollTax.setLzz(1); //Lohnzahlungszeitraum: 1 = Jahr  2 = Monat  3 = Woche  4 = Tag
        this.payrollTax.setPkv(this.privtePensionEnsrance ? 0 : 1); // Nicht privat Versichert = 0
        this.payrollTax.setPvs(0); //1, wenn Besonderheiten der sozialen Pflegeversicherung in Sachsen
        this.payrollTax.setPvz(1); // 1, wenn der Arbeitnehmer den Zuschlag zur sozialen Pflegeversicherung zu zahlen hat
        this.payrollTax.setR(religionTaxPercent > 1 ? 1:0); //Religionszugehörigkeit 1 = rkat.
        this.payrollTax.setRe4(new BigDecimal(this.grossSalaryPerYer * 100));
        this.payrollTax.setStkl(this.taxClass); // Steuerklasse
        this.payrollTax.setJfreib(new BigDecimal(this.annualTaxAllowance));

        this.payrollTax.setAf(0);
        this.payrollTax.setAjahr(0);
        this.payrollTax.setAlter1(0);
        this.payrollTax.setEntsch(new BigDecimal("0.0"));
        this.payrollTax.setF(1.000);
        this.payrollTax.setJhinzu(new BigDecimal("0.0"));
        this.payrollTax.setJre4ent(new BigDecimal("0.0"));
        this.payrollTax.setJvbez(new BigDecimal("0.0"));
        this.payrollTax.setLzzfreib(new BigDecimal("0.0"));
        this.payrollTax.setLzzhinzu(new BigDecimal("0.0"));
        this.payrollTax.setPkpv(new BigDecimal("0.0"));
        this.payrollTax.setSonstb(new BigDecimal("0.0"));
        this.payrollTax.setSonstent(new BigDecimal("0.0"));
        this.payrollTax.setSterbe(new BigDecimal("0.0"));
        this.payrollTax.setVbez(new BigDecimal("0.0"));
        this.payrollTax.setVbezm(new BigDecimal("0.0"));
        this.payrollTax.setVbezs(new BigDecimal("0.0"));
        this.payrollTax.setVbs(new BigDecimal("0.0"));
        this.payrollTax.setVjahr(2018);
        this.payrollTax.setVmt(new BigDecimal("0.0"));
        this.payrollTax.setZkf(new BigDecimal("0.0"));
        this.payrollTax.setZmvb(0);

        this.payrollTax.main();
    }
}

