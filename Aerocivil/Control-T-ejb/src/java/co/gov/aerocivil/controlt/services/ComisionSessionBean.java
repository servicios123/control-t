/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Aeropuerto;
import co.gov.aerocivil.controlt.entities.Comision;
import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class ComisionSessionBean implements ComisionSession {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @EJB
    private AuditoriaService auditoria;

    @Override
    public Comision guardar(Comision c, Funcionario f) {
        c.setDependencia(em.find(Dependencia.class, c.getDependencia().getDepId()));
        return (Comision) auditoria.auditar(c,f);
    }

}
