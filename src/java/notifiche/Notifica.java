package notifiche;

import java.sql.Date;
import restaurant_photos.Photo;
import users.User;

/**
 *
 * @author postal
 */
public class Notifica {
    private String descrizione;
    private Date data;
    private String tipo;
    private Photo foto;
    private Integer idGen;
    private User user; 

    public Notifica(String descrizione, Date data, String tipo, Integer idGen, User user) {
        this.descrizione = descrizione;
        this.data = data;
        this.tipo = tipo;
        this.idGen = idGen;
        this.user = user;
    }

    public Notifica(String descrizione, Photo foto, String tipo, Integer idGen, User user) {
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.foto = foto;
        this.idGen = idGen;
        this.user = user;
        this.data = foto.getDate_creation();
    }

    public User getUser() {
        return user;
    }

    public Photo getFoto() {
        return foto;
    }

    public Date getData() {
        return data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setFoto(Photo foto) {
        this.foto = foto;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getIdGen() {
        return idGen;
    }
}