package org.srcm.gems.regapp.web.helper;

import java.util.Arrays;

import org.springframework.faces.webflow.FlowFacesContextLifecycleListener;
import org.springframework.webflow.execution.RequestContext;

public class CustomFlowListener extends FlowFacesContextLifecycleListener {

	@Override
	public void requestProcessed(RequestContext context) {
		
		super.requestProcessed(context);
		
		System.out.println("requestProcessed...");
		System.out.println(Arrays.asList(context.getActiveFlow().getPossibleOutcomes()));
		if(context != null && context.getCurrentTransition() != null){
			System.out.println("Request Processed "+context.getCurrentTransition().getId());
		}
		
	try{
		if(context != null && context.getCurrentState() != null){
			System.out.println("Request Processed "+context.getCurrentState().getId());
		}
	}catch(Throwable e){
		e.printStackTrace();
	}
	}

	@Override
	public void requestSubmitted(RequestContext context) {
		
		super.requestSubmitted(context);
		
		System.out.println("requestSubmitted...");
		
		try{
			if(context != null && context.getCurrentState() != null){
				System.out.println("Request Submitted "+context.getCurrentState().getId());
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
			if(context != null && context.getCurrentTransition() != null){
				System.out.println("Request Submitted "+context.getCurrentTransition().getId());
			}
	}

}
