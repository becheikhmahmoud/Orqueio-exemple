package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.DecisionService;
import io.orqueio.bpm.engine.RuntimeService;
import io.orqueio.bpm.engine.variable.Variables;
import io.orqueio.bpm.exemple.dmn.model.Order;

import io.orqueio.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import io.orqueio.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableProcessApplication
public class CommandeClientApplication {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DecisionService decisionService;

    public static void main(String... args) {
        SpringApplication.run(CommandeClientApplication.class, args);
    }

    @EventListener
    @Transactional
    public void onPostDeploy(PostDeployEvent event) {
        System.out.println("Camunda deployment completed. Testing the process and decision table...");

        // Example order with double values
        Order order = new Order("VIP", 600.0, true);
        order.setPaymentValid(true);
        order.setStatus("EN_COURS");

        // Variables for the DMN evaluation
        Map<String, Object> dmnVars = new HashMap<>();
        dmnVars.put("clientType", order.getClientType());
        dmnVars.put("orderAmount", order.getOrderAmount());

        // Evaluate the decision table
        var decisionResult = decisionService.evaluateDecisionTableByKey("discountDecision", Variables.fromMap(dmnVars));
        double reduction = Double.parseDouble(decisionResult.getSingleResult().get("reduction").toString());
        order.setDiscountApplied(reduction);
        order.calculateFinalAmount();

        System.out.printf("Client: %s | Amount: %.2f | Discount: %.2f%% | Final amount: %.2f%n",
                order.getClientType(), order.getOrderAmount(), order.getDiscountApplied(), order.getFinalAmount());

        // Variables for the BPMN process
        Map<String, Object> processVars = new HashMap<>();
        processVars.put("clientType", order.getClientType());
        processVars.put("orderAmount", order.getOrderAmount());
        processVars.put("reduction", order.getDiscountApplied());
        processVars.put("finalAmount", order.getFinalAmount());
        processVars.put("onlinePayment", order.getPaymentOnline());
        processVars.put("paymentValidated", order.getPaymentValid());
        processVars.put("status", order.getStatus());

        runtimeService.startProcessInstanceByKey("order_process", processVars);
        System.out.println("Process 'order_process' started with variables: " + processVars);
    }
}
