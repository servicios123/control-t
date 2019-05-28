/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.JornadaRestriccion;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.models.Day;
import co.gov.aerocivil.models.Setting;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrador
 */
@Stateless
public class ModificarTurnoServiceBean implements ModificarTurnoService {

    @EJB
    private ProgramacionTotalService programacionTotalServiceBean;
    @EJB
    private AuditoriaService auditoriaService;
    @EJB
    private TurnoService turnoService;
    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    private ArrayList<Day> days;
    private List<PosicionHabilitada> enabledPositions;
    private List<JornadaRestriccion> restrictivePeriod;
    private Setting setting;
    private Programacion programming;

    @Override
    public List<Funcionario> getFuncionarioTurnoPorFecha(Date date, long dep) {

        try {
            Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId AND f.funId IN (SELECT t.funcionario.funId FROM Turno t WHERE t.turFecha= :fecha AND t.programacion.dependencia.depId = :depId) ORDER BY f.funAlias ASC ");
            query.setParameter("fecha", date, TemporalType.DATE);
            query.setParameter("depId", dep);
            return (List<Funcionario>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<Funcionario> getFuncionarioTurnoEspecialPorFecha(Date date, long dep) {

        try {
            Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId AND f.funId IN (SELECT t.funcionario.funId FROM Turno t WHERE t.turFecha= :fecha AND t.programacion.dependencia.depId = :depId and t.turTipo in (3,4,5)) ORDER BY f.funAlias ASC ");
            query.setParameter("fecha", date, TemporalType.DATE);
            query.setParameter("depId", dep);
            return (List<Funcionario>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<Funcionario> getFunctionariesAvaible(Date date, Dependencia dependencia) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(date);
            Query query = this.em.createQuery("SELECT f FROM Funcionario f WHERE "
                    + "f.dependencia.depId =:depId "
                    + "and f.funEstado= :estado "
                    + "and f.funFvCertmedico > :fecha "
                    + "and f.funFvCurso> :fecha "
                    + "and f.funFvEvaluacion> :fecha "
                    + "and f.fuNivel IN (2,3,4,5,6) "
                    + "and  f.funId IN ("
                    + "SELECT p.funcionario.funId "
                    + "FROM PosicionHabilitada p "
                    + "WHERE p.funcionario.dependencia.depId = :depId "
                    + "and p.posicion.posEstado= :estado "
                    + "and p.posicion.dependencia.depId = :depId)  ");
            //System.out.println("query = " + query.toString());
            query.setParameter("depId", dependencia.getDepId());
            query.setParameter("estado", "Activo");
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    public List<Funcionario> getFunctionariesAvaibleSpecial(Date date, Dependencia dependencia) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(date);
            Query query = this.em.createQuery("SELECT f FROM Funcionario f WHERE "
                    + "f.dependencia.depId =:depId "
                    + "and f.funEstado= :estado "
                    + "and f.funFvCertmedico > :fecha "
                    + "and f.funFvCurso> :fecha "
                    + "and f.funFvEvaluacion> :fecha "
                    + "and f.fuNivel IN (2,3,4,5,6) "
                    + "and f.funId not in (select t.funcionario.funId "
                    + "from Turno t "
                    + "where t.turFecha = :fecha "
                    + "group by t.funcionario.funId "
                    + "having count(t.funcionario.funId) >= 2) "
                    + "and f.funId not in (select t1.funcionario.funId "
                    + "from Turno t1 "
                    + "where t1.turFecha = :fecha "
                    + "and t1.funcionario.dependencia.depId = :depId "
                    + "and t1.turHInicio = 0 and t1.turHFin = 23)"
                    + "");
            System.out.println("query = " + query.toString());
            query.setParameter("depId", dependencia.getDepId());
            query.setParameter("estado", "Activo");
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PosNoAsig> getPosNoAsigPorFecha(Date date, long dep) {
        try {
            Query query = em.createQuery("SELECT p FROM PosNoAsig p WHERE p.fecha=:fecha and p.dependencia = :depId ORDER BY p.posicionJornada.pjAlias ASC ");
            query.setParameter("fecha", date, TemporalType.DATE);
            query.setParameter("depId", dep);
            return (List<PosNoAsig>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<Funcionario> getFuncionarios(long dep) {
        try {
            Calendar actual = Calendar.getInstance();
            Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId and  f.funFvCertmedico > :fecha and f.funFvCurso > :fecha and f.funFvEvaluacion > :fecha  ORDER BY f.funAlias ASC");
            query.setParameter("depId", dep);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);

            return (List<Funcionario>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<TurnoEspecial> getTurnoEspecial(long dep) {
        try {

            Query query = em.createQuery("SELECT t FROM TurnoEspecial t WHERE t.dependencia.depId=:depId AND t.teEstado = :estado  ORDER BY t.teSigla ASC");
            query.setParameter("depId", dep);
            query.setParameter("estado", "Activo");

            return (List<TurnoEspecial>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<Vistaprogramacion> getTurnoPorFunFecha(Date date, long fun, boolean special) {
        try {
            Query query;
            if (special) {
                query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.funId=:funId AND v.turFecha=:fecha AND v.turTipo NOT IN (1,2) ORDER BY v.turHInicio ASC");
            } else {
                query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.funId=:funId AND v.turFecha=:fecha AND v.turTipo IN (1,2) ORDER BY v.turHInicio ASC");
            }

            query.setParameter("fecha", date, TemporalType.DATE);
            query.setParameter("funId", fun);
            return (List<Vistaprogramacion>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public boolean anularTurno(Vistaprogramacion vp, Funcionario f) {
        try {
            Turno t = em.find(Turno.class, vp.getTurId());
            long id = t.getTurPosicionJornada();

            em.remove(t);

            /*if (t.getTurTipo() == 3L) {
             TurnoEspFuncionario tef = em.find(TurnoEspFuncionario.class, id);
             auditoriaService.auditarDelete("TURNO_ESPECIAL_FUNCIONARIO", f, 0L, id);
             em.remove(tef);
             }*/

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String asignarEspecial(Date date, Funcionario f, TurnoEspecial tes, Funcionario mod) {

        Programacion p = this.getProgramacion(f.getDependencia().getDepId(), date);
        if (p == null) {
            return "No existe programación para esa fecha";
        }

        List<Vistaprogramacion> turnos = this.getTurnosByDayAndFun(f.getFunId(), date);
        long horafin = (long) tes.getTeHfin();
        long horainicio = (long) tes.getTeHinicio();
        long mininicio = (long) tes.getTeMinicio();
        long minfin = (long) tes.getTeMfin();
        String hIniNew = horainicio + ":" + mininicio + ":00";
        String hFinNew = horafin + ":" + minfin + ":00";
        long totalTes = horasTurno(hIniNew, hFinNew);

        if (turnos != null) {
            long horafin_1 = horafin;
            for (Vistaprogramacion turno : turnos) {
                String turIni = turno.getTurHInicio() + ":" + turno.getTurMInicio() + ":00";
                String turFin = turno.getTurHFin() + ":" + turno.getTurMFin() + ":00";
                long totalTur = horasTurno(turIni, turFin);
                if (totalTur + totalTes > 12) {
                    return "Las horas totales maximas permitidas han sido exedidas por el turno " + turno.getPjAlias();
                }
                if ((horafin_1 >= turno.getTurHInicio() && horafin_1 <= turno.getTurHFin()) || (horainicio >= turno.getTurHInicio() && horainicio <= turno.getTurHFin())) {
                    return "Las horas del turno especial " + tes.getTeSigla() + " se cruzan con el turno " + turno.getPjAlias();
                }
            }
        }

        Turno nuevo = new Turno();
        nuevo.setFuncionario(f);
        nuevo.setProgramacion(p);
        nuevo.setTurFecha(date);
        nuevo.setTurEstado(0L);
        nuevo.setTurTipo(3L);
        nuevo.setTurMFin(minfin);
        nuevo.setTurMInicio(mininicio);
        nuevo.setTurHInicio(horainicio);
        nuevo.setTurHFin(horafin);
        TurnoEspFuncionario tef = null;
        /*try {
         Query q1 = em.createQuery("SELECT t FROM TurnoEspFuncionario t WHERE t.funcionario.funId = :funId and t.turnoEspecial.teId = :tes");
         q1.setParameter("funId", f.getFunId());
         q1.setParameter("tes", tes.getTeId());
         List resultList = q1.getResultList();
         if (resultList != null && resultList.size() > 0) {
         tef = (TurnoEspFuncionario) resultList.get(0);
         } else {*/
        tef = new TurnoEspFuncionario();
        tef.setTurnoEspecial(tes);
        tef.setTefEstado("Programado");
        tef.setFuncionario(f);
        tef.setTefFini(date);
        tef.setTefFfin(date);
        tef.setGroupId(obtenerGrupoId());

        /* }
         } catch (Exception e) {
         tef = new TurnoEspFuncionario();
         tef.setTurnoEspecial(tes);
         tef.setTefEstado("Programado");
         tef.setFuncionario(f);
         tef.setTefFini(date);
         tef.setTefFfin(date);
         }*/
        em.persist(tef);
        nuevo.setTurPosicionJornada(tef.getTefId());

        auditoriaService.auditar(nuevo, mod);

        return null;
    }

    private long horasTurno(String inicio, String fin) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            Date date1 = format.parse(inicio);
            Date date2 = format.parse(fin);
            int horas = 0;
            int minutos = 0;
            int diferencia = (int) ((date2.getTime() - date1.getTime()) / 1000);
            if (diferencia > 3600) {
                horas = (int) Math.floor(diferencia / 3600);
                diferencia = diferencia - (horas * 3600);
            }
            if (diferencia > 60) {
                minutos = (int) Math.floor(diferencia / 60);
                diferencia = diferencia - (minutos * 60);
            }
            if(minutos>=59){
                horas = horas+1;
            }
            return new Long(horas).longValue();
        } catch (ParseException ex) {
            Logger.getLogger(ModificarTurnoServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private Long obtenerGrupoId() {
        Query q = em.createNativeQuery("SELECT SEQ_TUR_ESPEC_FUN_GROUP.nextval from DUAL");
        BigDecimal result = (BigDecimal) q.getSingleResult();
        return result.longValue();
    }

    private List<Vistaprogramacion> getTurnosByDayAndFun(Long funId, Date fecha) {
        try {
            em.clear();
            em.flush();
            Query query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.funId = :funId AND v.turFecha = :fecha order by v.turHInicio ASC");
            query.setParameter("funId", funId);
            query.setParameter("fecha", fecha, TemporalType.DATE);
            return (List<Vistaprogramacion>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    private Programacion getProgramacion(Long depId, Date fecha) {
        try {
            Query query = em.createQuery("SELECT p FROM Programacion p WHERE p.dependencia.depId=:depId and :fecha BETWEEN p.proFechaInicio and p.proFechaFin");
            query.setParameter("depId", depId);
            query.setParameter("fecha", fecha, TemporalType.DATE);
            query.setMaxResults(1);
            return (Programacion) query.getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    private String printResult(String[] razon) {
        String rta = "Func " + razon[2] + " \t " + razon[1] + " [" + razon[3] + " " + razon[4] + "]: " + razon[5];
        if (razon.length >= 7) {
            rta += " (" + razon[6] + ")";
        }

        //System.out.println(rta);

        return rta + "\n";
    }

    @Override
    public String cambiarTurnos(Date date1, Funcionario f1, Vistaprogramacion vp1, Date date2, Funcionario f2, Vistaprogramacion vp2, Funcionario mod) {

        if (vp1 == null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            //System.out.println("vp1 es nulo");
            return "No existe turno del funcionario " + f1.getFunAlias() + " Para el día " + c.get(Calendar.DATE) + "/" + (c.get(Calendar.MONTH) + 1);
        }


        Turno turno1 = em.find(Turno.class, vp1.getTurId());
        turno1.setFuncionario(f2);
        turno1.setTurFecha(date2);

        if (vp2 == null) {
            List<Vistaprogramacion> turnos = this.getTurnosByDayAndFun(f2.getFunId(), date2);
            if (turnos != null && turnos.size() >= 2) {
                Calendar c = Calendar.getInstance();
                c.setTime(date2);

                String error = "El Func " + f2.getFunAlias() + " Tiene más de 2 turnos el " + c.get(Calendar.DATE) + "/" + (c.get(Calendar.MONTH) + 1) + " [";
                for (Vistaprogramacion vp : turnos) {
                    error += " " + vp.getPjAlias() + " ";
                }
                error += "]";

                return error;
            }



            String resultado = "-;-;-;-;-;OK;-;";;
            if (turno1.getTurTipo() == 1L || turno1.getTurTipo() == 2) {
                resultado = programacionTotalServiceBean.validateTurn(turno1, null);
            }



            String[] razon = resultado.split(";");

            if (razon[5].equals("OK")) {
                try {
                    Long tipo = Long.valueOf(razon[7]);
                    if (tipo > 0) {
                        turno1.setTurTipo(tipo);
                    }
                } catch (Exception e) {
                    Long tipo = Long.valueOf(razon[6]);
                    if (tipo > 0) {
                        turno1.setTurTipo(tipo);
                    }
                }
                auditoriaService.auditar(turno1, mod);


                //corregimos la data de tipoTurno para no afectar la programacion

                //Corregir 1 turno origen
                try {
                    turnoService.corregirTipos(date2, vp1.getProgramacion().getProId(), f1.getFunId(), mod);

                    //coregir turno 2 destino

                    turnoService.corregirTipos(date2, vp1.getProgramacion().getProId(), f2.getFunId(), mod);
                } catch (Exception w) {
                    w.printStackTrace();
                }
                return null;
            } else {
                return printResult(razon);
            }


        } else {

            Turno turno2 = em.find(Turno.class, vp2.getTurId());
            turno2.setFuncionario(f1);
            turno2.setTurFecha(date1);


            String resultado_1 = "-;-;-;-;-;OK;-;";

            if (turno1.getTurTipo() == 1L || turno1.getTurTipo() == 2) {
                resultado_1 = programacionTotalServiceBean.validateTurn(turno1, turno2.getTurId());
            }


            String resultado_2 = "-;-;-;-;-;OK;-;";;
            if (turno2.getTurTipo() == 1L || turno2.getTurTipo() == 2) {
                resultado_2 = programacionTotalServiceBean.validateTurn(turno2, turno1.getTurId());
            }


            String[] razon1 = resultado_1.split(";");
            String[] razon2 = resultado_2.split(";");

            if (razon1[5].equals("OK") && razon2[5].equals("OK")) {

                em.merge(turno1);
                em.merge(turno2);


                return null;
            } else {
                String respuesta = "";

                if (!razon1[5].equals("OK")) {
                    respuesta += printResult(razon1);
                }

                if (!razon2[5].equals("OK")) {
                    if (!razon1[5].equals("OK")) {
                        respuesta += " | ";
                    }
                    respuesta += printResult(razon2);
                }


                return respuesta;
            }
        }





    }

    @Override
    public String asignarPosNoAsig(PosNoAsig pn, Date date, Funcionario fun, Funcionario mod) {
        Turno turno = new Turno();
        turno.setFuncionario(fun);
        turno.setProgramacion(em.find(Programacion.class, pn.getProgramacion()));
        turno.setTurEstado(0L);
        turno.setTurFecha(date);
        turno.setTurHFin((long) pn.getPosicionJornada().getJornada().getJoHoraFin());
        turno.setTurHInicio((long) pn.getPosicionJornada().getJornada().getJoHoraInicio());
        turno.setTurMInicio((long) pn.getPosicionJornada().getJornada().getJoMinutoInicio());
        turno.setTurMFin((long) pn.getPosicionJornada().getJornada().getJoMinutoFin());
        turno.setTurPosicionJornada(pn.getPosicionJornada().getPjId());
        turno.setTurTipo(1L);

        String resultado = programacionTotalServiceBean.validateTurn(turno, null);


        String[] razon = resultado.split(";");

        if (razon[5].equals("OK")) {

//            em.persist(turno);
            auditoriaService.auditar(turno, mod);
            pn = em.merge(pn);
            em.remove(pn);
            return null;
        } else {

            return printResult(razon);

        }



    }

    @Override
    public Vistaprogramacion getVp(Date fecha, Funcionario fun, Long pos_jor) {
        try {
            StringBuilder strQry = new StringBuilder();
            if (pos_jor != null) {
                strQry.append(" AND v.turPosicionJornada = :pos_jor;");
            }

            Query query = em.createQuery("SELECT v FROM Vistaprogramacion v WHERE v.turFecha= :fecha AND v.funId=:funId ".concat(strQry.toString()));
            query.setParameter("funId", fun.getFunId());
            if (pos_jor != null) {
                query.setParameter("pos_jor", pos_jor);
            }
            query.setParameter("fecha", fecha, TemporalType.DATE);
            query.setMaxResults(1);
            return (Vistaprogramacion) query.getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}
