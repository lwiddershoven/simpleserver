package net.leonw.simpleserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SimpleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleServerApplication.class, args);
    }

}

@Data
@AllArgsConstructor
class Order {
    private String id;
    private String data;
}

@Data
@AllArgsConstructor
class OrderLine {
    private String id;
    private String data;
}

@Data
@AllArgsConstructor
class Product {
    private String id;
    private String data;
}

@Slf4j
@RestController
class Controller {
    @GetMapping("/orders/{id}")
    public Order orderById(@PathVariable("id") String id) {
        sleep(1);
        return new Order(id, id);
    }

    @GetMapping("/orders/")
    public List<Order> searchOrders(@RequestParam("tag") String tag) {
        if ("open".equals(tag)) {
            sleep(1);
            return Stream.of("a", "b", "c")
                    .map(id -> new Order(id, id))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Tag " + tag + " is not supported. Supported is: 'open'");
        }
    }

    @GetMapping("/orderlines/{id}")
    public OrderLine orderLineById(@PathVariable("id") String id) {
        sleep(1);
        return new OrderLine(id, id);
    }

    @GetMapping("/products/{id}")
    public Product productById(@PathVariable("id") String id) {
        sleep(1);
        return new Product(id, id);
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.info("Ignored interrupted exception");
        }
    }
}
