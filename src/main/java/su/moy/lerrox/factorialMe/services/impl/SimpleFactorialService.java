package su.moy.lerrox.factorialMe.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import su.moy.lerrox.factorialMe.services.FactorialService;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("simpleFactorialService")
public class SimpleFactorialService implements FactorialService {

    @Autowired
    private SimpMessagingTemplate template;
    private AtomicInteger counter;
    private Map<Integer, Optional<String>> factorials;
    private ExecutorService executorService;


    @PostConstruct
    public void init() {
        counter = new AtomicInteger(0);
        factorials = new ConcurrentSkipListMap<Integer, Optional<String>>();
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void factorialize(Long number) {
        Integer count = counter.incrementAndGet();
        factorials.put(count, Optional.empty());
        this.template.convertAndSend("/topic/newfactorial", getFactorials());
        executorService.submit(() -> {
            BigInteger factorial = count(number);
            factorials.put(count, Optional.of(factorial.toString()));
            this.template.convertAndSend("/topic/newfactorial", getFactorials());
        });
    }

    @Override
    public String getFactorials() {
        return factorials.entrySet()
                .stream()
                .map(item -> item.getValue().orElseGet(() -> "[please wait, counting]"))
                .collect(Collectors.joining(", "));
    }

    protected BigInteger count(long number) {

        BigInteger fact = new BigInteger("1");
        BigInteger inc = new BigInteger("1");
        long c;
        try {
            for (c = 1; c <= number; c++) {
                fact = fact.multiply(inc);
                inc = inc.add(BigInteger.ONE);
            }
        } catch (Throwable e) {
            int fds = 132;
        }


        return fact;
    }
}
