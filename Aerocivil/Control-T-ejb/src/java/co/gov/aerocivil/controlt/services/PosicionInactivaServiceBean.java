/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.PosicionInactiva;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PosicionInactivaServiceBean implements PosicionInactivaService {

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private AuditoriaService auditoria;

    @Override
    public List<PosicionInactiva> getLista(Dependencia dep, Date fIni, Date fFin) {
        StringBuilder strQry = new StringBuilder("Select p from PosicionInactiva p ");
        strQry.append(" where p.posicionJornada.dependencia.depId=:depId and p.fecha between :fIni and :fFin ");
        strQry.append(" order by p.fecha ");
        return em.createQuery(strQry.toString()).setParameter("depId", dep.getDepId()).
                setParameter("fIni", fIni).setParameter("fFin", fFin).getResultList();
    }

    @Override
    public void guardarPosiciones(Map<String, Integer[]> mapa, Funcionario f) {
        StringBuilder strQry = new StringBuilder();
        Query query;

        for (Map.Entry e : mapa.entrySet()) {
            //1. Eliminar los que están en base de datos
            Date d = convertToDate(e.getKey().toString());
            strQry = new StringBuilder();

            strQry.append("delete from posicion_inactiva p where p.pi_id in( ")
                    .append("select pi.pi_id from posicion_inactiva pi, ")
                    .append("posicion_jornada pj ")
                    .append("where pi.pi_posicion_jornada = pj.pj_id ")
                    .append("and pj.pj_dependencia = ")
                    .append(f.getDependencia().getDepId())
                    .append(" and to_char(pi.pi_fecha, 'dd/mm/yyyy') = '")
                    .append(e.getKey().toString())
                    .append("')");
            //System.out.println("DelQry: "+strQry.toString());
            query = em.createNativeQuery(strQry.toString());
            //query.setParameter("fecha", d);
            int a = query.executeUpdate();
            Integer[] posiciones = (Integer[]) e.getValue();
            for (int i = 0; i < posiciones.length; i++) {
                PosicionInactiva posIn = new PosicionInactiva();
                posIn.setFecha(d);
                PosicionJornada pj = new PosicionJornada(posiciones[i].longValue());
                posIn.setPosicionJornada(pj);
                auditoria.auditar(posIn, f);
            }


        }
    }

    private Date convertToDate(String toString) {
        Calendar c = Calendar.getInstance();
        String[] arrDate = toString.split("/");
        c.set(Integer.parseInt(arrDate[2]),
                Integer.parseInt(arrDate[1]) - 1,
                Integer.parseInt(arrDate[0]));
        return c.getTime();
    }
}
