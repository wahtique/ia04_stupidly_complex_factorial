package tp2.message;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import tp2.model.Product;

/**
 * Message envoyé par l'agent factorielle pour demander un résultat de produit
 * 
 * @author cmoulin
 *
 */
public class RequestMultMessage extends ACLMessage {

	public RequestMultMessage(Product p, String reply_with, AID receiver) {
		super(ACLMessage.REQUEST);
		setReplyWith(reply_with);
		addReceiver(receiver);
		setContent(p.toJSON());
	}

}
