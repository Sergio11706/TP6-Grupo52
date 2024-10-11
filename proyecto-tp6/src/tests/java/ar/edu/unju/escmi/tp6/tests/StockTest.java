package ar.edu.unju.escmi.tp6.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.tp6.collections.CollectionProducto;
import ar.edu.unju.escmi.tp6.dominio.Producto;
import ar.edu.unju.escmi.tp6.dominio.Stock;
import ar.edu.unju.escmi.tp6.collections.CollectionStock;

class StockTest {
    
    @BeforeEach
    void setUp() {
        
        CollectionProducto.precargarProductos();
        CollectionStock.precargarStocks();
    }

    @Test
    void testReducirStockProducto() {
       
        Producto producto = CollectionProducto.buscarProducto(1111); 
        Stock stock = CollectionStock.buscarStock(producto);
        int stockInicial = stock.getCantidad();
        int cantidadAReducir = 11;
        CollectionStock.reducirStock(stock, cantidadAReducir);
        int stockEsperado = stockInicial - cantidadAReducir;
        assertEquals(stockEsperado, stock.getCantidad(), "El stock se redujo correctamente");
    }

}
