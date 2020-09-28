package net.leonw.simpleserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
class Order { private String id; private List<String> orderLineIds;}

@Data
@AllArgsConstructor
class OrderLine {
    private String id, productId;
}

@Data
@AllArgsConstructor
class Product {
    private String id, data;
}

@Slf4j
@RestController
class Controller {
    private static final int WAIT_TIME_SECONDS = 2;
    private static final List<String> ORDER_LINE_IDS = List.of("a-1", "a-2", "b-1");

    @GetMapping("/orders/{id}")
    public Order orderById(@PathVariable("id") String id) {
        sleep(WAIT_TIME_SECONDS);
        // throw new ResponseStatusException(  HttpStatus.NOT_FOUND, "entity not found" );
        return new Order(id, ORDER_LINE_IDS);
    }

    @GetMapping("/orders/")
    public List<Order> searchOrders(@RequestParam("tag") String tag) {
        if ("open".equals(tag)) {
            sleep(WAIT_TIME_SECONDS);
            return Stream.of("a", "b", "c")
                    .map(id -> new Order(id, ORDER_LINE_IDS))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Tag " + tag + " is not supported. Supported is: 'open'");
        }
    }

    @GetMapping("/orderlines/{id}")
    public OrderLine orderLineById(@PathVariable("id") String id) {
        sleep(WAIT_TIME_SECONDS);
        return new OrderLine(id, id);
    }

    @GetMapping("/products/{id}")
    public Product productById(@PathVariable("id") String id) {
        sleep(WAIT_TIME_SECONDS);
        return new Product(id, id);
    }

    @GetMapping("/notFound")
    public void notFound() {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.info("Ignored interrupted exception");
        }
    }
}
