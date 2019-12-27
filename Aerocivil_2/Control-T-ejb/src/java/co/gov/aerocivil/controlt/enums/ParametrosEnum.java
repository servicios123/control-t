/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.enums;

/**
 *
 * @author Administrador 
 */
public enum ParametrosEnum {
    //LDAP PARAMETERS
    autenticacion_db(0L),
    ldap_server(1L),
    ldap_server2(2L),
    ldap_server3(3L),
    //MAIL PARAMS
    mail_server(6L),
    mail_port(7L),
    mail_from(8L),
    //REPORT PARAMETERS
    rep_prog_clave(10L),
    rep_prog_version(11L),
    rep_prog_consec(12L),
    rep_prog_pie_pag(16L),
    rep_prog_fecha(17L),
    rep_ctr_diario_clave(13L),
    rep_ctr_diario_vers(14L),
    rep_ctr_diario_fecha(15L),

    rep_diario_sen_clave(19L), 
    rep_diario_sen_vers(20L),
    date_diario_sen_vers(21L), 
    rep_diario_sen_date(22L), 
    rep_diario_sen_pie(23L), 
    /*rep_ctr_dia_ind_clave(24L),     
    rep_ctr_dia_ind_vers(25L), 
    rep_ctr_dia_ind_fecha(26L)*/
    ;

    private Long id;

    private ParametrosEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
