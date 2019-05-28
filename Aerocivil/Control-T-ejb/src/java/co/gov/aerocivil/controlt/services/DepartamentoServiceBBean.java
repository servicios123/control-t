/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Departamento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Viviana
 */
@Stateless
public class DepartamentoServiceBBean implements DepartamentoService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @Override
    public List<Departamento> getLista(){
        return em.createNamedQuery("Departamento.findAll").getResultList();
    }
    

}
