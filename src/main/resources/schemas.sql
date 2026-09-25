-- Schemas que vamos a utilizar para organizar las tablas en la base de datos

-- 1. Crear los esquemas nuevos
CREATE SCHEMA IF NOT EXISTS clientes;
CREATE SCHEMA IF NOT EXISTS catalogo;
CREATE SCHEMA IF NOT EXISTS ventas;
CREATE SCHEMA IF NOT EXISTS admin;

-- 2. Mover las tablas (esto conserva datos, índices, FKs, secuencias asociadas, todo)
ALTER TABLE public.cliente        SET SCHEMA clientes;
ALTER TABLE public.contacto       SET SCHEMA clientes;
ALTER TABLE public.domicilio      SET SCHEMA clientes;
ALTER TABLE public.condicion_iva  SET SCHEMA clientes;

ALTER TABLE public.articulo             SET SCHEMA catalogo;
ALTER TABLE public.marca                SET SCHEMA catalogo;
ALTER TABLE public.rubro                SET SCHEMA catalogo;
ALTER TABLE public.lista_precio         SET SCHEMA catalogo;
ALTER TABLE public.lista_precio_articulo SET SCHEMA catalogo;

ALTER TABLE public.factura_venta         SET SCHEMA ventas;
ALTER TABLE public.factura_venta_detalle SET SCHEMA ventas;
ALTER TABLE public.punto_venta           SET SCHEMA ventas;
ALTER TABLE public.tipo_moneda           SET SCHEMA ventas;

ALTER TABLE public.usuario SET SCHEMA admin;