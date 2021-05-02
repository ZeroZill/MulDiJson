package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Clients;

import java.util.*;

public class ClientsGenerator extends InputGenerator {

    private Clients clients;
    private Random random;

    public ClientsGenerator() {
        random = new Random();

        Set<String> ips = new HashSet<>();
        int ipNum = random.nextInt(20);
        for (int i = 0; i < ipNum; i++) {
            ips.add(genIp());
        }

        Map<String, Boolean> online = new HashMap<>();
        for (String ip : ips) {
            online.put(ip, random.nextBoolean());
        }

        List<String> record = new ArrayList<>();
        String[] onlineKeys = online.keySet().toArray(new String[0]);
        int recordNum = random.nextInt(onlineKeys.length);
        for (int i = 0; i < recordNum; i++) {
            int ipNo = random.nextInt(onlineKeys.length);
            String ip = onlineKeys[ipNo];
            record.add(ip + (online.get(ip) ? " get online." : " get off line."));
        }
        clients = new Clients(ips, online, record);
    }

    private String genIp(){
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ip.append(random.nextInt(256));
            if (i != 3) {
                ip.append(".");
            }
        }
        return ip.toString();
    }

    @Override
    public String generateJson() {
        return new Gson().toJson(clients);
    }

    @Override
    public Object generateBean() {
        return clients;
    }
}
