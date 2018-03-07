/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cosw.examples.productorders.services;

import edu.eci.cosw.examples.productorders.repositories.ClientRepository;
import edu.eci.cosw.examples.productorders.repositories.DispatchRepository;
import edu.eci.cosw.examples.productorders.repositories.OrdersRepository;
import edu.eci.cosw.examples.productorders.repositories.ProductsRepository;
import edu.eci.cosw.examples.productorders.repositories.VehicleRepository;
import edu.eci.cosw.samples.model.Cliente;
import edu.eci.cosw.samples.model.Despacho;
import edu.eci.cosw.samples.model.Pedido;
import edu.eci.cosw.samples.model.Producto;
import edu.eci.cosw.samples.model.Vehiculo;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author hcadavid
 */
@Service
public class ApplicationServicesImpl implements ApplicationServices{
   
    @Autowired
    private OrdersRepository ordrepo;

    @Autowired
    private ProductsRepository prorepo;
    
    @Autowired
    private DispatchRepository disprepo;
    
    @Autowired
    private VehicleRepository vehiclerepository;
    
    @Autowired
    private ClientRepository clientrepository;
    
    
    @Override
    public List<Pedido> getAllOrders() throws ServicesException{
        List<Pedido> p=ordrepo.findAll();
        return p;
    }

    @Override
    public List<Producto> getAllProducts() throws ServicesException{
        return  prorepo.findAll();
    }

    @Override
    public Pedido orderById(Integer id) throws ServicesException{
        return ordrepo.findOne(id);
    }
    
    
    @Override
    public Despacho dispatchByID(Integer id) throws ServicesException{
        return disprepo.findOne(id);
    }
    
    @Override
    public InputStream imageQRdispatchByID(Integer id) throws ServicesException, SQLException {
        return disprepo.imageQRdispatchByID(id).getQrcode().getBinaryStream();
    }

    @Override
    public List<Vehiculo> getAllVehiculosByIdProducto(int idProducto) throws ServicesException {
        return vehiclerepository.vehiculosByIdProducto(idProducto);
    }

    @Override
    public List<Cliente> getAllClientsByProductPrice(long precio) throws ServicesException {
        return clientrepository.clientByProdPrice(precio);
    }

    @Override
    public void addNewDispatch(MultipartHttpServletRequest request, int idpedido, String idvehiculo) throws ServicesException, IOException, SQLException {
        Iterator<String> itr = request.getFileNames();
        while (itr.hasNext()) {
            String uploadedFile = itr.next();
            MultipartFile file = request.getFile(uploadedFile);
            
            Pedido pedido = ordrepo.findOne(idpedido);     
            Vehiculo vehiculo = vehiclerepository.findOne(idvehiculo);
            Despacho despacho = new Despacho(pedido,vehiculo);
            
            despacho.setQrcode(new SerialBlob(StreamUtils.copyToByteArray(file.getInputStream())));
            disprepo.save(despacho);
        }
    }

    
    
}
