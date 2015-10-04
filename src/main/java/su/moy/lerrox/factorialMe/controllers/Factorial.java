package su.moy.lerrox.factorialMe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import su.moy.lerrox.factorialMe.services.FactorialService;

import javax.annotation.Resource;

@RestController
public class Factorial {

    @Resource(name = "simpleFactorialService")
    FactorialService factorialService;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getResult(){
        return factorialService.getFactorials();
    }

    @RequestMapping(value = "/factorial", method = RequestMethod.GET)
    public String factorialMe(@RequestParam(value = "number", defaultValue = "0") Long number){
        if(number.compareTo(0l) < 0) return "Отрицательные числа не cчитаются ж)";
        factorialService.factorialize(number);
        return "Сейчас мы сделаем это...";
    }

}
