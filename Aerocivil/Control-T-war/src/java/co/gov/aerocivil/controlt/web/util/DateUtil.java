/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.util;

import co.gov.aerocivil.controlt.enums.UnitTimeEnum;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrador
 */
public class DateUtil {

    public static Long daysBetween(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1 = setCeroHoras(c1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2 = setCeroHoras(c2);
        return Math.abs(c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 24 * 60 * 60);
    }

    public static Long getTimeDiff(Byte finHour, Byte finMin, Long iniHour, Long iniMin, UnitTimeEnum unit) {
        int div = 1000;
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, finHour);
        c2.set(Calendar.MINUTE, finMin);

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, iniHour.intValue());
        c1.set(Calendar.MINUTE, iniMin.intValue());

        return Math.abs(c2.getTimeInMillis() - c1.getTimeInMillis()) / unit.getMillisecs();
    }

    public static Calendar setCeroHoras(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static Date setCeroHoras(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c = setCeroHoras(c);
        d = new Date();
        d.setTime(c.getTimeInMillis());
        return d;
    }

    public static Date addDays(Date d, int days) {
        d = setCeroHoras(d);
        Long milis = d.getTime();
        d.setTime(milis + (days * 86400000L));
        return d;
    }

    public static Date addMonth(Date d, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c = setCeroHoras(c);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Date getFirstDayMonth(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.set(Calendar.DATE, 1);
        c = DateUtil.setCeroHoras(c);
        return c.getTime();
    }

    public static Date getLastDayMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DATE, maxDay);
        return c.getTime();
    }

    public static String formatDate(Date d) {
        if (d == null) {
            return "";
        }
        return formatDate(d, "dd/MM/yyyy");
    }

    public static String formatDateProg(Date d) {
        if (d == null) {
            return "";
        }
        return formatDate(d, "EE-dd");
    }

    public static String formatDate(Date d, String pattern) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(d);
    }

    public static String formatJornada(String jornada) {
        try {
            if (jornada == null || "".equals(jornada.trim())) {
                return null;
            }
            String[] hora = jornada.split("-");
            return formatHora(hora[0]) + "-" + formatHora(hora[1]);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatHora(String jorIni) {
        try {
            if (jorIni == null || "".equals(jorIni.trim())) {
                return null;
            }
            Calendar c = Calendar.getInstance();
            String[] hora = jorIni.split(":");
            c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hora[0]));
            c.set(Calendar.MINUTE, Integer.valueOf(hora[1]));
            DateFormat df = new SimpleDateFormat("HH:mm");
            return df.format(c.getTime());
        } catch (Exception e) {
            return null;
        }
    }
    
    public static long diferenciaHorasDias(Calendar fechaInicial, Calendar fechaFinal) {
        long diferenciaHoras = 0;
        long milisegundos_dia = 24 * 60 * 60 * 1000;
        diferenciaHoras = (fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / milisegundos_dia;
        if (diferenciaHoras > 0) { // Lo Multiplicaos por 24 por que estamos utilizando el formato militar 
            diferenciaHoras *= 24;
        }
        return diferenciaHoras;
    }
}
