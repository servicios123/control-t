/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.DepCategoria;
import co.gov.aerocivil.controlt.entities.Funcionario;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Viviana
 */
@Stateless
public class DepCategoriaServiceBean implements DepCategoriaService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @EJB
    private AuditoriaService auditoria;

    @Override
    public DepCategoria guardar(DepCategoria depcategoria, Funcionario f) {
        return (DepCategoria) auditoria.auditar(depcategoria,f);
    }

    @Override
    public List<DepCategoria> getLista(DepCategoria depcategoria) {


        //return em.createNamedQuery("Dependencia.findAll").getResultList();

        try {
            List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder("Select dc from DepCategoria dc ");

            if (depcategoria.getDcNombre() != null) {
                condiciones.add("upper(dc.dcNombre) = :nombre ");
                
            }
           
            if(condiciones.size()>0){
                strQry.append("where ");
            }
            for (Iterator<String> it = condiciones.iterator(); it.hasNext();) {
                strQry.append(it.next());
                if(it.hasNext()){
                    strQry.append(" and ");
                }
            }

            Query query = em.createQuery(strQry.toString());
            
            if (depcategoria.getDcNombre() != null) {
                query.setParameter("nombre", "%"+depcategoria.getDcNombre().toUpperCase()+"%");
                
            }
            
            
           
            
            return (List<DepCategoria>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }


    }

    @Override
    public List<DepCategoria> getLista() {
        Query q = em.createNamedQuery("DepCategoria.findAll");
        return q.getResultList();
    }
    
}
