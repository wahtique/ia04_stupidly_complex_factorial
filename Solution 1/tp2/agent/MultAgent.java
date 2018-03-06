package tp2.agent;

import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import tp2.message.InformMultResultMessage;
import tp2.model.Constants;

public class MultAgent extends Agent {
	protected void setup() {
		register();
		System.out.println(getLocalName() + "--> Installed");
		addBehaviour(new MultBehaviour());
	}

	/**
	 * Enregistrement de l'agent sous forme de service operation, multiplication
	 */
	private void register() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.OPERATION);
		sd.setName(Constants.MULTIPLICATION);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	@Override
	protected void takeDown() {
		System.out.println("---> "+getLocalName() + " : Good bye");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Attend un message contenant la requête d'un produit à effectuer Répond
	 * après un temps compris entre 0,5 et 2,5 secondes
	 * 
	 * @author Claude Moulin
	 * 
	 */
	private class MultBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = myAgent.receive(mt);
			if (message != null) {
				Random r = new Random();
				long d = r.nextInt(Constants.DELAY) + 50;
				System.out.println(myAgent.getLocalName() + "--> delay: " + d);
				addBehaviour(new SendDelayBehaviour(myAgent, d, message));

			} else
				block();
		}

	}

	private class SendDelayBehaviour extends WakerBehaviour {
		ACLMessage message;

		public SendDelayBehaviour(Agent a, long timeout, ACLMessage message) {
			super(a, timeout);
			this.message = message;
		}

		@Override
		protected void onWake() {
			System.out.println(myAgent.getLocalName() + "--> "
					+ message.getContent());
			send(InformMultResultMessage.answer(message));
		}

	}
}
