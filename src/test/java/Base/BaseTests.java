package Base;

import binding_TestRail.*;
import com.google.common.io.Files;
import com.qaprosoft.carina.core.foundation.IAbstractTest;
import com.zebrunner.carina.utils.R;
import net.minidev.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;


public class BaseTests implements IAbstractTest {

    @AfterMethod
    public void recordFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            var camera = (TakesScreenshot) getDriver();
            File screenshot = camera.getScreenshotAs(OutputType.FILE);
            try {
                Files.move(screenshot, new File("resources/screenshots/" + result.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @BeforeClass
    public void createTestRailRun(ITestContext context) throws IOException, AccessException, APIException {
        APIClient client = new APIClient(R.TESTDATA.get("testRailURL"));
        client.setUser(R.TESTDATA.get("testRailUsername"));
        client.setPassword(R.TESTDATA.get("testRailPassword"));
        Map<String,Object> data = new HashMap<>();
        data.put("include_all",false);//if we set it true, it will add all test cases to this run
        //instantiate an array with caseId's annotations
        ITestNGMethod[] methodArray = context.getAllTestMethods();
        int[] testCaseIds = new int[methodArray.length];
        for (int i = 0; i < methodArray.length; i++) {
            try {
                Method method = methodArray[i].getRealClass().getMethod(methodArray[i].getMethodName());
                testCaseIds[i] = Integer.parseInt(method.getAnnotation(TestRailCaseId.class).id());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        data.put("case_ids", testCaseIds);
        //the name of the created testRun is generated with the current date and time, e.g:Test Run 28-02-2023 T16:18:43
        data.put("name", "Test Run " + DateFormatting.getCurrentTime());
        JSONObject c = null;
        c = (JSONObject) client.sendPost("add_run/" + TestRailManager.PROJECT_ID, data);
        Long suite_id = Long.parseLong(c.get("id").toString());
        context.setAttribute("suiteId",suite_id);
    }

    public void skipTestException(String message){
        throw new SkipException(message);
    }
}