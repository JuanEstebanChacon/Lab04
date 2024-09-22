package com.mycompany.eps;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.PriorityQueue;

// Clase Paciente
class Paciente implements Comparable<Paciente> {
    private final String nombre;
    private final int edad;
    private final String afiliacion; // POS o PC
    private final boolean embarazo;
    private final boolean limitacionMotriz;
    private final int prioridad;

    public Paciente(String nombre, int edad, String afiliacion, boolean embarazo, boolean limitacionMotriz) {
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

    public int getEdad() {
        return edad;
    }

    public String getAfiliacion() {
        return afiliacion;
    }

    public boolean isEmbarazo() {
        return embarazo;
    }

    public boolean isLimitacionMotriz() {
        return limitacionMotriz;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public int compareTo(Paciente other) {
        return Integer.compare(other.getPrioridad(), this.getPrioridad());
    }

    @Override
    public String toString() {
        return nombre + " (Prioridad: " + prioridad + ")";
    }
}

// Clase TurnoManager para manejar la cola de turnos
class TurnoManager {
    private final PriorityQueue<Paciente> colaTurnos;
    private Paciente turnoActual;

    public TurnoManager() {
        colaTurnos = new PriorityQueue<>();
    }

    public void agregarPaciente(Paciente paciente) {
        colaTurnos.add(paciente);
    }

    public Paciente siguienteTurno() {
        turnoActual = colaTurnos.poll();
        return turnoActual;
    }

    public Paciente getTurnoActual() {
        return turnoActual;
    }

    public Paciente getProximoTurno() {
        return colaTurnos.peek();
    }

    public int turnosPendientes() {
        return colaTurnos.size();
    }

    public boolean hayTurnos() {
        return !colaTurnos.isEmpty();
    }

    public void extenderTurnoActual() {
        // Extender turno actual si es necesario
    }
}

// Interfaz gráfica principal con Swing
public class EPS extends JFrame {
    private final TurnoManager turnoManager;
    private final JLabel turnoActualLabel;
    private final JLabel turnoActualDetalles;
    private final JLabel proximoTurnoLabel;
    private final JLabel proximoTurnoDetalles;
    private JLabel tiempoRestanteLabel;
    private final JLabel turnosPendientesLabel;
    private final Timer timer;
    private int tiempoRestante;

    public EPS() {
        turnoManager = new TurnoManager();
        setTitle("Sistema de Turnos EPS");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de información de turnos
        JPanel panelDatos = new JPanel(new GridLayout(6, 2));
        turnoActualLabel = new JLabel("Turno Actual: ");
        turnoActualDetalles = new JLabel("Detalles del turno actual: ");
        proximoTurnoLabel = new JLabel("Próximo Turno: ");
        proximoTurnoDetalles = new JLabel("Detalles del próximo turno: ");
        tiempoRestanteLabel = new JLabel("Tiempo restante: 5s");
        turnosPendientesLabel = new JLabel("Turnos Pendientes: 0");

        panelDatos.add(turnoActualLabel);
        panelDatos.add(turnoActualDetalles);
        panelDatos.add(proximoTurnoLabel);
        panelDatos.add(proximoTurnoDetalles);
        panelDatos.add(tiempoRestanteLabel);
        panelDatos.add(turnosPendientesLabel);

        add(panelDatos, BorderLayout.NORTH);

        // Botón para agregar un paciente
        JButton agregarPacienteBtn = new JButton("Agregar Paciente");
        agregarPacienteBtn.addActionListener((ActionEvent e) -> {
            agregarPaciente();
        });


        // Panel con los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(agregarPacienteBtn);

        add(panelBotones, BorderLayout.SOUTH);

        // Timer para simular el paso del tiempo y los turnos automáticos
        tiempoRestante = 5;
        timer = new Timer(1000, (ActionEvent e) -> {
            tiempoRestante--;
            if (tiempoRestante <= 0) {
                tiempoRestante = 5;
                procesarSiguienteTurno();
            }
            tiempoRestanteLabel.setText("Tiempo restante: " + tiempoRestante + "s");
        });
        timer.start();
    }

    // Método para agregar un paciente nuevo
    private void agregarPaciente() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del paciente:");
        int edad = Integer.parseInt(JOptionPane.showInputDialog(this, "Edad:"));
        String afiliacion = JOptionPane.showInputDialog(this, "Afiliación (POS/PC):");
        boolean embarazo = JOptionPane.showConfirmDialog(this, "¿Embarazo?", "Condición especial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        boolean limitacionMotriz = JOptionPane.showConfirmDialog(this, "¿Limitación motriz?", "Condición especial", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION;

        Paciente paciente = new Paciente(nombre, edad, afiliacion, embarazo, limitacionMotriz);
        turnoManager.agregarPaciente(paciente);
        turnosPendientesLabel.setText("Turnos Pendientes: " + turnoManager.turnosPendientes());
    }

    // Método para procesar el siguiente turno en la cola
    private void procesarSiguienteTurno() {
        if (turnoManager.hayTurnos()) {
            Paciente siguiente = turnoManager.siguienteTurno();
            turnoActualLabel.setText("Turno Actual: " + siguiente.getNombre());
            turnoActualDetalles.setText(
                    "Edad: " + siguiente.getEdad() + ", Afiliación: " + siguiente.getAfiliacion() +
                            ", Embarazo: " + (siguiente.isEmbarazo() ? "Sí" : "No") +
                            ", Limitación Motriz: " + (siguiente.isLimitacionMotriz() ? "Sí" : "No")
            );

            Paciente proximo = turnoManager.getProximoTurno();
            if (proximo != null) {
                proximoTurnoLabel.setText("Próximo Turno: " + proximo.getNombre());
                proximoTurnoDetalles.setText(
                        "Edad: " + proximo.getEdad() + ", Afiliación: " + proximo.getAfiliacion() +
                                ", Embarazo: " + (proximo.isEmbarazo() ? "Sí" : "No") +
                                ", Limitación Motriz: " + (proximo.isLimitacionMotriz() ? "Sí" : "No")
                );
            } else {
                proximoTurnoLabel.setText("Próximo Turno: Ninguno");
                proximoTurnoDetalles.setText("No hay más pacientes en espera.");
            }

            turnosPendientesLabel.setText("Turnos Pendientes: " + turnoManager.turnosPendientes());
        } else {
            turnoActualLabel.setText("Turno Actual: No hay turnos");
            turnoActualDetalles.setText("");
            proximoTurnoLabel.setText("Próximo Turno: Ninguno");
            proximoTurnoDetalles.setText("No hay más pacientes en espera.");
        }
    }

    
    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        EPS gui = new EPS();
        gui.setVisible(true);
    }
}

