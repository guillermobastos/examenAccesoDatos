package ejers;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;

@Provider
public class DebugExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        return Response.serverError().entity(exception.getMessage()).build();
    }
}
