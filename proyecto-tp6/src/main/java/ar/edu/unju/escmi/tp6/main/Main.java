package ar.edu.unju.escmi.tp6.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.tp6.collections.CollectionCliente;
import ar.edu.unju.escmi.tp6.collections.CollectionCredito;
import ar.edu.unju.escmi.tp6.collections.CollectionFactura;
import ar.edu.unju.escmi.tp6.collections.CollectionProducto;
import ar.edu.unju.escmi.tp6.collections.CollectionStock;
import ar.edu.unju.escmi.tp6.collections.CollectionTarjetaCredito;
import ar.edu.unju.escmi.tp6.dominio.*;

public class Main {
	
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		
		CollectionTarjetaCredito.precargarTarjetas();
        CollectionCliente.precargarClientes();
        CollectionProducto.precargarProductos();
        CollectionStock.precargarStocks();
        int opcion = 0;
        do {
        	System.out.println("\n====== Menu Principal =====");
            System.out.println("1- Realizar una venta");
            System.out.println("2- Revisar compras realizadas por el cliente (debe ingresar el DNI del cliente)");
            System.out.println("3- Mostrar lista de los electrodomésticos");
            System.out.println("4- Consultar stock");
            System.out.println("5- Revisar creditos de un cliente (debe ingresar el DNI del cliente)");
            System.out.println("6- Salir");

            System.out.println("Ingrese su opcion: ");
            opcion = scanner.nextInt();
            
            switch (opcion) {
				case 1: 
					realizarVenta(scanner);
				break;
				
				case 2:
					verCompras(scanner);
				break;
				
				case 3:
					mostrarElectrodomesticos();
				break;
				
				case 4: 
					long codigo = mostrarListaProd();
					Producto prodBuscado = CollectionProducto.buscarProducto(codigo);
					System.out.println("\nEl stock de " + prodBuscado.getDescripcion() + " es de " + consultarStock(codigo) + " unidades.");	
				break;
				
				case 5: 
					List<TarjetaCredito> tarjetas = CollectionTarjetaCredito.tarjetas;
					for(TarjetaCredito TarjetaCredito : tarjetas) {
						TarjetaCredito.mostrarTarjeta();
					}
					System.out.println("\nIngrese el numero de la tarjeta: ");
					long num = scanner.nextLong();
					System.out.println("Los creditos del cliente son: " +revisarCreditos(num));
				break;
				
				case 6:
	                System.out.println("SALIENDO DEL MENU");
	                break;
	            default:
	                System.out.println("OPCION INVALIDA. Intentelo nuevamente");
			}

        }while(opcion != 6);
        scanner.close();

	}
	
	public static long mostrarListaProd() {
		List<Producto> productos = CollectionProducto.productos;
		for(Producto producto : productos) {
			producto.mostrarProducto();
		}
		System.out.println("\nIngrese el codigo del producto: ");
		long codProd = scanner.nextLong();
		return codProd;
	}
	  
	
	public static void realizarVenta(Scanner scanner){
		TarjetaCredito tarjetaEncontrada = null;
		System.out.println("Ingrese el codigo del producto a comprar: ");
		long codigo = mostrarListaProd();
		Producto productoAcomprar = CollectionProducto.buscarProducto(codigo);
		
		List<Cliente> clientes = CollectionCliente.clientes;
		for (Cliente cliente : clientes) {
			cliente.mostrarCliente();
		}
		System.out.println("\nIngrese el DNI del cliente: ");
		long dni = scanner.nextLong();
		
		List<TarjetaCredito> tarjetas = CollectionTarjetaCredito.tarjetas;
		for(TarjetaCredito TarjetaCredito : tarjetas) {
			TarjetaCredito.mostrarTarjeta();
		}
		System.out.println("\nIngrese el numero de la tarjeta: ");
		long num = scanner.nextLong();
		
		tarjetaEncontrada = CollectionTarjetaCredito.buscarTarjetaCredito(num);
		
		
		if(consultarStock(codigo) > 0) {
			if (productoAcomprar.getOrigenFabricacion().contains("Argentina")) {
					if (revisarCreditos(num)>productoAcomprar.getPrecioUnitario()) {
						Factura factura = new Factura();
						Credito credito = new Credito();
						factura = crearFactura(dni, codigo);
						credito = crearCredito(tarjetaEncontrada, factura);
						
						
					}
			}
			else {
				System.out.println("Dado que el origen del producto no es nacional no se aplicara el programa 'Ahora 30'");
			}
		}
		else {
			System.out.println("\nProducto no disponible");
		}	
}

	
	public static Credito crearCredito(TarjetaCredito tarjeta, Factura factura) {
		List<Cuota> cuotas= new ArrayList<Cuota>();
		Credito credito = new Credito(tarjeta, factura, cuotas);
		CollectionCredito.agregarCredito(credito);
		System.out.println("\nCredito generado.");
		return credito;
	}
	
	public static Factura crearFactura(long dniCliente, long codigo) {
		
		System.out.println("Ingrese la cantidad de productos que se desean comprar: ");
		int cant = scanner.nextInt();
		
		scanner.nextLine();
		
        List<Detalle> detalles = new ArrayList<Detalle>();
		Producto producto = CollectionProducto.buscarProducto(codigo);

		Detalle detalle = new Detalle(cant, 0, producto);
		detalles.add(detalle);

		System.out.println("\nIngrese la fecha de la factura (dd/MM/yyyy): ");
		String fechaFac = scanner.nextLine();
		LocalDate fecFactura;
		DateTimeFormatter formato = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
        fecFactura = LocalDate.parse(fechaFac, formato);
        
		System.out.println("Ingrese el numero de factura: ");
		long nroFac = scanner.nextLong();
		Cliente cliente = CollectionCliente.buscarCliente(dniCliente);
		
		Factura factura = new Factura(fecFactura, nroFac, cliente, detalles);
		CollectionFactura.agregarFactura(factura);
		return factura;
	}
	
	
	
	public static void verCompras(Scanner scanner) {
		List<Cliente> clientes = CollectionCliente.clientes;
		for (Cliente cliente : clientes) {
			cliente.mostrarCliente();
		}
		System.out.println("\nIngrese el DNI del cliente: ");
		long dniCliente = scanner.nextLong();
		
		Cliente clienteBuscado = CollectionCliente.buscarCliente(dniCliente);
		System.out.println(clienteBuscado.consultarCompras());
	}

	public static void mostrarElectrodomesticos() {
		System.out.println("\nElectrodomesticos: ");
		List<Producto> productos = CollectionProducto.productos;
		for(Producto producto : productos) {
			if(!producto.getDescripcion().contains("Celular")) producto.mostrarProducto();
		}
	}
	
	public static long consultarStock(long codigo) {
		Producto prodBuscado = CollectionProducto.buscarProducto(codigo);
		return CollectionStock.buscarStock(prodBuscado).getCantidad();
	}
	
	
	public static double revisarCreditos(long num) { 
		TarjetaCredito tarjetaEncontrada = CollectionTarjetaCredito.buscarTarjetaCredito(num);
		return tarjetaEncontrada.getLimiteCompra();
	}
}