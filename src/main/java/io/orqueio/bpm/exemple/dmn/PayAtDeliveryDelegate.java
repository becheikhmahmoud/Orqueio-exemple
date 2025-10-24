package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component("payAtDeliveryDelegate")
public class PayAtDeliveryDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Marking order as to pay at delivery...");
        execution.setVariable("paymentValidated", true);
    }
}