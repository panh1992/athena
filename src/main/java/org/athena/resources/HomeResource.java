package org.athena.resources;

import com.google.common.collect.Maps;
import org.athena.business.UserBusiness;
import org.athena.dto.UserDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HomeResource {

    private UserBusiness userBusiness;

    public HomeResource(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    @POST
    @Path("register")
    public Map<String, Object> register(UserDTO userDTO) {
        userBusiness.register(userDTO);
        Map<String, Object> result = Maps.newHashMap();
        result.put("test", "test");
        return result;
    }

    @GET
    @Path("/list")
    public List<UserDTO> findAll() {
        return userBusiness.findAll();
    }

    @GET
    @Path("/test")
    public Integer test() {
        return userBusiness.test();
    }

}
