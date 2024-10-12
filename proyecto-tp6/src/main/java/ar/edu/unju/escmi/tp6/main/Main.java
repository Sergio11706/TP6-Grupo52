package ar.edu.unju.escmi.tp6.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
		
        CollectionCliente.precargarClientes();
		CollectionTarjetaCredito.precargarTarjetas();
        CollectionProducto.precargarProductos();
        CollectionStock.precargarStocks();
        
        String opcion;
        do {
        	System.out.println("\n====== Menu Principal =====");
            System.out.println("1- Realizar una venta");
            System.out.println("2- Revisar compras realizadas por el cliente (debe ingresar el DNI del cliente)");
            System.out.println("3- Mostrar lista de los electrodomésticos");
            System.out.println("4- Consultar stock");
            System.out.println("5- Revisar creditos de un cliente (debe ingresar el DNI del cliente)");
            System.out.println("6- Salir");

            System.out.print("Ingrese su opcion: ");
            opcion = scanner.nextLine();
            
            switch (opcion) {
				case "1": 
					String opc;
					do {
						
						System.out.println("\nQue operacion desea realizar?");
						System.out.println("1- Registrar cliente.");
						System.out.println("2- Realizar una venta con los clientes registrados.");
						System.out.println("3- Volver.");
						System.out.print("Ingrese su opcion: ");
						opc = scanner.nextLine();
						
						switch(opc) {
							case "1":
								registrarCliente(scanner);
								System.out.println("\nCliente y tarjeta registrados con exito.");
							break;
							
							case "2":
								realizarVenta(scanner);
							break;
							
							case "3": 
								System.out.println("Retornando al menu...");
							break;
							
							default:
								System.out.println("Opcion incorrecta. Intente nuevamente.");
						}
					} while (!opc.equals("3"));
				break;
				
				case "2":
					verCompras(scanner);
				break;
				
				case "3":
					mostrarElectrodomesticos();
				break;
				
				case "4": 
					List<Producto> productos = CollectionProducto.productos;
					for(Producto producto : productos) {
						producto.mostrarProducto();
					}
					
					System.out.print("\nIngrese el codigo del producto a comprar: ");
					long codigo = scanner.nextLong();
					scanner.nextLine();
					
					Producto prodBuscado = CollectionProducto.buscarProducto(codigo);
					System.out.println("\nEl stock de " + prodBuscado.getDescripcion() + " es de " + consultarStock(codigo) + " unidades.");	
				break;
				
				case "5": 
					
					
					List<Cliente> clientes = CollectionCliente.clientes;
					for(Cliente cliente : clientes) {
						cliente.mostrarCliente();
					}
					
					System.out.print("\nIngrese el DNI del cliente: ");
					long dni = scanner.nextLong();
					scanner.nextLine();
					
					TarjetaCredito tarjetaEncontrada = new TarjetaCredito();
					List<TarjetaCredito> tarjetas = CollectionTarjetaCredito.tarjetas;
					for(TarjetaCredito tarjeta : tarjetas) {
						if(tarjeta.getCliente().getDni()==dni) {
							tarjetaEncontrada = tarjeta;
								
						}
					}
					
					boolean band=true;
					List<Credito> creditos = CollectionCredito.creditos;
					for(Credito credito : creditos) {
						if(credito.getTarjetaCredito().getNumero() == tarjetaEncontrada.getNumero()) {
							System.out.println("\nCredito restante: "+credito.getCreditoRestante());
							band=false;
						}
					}
					if(band) System.out.println("\nCredito restante: "+tarjetaEncontrada.getLimiteCompra());
				break;
				
				case "6":
	                System.out.println("SALIENDO DEL MENU");
	            break;
	            
				default:
	                System.out.println("OPCION INVALIDA. Intentelo nuevamente");
			}

        } while(!opcion.equals("6"));
        scanner.close();
	}
	
	public static void registrarCliente(Scanner scanner){
		
		boolean dniValido=true;
		long dni=0;
		while(dniValido) {
			try {
				dniValido=false;
				System.out.print("\nIngrese el dni del cliente: ");
				dni = scanner.nextLong();
				scanner.nextLine();
			}
			catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("\nEl valor ingresado no esta permitido");
				dniValido=true;
			}
		}
		
		System.out.print("Ingrese nombre del cliente: ");
		String nombre = scanner.nextLine();
		
		System.out.print("Ingrese direccion del cliente: ");
		String dir = scanner.nextLine();
		
		System.out.print("Ingrese numero de telefonico del cliente: ");
		String tel = scanner.nextLine();
		
		Cliente cliente = new Cliente(dni, nombre, dir, tel);
		
		CollectionCliente.agregarCliente(cliente);
		
		registrarTarjeta(cliente, scanner);
	}
	
	
	public static void registrarTarjeta(Cliente cliente, Scanner scanner) {
		
		boolean numValido=true;
		long num=0;
		while(numValido) {
			try {
				numValido=false;
				System.out.print("Ingrese el numero de la tarjeta: ");
				num = scanner.nextLong();
				scanner.nextLine();
			}
			catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("\nEl valor ingresado no esta permitido");
				numValido=true;
			}
		}
		
		boolean fechaValida=true;
		String fechaCad;
		LocalDate fecCad=LocalDate.now();
		while(fechaValida) {
			fechaValida=false;
			try {
				System.out.print("Ingrese fecha de caducacion (dd/MM/yyyy): ");
				fechaCad = scanner.nextLine();
				DateTimeFormatter formato = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
				fecCad = LocalDate.parse(fechaCad, formato);
			}
			catch(DateTimeParseException e) {
				System.out.println("\nFormato de fecha incorrecto");
				fechaValida=true;
			}
		}
		
        double lim=0;
        numValido=true;
		while(numValido) {
			try {
				numValido=false;
				System.out.print("Ingrese limite de compra: ");
				lim = scanner.nextDouble();
				scanner.nextLine();
			}
			catch (InputMismatchException e) {
				scanner.nextLine();
				System.out.println("\nEl valor ingresado no esta permitido");
				numValido=true;
			}
		}

		
		TarjetaCredito tarjetaCredito = new TarjetaCredito(num, fecCad, cliente, lim);
		CollectionTarjetaCredito.agregarTarjetaCredito(tarjetaCredito);
	}
	
	public static void realizarVenta(Scanner scanner){
		
		List<Producto> productos = CollectionProducto.productos;
		for(Producto producto : productos) {
			producto.mostrarProducto();
		}
		
		System.out.print("\nIngrese el codigo del producto a comprar: ");
		long codigo = scanner.nextLong();
		scanner.nextLine();
		
		Producto productoAcomprar = CollectionProducto.buscarProducto(codigo);
		
		List<Cliente> clientes = CollectionCliente.clientes;
		for (Cliente cliente : clientes) {
			cliente.mostrarCliente();
		}
		
		System.out.print("\nIngrese el DNI del cliente: ");
		long dni = scanner.nextLong();
		scanner.nextLine();
		
		List<TarjetaCredito> tarjetas = CollectionTarjetaCredito.tarjetas;
		TarjetaCredito tarjetaEncontrada = new TarjetaCredito();
		for(TarjetaCredito tarjeta : tarjetas) {
			if(tarjeta.getCliente().getDni()==dni) tarjetaEncontrada=tarjeta;
		}
		
		if(consultarStock(codigo) > 0) {
			
			if (productoAcomprar.getOrigenFabricacion().contains("Argentina")) {
					
				if (tarjetaEncontrada.getLimiteCompra()>productoAcomprar.getPrecioUnitario()) {
					Factura factura = new Factura();
					factura = crearFactura(dni, codigo, scanner);
					crearCredito(tarjetaEncontrada, factura);
					System.out.println("\nVenta realizada.");
				}
				
				else {
					System.out.println("No cuenta con el credito necesario para realizar la transaccion.");
				}
			}
			else {
				System.out.println("Dado que el origen del producto no es nacional no se aplicara el programa 'Ahora 30'");
				
				System.out.println("\nDesea continuar con la compra (y/n)? (El programa de cuotas no se aplicara)");
				String opc = scanner.nextLine();
				
				switch(opc){
					case "y":
						crearFacturaEspecial(dni, codigo, tarjetaEncontrada, scanner);	
						System.out.println("\nVenta realizada.");
					break;
					case "n":
						System.out.println("Retornando al menu...");
					break;
					default: System.out.println("\nOpcion no disponible.");
				}
			}
		}
		else {
			System.out.println("\nProducto no disponible");
		}	
	}

	
	public static void crearFacturaEspecial(long dni, long codigo, TarjetaCredito tarjetaEncontrada, Scanner scanner) {
		
		System.out.print("Ingrese la cantidad de productos que se desean comprar: ");
		int cant = scanner.nextInt();
		tarjetaEncontrada.setLimiteCompra(tarjetaEncontrada.getLimiteCompra()-CollectionProducto.buscarProducto(codigo).getPrecioUnitario()*cant);
		
		scanner.nextLine();
		
        List<Detalle> detalles = new ArrayList<Detalle>();
		Producto producto = CollectionProducto.buscarProducto(codigo);

		Detalle detalle = new Detalle(cant, 0, producto);
		detalles.add(detalle);

		System.out.print("\nIngrese la fecha de la factura (dd/MM/yyyy): ");
		String fechaFac = scanner.nextLine();
		
		LocalDate fecFactura;
		DateTimeFormatter formato = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
        fecFactura = LocalDate.parse(fechaFac, formato);
        
		System.out.print("Ingrese el numero de factura: ");
		long nroFac = scanner.nextLong();
		scanner.nextLine();
		Cliente cliente = CollectionCliente.buscarCliente(dni);
		
		Factura factura = new Factura(fecFactura, nroFac, cliente, detalles);
		CollectionFactura.agregarFactura(factura);
		
		Stock stock = CollectionStock.buscarStock(producto);
		CollectionStock.reducirStock(stock, cant);

	}
	
	
	public static void crearCredito(TarjetaCredito tarjeta, Factura factura) {
		
		List<Cuota> cuotas= new ArrayList<Cuota>();
		Credito credito = new Credito(tarjeta, factura, cuotas, tarjeta.getLimiteCompra());
		CollectionCredito.agregarCredito(credito);
		tarjeta.setLimiteCompra(tarjeta.getLimiteCompra()-factura.calcularTotal()/30);
		
		System.out.println("\nCredito generado.");
	}
	
	
	public static Factura crearFactura(long dniCliente, long codigo, Scanner scanner) {
		
		System.out.print("Ingrese la cantidad de productos que se desean comprar: ");
		int cant = scanner.nextInt();
		
		scanner.nextLine();
		
        List<Detalle> detalles = new ArrayList<Detalle>();
		Producto producto = CollectionProducto.buscarProducto(codigo);

		Detalle detalle = new Detalle(cant, 0, producto);
		detalles.add(detalle);

		System.out.print("\nIngrese la fecha de la factura (dd/MM/yyyy): ");
		String fechaFac = scanner.nextLine();
		
		LocalDate fecFactura;
		DateTimeFormatter formato = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toFormatter();
        fecFactura = LocalDate.parse(fechaFac, formato);
        
		System.out.print("Ingrese el numero de factura: ");
		long nroFac = scanner.nextLong();
		scanner.nextLine();
		
		Cliente cliente = CollectionCliente.buscarCliente(dniCliente);
		
		Factura factura = new Factura(fecFactura, nroFac, cliente, detalles);
		CollectionFactura.agregarFactura(factura);
		
		Stock stock = CollectionStock.buscarStock(producto);
		CollectionStock.reducirStock(stock, cant);

		return factura;
	}
	
	
	public static void verCompras(Scanner scanner) {
		
		List<Cliente> clientes = CollectionCliente.clientes;
		
		for (Cliente cliente : clientes) {
			cliente.mostrarCliente();
		}
		
		System.out.print("\nIngrese el DNI del cliente: ");
		long dniCliente = scanner.nextLong();
		scanner.nextLine();
		
		Cliente clienteBuscado = CollectionCliente.buscarCliente(dniCliente);
		List<Factura> compras = clienteBuscado.consultarCompras();
		
		if(compras.isEmpty()) System.out.println("\nEl cliente con DNI "+dniCliente+" no realizo compras");
		
		else {
			for(Factura compra:compras) {
				System.out.println(compra.toString()); 
			}
		}
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
	
}