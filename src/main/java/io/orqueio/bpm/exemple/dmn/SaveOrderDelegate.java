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
        System.out.println("📝 Sauvegarde de la commande en base de données...");

        String clientType = (String) execution.getVariable("typeClient");
        Double orderAmount = (Double) execution.getVariable("montantCommande");
        Boolean paymentOnline = (Boolean) execution.getVariable("paiementEnLigne");
        Double reduction = (Double) execution.getVariable("reduction");

        if (orderAmount == null) {
            throw new IllegalArgumentException("La variable 'montantCommande' est manquante !");
        }

        System.out.println("🎯 Réduction appliquée : " + (reduction != null ? reduction : 0) + "%");

        // Création de la commande avec double
        Order order = new Order();
        order.setClientType(clientType);
        order.setOrderAmount(orderAmount);
        order.setPaymentOnline(paymentOnline);
        order.setDiscountApplied(reduction != null ? reduction : 0);
        order.setStatus("CREATED");

        // Calcul du montant final
        order.calculateFinalAmount();
        System.out.println("💰 Montant final après réduction : " + order.getFinalAmount());

        Order savedOrder = orderService.save(order);
        execution.setVariable("orderId", savedOrder.getId());

        System.out.println("✅ Commande enregistrée avec ID : " + savedOrder.getId());
    }
}
