package VkUtils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;

public class VkConnector {
    private final int urfuGroup = 215509427;// 22941070;
    private final VkApiClient vk;
    private final UserActor actor;

    private final int sleepTime = 300;

    public VkConnector(int id, String accessToken){

        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(id, accessToken);
    }

    public HashMap<String, String> getUserInfo(String fullName) {
        /*try {
            System.out.println("Trying to connect! " +fullName);
            var user = vk
                    .users()
                    .search(actor)
                    .groupId(urfuGroup)
                    .q(fullName)
                    .fields(Fields.SEX, Fields.HOME_TOWN, Fields.BDATE, Fields.CONTACTS)
                    .count(1)
                    .execute()
                    .getItems()
                    .get(0);
            var userInfo = new HashMap<String, String>();
            userInfo.put("Sex", user.getSex().name());
            userInfo.put("Hometown", user.getHomeTown());
            userInfo.put("Phone", user.getMobilePhone());
            userInfo.put("BDate", user.getBdate());
            System.out.println("LET'S GOOO!!!");
            Thread.sleep(sleepTime);
            return userInfo;
        }
        catch (Exception ignore){
            try {

                System.out.println("IGNORED!" + ignore.toString());
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e){
                throw new RuntimeException();
            }
        }*/
        var nullHashMap = new HashMap<String, String>();
        nullHashMap.put("Sex", "unknown");
        nullHashMap.put("Hometown", "unknown");
        nullHashMap.put("Phone", "unknown");
        nullHashMap.put("BDate", "unknown");
        return nullHashMap;
    }
}
