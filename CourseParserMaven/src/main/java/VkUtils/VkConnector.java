package VkUtils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;

public class VkConnector {
    private final int urfuGroup = 22941070;
    private final VkApiClient vk;
    private final UserActor actor;

    public VkConnector(int id, String accessToken){

        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(id, accessToken);
    }

    public HashMap<String, String> getUserInfo(String fullName){
        try {
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
            Thread.sleep(6000);
            return userInfo;
        }
        catch (Exception ignore){}
        var nullHashMap = new HashMap<String, String>();
        nullHashMap.put("Sex", "unknown");
        nullHashMap.put("Hometown", "unknown");
        nullHashMap.put("Phone", "unknown");
        nullHashMap.put("BDate", "01.01.1970");
        return nullHashMap;
    }
}
