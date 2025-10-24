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
        System.out.println("✅ Déploiement Camunda terminé. Test du processus et de la table de décision...");

        // Exemple de commande avec double
        Order order = new Order("VIP", 600.0, true);
        order.setPaymentValid(true);
        order.setStatus("EN_COURS");

        // Variables pour le DMN
        Map<String, Object> dmnVars = new HashMap<>();
        dmnVars.put("typeClient", order.getClientType());
        dmnVars.put("montantCommande", order.getOrderAmount());

        // Évaluation de la décision
        var decisionResult = decisionService.evaluateDecisionTableByKey("discountDecision", Variables.fromMap(dmnVars));
        double reduction = Double.parseDouble(decisionResult.getSingleResult().get("reduction").toString());
        order.setDiscountApplied(reduction);
        order.calculateFinalAmount();

        System.out.printf("💰 Client: %s | Montant: %.2f | Réduction: %.2f%% | Montant final: %.2f%n",
                order.getClientType(), order.getOrderAmount(), order.getDiscountApplied(), order.getFinalAmount());

        // Variables pour le processus BPMN
        Map<String, Object> processVars = new HashMap<>();
        processVars.put("typeClient", order.getClientType());
        processVars.put("montantCommande", order.getOrderAmount());
        processVars.put("reduction", order.getDiscountApplied());
        processVars.put("montantFinal", order.getFinalAmount());
        processVars.put("paiementEnLigne", order.getPaymentOnline());
        processVars.put("paiementValide", order.getPaymentValid());
        processVars.put("status", order.getStatus());

        runtimeService.startProcessInstanceByKey("Process_CommandeClient", processVars);
        System.out.println("🚀 Processus 'Process_CommandeClient' démarré avec les variables : " + processVars);
    }
}
