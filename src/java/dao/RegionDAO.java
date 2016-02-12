/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.EstadoRepublica;
import dto.Municipio;
import dto.Region;
import java.util.List;

/**
 *
 * @author brionvega
 */
public interface RegionDAO {
  public Region insertar(Region region);
  public List<Integer> buscarMunicipiosRegion(int idDespacho);
  public List<Municipio> buscarMunicipiosZona(int idZona);
  public List<EstadoRepublica> buscarEstadosZona(int idZona);
}
