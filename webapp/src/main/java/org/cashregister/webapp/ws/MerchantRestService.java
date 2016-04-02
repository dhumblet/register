package org.cashregister.webapp.ws;

import org.apache.commons.codec.binary.Base64;
import org.cashregister.common.ws.MerchantResponse;
import org.cashregister.domain.Merchant;
import org.cashregister.domain.Token;
import org.cashregister.webapp.security.NotAuthorizedException;
import org.cashregister.webapp.service.api.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;

/**
 * Created by derkhumblet on 18/03/16.
 */
@Component
@Path("merchants")
@Produces(MediaType.APPLICATION_JSON)
public class MerchantRestService extends SecuredRestService {
    @Autowired
    private MerchantService merchantService;

    @GET
    @Path("/merchant")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMechant(@HeaderParam("Token") String tokenString) {
        try {
            Token token = validateToken(tokenString);
            Merchant merchant = token.getMerchant();
            MerchantResponse response = MerchantResponse.builder()
                    .uuid(merchant.getUuid())
                    .name(merchant.getName())
                    .truck(merchant.isTruck())
                    .build();
            return Response.status(201).entity(response).build();
        } catch (NotAuthorizedException e) {
            return Response.status(401).build();
        }
    }
}
