
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.models;

/**
 *
 * @author Maris
 */
public class Marca {
     int codigoMarca;
    String Marca;

    public Marca(int codigoMarca, String Marca) {
        this.codigoMarca = codigoMarca;
        this.Marca = Marca;
    }

    public Marca(String Marca) {
        this.Marca = Marca;
    }

    
    
    public Marca() {
    }

    public int getCodigoMarca() {
        return codigoMarca;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }
    
    
}