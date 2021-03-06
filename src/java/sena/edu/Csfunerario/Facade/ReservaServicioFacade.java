

package sena.edu.Csfunerario.Facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sena.edu.Csfunerario.Entity.ReservaServicio;

/**
 * Fecha: 20/02/2018
 * Hora:  11:48:19 AM    
 * Autor: Janel Gongora
 */
@Stateless
public class ReservaServicioFacade extends AbstractFacade<ReservaServicio> {

    @PersistenceContext(unitName = "CsfunerarioPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservaServicioFacade() {
        super(ReservaServicio.class);
    }

}
