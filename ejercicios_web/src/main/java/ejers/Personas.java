package ejers;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.*;

// http://localhost:8080/ejercicios/rest/application.wadl

@Path("personas")
public class Personas {
    static ArrayList<Persona> personas = new ArrayList<Persona>();

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response guardar(Persona p) {
        personas.add(p);
        return Response.ok(personas.get(personas.size() - 1)).build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML })
    public Response listar() {
        return Response.ok(new GenericEntity<List<Persona>>(personas) {
        }).build();
    }

    @GET
    @Path("{nombre}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response ver(@PathParam("nombre") String nombre) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getNombre().equals(nombre)) {
                return Response.ok(personas.get(i)).build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se encontró ningún nombre en el registro").build();
    }

    @GET
    @Path("buscar")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response verIgnoreCase(@DefaultValue("") @QueryParam("nombre") String nombre) {
        ArrayList<Persona> p = new ArrayList<Persona>();
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getNombre().equalsIgnoreCase(nombre)) {
                p.add(personas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Persona>>(personas) {
        }).build();
    }

    @POST
    @Path("add")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response insertarPersonas(@QueryParam("nombre") String... nombre) {
        Persona persona;
        for (int i = 0; i < nombre.length; i++) {
            persona = new Persona(1, nombre[i], false, null);
            personas.add(persona);
        }
        return Response.ok().build();
    }

    @POST
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response borrarPersona(@PathParam("id") int id) {
        boolean entra = false;
        String borrado = "No se ha borrado nada";
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId() == id) {
                if (!entra) {
                    entra = true;
                    borrado = "Se ha borrado la persona con el nombre " + personas.get(i).getNombre() + "\n";
                } else {
                    borrado += "Se ha borrado la persona con el nombre " + personas.get(i).getNombre() + "\n";
                }
                personas.remove(i);
                i--;
            }
        }
        return Response.ok(borrado).build();
    }

    @GET
    @Path("XML")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getPersona(@PathParam("id") int id) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId() == id) {
                return Response.ok(personas.get(i)).build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se ha encontrado ninguna persona con ese ID").build();
    }
}