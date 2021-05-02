package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Carriage;

import java.util.Random;

public class CarriageGenerator extends InputGenerator {
    private static String[] types = new String[]{"Soft seat", "Hard seat", "Hard sleeper", "Soft sleeper", "Dining", "Locomotive"};
    private Carriage[] carriages;
    private final int carriageNo = 20;
    private final Random random = new Random();

    public CarriageGenerator() {
        carriages = new Carriage[carriageNo];
        for (int i = 0; i < carriages.length; i++) {
            carriages[i] = genCar(i);
        }
        for (int i = 0; i < carriages.length - 1; i++) {
            if (i != carriages.length - 1) {
                carriages[i].nextCarriage = carriages[i + 1];
            }
        }
    }

    private Carriage genCar(int i) {
        String carId = "CAR-1" + String.format("%04d", i);
        int pNo;
        String type;
        boolean avail;
        if (i != 0) {
            type = types[random.nextInt(types.length - 1)];
            avail = random.nextBoolean();
            if (avail) {
                pNo = random.nextInt(150);
            } else {
                pNo = 0;
            }
        } else {
            type = types[types.length - 1];
            avail = true;
            pNo = 2;
        }
        return new Carriage(carId, pNo, type, avail);
    }

    @Override
    public String generateJson() {
        return new Gson().toJson(carriages[0]);
    }

    @Override
    public Object generateBean() {
        return carriages[0];
    }

    @Override
    public String toString() {
        return "Carriage";
    }
}
