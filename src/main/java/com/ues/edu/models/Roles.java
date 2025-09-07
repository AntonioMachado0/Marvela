/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author mayel
 */
public class Roles {
    
   private int codigoRoles;
   private  String nombreRol;

    public Roles() {
    }

    public Roles(String nombreRol) {
        this.nombreRol = nombreRol;
    }
    
    

    public Roles(int codigoRoles, String nombreRol) {
        this.codigoRoles = codigoRoles;
        this.nombreRol = nombreRol;
    }

    public int getCodigoRoles() {
        return codigoRoles;
    }

    public void setCodigoRoles(int codigoRoles) {
        this.codigoRoles = codigoRoles;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
    
    
    
}
