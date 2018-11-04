package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("webHook")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	private String accessToken = "EAAFH80GvlGMBAPCLZCL0WIkZAwy5MWXKIsTPozyUZCrySddOj9XHd7SCYL1hltiCeEthIlOAO6ZBQxvuhtFT1IAwpsiWmUzY0zK0cmm2oZC5ljwir6qjRCJRGklsYMcr4yfQGoZA1BKxNpW2emTA5MaXs4Op8ZAEOYZAZAfSsH0ibYAZDZD";
	private String verifyToken = "C2FC34283AFA604D62459772EC342B71E03324D285B6139328B5686861CBEF09";
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String webhook(@QueryParam("hub.mode") String mode,@QueryParam("hub.verify_token") String verify_token,@QueryParam("hub.challenge") String challenge) {
		if(verify_token.equals(verifyToken))
			return challenge;
		else
			return "incorrect token";
        
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String handleMessage(WebhookObject webhookObj) {
    	for(WebhookEntry entry : webhookObj.getEntryList()) {
    		if(entry.getMessaging()!=null) {
    			for(MessagingItem mItem : entry.getMessaging()) {
    				String senderId = mItem.getSender().getId();
    				IdMessageRecipient recipient = new IdMessageRecipient(senderId);
    				if(mItem.getMessage() != null && mItem.getMessage().getText() != null) {
    					sendMessage(recipient, new Message("Hi!"));
    				}
    			}
    		}
    	}
		return null;
	
    }
    
    void sendMessage(IdMessageRecipient recipient, Message message) {
    
    	FacebookClient pageClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_10);

    	SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
	     Parameter.with("recipient", recipient), // the id or phone recipient
		 Parameter.with("message", message)); // one of the messages from above
    }
}
