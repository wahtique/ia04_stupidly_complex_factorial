package tp2.message;

import jade.lang.acl.ACLMessage;
import tp2.model.Product;

/**
 * Message envoyé par l'agent multiplication en réponse à une demande de produit
 * 
 * @author cmoulin
 *
 */
public class InformMultResultMessage {
	public static ACLMessage answer(ACLMessage message) {
		String par = message.getContent();
		ACLMessage reply = message.createReply();
		Product p = Product.read(par);

		if (p != null && p.getAction().equals("multiplication")) {
			double n = p.product();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(String.valueOf(n));
		} else {
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("unknown operator");
		}
		return reply;
	}
}
