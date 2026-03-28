package protoactor;

import domain.MathUtils;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public class CalcActor extends AbstractProtoactor {
	
	// Costruttore
	
	public CalcActor(String name, ProtoActorContext ctx) {
		super(name, ctx);
	}

    // --- Gestione dei messaggi ---
	
	@Override
	protected IApplMessage elabRequest(IApplMessage req) {
		CommUtils.outblue(name + " | elabRequest:" + req);
		
		if (req.msgId().equals("calcRequest")) {			
			try {
				double x = Double.parseDouble(req.msgContent());
				double result = eval(x);
				return CommUtils.buildReply(name, req.msgId(), "" + result, req.msgSender());
			} catch (NumberFormatException e) {
				CommUtils.outred(name + " | Errore di formato input: " + req.msgContent());
				return CommUtils.buildReply(name, req.msgId(), "errore_input_non_numerico", req.msgSender());
			}
			
		} else {			
			return CommUtils.buildReply(name, req.msgId(), "requestUnknown", req.msgSender());
		}
	}
	
	@Override
	protected void elabReply(IApplMessage reply) {
		CommUtils.outblue(name + " | elabReply:" + reply);
	}
	
	@Override
	protected void elabEvent(IApplMessage event) {
		CommUtils.outcyan(name + " | elabEvent:" + event);
	}
	
	@Override
	protected void elabDispatch(IApplMessage dispatch) {
		CommUtils.outblue(name + " | elabDispatch:" + dispatch);
	}	

		
	
	// --- Logica di business ---
	
	private double eval(double x) {
		return MathUtils.calculate(x);
	}
	
	// --- Parte proattiva ---
	
	protected void proactiveJob() {
		
	}
}