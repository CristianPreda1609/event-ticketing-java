package com.e_ticketing.model;

import java.time.LocalDate;

public class Club extends Eveniment {
    private String dj;
    private int varstaMinima;

    public Club(int id, String nume, LocalDate data, int idLoc, int totalBilete,double pretBilet, String dj, int varstaMinima) {
        super(id,nume, data, idLoc, totalBilete,pretBilet);
        this.dj = dj;
        this.varstaMinima = varstaMinima;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public int getVarstaMinima() {
        return varstaMinima;
    }

    public void setVarstaMinima(int varstaMinima) {
        this.varstaMinima = varstaMinima;
    }

    @Override
    public String toString() {
        return super.toString() + " | DJ: " + dj + " | Vârstă minimă: " + varstaMinima + "+";
    }
}
