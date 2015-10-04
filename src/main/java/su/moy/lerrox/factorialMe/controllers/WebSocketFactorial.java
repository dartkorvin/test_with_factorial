package su.moy.lerrox.factorialMe.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import su.moy.lerrox.factorialMe.services.FactorialService;

import javax.annotation.Resource;

@Controller
public class WebSocketFactorial {

    @Resource(name = "simpleFactorialService")
    FactorialService factorialService;

    @RequestMapping(value = "/websocket/factorial", method = RequestMethod.GET)
    public String factorialMe(@RequestParam(value = "number", defaultValue = "0") Long number, Model model){

        if(number.compareTo(0l) < 0) model.addAttribute("message", "Отрицательные числа не cчитаются ж)");
        else {
            factorialService.factorialize(number);
            model.addAttribute("message", "Сейчас мы сделаем это...");
        }

        model.addAttribute("message", factorialService.getFactorials());

        return "factorial";
    }

    @MessageMapping("/factorialpoint")
    @SendTo("/topic/newfactorial")
    public String greeting() throws Exception {
        return factorialService.getFactorials();
    }

}
