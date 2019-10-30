package com.alex.tool;

import com.alex.tool.util.sqlservertomk.service.DbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

}
