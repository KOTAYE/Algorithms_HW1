package com.example.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.example.data.StudentStorage;

public class Benchmark {
    private final StudentStorage db;
    private final List<String> phones;
    private final List<String> groups;
    private final List<String> surnames;

    private final int A = 2;
    private final int B = 10;
    private final int C = 30;

    public Benchmark(StudentStorage db) {
        this.db = db;
        this.phones = new ArrayList<>(db.getAllPhones());
        this.groups = new ArrayList<>(db.getAllGroups());
        this.surnames = new ArrayList<>(db.getAllSurnames());
    }

    public void runFor10Seconds() {
        long operations = runFor10SecondsWithCount();
        System.out.println("Executed operations in 10s: " + operations);
    }

    public long runFor10SecondsWithCount() {
        long endTime = System.currentTimeMillis() + 10_000;
        long operations = 0;
        Random random = ThreadLocalRandom.current();

        final int totalWeight = A + B + C;

        while (System.currentTimeMillis() < endTime) {
            int op = random.nextInt(totalWeight);

            if (op < A) {
                String phone = getRandom(phones);
                String newGroup = getRandom(groups);
                if (phone != null && newGroup != null)
                    db.changeGroup(phone, newGroup);

            } else if (op < A + B) {
                db.getStudentsByGroup(getRandom(groups));

            } else {
                db.getGroupsBySurname(getRandom(surnames));
            }

            operations++;
        }

        return operations;
    }

    private <T> T getRandom(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}