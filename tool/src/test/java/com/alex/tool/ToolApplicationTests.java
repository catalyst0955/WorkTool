package com.alex.tool;

import com.alex.tool.pojo.PojoAfter;
import com.alex.tool.pojo.PojoOrig;
import com.alex.tool.util.GsonUtil;
import com.alex.tool.util.sqlservertomk.service.DbService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToolApplicationTests {

    @Autowired
    private DbService service;

    @Test
    public void contextLoads() {
        String tableName = "ED3_CaseData";
        String fileName = "D:/" + tableName + ".md";
        String ed3_caseData = service.generateMDString("ED3_CaseData");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            writer.write(ed3_caseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void gsonTest(){
        PojoAfter pojoAfter = new PojoAfter();
        PojoOrig pojoOrig = new PojoOrig();
//        Gson gson = new Gson();
        PojoAfter pojoAfter1 = GsonUtil.merge(pojoAfter, pojoOrig, PojoAfter.class, false);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        System.out.println(gson.toJson(pojoAfter1));
//        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
//        String s1 = gson.toJson(pojoOrig);
//        Map<String, Object> o = gson.fromJson(s1, mapType);
//        System.out.println(o);
//        new ObjectMapper().readValue(s, HashMap.class);
//        GsonUtil.mergeForTest(pojoAfter, pojoOrig, PojoAfter.class, false);

    }

}
