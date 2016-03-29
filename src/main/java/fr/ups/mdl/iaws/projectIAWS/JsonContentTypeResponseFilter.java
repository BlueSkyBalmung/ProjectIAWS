package fr.ups.mdl.iaws.projectIAWS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;

public class JsonContentTypeResponseFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        List<String> contentType = new ArrayList<>(1);
        contentType.add(MediaType.APPLICATION_JSON);
        responseContext.getHeaders().put("Content-Type", contentType);
    }
}