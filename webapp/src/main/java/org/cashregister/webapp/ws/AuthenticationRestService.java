package org.cashregister.webapp.ws;

import org.apache.commons.codec.binary.Base64;
import org.cashregister.domain.Token;
import org.cashregister.domain.User;
import org.cashregister.webapp.error.PasswordException;
import org.cashregister.webapp.password.api.PasswordService;
import org.cashregister.webapp.persistence.api.TokenRepository;
import org.cashregister.webapp.security.impl.SecurityDetails;
import org.cashregister.webapp.security.impl.SecurityDetailsAssembler;
import org.cashregister.webapp.service.api.UserService;
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
 * Created by derkhumblet on 04/03/16.
 */
@Component
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationRestService {
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenRepository tokenRepository;


    @GET
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAuthenticate(@HeaderParam("Authorization") String authorization) {
        // Parse headers
        byte[] bytes = authorization.getBytes(StandardCharsets.UTF_8);
        String decoded = new String(Base64.decodeBase64(bytes), StandardCharsets.UTF_8);
        if (decoded.split(":").length == 5 && decoded.split(":")[1].equals("jfx") && decoded.split(":")[3].equals("up")) {
            String clientId = decoded.split(":")[0];
            String username = decoded.split(":")[2];
            String password = decoded.split(":")[4];
            String clientType = "jfx";
            // Find user
            User user = userService.findByName(username);
            SecurityDetails details = SecurityDetailsAssembler.build(user);
            try {
                passwordService.checkPassword(details, password);
                String token = tokenRepository.createTokenForUser(user, clientId, clientType);
                return Response.status(201).entity(token).build();
            } catch (PasswordException e) {
                e.printStackTrace();
                return Response.status(401).build();
            }
        } else {
            return Response.status(401).build();
        }
    }
}
