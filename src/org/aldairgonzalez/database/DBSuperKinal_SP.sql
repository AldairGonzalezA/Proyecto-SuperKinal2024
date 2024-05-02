use DBSuperKinal;

DELIMITER $$
create procedure sp_AgregarClientes(in nom varchar(30), in ape varchar(30), in tel varchar(15),in dir varchar(150), in nit varchar(15))
begin
	insert into Clientes(nombre,apellido,telefono,direccion,nit) values
		(nom,ape,tel,dir,nit);
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_ListarClientes()
begin
	select 
		Clientes.clienteId,
        Clientes.nombre,
        Clientes.apellido,
        Clientes.telefono,
		Clientes.direccion,
        Clientes.nit
			from Clientes;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_EliminarClientes(in cliId int)
begin
	delete
		from Clientes
			where clienteId = cliId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_BuscarClientes(in cliId int)
begin
	select 
		Clientes.clienteId,
        Clientes.nombre,
        Clientes.apellido,
        Clientes.telefono,
		Clientes.direccion,
        Clientes.nit
			from Clientes
				where clienteId = cliId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_EditarClientes(in cliId int,in nom varchar(30), in ape varchar(30), in tel varchar(15),in dir varchar(150), in nit_ varchar(15))
begin
	update Clientes
		set
			nombre = nom,
            apellido = ape,
            telefono = tel,
            direccion = dir,
            nit = nit_
				where clienteId = cliId;
end $$
DELIMITER ;

-- CRUD Cargo //////////////////////////////////////////////////
DELIMITER $$
create procedure sp_agregarCargo(nomCar varchar(30), desCar varchar(100)) 
begin
		insert into Cargos(nomC,desCar) values
        (nombreCargo, descripcionCargo);
end$$
DELIMITER ;nombreCargo, descripcionCargo
 call sp_agregarCargo('Prueba', 'Prueba');
DELIMITER $$
create procedure sp_listarCargos()
begin 
		select * from Cargos;
end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_buscarCargo(carId int)
begin
		select * from Cargos C
        where carId = C.cargoId;
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarCargo(carId int)
begin
	delete from Cargos 
	where carId = cargoId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCargo(carId int, nomCar varchar(30), desCar varchar(100))
begin 
	update Cargos C set
		C.nombreCargo = nomCar,
		C.descripcionCargo = desCar
			where carId = C.cargoId;
end $$
DELIMITER ;
 
 --  CRUD Compras 
DELIMITER $$
create procedure sp_listarCompra()
	begin
		select * from Compras;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_agregarCompra(in fecCom date, in totCom decimal (10,2))
	begin 
		insert into Compras (fechaCompra, totalCompra) values
			(fecCom, totCom);
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarCompra(in comId int)
	begin	
		select * from Compras 
			where compraId = comId;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCompra(in comId int,in fecCom date,in totCom decimal (10,2))
	begin 
		update Compras
			set 
				fechaCompra = fecCom,
                totalCompra = totCom
                where compraId = comId;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarCompra(in comId int)
	begin 
		delete from Compras
        where compraId = comId;
    end $$
DELIMITER ;

 -- CRUD Categoria Productos
DELIMITER $$
create procedure sp_agregarCategoriaProductos(nomCat varchar(30),desCat varchar(100))
begin 
	insert into CategoriaProductos(nombreCategoria,descrippcionCategoria) values
		(nomCat,desCat);
end$$
DELIMITER ;
 call sp_agregarCategoriaProductos('Fruta', 'Alimento Frutal');
DELIMITER $$
create procedure sp_listarCategoriaProductos()
begin
	select * from CategoriaProductos;
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_buscarCategoriaProductos(catProId int)
begin
	select * from CategoriaProductos CP
	where catProId = CP.categoriaProductoId;
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarCategoriaProductos(catProId int)
begin 
	delete from CategoriaProductos
	where catProId = categoriaProductoId;
end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_editarCategoriaProductos(catProId int,nomCat varchar(30),desCat varchar(100) )
begin
	update CategoriaProductos CP set
		CP.nombreCategoria = nomCat,
        CP.descripcionCategoria = desCat
		where catProId = CP.categoriaProductosId;
end$$
DELIMITER ;

 -- CRUD Distribuidores ////////////////////////////////////////////////////
DELIMITER $$
create procedure sp_agregarDistribuidor(nomDis varchar(30), dirDis varchar(200), nitDis varchar(15), telDis varchar(15),web varchar(50))
begin
	insert into Distribuidores(nombreDistribuidor,direccionDistribuidor,nitDistribuidor,telefonoDistribuidor,web) values
		(nomDis,dirDis,nitDis,telDis,web);
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_listarDistribuidores()
begin
	select * from Distribuidores;
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_buscarDistribuidor(disId int)
begin 
	select * from Distribuidores D 
	where disId = D.distribuidorId;
