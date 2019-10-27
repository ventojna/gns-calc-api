package org.ventojna.gns.gnscalcapi;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GnsCalcApiController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/gnCalc")
    public Salary grossNet() {
        return new Salary(
                1,
                82000,
                32,
                1,
                1.1,
                false,
                false,
                8.0,
                0
        );
    }

    @RequestMapping("/gnsCalc")
    public ArrayList<Salary> grossNets(
            @RequestParam(value = "start", defaultValue = "50000") int start,
            @RequestParam(value = "end", defaultValue = "100000") int end,
            @RequestParam(value = "interval", defaultValue = "1000") int interval,
            @RequestParam(value = "age", defaultValue = "30") int age,
            @RequestParam(value = "taxClass", defaultValue = "1") int taxClass,
            @RequestParam(value = "hiAddCon", defaultValue = "1.1") double hiAddCon,
            @RequestParam(value = "privateHI", defaultValue = "false") boolean privateHI,
            @RequestParam(value = "privatePI", defaultValue = "false") boolean privatePI,
            @RequestParam(value = "religionTaxPercent", defaultValue = "8") double religion,
            @RequestParam(value = "annualTaxAllow", defaultValue = "0") int annualTaxAllow) {

        ArrayList<Salary> salariesList = new ArrayList<>();

        for (int i = start; i <= end; i += interval) {
            salariesList.add(new Salary(
                    counter.incrementAndGet(),
                    i,
                    age,
                    taxClass,
                    hiAddCon,
                    privateHI,
                    privatePI,
                    religion,
                    annualTaxAllow)
            );
        }

        counter.set(0);

        return salariesList;
    }

}