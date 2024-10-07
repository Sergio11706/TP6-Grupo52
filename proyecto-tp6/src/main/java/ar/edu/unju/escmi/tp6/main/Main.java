package ar.edu.unju.escmi.tp6.main;

import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.tp6.collections.CollectionCliente;
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
					registrarCliente(scanner); 					
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
					revisarCreditos();
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
	
	
	public static void registrarCliente(Scanner scanner) {
	  System.out.println("\nRegistrar Cliente");
	  System.out.println("Ingrese el dni del cliente: "); 
	  long dniCliente = scanner.nextLong(); 
	  scanner.nextLine();
	  System.out.println("Ingrese nombre del cliente: ");
	  String nombreCliente = scanner.nextLine();
	  System.out.println("Ingrese direccion del cliente: "); 
	  String dirCliente = scanner.nextLine(); 
	  System.out.println("Ingrese el telefono del cliente: ");
	  String telCliente = scanner.nextLine();
	  
	  Cliente cliente = new Cliente(dniCliente, nombreCliente, dirCliente,
	  telCliente); CollectionCliente.agregarCliente(cliente); 
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
		long codigo = mostrarListaProd();
		if(consultarStock(codigo) > 0) {
			String opc;
			long dniCliente = 0;
			do {
				System.out.println("\nClientes disponibles: ");
				List<Cliente> clientes = CollectionCliente.clientes;
				for (Cliente cliente : clientes) {
					cliente.mostrarCliente();
				}
				System.out.println("Desea ingresar mas usuarios? (Y/N)");
				opc = scanner.nextLine();
				switch (opc) {
					case "y": 
						registrarCliente(scanner);
					break;
					
					case "n":
						System.out.println("\nIngrese el DNI del cliente: ");
						dniCliente = scanner.nextLong();
					break;
					
					default:
						throw new IllegalArgumentException("Opcion incorrecta: " + opc);
					}
			} while (opc == "n");
			
			List<TarjetaCredito> tarjetas = CollectionTarjetaCredito.tarjetas; 
			for(TarjetaCredito tarjeta : tarjetas) { 
				if(tarjeta.getCliente().getDni() == dniCliente) {
					
				} 
			}			
		}
		else {
			System.out.println("\nProducto no disponible");
		}	
	}
	
	public static void verCompras(Scanner scanner) {
		List<Cliente> clientes = CollectionCliente.clientes;
		for (Cliente cliente : clientes) {
			cliente.mostrarCliente();
		}
		System.out.println("\nIngrese el DNI del cliente: ");
		long dniCliente = scanner.nextLong();
		
		Cliente clienteBuscado = CollectionCliente.buscarCliente(dniCliente);
		clienteBuscado.consultarCompras();
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
	
	public static void revisarCreditos() {
		
	}
}
