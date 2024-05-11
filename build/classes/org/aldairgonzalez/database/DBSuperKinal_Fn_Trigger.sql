use DBSuperKinal; 

DELIMITER $$
create function fn_calcularTotalCompra(comId int) returns decimal(10,2) deterministic
begin
DECLARE total DECIMAL(10,2) DEFAULT 0.0;
    DECLARE precio DECIMAL(10,2);
    DECLARE cantidad INT;
    
    -- Obtener el total para la compra actual
    SELECT SUM(P.precioCompra * DC.cantidadCompra) INTO total
    FROM DetalleCompra DC
    JOIN Productos P ON DC.productoId = P.productoId
    WHERE DC.compraId = comId;
    
    -- Actualizar el total de la compra en la tabla Compras
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
		set precio = (select P.precioVentaUnitario from Productos P where P.productoId = curProId);
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
CREATE TRIGGER tg_totalFactura
AFTER INSERT ON DetalleFactura
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(10,2);
    SET total = fn_calcularTotal(NEW.facturaId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_asignarTotal(IN tot DECIMAL(10,2), IN comId INT)
BEGIN 
    UPDATE Facturas
    SET totalCompra = tot * (1 + 0.12) 
    WHERE facturaId = comId; 
END $$
DELIMITER ;