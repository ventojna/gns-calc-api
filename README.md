# gns-calc-api
German net salray calucaltion REST API 

[Lohnsteuer from MarcelLehmann ](https://github.com/MarcelLehmann/Lohnsteuer) is used to calculate German salry taxes.



To build the projekct you need to add Lohnsteuer.jar from MarcelLehmann to your local maven repository:
* Download [Lohnsteuer.jar](https://github.com/MarcelLehmann/Lohnsteuer/raw/master/LohnPapGenerator/lohnsteuer.jar)
* Add the jar to your local repository:
```
  mvn install:install-file 
    -Dfile=<path to the jar e.g. c:\tmp\lohnsteuer.jar> 
    -DgroupId=de.powerproject.lohnpap.pap 
    -DartifactId=lohnsteuer 
    -Dversion=2019 
    -Dpackaging=jar 
    -DgeneratePom=true
```
