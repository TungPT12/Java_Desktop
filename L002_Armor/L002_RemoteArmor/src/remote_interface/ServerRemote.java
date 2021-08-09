package remote_interface;

/**
 *
 * @author Sword Lake
 */
import dto.ArmorDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
 
public interface ServerRemote extends Remote {
    public String tung(String id) throws RemoteException;
    public String creatArmor(ArmorDTO dto) throws RemoteException;
    public ArmorDTO findByArmorID(String id) throws RemoteException;
    public List<ArmorDTO> getAllArmor() throws RemoteException;
    public boolean removeArmor(String id) throws RemoteException;
    public String updateArmor(ArmorDTO dto) throws RemoteException;
    public String getTime() throws RemoteException;
    public String checkLogin(String username, String password) throws RemoteException;
}
