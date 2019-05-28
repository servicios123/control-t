/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Regional;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class RegionalServiceBean implements RegionalService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @EJB
    private AuditoriaService auditoria; 

    @Override
    public Regional guardar(Regional regional, Funcionario f) {
        return (Regional) auditoria.auditar(regional, f);

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Regional> getLista(){
        return em.createNamedQuery("Regional.findAll").getResultList();
    }

    @Override
    public Regional getRegional(Long regId) {
        return (Regional) em.createNamedQuery("Regional.findById").setParameter("regId", regId).getSingleResult();
    }

}
