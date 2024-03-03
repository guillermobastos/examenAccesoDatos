package ejers;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Deportista {
    private int id;
    private String nombre;
    private String deporte;
    private boolean activo;
    private String genero;

    public Deportista(int id, String nombre, String deporte, boolean activo, String genero) {
        setId(id);
        setNombre(nombre);
        setDeporte(deporte);
        setActivo(activo);
        setGenero(genero);
    }

    public Deportista() {
        
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDeporte() {
        return deporte;
    }

    public String getGenero() {
        return genero;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }
}
