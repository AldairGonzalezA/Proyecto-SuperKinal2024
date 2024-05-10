use DBSuperKinal;

delimiter $$ 
create procedure sp_agregarFactura(in cliId int , in empId int)
begin 
	insert into Facturas (fecha, clienteId, empleadoId) values
		(date(now()), cliId, empId);
end $$
delimiter ;
 
delimiter $$
create procedure sp_agregarDetalleFactura(in facId int, in proId int)
begin
	insert into DetalleFactura(facturaId,ProductoId) values
	(facId, proId);
end $$
delimiter ;
 
delimiter $$
create function fn_calcularTotal(facId int) returns decimal(10,2) deterministic
begin
	declare total decimal(10,2) default 0.0;
    declare precio decimal(10,2);
    declare i int default 1;
    declare curFacId, curProId int;
    declare cursorDetalleFactura cursor for
		Select DF.facturaId, DF.productoId from DetalleFactura DF;
	open cursorDetalleFactura;
    totalLoop :loop
    fetch cursorDetalleFactura into curFacId, curProId;
	if facId = curFacId then
		set precio = (select P.precio from Productos P where P.productoId = curProId);
        set total = total + precio;
    end if;
    if i = (select count(*) from detalleFactura) then
		leave totalLoop;
	end if;
    set i = i + 1;
    end loop totalLoop;
    call sp_asignarTotal(total, facId);
    return total;
end $$
delimiter ;

DELIMITER $$
create function fn_calcularTotalCompra(comId int) returns decimal(10,2) deterministic
begin
declare total decimal(10,2) default 0.0;
declare precio decimal(10,2);
declare i int default 1;
declare curComId, curProId int;
	select DC.compraId, DCproductoId from DetalleCompra DC;
    open cursorDetalleCompra;
    
    totalLoop : loop
    fetch cursorDetalleCompra into curComId, curProId;
    if comId = curComId then
    set precio = (select P.precioCompra from Productos P where P.productoId = curProId);
    set total = total + precio;
    end if;
    if i = (select count(*) from detallecompra) then
		leave totalLoop;
	end if;
    set i = i+1;
    end loop totalLoop; 
    call sp_asignarTotalCompra(total, comId);
    return total;
end $$
DELIMITER ;
 
delimiter $$
create procedure sp_asignarTotalCompra(in tot decimal(10,2), in comId int)
begin 
	update Compras
		set totalCompra = tot * (1 +  0.12) 
			where compraId = comId; 
end $$
delimiter ;
 
delimiter $$
create trigger tg_totalCompra
after insert on DetalleCompra
for each row
begin
	declare total decimal(10,2);
    set total = fn_calcularTotalCompra(new.compraId);
end $$
delimiter ;

DELIMITER $$
create trigger tg_agregarDetalleCompra
after insert on Compras
for each row
begin
	call sp_agregarDetalleCompra();
end $$
DELIMITER ;