end$$

DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarDistribuidor(dirId int)
begin
	delete from Distribuidores 
	where dirId = distribuidorId;
end$$
DELIMITER ;
 
DELIMITER $$
create procedure sp_editarDistribuidor(dirId int, nomDis varchar(30), dirDis varchar(200), nitDis varchar(15), telDis varchar(15),web varchar(50))
begin
	update Distribuidores D set
        D.nombreDistribuidor = nomDis,
        D.direccionDistribuidor = dirDis,
        D.nitDistribuidor = nitDis,
        D.telefonoDistribuidor = telDis,
        D.web = web
        where dirId = D.distribuidorId;
end$$
DELIMITER ;

-- CRUD Empleados
DELIMITER  $$
create procedure sp_agregarEmpleados(in nomEmp varchar(30),in apeEmp varchar(30), in sue decimal(10, 2), in hoEn time, in hoSa time, in carId int, in encarId int)
	begin
		insert into Empleados (nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId, encargadoId) values
			(nomEmp, apeEmp, sue, hoEn, hoSa, carid, encarId);
    end $$
DELIMITER ;
 call sp_agregarEmpleados('Pedro', 'Navaja', 250.00, 0800, 1700, null, null);

DELIMITER $$
create procedure sp_listarEmpleados()
	begin
		select * from Empleados;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_buscarEmpleados(in empId int)
	begin
		select * from Empleados
			where empleadoId = empId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarEmpleados(in empId int)
	begin
		delete 
			from Empleados
				where empleadoId = empId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_editarEmpleados(in empId int, in nomEmp varchar(30),in apeEmp varchar(30), in sue decimal(10, 2), in hoEn time, in hoSa time, in carId int, in encarId int)
	begin
		update Empleados
			set 
            nombreEmpleado = nomEmp,
            apeEmp = apellidoEmpleado,
            sueldo = suel,
            horaEntrada = hoEn,
            horaSalida = hoSa,
            cargoId = carId,
            encargadoId = encarId
            where empleadoId = empId;
    end $$
DELIMITER ;
 
 -- CRUD Facturas ////////////////////////////////////////////////////////
DELIMITER $$
create procedure sp_agregarFacturas(in fe date, in ho time, in tot decimal(10, 2), in cliId int, in empId int)
	begin
		insert into Facturas (fe, ho, tot, cliId, empId) values
			(fecha, hora, total, clienteId, empleadoId);
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_listarFacturas()
	begin
		select * from Facturas;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarFacturas(in facId int)
	begin
		select * from Facturas
			where facturaId = facId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarFacturas(in facId int)
	begin
		delete 
			from Facturas
				where facturaId = facId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_editarFacturas(in facId int, in fe date, in ho time, in tot decimal(10, 2), in cliId int, in empId int)
	begin
		update Facturas
			set 
            fecha = fe,
            hora = ho,
            total = tot,
            clienteId = cliId,
            empleadoId = empId
            where facturaId = facId;
    end $$
DELIMITER ;
 
-- CRUD Ticket Soporte ////////////////////////////////////////////
DELIMITER $$
create procedure sp_AgregarTicketSoporte(in des varchar(250), in cliId int, in facId int)
begin
	insert into TicketSoporte(descripcionTicket,estatus,clienteId,facturaId) values
		(des,'Recien Creado',cliId,facId);
end $$
DELIMITER ;
drop procedure sp_AgregarTicketSoporte;
DELIMITER $$
create procedure sp_ListarTicketSoporte()
begin
	select TS.ticketSoporteId, TS.descripcionTicket, TS.estatus,
			CONCAT('ID : ',C.clienteId,' | ' ,C.nombre, C.apellido) as 'Cliente', TS.facturaId from TicketSoporte TS
    join Clientes C on TS.clienteId = c.clienteId;
		
end $$
DELIMITER ;

-- drop procedure sp_ListarTicketSoporte;

DELIMITER $$
create procedure sp_EliminarTicketSoporte(in tikId int)
begin
	delete
		from TicketSoporte
			where ticketSoporteId = tikId;
end$$
DELIMITER ;

DELIMITER $$
create procedure sp_BuscarTicketSoporte(in tikiId int)
begin 
	select
		TicketSoporte.ticketSoporteId,
        TicketSoporte.descripcionTicket,
        TicketSoporte.estatus,
        TicketSoporte.clienteId,
        TicketSoporte.facturaId
			from TicketSoporte
			where ticketSoporteId = tikId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_EditarTicketSoporte(in tikId int,in des varchar(250), in est varchar(30), in cliId int, in facId int )
