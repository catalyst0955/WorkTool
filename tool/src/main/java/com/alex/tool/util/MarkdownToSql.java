package com.alex.tool.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MarkdownToSql {
    public static void main(String[] args) {
        File file = new File("D:\\FUCO\\KGI\\OpenAccount\\oabook\\DB\\數三");
        List<File> ed3 = Arrays.stream(file.listFiles()).filter(f -> f.getName().indexOf("ED3") > -1).collect(Collectors.toList());
        String tableName = "";


        for (File sf : ed3) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader br = new BufferedReader(new FileReader(sf.getPath()))){
                String s = null;
                ArrayList<String> keyList = new ArrayList<>();
                int line = 0;
                while((s=br.readLine())!=null){
                    if(line > 1 && s.equalsIgnoreCase("") ){
                        break;
                    }
                    if(s.indexOf("###")>-1){
                        tableName = s.substring(s.lastIndexOf("(") + 1, s.lastIndexOf(")"));
                        sb.append("Create Table ").append(tableName).append(" \n( \n");
                        line++;
                        continue;
                    }
                    List<String> strings = Arrays.asList(s.split("\\|")).stream().map(String::trim).collect(Collectors.toList());
                    if(strings.indexOf("PK")>-1 || strings.contains("---") || strings.size()<2){
                        line++;
                        continue;
                    }else{
                        //PK
                        if(strings.get(1).equalsIgnoreCase("v")){
                            keyList.add(strings.get(4));
                        }
                        sb.append("  ").append(strings.get(4)).append("   ").append(strings.get(5)).append("  ");
                        //NULL
                        if(!strings.get(2).equalsIgnoreCase("v")){
                            sb.append(" not null ");
                        }
                        if(strings.get(3).equalsIgnoreCase("v")){
                            sb.append(" identity ");
                        }
                        sb.append(",\n");
                    }
                }

                if(keyList.size()>0){
                    sb.append(" constraint ").append("PK_").append(tableName).append("\n").append("  primary key (");
                    keyList.forEach(name->sb.append(name).append(" ,"));
                    sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, ")");
                }

                System.out.println(sb.append("\n").append(")"));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
