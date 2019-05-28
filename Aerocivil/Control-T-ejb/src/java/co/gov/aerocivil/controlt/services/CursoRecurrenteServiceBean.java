/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.CursoRecurrente;
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
public class CursoRecurrenteServiceBean implements CursoRecurrenteService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @EJB
    private AuditoriaService auditoria;

    @Override
    public CursoRecurrente guardar(CursoRecurrente cursoRecurrente, Funcionario f) {
        return (CursoRecurrente) auditoria.auditar(cursoRecurrente, f);
    }

}
