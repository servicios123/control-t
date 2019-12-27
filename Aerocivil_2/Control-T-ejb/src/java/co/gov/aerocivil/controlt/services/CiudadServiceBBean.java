/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Ciudad;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class CiudadServiceBBean implements CiudadService {
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;

    @Override
    public List<Ciudad> getLista(Ciudad ciudad) {


        //return em.createNamedQuery("Dependencia.findAll").getResultList();

        try {
            List<String> condiciones = new ArrayList<String>();
            StringBuilder strQry = new StringBuilder("Select c from Ciudad c ");

            if (ciudad.getDepartamento().getDeptoId() != null) {
                condiciones.add("upper(c.ciuDepto) = :depto ");
                
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
            
            if (ciudad.getDepartamento().getDeptoId() != null) {
                query.setParameter("depto", ciudad.getDepartamento().getDeptoId());
                
            }
            
            
           
            
            return (List<Ciudad>) query.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();
            return null;
        }


    }

    

}
