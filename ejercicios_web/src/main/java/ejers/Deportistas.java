package ejers;

import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.*;

// http://localhost:8080/ejercicios/rest/application.wadl

@Path("deportistas")
public class Deportistas {
    static ArrayList<Deportista> deportistas = new ArrayList<Deportista>();

    static { // TODO BORRAR
        deportistas.add(new Deportista(1, "Nicolas", "Hipica", true, "Masculino"));
        deportistas.add(new Deportista(2, "Guille", "Futbol", false, "Masculino"));
        deportistas.add(new Deportista(3, "Laura", "Balonmano", false, "Femenino"));
        deportistas.add(new Deportista(4, "Jacobo", "Balonmano", true, "Masculino"));
        deportistas.add(new Deportista(5, "Chocou", "Waterpolo", true, "Masculino"));
        deportistas.add(new Deportista(6, "Chocou", "Balonmano", false, "Masculino"));
        deportistas.add(new Deportista(7, "Maria", "Waterpolo", false, "Femenino"));
        deportistas.add(new Deportista(8, "Silvia", "Hipica", true, "Femenino"));
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML })
    public Response todos() {
        return Response.ok(new GenericEntity<List<Deportista>>(deportistas) {
        }).build();
    }

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response buscarJugador(@PathParam("id") int id) {
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getId() == id) {
                return Response.ok(deportistas.get(i)).build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se encontró ningún nombre en el registro").build();
    }

    @GET
    @Path("deporte/{deporte}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response porDeporte(@PathParam("deporte") String deporte) {
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getDeporte().equalsIgnoreCase(deporte)) {
                return Response.ok(deportistas.get(i)).build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se encontró ningún nombre en el registro").build();
    }

    @GET
    @Path("activos")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response activos() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).isActivo()) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("retirados")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response retirados() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (!deportistas.get(i).isActivo()) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("masculinos")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response masculinos() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getGenero().toLowerCase().startsWith("m")) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("femeninos")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response femeninos() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getGenero().toLowerCase().startsWith("f")) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("xg") // TODO
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response deportesPorGenero() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getGenero().toLowerCase().startsWith("f")) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("deporte/{deporte}/activos")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response activosPorDeporte(@PathParam("deporte") String deporte) {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getDeporte().equalsIgnoreCase(deporte) && deportistas.get(i).isActivo()) {
                aux.add(deportistas.get(i));
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("sdepor")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response contarDeportistas() {
        ArrayList<Deportista> aux = new ArrayList<Deportista>();
        ArrayList<String> nombres = new ArrayList<String>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (!nombres.contains(deportistas.get(i).getNombre())) {
                aux.add(deportistas.get(i));
                nombres.add(deportistas.get(i).getNombre());
            }
        }
        return Response.ok(new GenericEntity<List<Deportista>>(aux) {
        }).build();
    }

    @GET
    @Path("deportes")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response listaDeportes() {
        ArrayList<String> deportes = new ArrayList<String>();
        for (int i = 0; i < deportistas.size(); i++) {
            if (!deportes.contains(deportistas.get(i).getDeporte())) {
                deportes.add(deportistas.get(i).getDeporte());
            }
        }
        deportes.sort(String.CASE_INSENSITIVE_ORDER);
        return Response.ok(new GenericEntity<List<String>>(deportes) {
        }).build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response crearDeportista(@QueryParam("nombre") String nombre, @QueryParam("deporte") String deporte,
            @QueryParam("activo") boolean activo, @QueryParam("genero") String genero) {
        deportistas.add(new Deportista(deportistas.size(), nombre, deporte, activo, genero));
        return Response.ok("Se ha añadido el deportista correctamente").build();
    }

    @POST
    @Path("adds")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response crearDeportistas() {
        deportistas.add(new Deportista(deportistas.size(), "Nicolas", "Hipica", true, "Masculino"));
        deportistas.add(new Deportista(deportistas.size(), "Guille", "Futbol", false, "Masculino"));
        deportistas.add(new Deportista(deportistas.size(), "Laura", "Balonmano", false, "Femenino"));
        deportistas.add(new Deportista(deportistas.size(), "Jacobo", "Balonmano", true, "Masculino"));
        deportistas.add(new Deportista(deportistas.size(), "Chocou", "Waterpolo", true, "Masculino"));
        deportistas.add(new Deportista(deportistas.size(), "Chocou", "Balonmano", false, "Masculino"));
        deportistas.add(new Deportista(deportistas.size(), "Maria", "Waterpolo", false, "Femenino"));
        deportistas.add(new Deportista(deportistas.size(), "Silvia", "Hipica", true, "Femenino"));
        return Response.ok("Se han añadido varios deportistas correctamente").build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response actualizarDeportista(@QueryParam("id") int id, @QueryParam("nombre") String nombre,
            @QueryParam("deporte") String deporte, @QueryParam("activo") boolean activo,
            @QueryParam("genero") String genero) {
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getId() == id) {
                deportistas.remove(i);
                deportistas.add(new Deportista(i, nombre, deporte, activo, genero));
                return Response.ok("Deportista modificado correctamente").build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se encontró ningún deportista con ese id en el registro").build();
    }

    @POST
    @Path("del/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response borrarDeportista(@PathParam("id") int id) {
        for (int i = 0; i < deportistas.size(); i++) {
            if (deportistas.get(i).getId() == id) {
                deportistas.remove(i);
                return Response.ok("Deportista eliminado correctamente").build();
            }
        }
        return Response.status(Status.NOT_FOUND).entity("No se encontró ningún deportista con ese id en el registro").build();
    }
}