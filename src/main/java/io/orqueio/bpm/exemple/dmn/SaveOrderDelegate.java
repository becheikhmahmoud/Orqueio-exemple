package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import io.orqueio.bpm.exemple.dmn.model.Order;
import io.orqueio.bpm.exemple.dmn.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("saveOrderDelegate")
public class SaveOrderDelegate implements JavaDelegate {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Saving order in database...");

        String clientType = (String) execution.getVariable("clientType");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        Boolean paymentOnline = (Boolean) execution.getVariable("onlinePayment");
        Double reduction = (Double) execution.getVariable("reduction");

        if (orderAmount == null) {
            throw new IllegalArgumentException("The variable 'orderAmount' is missed !");
        }

        System.out.println("Discount applicated : " + (reduction != null ? reduction : 0) + "%");

        Order order = new Order();
        order.setClientType(clientType);
        order.setOrderAmount(orderAmount);
        order.setPaymentOnline(paymentOnline);
        order.setDiscountApplied(reduction != null ? reduction : 0);
        order.setStatus("CREATED");

        order.calculateFinalAmount();
        System.out.println("Final amount after discount : " + order.getFinalAmount());

        Order savedOrder = orderService.save(order);
        execution.setVariable("orderId", savedOrder.getId());

        System.out.println("Order  saved with ID : " + savedOrder.getId());
    }
}
