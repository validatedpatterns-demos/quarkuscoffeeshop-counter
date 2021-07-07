package io.quarkuscoffeeshop.testing;

import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkuscoffeeshop.counter.domain.*;
import io.quarkuscoffeeshop.counter.domain.commands.CommandItem;
import io.quarkuscoffeeshop.counter.domain.commands.PlaceOrderCommand;
import io.quarkuscoffeeshop.counter.domain.events.OrderCreatedEvent;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderEventResult;
import io.quarkuscoffeeshop.counter.domain.valueobjects.OrderTicket;
import io.quarkuscoffeeshop.counter.domain.valueobjects.TicketUp;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TestUtil {

    public static PlaceOrderCommand stubPlaceOrderCommand(final String id) {
        return new PlaceOrderCommand(
                id,
                OrderSource.WEB,
                Location.ATLANTA,
                UUID.randomUUID().toString(),
                Optional.of(stubSingleBaristaItem()),
                Optional.empty());
    }

    public static PlaceOrderCommand stubPlaceOrderCommand() {
        return stubPlaceOrderCommand(UUID.randomUUID().toString());
    };

    private static List<CommandItem> stubSingleBaristaItem() {
        return Arrays.asList(new CommandItem(Item.COFFEE_BLACK, "Foo", BigDecimal.valueOf(3.25)));
    }

    public static Order stubOrder() {
        Order order = new Order(
                UUID.randomUUID().toString(),
                OrderSource.COUNTER,
                Location.RALEIGH,
                UUID.randomUUID().toString(),
                Instant.now(),
                OrderStatus.PLACED,
                null,
                null);
        order.addBaristaLineItem(new LineItem(Item.COFFEE_BLACK, "Rocky", BigDecimal.valueOf(3.00), LineItemStatus.PLACED, order));
        return order;
    }

    public static OrderEventResult stubOrderEventResult() {

        // create the return value
        OrderEventResult orderEventResult = new OrderEventResult();

        // build the order from the PlaceOrderCommand
        Order order = new Order(UUID.randomUUID().toString());
        order.setOrderSource(OrderSource.WEB);
        order.setLocation(Location.ATLANTA);
        order.setTimestamp(Instant.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderEventResult.setOrder(order);
        orderEventResult.setBaristaTickets(TestUtil.stubBaristaTickets());
        orderEventResult.setOutboxEvents(mockOrderInEvent());
        return orderEventResult;
    }

    private static List<ExportedEvent> mockOrderInEvent() {
        return Arrays.asList(OrderCreatedEvent.of(stubOrder()));
    }

    private static List<OrderTicket> stubBaristaTickets() {
        return Arrays.asList(new OrderTicket(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Item.COFFEE_BLACK, "Rocky"));
    }

    public static TicketUp stubOrderTicketUp() {

        return new TicketUp(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                Item.COFFEE_BLACK,
                "Capt. Kirk",
                "Mr. Spock"
        );
    }
}
