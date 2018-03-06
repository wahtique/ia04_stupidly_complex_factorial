package tp2.message;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import tp2.model.FactValue;

/**
 * Message envoyé à l'agent store indiquant une factorielle calculée
 * 
 * @author cmoulin
 *
 */
public class StoreFactMessage extends ACLMessage {

	public StoreFactMessage(FactValue fv) {
		super(ACLMessage.INFORM);
		addReceiver(new AID("STORE", AID.ISLOCALNAME));
		setContent(fv.toJSON());
	}

}
