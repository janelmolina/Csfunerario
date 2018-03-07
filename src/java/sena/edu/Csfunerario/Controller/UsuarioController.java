/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sena.edu.Csfunerario.Controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import sena.edu.Csfunerario.Entity.Beneficiario;
import sena.edu.Csfunerario.Entity.Ciudad;
import sena.edu.Csfunerario.Entity.TipoDocumento;
import sena.edu.Csfunerario.Entity.Usuario;
import sena.edu.Csfunerario.Facade.BeneficiarioFacade;
import sena.edu.Csfunerario.Facade.CiudadFacade;
import sena.edu.Csfunerario.Facade.TipoDocumentoFacade;
import sena.edu.Csfunerario.Facade.UsuarioFacade;

/**
 *
 * @author Janel Gongora
 */
@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    /**
     * Creates a new instance of UsuarioController
     */
    public UsuarioController() {
        usuario = new Usuario();
        ciudad = new Ciudad();
        tipoDocumento = new TipoDocumento();
        usuarioSesion = new Usuario();

    }

    @EJB
    UsuarioFacade usuarioFacade;

    @EJB
    CiudadFacade ciudadFacade;

    @EJB
    TipoDocumentoFacade tipoDocumentoFacade;


    private Usuario usuario;
    private Ciudad ciudad;
    private TipoDocumento tipoDocumento;
    private Usuario usuarioSesion;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String iniciarSesion() {

        usuarioSesion = new Usuario();

        try {
            usuarioSesion = usuarioFacade.iniciarSesion(usuario);
            usuario = new Usuario();
            if (usuarioSesion != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLog", usuarioSesion);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Usuario y/o clave incorrecto"));
            }
            return "/usuario/Principal_Usuario?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return "index?faces-redirect=true";
        }
    }

    public Usuario mostrarSesion() {
        Usuario usuarioSesionActive = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLog");

        return usuarioSesionActive;
    }


    public String cerrarSesion() {

        try {

            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "/index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return "Principal_Usuario?faces-redirect=true";
        }
    }

    public void crearUsuario() {
        usuario.setIdCiudad(ciudadFacade.find(ciudad.getIdCiudad()));
        usuario.setIdTipoDocumento(tipoDocumentoFacade.find(tipoDocumento.getIdTipoDocumento()));
        usuarioFacade.create(usuario);
        usuario = new Usuario();
    }

    public List<Usuario> listarUsuario() {
        return usuarioFacade.findAll();
    }

    public void eliminarUsuario(Usuario user) {
        usuario = user;
        usuarioFacade.remove(user);
        usuario = new Usuario();
    }

    public String editarUsuario() {

        usuarioSesion.setIdCiudad(ciudadFacade.find(ciudad.getIdCiudad()));
        Query consulta = tipoDocumentoFacade.getEm().createQuery("SELECT t FROM TipoDocumento t WHERE t.nombre = :nombre");
        consulta.setParameter("nombre", usuarioSesion.getIdTipoDocumento().getNombre());
        List<TipoDocumento> listDoc = consulta.getResultList();
        usuarioSesion.setIdTipoDocumento(listDoc.get(0));
        this.usuarioFacade.edit(this.usuarioSesion);
        return "Ver_Mis_Datos?faces-redirect=true";
    }

}