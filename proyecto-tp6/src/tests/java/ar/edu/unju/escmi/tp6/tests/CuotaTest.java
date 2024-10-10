package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.unju.escmi.tp6.dominio.Cuota;

class CuotaTest {
	
    private List<Cuota> cuotas;

    private List<Cuota> generarCuotas() {
        List<Cuota> cuotas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            cuotas.add(new Cuota());
        }
        return cuotas;
    }

    @BeforeEach
    void setUp() {
        cuotas = generarCuotas(); 
    }

    @Test
    public void testListaCuotasNoEsNull() {
        assertNotNull(cuotas, "La lista de cuotas no debe ser null");
    }

    @Test
    void testListaDe30Cuotas() {
        int cuotasEsperadas = 30; 
        int cuotasObtenidas = cuotas.size(); 
        assertEquals(cuotasEsperadas, cuotasObtenidas);
    }

    @Test
    void testCuotasNoSuperanCantidadPermitida() {
        int cuotasPermitidas = 30; 
        int cuotasObtenidas = cuotas.size(); 
        assertTrue(cuotasObtenidas <= cuotasPermitidas);
    }
}
