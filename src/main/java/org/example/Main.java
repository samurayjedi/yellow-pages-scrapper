package org.example;

import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            List<Map<String, String>> data = CompanyContactInfoData.builder()
                .search("Restaurants", "Colorado")
                .extractNames()
                .extractAddresses()
                .extractPhones()
                .finish();
            System.out.println(data.toString());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}