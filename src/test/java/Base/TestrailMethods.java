package Base;

import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestrailMethods {

    public TestrailMethods() {

    }

    private static String url = "https://domsample.testrail.io/";
    private static String userId = "testuser@test.com";
    private static String pwd = "TestUser1!";
    //public static String projectId = "1";

    public static void addTestrailResultForTestCase(String testRunId, String testCaseId, int status, String comment) throws IOException, APIException {

        APIClient client = new APIClient(url);
        client.setUser(userId);
        client.setPassword(pwd);
        Map data = new HashMap();
        data.put("status_id", status);
        data.put("comment", comment);
        JSONObject r = (JSONObject) client.sendPost("add_result_for_case/"+testRunId+"/"+testCaseId+"", data );

    }
}
