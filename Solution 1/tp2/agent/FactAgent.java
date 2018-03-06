package tp2.agent;

import java.util.Random;

import tp2.model.Product;
import tp2.message.RequestMultMessage;
import tp2.message.StoreFactMessage;
import tp2.model.Constants;
import tp2.model.FactValue;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class FactAgent extends Agent {
	protected AID[] receiver;


	/*on add juste les wait behaviour, les calculs s'ajoutant plus tard*/
	protected void setup() {
		System.out.println(getLocalName() + "--> Installed");
		addBehaviour(new WaitforNumberBehaviour());
		addBehaviour(new WaitforAbsentBehaviour());
		addBehaviour(new WaitforCalculatedBehaviour());
	}

	/**
	 * Recherche un agent capable de rendre le service operation,
	 * multiplication. Choisit au hasard parmi les candidats possibles. Retourne
	 * null s'il n'y a pas de tels services enregistrés.
	 * 
	 * @return AID
	 */
	private AID searchAgents() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Constants.OPERATION);
		sd.setName(Constants.MULTIPLICATION);
		template.addServices(sd);
		AID receiver = null;
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			int n = result.length;
			Random r = new Random();
			if (n > 0) {
				int v = r.nextInt(n);
				receiver = result[v].getName();
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		return receiver;
	}

	/**
	 * Behaviour de calcul de factorielle.
	 */
	private class FactBehaviour extends Behaviour {
		int n;
		/*date de debut et de fin (pour le delai)*/
		long tinit, tend = 0;
		MessageTemplate mt;
		// Nombre initial pour le calcul (!1 = 1 : inutile à calculer)
		// Indique le niveau de calcul effectué (on arrête lorqu'il dépasse n)
		int level = 2;
		// Factorielle intermédiaire et résultat à la fin
		double result = 1;
		int step = 0;

		/*on construit un FactBehaviour avec la fact a calc*/
		public FactBehaviour(int n) {
			this.n = n;
			tinit = System.currentTimeMillis();
		}

		@Override
		public void action() {
			switch (step) {
			case 0:
				/**
				 * état 0 : Sollicite un agent MULT? ; demande d'un calcul
				 */
				AID receiver = searchAgents();
				/*si il ya au moins un agent qui peut faire ma mult*/
				if (receiver != null) 
				{
					String mirt = "request" + System.currentTimeMillis();
					mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.MatchInReplyTo(mirt));
					Product p = new Product("multiplication", new Double[] {level * 1.0, result });
					
					send(new RequestMultMessage(p, mirt, receiver));
					step++;
				}
				break;
			case 1:
				/**
				 * Réception d'un produit
				 */
				ACLMessage resultmessage = myAgent.receive(mt);
				if (resultmessage != null) {
					result = Double.parseDouble(resultmessage.getContent());
					level++;
					if (level <= n) {
						step = 0;
					} else {
						// Calcul terminé
						tend = System.currentTimeMillis();
						System.out.format("%s--> fact(%d) = %.1f time=%d \n",myAgent.getLocalName(), n,result,tend-tinit);
						FactValue fv = new FactValue(n, result);
						send(new StoreFactMessage(fv));
					}
				} else
					block();
				break;
			}

		}

		@Override
		public boolean done() {
			return level > n;
		}

	}

	/**
	 * Attend un message contenant le nombre de n! envoyé à partir de la console
	 * JADE
	 */
	private class WaitforNumberBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = receive(mt);
			if (message != null) {
				String s = message.getContent();
				System.out.printf("\n", getLocalName(),s);
				ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
				request.addReceiver(new AID("STORE",AID.ISLOCALNAME));
				request.setContent(s);
				send(request);
			} else
				block();
		}

	}
	/**
	 * Attend un message indiquant une absence de factorielle
	 * Doit déclencher le calcul
	 * JADE
	 */
	private class WaitforAbsentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REFUSE);
			ACLMessage message = receive(mt);
			if (message != null) {
				FactValue fv = FactValue.read(message.getContent());
				System.out.printf("%s %d! inconnu\n", getLocalName(),fv.number );
				addBehaviour(new FactBehaviour(fv.number));
			} else
				block();
		}

	}
	/**
	 * Attend un message indiquant un factorielle
	 * déjà calculé
	 * JADE
	 */
	private class WaitforCalculatedBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.CONFIRM);
			ACLMessage message = receive(mt);
			if (message != null) {
				FactValue fv = FactValue.read(message.getContent());
				System.out.printf("%s %d! = %f\n", getLocalName(),fv.number,fv.value );
				System.out.printf("%s id=%s\n", getLocalName(), message.getConversationId());
			} else
			{}
				//block();
		}

	}
}
