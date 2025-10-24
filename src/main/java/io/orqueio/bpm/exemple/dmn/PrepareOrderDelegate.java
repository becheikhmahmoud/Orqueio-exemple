package io.orqueio.bpm.exemple.dmn;


import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("prepareOrderDelegate")
public class PrepareOrderDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(" Preparing order...");
    }
}