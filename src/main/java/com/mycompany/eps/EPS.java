package com.mycompany.eps;

public class EPS implements Comparable<EPS> {
    private final String nombre;
    private final int edad;
    private final String afiliacion; // POS o PC
    private final boolean embarazo;
    private final boolean limitacionMotriz;
    private final int prioridad;

    public EPS(String nombre, int edad, String afiliacion, boolean embarazo, boolean limitacionMotriz) {
        this.nombre = nombre;
        this.edad = edad;
        this.afiliacion = afiliacion;
        this.embarazo = embarazo;
        this.limitacionMotriz = limitacionMotriz;
        this.prioridad = calcularPrioridad();
    }

    private int calcularPrioridad() {
        int p = 0;
        if (edad >= 60 || edad < 12) p += 3; // Personas mayores de 60 o menores de 12 años
        if (embarazo) p += 2; // Mujeres embarazadas
        if (limitacionMotriz) p += 2; // Pacientes con limitación motriz
        if (afiliacion.equalsIgnoreCase("PC")) p += 1; // Afiliados a plan complementario
        return p;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public int compareTo(EPS other) {
        return Integer.compare(other.getPrioridad(), this.getPrioridad());
    }

    @Override
    public String toString() {
        return nombre + " (Prioridad: " + prioridad + ")";
    }
    
    
    
      public static void main(String[] args) {
    
    }
}
