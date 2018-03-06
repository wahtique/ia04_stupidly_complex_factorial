package tp2.agent;

import java.util.HashMap;
import java.util.Map;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import tp2.model.FactValue;

public class StoreAgent extends Agent {
	Map<Integer, Double> mapFact = new HashMap<>();

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		System.out.println(getLocalName() + "--> Installed");
		addBehaviour(new WaitResquestedFact());
		addBehaviour(new WaitCalculatedFact());
	}

	/**
	 * Attends des requêtes de factorielles. Regarde s'il possède la valeur et
	 * répond selon
	 * 
	 * @author cmoulin
	 *
	 */
	public class WaitResquestedFact extends CyclicBehaviour {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		@Override
		public void action() {
			ACLMessage message = receive(mt);
			if (message != null) {
				String s = message.getContent();
				System.out.println(getLocalName() + "--> fact(" + s + ") ?");
				int n = Integer.parseInt(s);
				Double value = mapFact.get(n);
				ACLMessage reply = message.createReply();
				FactValue fv;
				if (value != null) {
					reply.setPerformative(ACLMessage.CONFIRM);
					fv = new FactValue(n, value);
				} else {
					reply.setPerformative(ACLMessage.REFUSE);
					fv = new FactValue(n, 0d);

				}
				reply.setContent(fv.toJSON());
				send(reply);
			} else
				block();
		}

	}

	/**
	 * Attend des valeurs de factorielle pour les stocker dans la Map.
	 * 
	 * @author cmoulin
	 *
	 */
	public class WaitCalculatedFact extends CyclicBehaviour {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

		@Override
		public void action() {
			ACLMessage message = receive(mt);
			if (message != null) {
				FactValue fv = FactValue.read(message.getContent());
				mapFact.put(fv.number, fv.value);
				System.out.printf("%s --> %d! = %f\n", getLocalName(), fv.number, fv.value);
			} else
				block();
		}

	}
}
