package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("discountDecisionDelegate")
public class DiscountDecisionDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("💡 Application d'une règle DMN de réduction...");
        // Simuler une réduction
        execution.setVariable("discountType", "FIDELITE");
    }
}