package io.orqueio.bpm.exemple.dmn;

import org.springframework.stereotype.Component;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;

@Component("payAtDeliveryDelegate")
public class PayAtDeliveryDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("💵 Marquage de la commande à payer à la livraison...");
        execution.setVariable("paiementValide", true);
    }
}