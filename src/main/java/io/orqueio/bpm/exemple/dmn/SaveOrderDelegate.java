package io.orqueio.bpm.exemple.dmn;

import io.orqueio.bpm.engine.delegate.DelegateExecution;
import io.orqueio.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("saveOrderDelegate")
public class SaveOrderDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("📝 Sauvegarde de la commande en base de données...");
        // Exemple de données simulées
        execution.setVariable("paiementEnLigne", true); // true = paiement en ligne, false = paiement à la livraison
    }
}