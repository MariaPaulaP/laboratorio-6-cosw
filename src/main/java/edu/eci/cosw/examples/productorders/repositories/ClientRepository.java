/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cosw.examples.productorders.repositories;

import edu.eci.cosw.samples.model.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ClientRepository extends JpaRepository<Cliente, Integer>{
  
    @Query("select ped.cliente from Pedido ped left join ped.detallesPedidos as detalleped left join detalleped.producto as producto with producto.precio>?1")
    List<Cliente> clientByProdPrice(long precio);
    
}
