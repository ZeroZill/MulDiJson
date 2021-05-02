package org.zerozill.muldijson.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@JsonObject
public class Clients {

    @JsonField
    private Set<String> ips;

    @JsonField
    private Map<String, Boolean> online;

    @JsonField
    private List<String> record;

    public Clients() {

    }

    public Clients(Set<String> ips, Map<String, Boolean> online, List<String> record) {
        this.ips = ips;
        this.online = online;
        this.record = record;
    }

    public Set<String> getIps() {
        return ips;
    }

    public void setIps(Set<String> ips) {
        this.ips = ips;
    }

    public Map<String, Boolean> getOnline() {
        return online;
    }

    public void setOnline(Map<String, Boolean> online) {
        this.online = online;
    }

    public List<String> getRecord() {
        return record;
    }

    public void setRecord(List<String> record) {
        this.record = record;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clients clients = (Clients) o;

        if ((ips == null && clients.ips != null) || (ips != null && clients.ips == null) || (Objects.requireNonNull(ips).size() != Objects.requireNonNull(clients.ips).size())) {
            return false;
        }

        if ((online == null && clients.online != null) || (online != null && clients.online == null) || (Objects.requireNonNull(online).size() != Objects.requireNonNull(clients.online).size())) {
            return false;
        }

        if ((record == null && clients.record != null) || (record != null && clients.record == null) || (Objects.requireNonNull(record).size() != Objects.requireNonNull(clients.record).size())) {
            return false;
        }

        boolean ipsEqual = ips.containsAll(clients.ips);


        for (Map.Entry<String, Boolean> kv : online.entrySet()) {
            if (!clients.online.containsKey(kv.getKey())) {
                return false;
            }

            if (clients.online.get(kv.getKey()) != online.get(kv.getKey())) {
                return false;
            }
        }

        boolean recordEqual = record.equals(clients.record);

        return ipsEqual && recordEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ips, online, record);
    }

    @Override
    public String toString() {
        return "Clients{" +
                "ips=" + ips +
                ", online=" + online +
                ", record=" + record +
                '}';
    }
}
