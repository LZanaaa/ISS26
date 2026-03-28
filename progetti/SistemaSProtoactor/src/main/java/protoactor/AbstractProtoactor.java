package protoactor;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import protoactor.ProtoActorContext;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

public abstract class AbstractProtoactor {
	
	protected String name ;
	protected ProtoActorContext context;
	protected IApplMessage reply; 
	protected ScheduledExecutorService msgexecutor  = Executors.newSingleThreadScheduledExecutor();
	
	
	public AbstractProtoactor(String name, ProtoActorContext ctx) {
		this.name     = name;
		this.context  = ctx;
		ctx.register(this); 
		proactiveJob(  );
	}

	//To Be Implemented in the application class
	protected abstract void elabDispatch(IApplMessage m );    
	protected abstract IApplMessage elabRequest(IApplMessage req  );
	protected abstract void elabReply(IApplMessage req  );
	protected abstract void elabEvent(IApplMessage ev );
	protected abstract void proactiveJob(  );
	
	public IApplMessage execMsg(IApplMessage am) {
		if (am.isEvent()) {
			executeTask( msgexecutor, ( ) -> elabEvent(am) );
			return null;
		} else if (am.isDispatch()) {
			executeTask(msgexecutor, () -> elabDispatch(am) );
			return null;
		} else if (am.isRequest()) { 
			return dorequestSynch(am);
			//return dorequestAsynch(am);
		}else if (am.isReply()) {
			executeTask(msgexecutor, () -> elabReply(am) );
			return null;        	
		}else return null;      
	  }
	//bloccnte
	protected IApplMessage dorequestSynch(IApplMessage am) {
    	try { 
			Future<IApplMessage> res = msgexecutor.submit( () -> { 
				IApplMessage replyMsg = elabRequest(am);
		    	return replyMsg;
	 		});  
			IApplMessage answer = res.get(); 
			return answer;
    	}catch( Exception e) {
    		return null;
    	}      	
    }
	
	//Non bloccante
	protected IApplMessage dorequestAsynch(IApplMessage am) {
		executeTask(msgexecutor, () -> {
        	IApplMessage r = elabRequest(am);
        	CommUtils.outyellow("AbstractProtoactor26 request r=" + r  );
        	//dico al contesto che deve elaborare la reply
        	context.elabMsg( r,null );
        });
        return null;
	}
	
	// --- ELABORAIZONE MESSAGGI ---
	protected void executeTask(ScheduledExecutorService executor, Runnable task) {
		try {
			executor.execute(task);
		}catch (Exception e) {
			CommUtils.outred("AbstractProtoactor26 executeTask ERROR " + e.getMessage() );
		}
	}
	
	// --- INTERAZIONE ---
	
	//Invia un messaggio ad un altro protoactor
	protected void forward(IApplMessage m) {
		context.elabMsg(m, null);		
	}
	
	//Invio a tutti i componenti esterni
	protected void emitInfo(IApplMessage event) {
		context.emitInfo(event);
	}
	
	//Invia una request sincrona ad un altro protoactor e attende la risposta
	protected IApplMessage request(IApplMessage req) {
		return context.elabMsg(req, null);
	}
	
	
	
}
