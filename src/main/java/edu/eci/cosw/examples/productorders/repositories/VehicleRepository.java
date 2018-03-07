/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cosw.examples.productorders.repositories;

import edu.eci.cosw.samples.model.Vehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface VehicleRepository extends JpaRepository<Vehiculo, String>{
     @Query("select desp.vehiculo from Despacho desp  inner join desp.pedidos as pedido  inner join pedido.detallesPedidos as detalleped inner join detalleped.producto as producto with producto.idproducto=?1")
    List<Vehiculo> vehiculosByIdProducto(int idProducto);
}
