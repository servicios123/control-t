/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.aerocivil.controlt.web.enums;

/**
 *
 * @author juancamilo
 */
public enum ValoresLugarSuceso {

    CUNDINAMARCA("CUNDINAMARCA"),
    ATLANTICO("ATLANTICO"),
    NORTE_SANTANDER("NORTE DE SANTANDER"),
    ANTIOQUIA("ANTIOQUIA"),
    ELDORADO("102"),
    CORTISSOZ("141"),
    PALONEGRO("162"),
    CAMILO_DAZA("163"),
    FLAMINIO_SUAREZ_CAMACHO("182"),
    OLAYA_HERRERA("131"),
    JOSE_MARIA_CORDOBA("130"),
    TORRE_CONTROL("222"),
    CENTRO_CONTROL("247"),
    ATS("ATS"),
    AIS("AIS"),
    SKBO("SKBO"),
    SKGY("SKGY"),
    SKMD("SKMD"),
    SKRG("SKRG"),
    ;
    private String value;

    private ValoresLugarSuceso(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    
}
