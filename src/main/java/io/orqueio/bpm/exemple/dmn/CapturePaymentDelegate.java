package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("capturePaymentDelegate")
public class CapturePaymentDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Capturing online payment...");
        boolean paymentSuccessful = true;
        execution.setVariable("paymentValidated", paymentSuccessful);
    }
}