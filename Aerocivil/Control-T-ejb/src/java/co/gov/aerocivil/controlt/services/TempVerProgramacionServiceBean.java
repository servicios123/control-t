/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.ProcesoProgramacion;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Resumen;
import co.gov.aerocivil.controlt.entities.TempVerProgramacion;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import weblogic.jws.Transactional;

/**
 *
 * @author Viviana
 */
@Stateless
public class TempVerProgramacionServiceBean implements TempVerProgramacionService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private Long count;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<TempVerProgramacion> getLista(Programacion programacion) {
        //01 Oct 2013 Ordenar con respecto al puntaje de los operadores
        //if(programacion.getDependencia().getDepcategoria().getDcId()==1){
        //System.out.println("TempVerProgramacion.orderpuntaje :::::::::::::::::::::");
        return em.createNamedQuery("TempVerProgramacion.orderpuntaje").getResultList();

        /*}
         else{
         return em.createNamedQuery("TempVerProgramacion.ordergrado").getResultList();
         }*/

    }

    @Override
    public void consultarTurnos(Programacion programacion) {

        StringBuilder strQry = new StringBuilder();
        Query q = em.createNativeQuery(strQry.toString());

        strQry.append("begin verProgramacion(").append(" programacionId => ")
                .append(programacion.getProId())
                .append(");").append("end;");

        q = em.createNativeQuery(strQry.toString());
        q.executeUpdate();
    }

    public EntityManager getEm() {
        return em;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public Long totalHrEx(long programacion, Funcionario funcionario) {

        Query q = em.createNamedQuery("Turno.hrex");
        q.setParameter("alias", funcionario.getFunAlias());
        q.setParameter("proId", programacion);
        List resultList = q.getResultList();
        if (resultList != null) {
            return new Long(resultList.size());
        }
        return 0L;
    }

    @Override
    public Long totalHrExNoc(long programacion, String funcionario) {

        Query q = em.createNativeQuery("SELECT * FROM TURNO T, FUNCIONARIO F, POSICION_JORNADA pj, JORNADA_OP jo "
                + " WHERE T.TUR_FUNCIONARIO = F.FUN_ID  "
                + " and T.TUR_POSICION_JORNADA = pj.PJ_ID "
                + " and pj.PJ_JORNADA_OP = jo.JO_ID "
                + " AND T.TUR_TIPO = 2 "
                + " AND T.TUR_PROGRAMACION = " + programacion + " "
                + " AND f.fun_alias='" + funcionario + "' "
                + " AND jo.JO_ALIAS = 'N'");

        List resultList = q.getResultList();
        if (resultList != null) {
            return new Long(resultList.size());
        }
        return 0L;
    }

    @Override
    public List<Vistaprogramacion> comprobacion(long programacion) {
        Query query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.programacion.proId = :programacionId ORDER BY v.pjAlias,v.turFecha desc");
        query.setParameter("programacionId", programacion);
        return (List<Vistaprogramacion>) query.getResultList();
    }

    @Override
    public List<Vistaprogramacion> fechasProgramacion(long programacion) {
        Query query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.programacion.proId = :programacionId ORDER BY v.turFecha desc");
        query.setParameter("programacionId", programacion);
        return (List<Vistaprogramacion>) query.getResultList();
    }

    @Override
    @Transactional
    public void obtenerResumen(Long programacion) {
        StringBuilder strQry = new StringBuilder();
        strQry.append("begin resumir( programacionId => ").append(programacion).append("); end;");
        Query q = em.createNativeQuery(strQry.toString());
        int executeUpdate = q.executeUpdate();
        System.out.println("executeUpdate = " + executeUpdate);
    }

    @Override
    public List<Resumen> consultarResumen(Long programacion) {
        Query query = em.createQuery("Select r from Resumen r where r.programacion = :proId");
        query.setParameter("proId", programacion);
        return query.getResultList();
    }

    @Override
    public String obtenerResumenProceso(Long programacion, Funcionario funcionario) {
        String resumen = "Programacion generada por: " + funcionario.getFunNombre() + "\n"
                + "Dependencia: " + funcionario.getDependencia().getDepAbreviatura() + "\n\n"
                + "Flujo del proceso:\n\n";
        try {
            Query query = em.createNamedQuery("ProcesoProgramacion.findById");
            query.setParameter("proId", programacion);
            List<ProcesoProgramacion> list = query.getResultList();
            int i = 1;
            for (ProcesoProgramacion p : list) {
                String inicio = new SimpleDateFormat("HH:mm:ss aa").format(p.getInicio());
                String fin = new SimpleDateFormat("HH:mm:ss aa").format(p.getFin());
                long secs = Math.abs(p.getFin().getTime() - p.getInicio().getTime()) / 1000;
                resumen += i + ". Proceso " + p.getProceso() + " ejecutado entre " + inicio + " y " + fin + " en (" + secs + " segundos)\n";
                i++;
            }
            resumen += "\n"
                    + "Proceso completado!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resumen;
    }
}
