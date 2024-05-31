use DBSuperKinal; 

DELIMITER $$
create function fn_calcularTotalCompra(comId int) returns decimal(10,2) deterministic
begin
DECLARE total DECIMAL(10,2) DEFAULT 0.0;
    DECLARE precio DECIMAL(10,2);
    DECLARE cantidad INT;
    
    SELECT SUM(P.precioCompra * DC.cantidadCompra) INTO total
    FROM DetalleCompra DC
    JOIN Productos P ON DC.productoId = P.productoId
    WHERE DC.compraId = comId;
    
    CALL sp_asignarTotalCompra(total, comId);
    
    RETURN total;
end $$
delimiter ;
 
 DELIMITER $$
CREATE PROCEDURE sp_asignarTotalCompra(IN tot DECIMAL(10,2), IN comId INT)
BEGIN 
    UPDATE Compras
    SET totalCompra = tot * (1 + 0.12) 
    WHERE compraId = comId; 
END $$
DELIMITER ;
 

DELIMITER $$
CREATE TRIGGER tg_totalCompra
AFTER INSERT ON DetalleCompra
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(10,2);
    SET total = fn_calcularTotalCompra(NEW.compraId);
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER tg_actualizarCompra
AFTER UPDATE ON DetalleCompra
FOR EACH ROW
BEGIN
	DECLARE total DECIMAL(10,2);
    SET total = fn_calcularTotalCompra(OLD.compraId);
END $$
DELIMITER ;

delimiter $$
create function fn_calcularTotal (factId int) returns decimal(10,2) deterministic
begin
 
	declare total decimal(10,2) default 0.0;
    declare precio decimal(10,2);
    declare i int default 1;
    declare curFacturaId, curProductoId int;
    declare curPromPrecio decimal(10,2);

    declare cursorDetalleFactura cursor for 
    select DF.facturaId , DF.productoId from DetalleFactura DF;
 
    open cursorDetalleFactura;
    totalLoop :loop
 
    fetch cursorDetalleFactura into curFacturaId, curProductoId;
    select PR.precioPromocion into curPromPrecio
        from Promociones PR
        where PR.productoId = curProductoId
        and NOW() between PR.fechaInicio and PR.fechaFinalizacion
        order by PR.fechaInicio desc
        Limit 1;
 
	if factId = curFacturaId then
			if curPromPrecio is not null then
				set precio = curPromPrecio;
            else 
				set precio = (select P.precioVentaUnitario from Productos P where P.productoId = curProductoId);
			end if;
        set total = total + precio;
    end if;
 
    if i = (select count(*) from detalleFactura) then
		leave totalLoop;
	end if;
 
    set i = i + 1;
    end loop totalLoop;
 
    call sp_asignarTotalFactura(total, factId);
    return total;
end $$
delimiter ;

delimiter $$
create procedure sp_asignarTotalFactura(in tot decimal(10,2), in facId int)
begin 
	update Facturas
		set total = tot * (1 +  0.12) 
			where facturaId = facId; 
end $$
delimiter ;

DELIMITER $$
create trigger tg_totalPrecio
after insert on DetalleFactura
for each row
begin
	declare total decimal(10,2);
    set total = fn_calcularTotal(NEW.facturaId);
end $$
DELIMITER ; 

delimiter $$
create procedure sp_manejoStock(in proId int)
begin
	update Productos
		set
			cantidadStock = cantidadStock - 1
            where productoId = proId;
end$$
delimiter ;

delimiter $$
create trigger tg_restarStock
before insert on detalleFactura
for each row
begin
    if (select P.cantidadStock from Productos P where productoId = NEW.productoId) = 0 then
		signal sqlstate'45000'
			set message_text="No esta el producto en Estock";
    else
		call sp_manejoStock(new.productoId);
	end if;
end $$
delimiter ;