begin
	update TicketSoporte
		set 
			descripcionTicket = des,
            estatus = est,
            clienteId = cliId,
            facturaId = facId
				where ticketSoporteId = tikId;
end $$
DELIMITER ;

-- CRUD Productos
DELIMITER $$
create procedure sp_listarProducto()
	begin 
		select * from Productos;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_agregarProducto(in nom varchar(50),in des varchar(100),in can int, in preU decimal(10,2),in preM decimal(10,2),in preC decimal(10,2), in ima blob, in disId int, in catId int)
	begin
		insert into Productos(nombreProducto, descripcionProducto, cantidadStock, precioUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaProductosId ) values
			(nom, des, can, preU, preM, preC, ima, disId, catId);
	end $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarProducto(in proId int)
	begin 
		select * from Productos
        where productoId = proId;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarProducto(in proId int, in nom varchar(50),in des varchar(100),in can int, in preU decimal(10,2),in preM decimal(10,2),in preC decimal(10,2), in ima blob, in disId int, in catId int )
	begin
		update Productos	
			set 
            nombreProducto = nom,
            descripcionProduto = des,
            cantidadStock = can,
            precioVentaUnitario = preU,
            precioVentaMayor = preM,
            precioCompra = preC,
            imagenProducto = ima,
            distribuidorId = disId,
            categoriaProductosId = catId
            where productoId = proId;
    end $$
DELIMITER ;

DELIMITER
create procedure sp_eliminarProducto(in proId int)
	begin
		delete from Productos
			where productoId = proId
    end $$
DELIMITER

-- CRUD Promociones
DELIMITER $$
create procedure sp_agregarPromociones(in prePro decimal(10, 2), in descPro varchar(100), in feIni date, in feFina date, in proId int)
	begin
		insert into Promociones (prePro, descPro, feIni, feFina, proId) values
			(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacione, productoId);
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_listarPromociones()
	begin
		select * from Promociones;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_buscarPromociones(in promoId int)
	begin
		select * from Promociones
			where promocionId = promoId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_eliminarPromociones(in promoId int)
	begin
		delete 
			from Promociones
				where promocionId = promoId;
    end $$
DELIMITER ;
 
DELIMITER $$
create procedure sp_editarPromociones(in promoId int, in prePro decimal(10, 2), in descPro varchar(100), in feIni date, in feFina date, in proId int)
	begin
		update Promociones
			set 
            precioPromocion = prePro,
            descripcionPromocion = descPro,
            fechaInicio = feIni,
            fechaFinalizacion = feFina,
            productoId = proId
            where promocionId = promoId;
    end $$
DELIMITER ;

-- CRUD DetalleFactura
DELIMITER $$
create procedure sp_AgregarDetalleFactura(in factId int, in prodId int)
begin
	insert into DetalleFactura(facturaId, productoId) values
		(factId, prodId);
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_ListarDetalleFactura()
begin
	select 
		DetalleFactura.detalleFacturaId,
        DetalleFactura.facturaId,
        DetalleFactura.productoId
			from DetalleFactura;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_EliminarDetalleFactura(in detId int)
begin
	delete
		from DetalleFactura
			where detalleFacturaId = detId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_BuscarDetalleFactura(in detId int)
begin
	select 
		DetalleFactura.detalleFacturaId,
        DetalleFactura.facturaId,
        DetalleFactura.productoId
			from DetalleFactura
			where detalleFacturaId = detId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_EditarDetalleFactura(in detId int, in factId int, in prodId int)
begin
	update DetalleFactura
		set 
			facturaId = factId,
            productoId = prodId
            where detalleFacturaId = detId;
end $$
DELIMITER ;

-- ----------------------------------------------------DetalleCompra------------------------------------------------------------------
DELIMITER $$
create procedure sp_ListarDetalleCompra()
	begin 
		select * from DetalleCompra;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_agregarDetalleCompra(in canC int, in proId int,in comId int)
	begin 
		insert into DetalleCompra(cantidadCompra, productoId, compraId)values
			(canC, proId, comId);
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarDetalleCompra(in detCId int)
	begin 
		select * from DetalleCompra
			where detalleCompraId = detCId;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarDetalleCompra(in detCId int, in canC int, in proId int,in comId int)
	begin 
		update DetalleCompra
			set 
				cantidadCompra = canC,
                productoId = proId,
                compraId = comId
                where detalleCompraId = detCId;
    end $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarDetalleCompra(in detCId int)
	begin 
    delete from DetalleCompra 
			where detalleCompraId = detCId;
    end $$
DELIMITER ;