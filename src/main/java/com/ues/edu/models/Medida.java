/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author thebe
 */
public class Medida {
    int id_medida;
    String Medida;
    String abreviacion;

    public Medida() {
    }
    
    

    public Medida(int id_medida, String Medida, String abreviacion) {
        this.id_medida = id_medida;
        this.Medida = Medida;
        this.abreviacion = abreviacion;
    }

    public int getId_medida() {
        return id_medida;
    }

    public void setId_medida(int id_medida) {
        this.id_medida = id_medida;
    }

    public String getMedida() {
        return Medida;
    }

    public void setMedida(String Medida) {
        this.Medida = Medida;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

   

   

   

    
    
    
    
    
}
