/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.services;

import co.gov.aerocivil.controlt.entities.Dependencia;
import co.gov.aerocivil.controlt.entities.DetSecuencia;
import co.gov.aerocivil.controlt.entities.DiaFestivo;
import co.gov.aerocivil.controlt.entities.Funcionario;
import co.gov.aerocivil.controlt.entities.Jornada;
import co.gov.aerocivil.controlt.entities.JornadaNoLaborable;
import co.gov.aerocivil.controlt.entities.JornadaRestriccion;
import co.gov.aerocivil.controlt.entities.PermisoEspecial;
import co.gov.aerocivil.controlt.entities.PosNoAsig;
import co.gov.aerocivil.controlt.entities.PosicionHabilitada;
import co.gov.aerocivil.controlt.entities.PosicionInactiva;
import co.gov.aerocivil.controlt.entities.PosicionJornada;
import co.gov.aerocivil.controlt.entities.Programacion;
import co.gov.aerocivil.controlt.entities.RestriccionDependencia;
import co.gov.aerocivil.controlt.entities.Turno;
import co.gov.aerocivil.controlt.entities.TurnoEspFuncionario;
import co.gov.aerocivil.controlt.entities.TurnoEspecial;
import co.gov.aerocivil.controlt.entities.Vistaprogramacion;
import co.gov.aerocivil.controlt.util.StringDateUtil;
import co.gov.aerocivil.models.Day;
import co.gov.aerocivil.models.Functionary;
import co.gov.aerocivil.models.Period;
import co.gov.aerocivil.models.Position;
import co.gov.aerocivil.models.Setting;
import co.gov.aerocivil.models.Turn;
import co.gov.aerocivil.models.Week;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    @PersistenceContext(unitName = "ControlT-ejbPU")
    private EntityManager em;
    @EJB
    private ProgramacionTotalService programacionTotalServiceBean;
    @EJB
    private AuditoriaService auditoriaService;
    @EJB
    private TurnoService turnoService;
    @EJB
    private JornadaNoLaboralService jornadaNoLaboralService;
    @EJB
    private JornadaService jornadaService;
    private ArrayList<Day> days;
    private List<PosicionHabilitada> enabledPositions;
    private List<JornadaRestriccion> restrictivePeriod;
    private Setting setting;
    private Programacion programming;
    private List<DetSecuencia> sequences;
    private List<DiaFestivo> holydays;
    private List<Jornada> jornadas;
    private ArrayList<Week> weeks;
    private ArrayList<String> log;
    private Long jornadasQty;
    private Boolean debug;
    private Day lastDay;

    private void getSetting(Long depId) {
        this.setting = new Setting();
        List<RestriccionDependencia> restricciones = getRestrictions(depId);
        for (RestriccionDependencia rd : restricciones) {
            if (rd.getRdValor() != null) {
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 4L) {
                    this.setting.setPeriodId(rd.getRdValor().longValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 10L) {
                    boolean max = false;
                    if (rd.getRdValor().longValue() > 0L) {
                        max = true;
                    }
                    this.setting.setMaxHoursExtra(max);
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 5L) {
                    this.setting.setTimeRecess(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 1L) {
                    this.setting.setMaxWorkedHoursByDay(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 2L) {
                    this.setting.setMaxContinuosDaysExtra(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 7L) {
                    this.setting.setMaxContinuosPeriod(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 9L) {
                    this.setting.setMaxContinuosDaysExtraModification(rd.getRdValor().intValue());
                }
            }
        }
    }

    private List<RestriccionDependencia> getRestrictions(Long depId) {
        try {
            Query query = this.em.createQuery("SELECT r FROM RestriccionDependencia r WHERE r.dependencia.depId = :depId");
            query.setParameter("depId", depId);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    @Override
    public List<Funcionario> getFuncionarioTurnoPorFecha(Date date, long dep) {

        try {

            Calendar actual = Calendar.getInstance();

            Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId AND f.funId IN (SELECT t.funcionario.funId FROM Turno t WHERE t.turFecha= :fecha AND t.programacion.dependencia.depId = :depId) and f.funFvCertmedico > :actual and f.funFvCurso > :actual and f.funFvEvaluacion > :actual ORDER BY f.funAlias ASC ");
            query.setParameter("fecha", date, TemporalType.DATE);
            query.setParameter("depId", dep);
            query.setParameter("actual", actual.getTime(), TemporalType.DATE);
            return (List<Funcionario>) query.getResultList();

        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public List<Funcionario> getFuncionarioTurnoEspecialPorFecha(Date date, long dep) {

        try {
            Query query = em.createQuery("SELECT f FROM Funcionario f WHERE f.dependencia.depId =:depId AND f.funId IN (SELECT t.funcionario.funId FROM Turno t WHERE t.turFecha= :fecha AND t.programacion.dependencia.depId = :depId and t.turTipo in (3,4,5,8)) ORDER BY f.funAlias ASC ");
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
                    + "ORDER BY f.funAlias ASC");
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
            Date fecha = t.getTurFecha();
            long id = t.getTurPosicionJornada();

            em.remove(t);

            if (t.getTurTipo() == 3L) {
                TurnoEspFuncionario tef = em.find(TurnoEspFuncionario.class, id);
                auditoriaService.auditarDelete("TURNO_ESPECIAL_FUNCIONARIO", f, 0L, id);
                em.remove(tef);
            }

            List<Turno> turnos = getTurnoPorUsuarioFecha(t.getFuncionario(), fecha);
            for (Turno t1 : turnos) {
                if (t1 != null && t1.getTurTipo() == 2 && t1.getTurId() != t.getTurId()) {
                    t1.setTurTipo(1L);
                    auditoriaService.auditar(t1, f);
                }
            }

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

        getSetting(p.getDependencia().getDepId());

        List<Vistaprogramacion> turnos = this.getTurnosByDayAndFun(f.getFunId(), date);
        long horafin = (long) tes.getTeHfin();
        long horainicio = (long) tes.getTeHinicio();
        long mininicio = (long) tes.getTeMinicio();
        long minfin = (long) tes.getTeMfin();
        String hIniNew = horainicio + ":" + mininicio + ":00";
        String hFinNew = horafin + ":" + minfin + ":00";
        Double totalTes = horasTurno(hIniNew, hFinNew);

        if (turnos != null) {
            long horafin_1 = horafin;
            for (Vistaprogramacion turno : turnos) {
                String turIni = turno.getTurHInicio() + ":" + turno.getTurMInicio() + ":00";
                String turFin = turno.getTurHFin() + ":" + turno.getTurMFin() + ":00";
                Double totalTur = horasTurno(turIni, turFin);
                if (totalTur + totalTes >= this.setting.getMaxWorkedHoursByDay()) {
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

    private Double horasTurno(String inicio, String fin) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Double result = null;
            Date date1 = format.parse(inicio);
            Date date2 = format.parse(fin);
            double horas = 0;
            double minutos = 0;
            double diferencia = (int) ((date2.getTime() - date1.getTime()) / 1000);
            if (diferencia > 3600) {
                horas = (int) Math.floor(diferencia / 3600);
                diferencia = diferencia - (horas * 3600);
            }
            if (diferencia > 60) {
                minutos = (int) Math.floor(diferencia / 60);
                diferencia = diferencia - (minutos * 60);
            }
            if (minutos >= 59) {
                horas = horas + 1;
                result = new Double(String.valueOf(horas));
            } else {
                double time = horas + (minutos / 60);
                result = new Double(time);
            }
            return result;
        } catch (ParseException ex) {
            Logger.getLogger(ModificarTurnoServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0D;
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
        String log = "";
        int i = 0;
        for (String s : razon) {
            log += "[" + i + "] => " + s + "-->";
            i++;
        }
        Logger.getLogger(ModificarTurnoServiceBean.class.getName()).log(Level.WARNING, log, new Exception("Info"));
        String rta = "Func " + razon[2] + " \t " + razon[1] + " [" + razon[3] + " " + razon[4] + "]: " + razon[5];
        if (razon.length >= 7) {
            rta += " (" + razon[6] + ")";
        }


        //System.out.println(rta);

        return rta + "\n";
    }
    
    @Override
    public String cambiarTurnos(Date date1, Funcionario f1, Vistaprogramacion vp1, Date date2, Funcionario f2, Vistaprogramacion vp2, Funcionario mod, Long countByDep) {
        jornadasQty = countByDep;
        return cambiarTurnos(date1, f1, vp1, date2, f2, vp2, mod);
    }

    @Override
    public String cambiarTurnos(Date date1, Funcionario f1, Vistaprogramacion vp1, Date date2, Funcionario f2, Vistaprogramacion vp2, Funcionario mod) {
        String turnFromPositionDiaryMessage = null;
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
                    if (vp.getTurTipo().equals(10L)) {
                        turnFromPositionDiaryMessage = "  El usuario " + vp.getFunAlias() + " está ejecutando el turno " + vp.getPjAlias() + " en registro del Diario de Posiciones y no puede ejecutar más turnos ese día";
                    }
                }
                error += "]";
                if (turnFromPositionDiaryMessage != null) {
                    error += turnFromPositionDiaryMessage;
                }
                return error;
            }


            if (programacionTotalServiceBean == null) {
                Logger.getLogger(ModificarTurnoServiceBean.class.getName()).log(Level.WARNING, "programacionTotalServiceBean Nulo ", new Exception("programacionTotalServiceBean Nulo "));

            }
            String resultado = "-;-;-;-;-;OK;-;";
            if (turno1.getTurTipo() == 1L || turno1.getTurTipo() == 2) {
                resultado = validateTurn(turno1, null);
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
                resultado_1 = validateTurn(turno1, turno2.getTurId());
            }


            String resultado_2 = "-;-;-;-;-;OK;-;";;
            if (turno2.getTurTipo() == 1L || turno2.getTurTipo() == 2) {
                resultado_2 = validateTurn(turno2, turno1.getTurId());
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

        String resultado = validateTurn(turno, null);


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

    public List<Turno> getTurnoPorUsuarioFecha(Funcionario f, Date date) {
        try {
            Query q = em.createQuery("select t from Turno t where t.funcionario.funId = :funId and t.turFecha = :fecha");
            q.setParameter("funId", f.getFunId());
            q.setParameter("fecha", date);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String validateTurn(Turno turno, Long cambio) {
        //System.out.println("Llegar a validateTurn ");
        String result = null;
        this.log = new ArrayList();
        this.debug = Boolean.valueOf(true);
        this.programming = turno.getProgramacion();
        List<Turno> turnos = getTurnosFunPro(turno.getFuncionario().getFunId().longValue(), turno.getProgramacion().getProId().longValue());
        this.days = new ArrayList();

        Calendar current = Calendar.getInstance();
        current.setTime(this.programming.getProFechaInicio());
        while (current.getTime().compareTo(this.programming.getProFechaFin()) <= 0) {
            this.days.add(new Day(current.getTime()));
            current.add(5, 1);
        }
        Functionary fun = new Functionary(turno.getFuncionario());
        fun.setAvailable(Boolean.valueOf(true));
        for (Day day : this.days) {
            ArrayList<Turn> turns = new ArrayList();
            for (Turno turno1 : turnos) {
                if (turno1.getTurFecha().compareTo(day.getDate()) == 0) {
                    if (cambio == null) {
                        turns.add(convertTurno2Turn(turno1, fun));
                    } else if (cambio != turno1.getTurId()) {
                        turns.add(convertTurno2Turn(turno1, fun));
                    }
                }
            }
            if (turno.getTurFecha().compareTo(day.getDate()) == 0) {
                Turn turn = convertTurno2Turn(turno, fun);
                turn.setFunctionary(null);
                turns.add(turn);
            }
            day.setTurns(turns);
        }
        this.enabledPositions = getEnabledPositions();
        this.restrictivePeriod = getRestrictivePeriod();
        this.sequences = getSequences();
        this.holydays = getDiaFestivo();


        prepareSetting();

        this.weeks = new ArrayList();
        Week week = new Week();
        for (Day day : this.days) {
            current.setTime(day.getDate());
            if ((day.getDate().compareTo(this.programming.getProFechaInicio()) != 0) && (current.get(7) == 1) && (!isHolyDay(day.getDate()).booleanValue())) {
                this.weeks.add(week);
                week = new Week();
            }
            week.addDay(day);
        }
        if (week.getDays().size() > 0) {
            this.weeks.add(week);
        }
        int tipo = 0;

        for (Day day : this.days) {
            if (day.getDate().compareTo(turno.getTurFecha()) == 0) {
                for (Turn turn : day.getTurns()) {
                    if (turn.getFunctionary() == null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(day.getDate());

                        Turn ordinary = getTurnOfFunctionary(fun, c.getTime(), 0, -1);
                        if (ordinary == null) {
                            validateOrdinaryTurn(fun, turn, day.getDate());
                            tipo = 1;
                        } else if ((turn.getPeriod().getStart() < ordinary.getPeriod().getEnd()) && (ordinary.getPeriod().getTotal() + turn.getPeriod().getTotal() < 24)) {
                            turn.setFunctionary(fun);
                            validateExtraordinaryTurn(turn, ordinary, day.getDate(), ordinary.getFunctionary().getMaxHoursExtra(), true);
                            tipo = 2;
                        } else {
                            validateExtraordinaryTurn(ordinary, turn, day.getDate(), ordinary.getFunctionary().getMaxHoursExtra(), true);
                            tipo = 2;
                        }
                    }
                }
            }
        }
        if (this.log.size() > 0) {
            result = (String) this.log.get(0);
            result += tipo + ";";
        }
        return result;
    }

    private List<Turno> getTurnosFunPro(long funId, long proId) {
        try {
            this.em.clear();
            this.em.flush();

            Query q1 = this.em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId = :funId AND t.programacion.proId = :proId");

            q1.setParameter("proId", Long.valueOf(proId));
            q1.setParameter("funId", Long.valueOf(funId));

            return q1.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Turn convertTurno2Turn(Turno turno1, Functionary fun) {
        try {
            if ((turno1.getTurTipo().longValue() == 1L) || (turno1.getTurTipo().longValue() == 2L)) {
                PosicionJornada pj = getPositionPeriod(turno1.getTurPosicionJornada());

                Position pos = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                Period per = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                if (pj.getJornada().getJornadaObligatoria() != null) {
                    per.setRequiredPeriod(pj.getJornada().getJornadaObligatoria().getJoId().longValue());
                }
                Turn turn = new Turn(per, pos, fun, turno1.getTurPosicionJornada().longValue());
                if (turno1.getTurTipo().longValue() == 1L) {
                    turn.setType(1);
                } else {
                    turn.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
                }
                return turn;
            }
            if (turno1.getTurTipo().longValue() == 3L) {
                TurnoEspFuncionario te = getSpecialTurn(turno1.getTurPosicionJornada());
                //System.out.println("turno id\t" + turno1.getTurId() + "\tpj\t" + turno1.getTurPosicionJornada());
                if (te == null) {
                    //System.out.println("fue nulo :( ");
                } else {
                    Position pos = new Position(0L, te.getTurnoEspecial().getTeSigla());
                    Period per = new Period(0L, "", te.getTurnoEspecial().getTeHinicio().byteValue(), te.getTurnoEspecial().getTeHfin().byteValue() + 1);
                    Turn turn = new Turn(per, pos, fun, turno1.getTurPosicionJornada().longValue());
                    turn.setPermiteHorasExtra(false);
                    turn.setType(ProgramacionTotalServiceBean.Type.SPECIAL);
                    return turn;
                }
            }
            Period period = new Period(0L, "", 0, 24);
            Position position = new Position(0L, "DES");
            Turn turn = new Turn(period, position, fun, 0L);
            turn.setType(ProgramacionTotalServiceBean.Type.REST);
            turn.setPermiteHorasExtra(false);
            return turn;
        } catch (Exception e) {
            Period period = new Period(0L, "", 0, 24);
            Position position = new Position(0L, "DES");
            Turn turn = new Turn(period, position, fun, 0L);
            turn.setType(ProgramacionTotalServiceBean.Type.REST);
            turn.setPermiteHorasExtra(false);
            return turn;
        }
    }

    private PosicionJornada getPositionPeriod(Long id) {
        return (PosicionJornada) this.em.find(PosicionJornada.class, id);
    }

    private TurnoEspFuncionario getSpecialTurn(Long id) {
        return (TurnoEspFuncionario) this.em.find(TurnoEspFuncionario.class, id);
    }

    private List<PosicionHabilitada> getEnabledPositions() {
        try {
            Query query = this.em.createQuery("SELECT p FROM PosicionHabilitada p WHERE p.funcionario.dependencia.depId = :depId and p.posicion.posEstado= :estado and p.posicion.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            //System.out.println("queryPH = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<JornadaRestriccion> getRestrictivePeriod() {
        try {
            Query query = this.em.createQuery("SELECT j FROM JornadaRestriccion j WHERE j.jornada.dependencia.depId = :depId AND j.jornadaEv.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<DiaFestivo> getDiaFestivo() {
        try {
            Query query = this.em.createQuery("SELECT d FROM DiaFestivo d WHERE d.dfFecha between :start and :finish");
            query.setParameter("start", this.programming.getProFechaInicio(), TemporalType.DATE);
            query.setParameter("finish", this.programming.getProFechaFin(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<DetSecuencia> getSequences() {
        try {
            Query query = this.em.createQuery("SELECT s FROM DetSecuencia s WHERE s.secuencia.dependencia.depId=:depId AND s.secuencia.secuEstado = :estado ORDER BY s.secuencia.secuId, s.dsOrden");
            query.setParameter("estado", "Activo");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private void prepareSetting() {
        this.setting = new Setting();
        List<RestriccionDependencia> restricciones = getRestrictions();
        for (RestriccionDependencia rd : restricciones) {
            if (rd.getRdValor() != null) {
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 4L) {
                    this.setting.setPeriodId(rd.getRdValor().longValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 10L) {
                    boolean max = false;
                    if (rd.getRdValor().longValue() > 0L) {
                        max = true;
                    }
                    this.setting.setMaxHoursExtra(max);
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 5L) {
                    this.setting.setTimeRecess(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 1L) {
                    this.setting.setMaxWorkedHoursByDay(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 2L) {
                    this.setting.setMaxContinuosDaysExtra(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 7L) {
                    this.setting.setMaxContinuosPeriod(rd.getRdValor().intValue());
                }
                if (rd.getRestriccionProgramacion().getRpId().longValue() == 9L) {
                    this.setting.setMaxContinuosDaysExtraModification(rd.getRdValor().intValue());
                }
            }
        }
        this.jornadas = getJornadas();
    }

    private List<Jornada> getJornadas() {
        try {
            Query query = this.em.createQuery("SELECT j FROM Jornada j WHERE j.dependencia.depId = :depId and j.joEstado = :estado ORDER BY j.joHoraInicio ASC");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Activo");
            //System.out.println("queryJornadas = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<RestriccionDependencia> getRestrictions() {
        try {
            Query query = this.em.createQuery("SELECT r FROM RestriccionDependencia r WHERE r.dependencia.depId = :depId");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Boolean isHolyDay(Date date) {
        for (DiaFestivo day : this.holydays) {
            if (date.compareTo(day.getDfFecha()) == 0) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    private Turn getTurnOfFunctionary(Functionary fun, Date current, int amountDay, int type) {
        Calendar eval = Calendar.getInstance();
        eval.setTime(current);
        eval.add(5, amountDay);
        String d = new SimpleDateFormat("dd/MM/yyyy").format(eval.getTime());
        for (Day day : this.days) {
            if (day.getDate().compareTo(eval.getTime()) == 0) {
                for (Turn turn : turnSortByType(day.getTurns())) {
                    if (type == -1) {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.ANY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((turn.getType() == ProgramacionTotalServiceBean.Type.ORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((turn.getType() == ProgramacionTotalServiceBean.Type.EXTRAORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                    } else if (type == -2) {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                            return turn;
                        }
                    } else {
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.ORDINARY) || (turn.getType() == type))) {
                            return turn;
                        }
                        if ((turn.getFunctionary() != null) && (turn.getPeriod() != null) && (turn.getPosition() != null) && (turn.getFunctionary().getId() == fun.getId()) && ((type == ProgramacionTotalServiceBean.Type.TROP) || (turn.getType() == type))) {
                            return turn;
                        }

                    }
                }
            }
        }
        return null;
    }

    public List<Turn> turnSortByType(List<Turn> turns) {
        Turn[] auxArr = new Turn[turns.size()];
        auxArr = turns.toArray(auxArr);

        Turn temp = null;

        for (int i = 0; i < (auxArr.length - 1); i++) {
            for (int j = 1; j < (auxArr.length - i - 1); j++) {
                if (auxArr[j + 1].getType() < auxArr[j].getType()) {
                    //swap the elements!
                    temp = auxArr[j + 1];
                    auxArr[j + 1] = auxArr[j];
                    auxArr[j] = temp;
                }
            }
        }
        List<Turn> respuesta = Arrays.asList(auxArr);
        return respuesta;
    }

    private Boolean validateOrdinaryTurn(Functionary fun, Turn turn, Date current) {
        Calendar c = Calendar.getInstance();
        c.setTime(current);

        String s = "Ordinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + fun.getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";;";
        if (!isJornadaNoLaboral(turn, fun)) {
            if (fun.isAvailable().booleanValue()) {
                if (satisfiesEnabledPosition(fun.getId(), turn.getPosition().getId()).booleanValue()) {
                    Turn previous = getTurnOfFunctionary(fun, current, -1, -1);
                    if (current.compareTo(this.programming.getProFechaInicio()) == 0) {
                        previous = getTurnLastDayFromLastProg(fun);
                    }
                    Turn next = getTurnOfFunctionary(fun, current, ProgramacionTotalServiceBean.TargetDay.NEXT, ProgramacionTotalServiceBean.Type.ANY);
                    if (next != null && turn != null) {
                        if (!satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()) {
                            write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                            return false;
                        }
                    }
                    if (satisfiesRequiredPeriod(previous, turn).booleanValue()) {
                        if (satisfiesRestrictionPreviousPeriod(previous, turn, false).booleanValue()) {
                            if (satisfiesRecessPeriod(previous, turn, Boolean.valueOf(false), current).booleanValue()) {
                                if (satisfiesSequence(previous, turn).booleanValue()) {
                                    if (satisfiesInactivePosition(turn, current)) {
                                        if (satisfiesPetitions(turn, current, fun)) {
                                            write(s + "OK;");
                                            return Boolean.valueOf(true);
                                        } else {
                                            write(s + "El funcionario tiene un peticion para la jornada: " + turn.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        }
                                    } else {
                                        write(s + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + "esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                    }
                                }
                                write(s + "No cumple con secuencia;");
                            } else {
                                write(s + "No cumple con la restricion de jornada de receso;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                            }
                        } else {
                            write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                        }
                    } else if (previous == null) {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior por que no existe;");
                    } else {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                    }
                } else {
                    write(s + "Funcionario No Tiene Posicion Habilitada;" + turn.getPosition().getAlias() + ";");
                }
            } else {
                write(s + "Funcionario ya tiene turno programado para ese dia;");
            }
        } else {
            System.out.println(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;");
            write(s + "El funcionario " + fun.getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
        }
        return Boolean.valueOf(false);
    }

    private Boolean validateExtraordinaryTurn(Turn turn, Turn extra, Date current, int hourEval, boolean exceeded) {
        Calendar c = Calendar.getInstance();
        c.setTime(current);


        String s = "ExtraOrdinario;" + c.get(5) + "/" + (c.get(2) + 1) + ";" + turn.getFunctionary().getAlias() + ";" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";" + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";";
        if (satisfiesInitialConditionalExtra(s, turn, extra, current, hourEval, exceeded).booleanValue()) {
            if (((satisfiesEnabledPosition(turn.getFunctionary().getId(), turn.getPosition().getId()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), extra.getPosition().getId()).booleanValue()))
                    || ((satisfiesEnabledSpecialPosition(turn.getFunctionary().getId(), turn.getPositionPeriod()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), extra.getPosition().getId()).booleanValue()))
                    || ((satisfiesEnabledSpecialPosition(turn.getFunctionary().getId(), extra.getPositionPeriod()).booleanValue())
                    && (satisfiesEnabledPosition(turn.getFunctionary().getId(), turn.getPosition().getId()).booleanValue()))) {

                Turn next = getTurnOfFunctionary(turn.getFunctionary(), current, 1, 1);
                if (next != null && turn != null) {
                    if (!satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                if (next != null && extra != null) {
                    if (!satisfiesRestrictionPreviousPeriod(extra, next, true).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                Turn previous = getTurnOfFunctionary(turn.getFunctionary(), current, -1, ProgramacionTotalServiceBean.Type.EXTRAORDINARY);

                if (previous == null) {
                    previous = getTurnOfFunctionary(turn.getFunctionary(), current, -1, ProgramacionTotalServiceBean.Type.ANY);
                }
                if (previous != null && turn != null) {
                    if (!satisfiesRestrictionPreviousPeriod(previous, turn, true).booleanValue()) {
                        write(s + "No cumple con la restricion de jornada restrictiva con respecto a la jornada anterior;" + previous.getPeriod().getAlias() + previous.getPosition().getAlias() + ";");
                        return false;
                    }
                }
                if (satisfiesMaxContinuosPeriod(turn, extra, next, current).booleanValue()) {
                    if ((satisfiesRequiredPeriod(turn, extra).booleanValue()) && (satisfiesRequiredPeriod(extra, next).booleanValue())) {
                        if ((satisfiesRestrictionPreviousPeriod(turn, extra, true).booleanValue()) && (satisfiesRestrictionPreviousPeriod(extra, next, true).booleanValue()) && ((satisfiesRestrictionPreviousPeriod(turn, next, false).booleanValue()))) {
                            if ((satisfiesRecessPeriod(turn, extra, Boolean.valueOf(true)).booleanValue()) && (satisfiesRecessPeriod(extra, next, Boolean.valueOf(false)).booleanValue())) {
                                if ((satisfiesSequence(turn, extra).booleanValue()) && (satisfiesSequence(extra, next).booleanValue())) {
                                    if (!satisfiesInactivePosition(extra, current)) {
                                        write(s + "La posicion: " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                    } else if (!satisfiesInactivePosition(turn, current)) {
                                        write(s + "La posicion: " + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " esta inactiva el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                    } else {
                                        if (!satisfiesPetitions(turn, current, turn.getFunctionary())) {
                                            write(s + "El funcionario tiene un peticion para la jornada: " + turn.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        } else if (!satisfiesPetitions(extra, current, turn.getFunctionary())) {
                                            write(s + "El funcionario tiene un peticion para la jornada: " + extra.getPeriod().getAlias() + " el dia: " + new SimpleDateFormat("dd/MM/yy").format(current));
                                        } else {

                                            if (isJornadaNoLaboral(turn, turn.getFunctionary())) {
                                                write("Ordinario;" + "El funcionario " + turn.getFunctionary().getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                            } else if (isJornadaNoLaboral(extra, turn.getFunctionary())) {
                                                write("Ordinario;" + "El funcionario " + turn.getFunctionary().getAlias() + " esta configurado con la jornada " + turn.getPeriod().getAlias() + " como no laboral;;;;");
                                            } else {
                                                write(s + "OK;");
                                                return Boolean.valueOf(true);
                                            }
                                        }
                                    }
                                }
                                write(s + "No cumple con secuencias;");
                            } else if (next == null) {
                                write(s + "No cumple con la restricion de jornada receso anterior;");
                            } else {
                                write(s + "No cumple con la restricion de jornada receso anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                            }
                        } else if (next == null) {
                            write(s + "No cumple con la restricion de jornada restrictivas anterior;");
                        } else {
                            write(s + "No cumple con la restricion de jornada restrictivas anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                        }
                    } else if (next == null) {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior;");
                    } else {
                        write(s + "No cumple con la restricion de jornada obligatoria anterior;Tener en cuenta turno sig - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + ";");
                    }
                } else if (next != null) {
                    write(s + "No cumple con la regla de maximo numero de jornadas continuas (" + this.setting.getMaxContinuosPeriod() + ");" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " - " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + " - " + next.getPeriod().getAlias() + next.getPosition().getAlias() + " ;");
                } else {
                    write(s + "No cumple con la regla de maximo numero de jornadas continuas (" + this.setting.getMaxContinuosPeriod() + ");" + turn.getPeriod().getAlias() + turn.getPosition().getAlias() + " - " + extra.getPeriod().getAlias() + extra.getPosition().getAlias() + ";");
                }
            } else {
                write(s + "Funcionario No Tiene Posicion Habilitada extra.getPosition().getId() validateExtraordinaryTurn 2;" + turn.getPosition().getAlias() + ";");
            }
        }
        return Boolean.valueOf(false);
    }

    private boolean isJornadaNoLaboral(Turn turn, Functionary fun) {
        List<JornadaNoLaborable> jornadaNoLaborables = this.jornadaNoLaboralService.getListaJornadasFuncionario(fun.getId());
        if (jornadaNoLaborables != null && jornadaNoLaborables.size() == this.jornadasQty.longValue()) {
            return true;
        }
        if (jornadaNoLaborables != null && jornadaNoLaborables.size() < this.jornadasQty.longValue()) {
            for (JornadaNoLaborable jornadaNoLaborable : jornadaNoLaborables) {
                long currentJoId = turn.getPeriod().getId();
                if (currentJoId == jornadaNoLaborable.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean satisfiesPetitions(Turn current, Date fecha, Functionary fun) {
        if ((current != null)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            if (current.getFunctionary() == null) {
                List<PermisoEspecial> petitions = getPetitions(fun.getId(), fecha);
                for (PermisoEspecial especial : petitions) {
                    List<Jornada> listaJornadas = especial.getListaJornadas();
                    if (listaJornadas.size() == this.jornadasQty.longValue()) {
                        return false;
                    }
                    if (listaJornadas.size() < this.jornadasQty.longValue()) {
                        for (Jornada j : listaJornadas) {
                            if (current.getPeriod().getId() == j.getJoId()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesEnabledPosition(long funId, long posId) {
        List<PosicionHabilitada> positionsIdsList = getEnabledPositionsFunctionary(funId, posId);
        if (positionsIdsList != null) {
            for (PosicionHabilitada pos : positionsIdsList) {
                if (pos.getPosicion().getPosId() == posId) {
                    return Boolean.valueOf(true);
                }
            }
        }
        return Boolean.valueOf(false);
    }

    private Boolean satisfiesEnabledSpecialPosition(long funId, long posId) {
        List<TurnoEspFuncionario> tes = getSpecialTurns();
        for (TurnoEspFuncionario te : tes) {
            if ((te.getTefId().longValue() == posId) && (te.getFuncionario().getFunId().longValue() == funId)) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    private List<TurnoEspFuncionario> getSpecialTurns() {
        try {
            Calendar actual = Calendar.getInstance();
            Query query = this.em.createQuery("SELECT t FROM TurnoEspFuncionario t WHERE t.turnoEspecial.dependencia.depId= :depId AND t.tefEstado= :estado AND t.funcionario.dependencia.depId = :depId AND t.funcionario.funFvCertmedico > :fecha AND t.funcionario.funFvCurso > :fecha AND t.funcionario.funFvEvaluacion > :fecha AND t.funcionario.funEstado = 'Activo' AND ((t.tefFini between :start and :finish) or (t.tefFfin between :start and :finish)) ");
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            query.setParameter("estado", "Programado");
            query.setParameter("start", this.programming.getProFechaInicio(), TemporalType.DATE);
            query.setParameter("finish", this.programming.getProFechaFin(), TemporalType.DATE);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            //System.out.println("query = " + query.toString());

            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private List<PosicionHabilitada> getEnabledPositionsFunctionary(Long funId, Long posId) {
        try {
            Query query = em.createQuery("SELECT p FROM PosicionHabilitada p WHERE p.funcionario.funId = :funId and p.posicion.posEstado= :estado and p.posicion.posId = :posId ");
            query.setParameter("funId", funId);
            query.setParameter("estado", "Activo");
            query.setParameter("posId", posId);
            return (List<PosicionHabilitada>) query.getResultList();
        } catch (NoResultException nre) {
            return new ArrayList<PosicionHabilitada>();
        }

    }

    private List<PermisoEspecial> getPetitions(Long funId, Date current) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(current);
            Query query = this.em.createNamedQuery("PermisoEspecial.findByUser");
            query.setParameter("funId", funId);
            query.setParameter("fecha", actual.getTime(), TemporalType.DATE);
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Boolean satisfiesInactivePosition(Turn current, Date fecha) {
        if ((current != null)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            List<PosicionInactiva> inactive = getInactivePositions(current.getPositionPeriod(), fecha);
            if (inactive != null) {
                for (PosicionInactiva inactiva : inactive) {
                    String fechaBd = new SimpleDateFormat("dd/MM/yyyy").format(inactiva.getFecha());
                    String actual = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
                    if (actual.equalsIgnoreCase(fechaBd)) {
                        return false;
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private List<PosicionInactiva> getInactivePositions(Long pjId, Date fecha) {
        try {
            Calendar actual = Calendar.getInstance();
            actual.setTime(fecha);
            Query query = this.em.createQuery("SELECT p FROM PosicionInactiva p WHERE p.posicionJornada.pjId = :pjId");
            query.setParameter("pjId", pjId);
            //System.out.println("queryIP = " + query.toString());
            return query.getResultList();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Boolean satisfiesSequence(Turn previous, Turn current) {
        if ((previous != null) && (current != null)) {
            long sec = -1L;
            long back = -1L;
            for (DetSecuencia ds : this.sequences) {
                if (ds.getSecuencia().getSecuId().longValue() != sec) {
                    sec = ds.getSecuencia().getSecuId().longValue();
                    back = -1L;
                }
                if ((ds.getJornada().getJoId().longValue() == current.getPeriod().getId()) && (previous.getPeriod().getId() == back)) {
                    return Boolean.valueOf(true);
                }
                back = ds.getJornada().getJoId().longValue();
            }
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(true);
    }

    private Turn getTurnLastDayFromLastProg(Functionary fun) {
        this.lastDay = getLastDay();
        if (this.lastDay != null) {
            for (Turn turn : this.lastDay.getTurns()) {
                if ((turn.getFunctionary() != null) && (turn.getFunctionary().getId() == fun.getId())) {
                    return turn;
                }
            }
        }
        return null;
    }

    private Day getLastDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.programming.getProFechaInicio());
        c.add(5, -1);
        try {
            Query query = this.em.createQuery("SELECT t FROM Turno t WHERE t.funcionario.funId is not null and t.programacion.dependencia.depId = :depId AND t.turFecha= :fecha AND t.programacion.proEstado = 1 AND (t.turTipo =2 OR ( t.turTipo =1 AND 0=(SELECT count(tt) FROM Turno tt WHERE tt.funcionario.funId is not null and t.funcionario.funId = tt.funcionario.funId AND t.turFecha=tt.turFecha AND t.turId <> tt.turId ) ) )");

            query.setParameter("fecha", c.getTime(), TemporalType.DATE);
            query.setParameter("depId", this.programming.getDependencia().getDepId());
            List<Turno> turnos = query.getResultList();

            ArrayList<Turn> turns = new ArrayList();
            for (Turno turno : turnos) {
                if (turno.getFuncionario() != null) {
                    Calendar f = Calendar.getInstance();
                    f.setTime(turno.getTurFecha());

                    PosicionJornada pj = (PosicionJornada) this.em.find(PosicionJornada.class, turno.getTurPosicionJornada());
                    Period period = new Period(pj.getJornada().getJoId().longValue(), pj.getJornada().getJoAlias(), pj.getJornada().getJoHoraInicio().byteValue(), pj.getJornada().getJoHoraFin().byteValue() + 1);
                    Position position = new Position(pj.getPosicion().getPosId().longValue(), pj.getPosicion().getPosicionNacional().getPnAlias());
                    Functionary fun = new Functionary(turno.getFuncionario());
                    Turn turn = new Turn(period, position, fun, turno.getTurPosicionJornada().longValue());
                    turns.add(turn);
                }
            }
            return new Day(c.getTime(), turns);
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Boolean satisfiesRestrictionPreviousPeriod(Turn previous, Turn current, boolean validateDiff) {
        if ((previous != null) && (current != null)) {
            JornadaRestriccion result = getRestrictivePeriod(previous.getPeriod().getId(), current.getPeriod().getId());
            if (previous.getPeriod().getAlias().equalsIgnoreCase("N") && current.getPeriod().getAlias().equalsIgnoreCase("M") && result != null) {
                return false;
            }
            int diffHrs = current.getPeriod().getStart() - (previous.getPeriod().getEnd() - 1);

            if (result != null) {
                if (validateDiff) {
                    return false;
                }

                if (diffHrs < 1) {
                    return false;
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private JornadaRestriccion getRestrictivePeriod(Long prev, Long current) {
        try {
            Query query = this.em.createNamedQuery("JornadaRestriccion.getRestrictions");
            query.setParameter("prev", prev);
            query.setParameter("current", current);

            return (JornadaRestriccion) query.getSingleResult();
        } catch (NoResultException nre) {
        }
        return null;
    }

    private Boolean satisfiesRequiredPeriod(Turn previous, Turn current) {
        if (current != null) {
            if (current.getPeriod().getRequiredPeriod() != -1L) {
                if (previous != null) {
                    if ((previous.getPeriod().getId() == current.getPeriod().getRequiredPeriod()) && (previous.getPosition().getId() == current.getPosition().getId())) {
                        return Boolean.valueOf(true);
                    }
                }
                return Boolean.valueOf(false);
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRecessPeriod(Turn previous, Turn current, Boolean same_day, Date currentDate) {
        Calendar r = Calendar.getInstance();
        r.setTime(currentDate);

        if ((previous != null) && (current != null)) {
            if ((this.setting.getPeriodId() != -1L) && (this.setting.getTimeRecess() != -1)) {
                if (previous.getPeriod().getId() == this.setting.getPeriodId()) {
                    int start = this.setting.getTimeRecess();
                    if (!same_day.booleanValue()) {
                        start %= 24;
                    }
                    Calendar today = Calendar.getInstance();
                    int year = r.get(Calendar.YEAR);
                    int month = r.get(Calendar.MONTH);
                    int day = r.get(Calendar.DATE);
                    today.set(year, month, day, current.getPeriod().getStart(), 0);

                    Calendar yesterday = Calendar.getInstance();
                    yesterday.setTime(currentDate);
                    yesterday.add(Calendar.DATE, -1);
                    yesterday.set(Calendar.HOUR_OF_DAY, previous.getPeriod().getEnd());
                    yesterday.set(Calendar.MINUTE, 0);

                    long resultado = StringDateUtil.diferenciaHorasDias(yesterday, today);
                    if (resultado < start) {
                        return Boolean.valueOf(false);
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesInitialConditionalExtra(String s, Turn turn, Turn extra, Date current, int hourEval, boolean exceeded) {
        extra.setType(ProgramacionTotalServiceBean.Type.EXTRAORDINARY);
        if (turn.getFunctionary().getCanExtra().booleanValue()) {
            if ((!this.setting.isMaxHoursExtra()) || ((hourEval > 0) && (turn.getFunctionary().getMaxHoursExtra() - extra.getPeriod().getTotal() > 0))) {
                if (extra.getPeriod().getStart() >= turn.getPeriod().getEnd()) {
                    if ((this.setting.getMaxWorkedHoursByDay() == -1) || (turn.getPeriod().getTotal() + extra.getPeriod().getTotal() <= this.setting.getMaxWorkedHoursByDay() + 1)) {
                        if (satisfiesMaxContinuosDaysExtra(turn, current, exceeded).booleanValue()) {
                            return Boolean.valueOf(true);
                        }
                        if (exceeded) {
                            write(s + "No cumple con la regla de maximo de dias continuos con horas extras;maximo de dias: " + (this.setting.getMaxContinuosDaysExtra() + this.setting.getMaxContinuosDaysExtraModification()) + ";");
                        } else {
                            write(s + "No cumple con la regla de maximo de dias continuos con horas extras;maximo de dias: " + this.setting.getMaxContinuosDaysExtra() + ";");
                        }
                    } else {
                        write(s + "El funcionario ha sobrepasado el numero de horas maximo que se trabaja al dia;" + this.setting.getMaxWorkedHoursByDay() + ";");
                    }
                } else {
                    write(s + "La jornada del turno extra es igual o anterior a la del turno ordinario;");
                }
            } else if (exceeded) {
                write(s + "El maximo numero de horas extras excedidas, permitido al funcionario se ha sobrepasado;Programadas: " + turn.getFunctionary().getInitialMaxHoursExtraExceeded() + " - Restantes:   " + turn.getFunctionary().getMaxHoursExtraExceeded() + ";");
            } else {
                write(s + "El maximo numero de horas extras, permitido al funcionario se ha sobrepasado;Programadas: " + turn.getFunctionary().getInitialMaxHoursExtra() + " - Restantes:   " + turn.getFunctionary().getMaxHoursExtra() + ";");
            }
        } else {
            write(s + "El funcionario no puede hacer horas extras;");
        }
        return Boolean.valueOf(false);
    }

    private Boolean satisfiesMaxContinuosDaysExtra(Turn turn, Date date, boolean exceed) {
        if ((this.setting.getMaxContinuosDaysExtra() > 0) && (turn != null)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int totalLeft = this.setting.getMaxContinuosDaysExtra();
            if (exceed) {
                totalLeft += this.setting.getMaxContinuosDaysExtraModification();
            }
            int limit = totalLeft;
            for (int i = 0; i < limit; i++) {
                Turn previous = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), -1, 2);
                if (previous == null) {
//                    return Boolean.valueOf(true);
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, -1);
            }
            calendar.setTime(date);
            for (int i = 0; i < limit; i++) {
                Turn next = getTurnOfFunctionary(turn.getFunctionary(), calendar.getTime(), 1, 2);
                if (next == null) {
                    break;
                }
                totalLeft--;
                if (totalLeft <= 0) {
                    return Boolean.valueOf(false);
                }
                calendar.add(5, 1);
            }
            if (totalLeft > 0) {
                return true;
            }
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesMaxContinuosPeriod(Turn turn, Turn extra, Turn next, Date date) {

        if ((this.setting.getMaxContinuosPeriod() > 0) && (turn != null) && (extra != null) && (turn.getType() == 1)) {
            if (turn.getPeriod().getEnd() == extra.getPeriod().getStart()) {
                if (this.setting.getMaxContinuosPeriod() >= 1) {
                    if ((next != null) && (extra.getPeriod().getEnd() >= 24) && (extra.getPeriod().getEnd() % 24 == next.getPeriod().getStart())) {
                        return Boolean.valueOf(false);
                    }
                } else {
                    return Boolean.valueOf(false);
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Boolean satisfiesRecessPeriod(Turn previous, Turn current, Boolean same_day) {
        if ((previous != null) && (current != null)) {
            if ((this.setting.getPeriodId() != -1L) && (this.setting.getTimeRecess() != -1)) {
                if (previous.getPeriod().getId() == this.setting.getPeriodId()) {
                    int start = previous.getPeriod().getEnd() + this.setting.getTimeRecess();
                    if (!same_day.booleanValue()) {
                        start %= 24;
                    }
                    if (current.getPeriod().getStart() < start) {
                        return Boolean.valueOf(false);
                    }
                }
            }
        }
        return Boolean.valueOf(true);
    }

    private Long totalDependenciasXJornada(Long dependencia) {
        String queryString = "SELECT Count(*) FROM JORNADA_OP WHERE JO_DEPENDENCIA = " + dependencia;
        Query query = em.createNativeQuery(queryString);
        return (Long) query.getSingleResult();
    }

    private void write(String s) {
        if (this.debug.booleanValue()) {
            this.log.add(s);
        }
    }
}
