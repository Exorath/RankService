/*
 *    Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.service.rank.service;

import com.exorath.service.rank.Service;
import com.exorath.service.rank.res.Rank;
import com.exorath.service.rank.res.RankPlayer;
import com.exorath.service.rank.res.Success;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Meta;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateOpsImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Toon on 4/9/2017.
 */
public class MongoService implements Service {
    final Morphia morphia = new Morphia();
    private MongoCollection<Document> ranksCollection;
    private Datastore datastore;
    private MongoCollection<Document> rankPlayersCollection;

    public MongoService(MongoClient client, String databaseName) {
        morphia.mapPackage("com.exorath.service.rank.res");
        datastore = morphia.createDatastore(client, databaseName);
        datastore.ensureIndexes();


        MongoDatabase db = client.getDatabase(databaseName);
        ranksCollection = db.getCollection("ranks");
        rankPlayersCollection = db.getCollection("rankplayers");

    }

    @Override
    public String[] getRanks() {
        ArrayList<String> ranks = new ArrayList<>();
        datastore.createQuery(Rank.class).project("_id", true)
                .forEach(rank -> ranks.add(rank.getId()));
        return ranks.toArray(new String[ranks.size()]);
    }

    @Override
    public Rank getRank(String rankId) {
        return datastore.createQuery(Rank.class).field("_id").equal(rankId).get();
    }

    @Override
    public Success updateRank(String rankId, Rank rank) {
        rank.setId(rankId);
        Key<Rank> key = datastore.save(rank);
        if (key == null || key.getId() == null)
            return new Success(-1, "no id key was returned");
        if (!key.getId().equals(rankId))
            return new Success(-1, "Wrong id was returned");
        return new Success(true);
    }

    @Override
    public RankPlayer getPlayer(String uuid) {
        RankPlayer rankPlayer = datastore.createQuery(RankPlayer.class).field("_id").equal(uuid).get();
        return rankPlayer == null ? new RankPlayer(uuid, null) : rankPlayer;
    }

    @Override
    public Success setPlayerRank(String uuid, RankPlayer rankPlayer) {
        rankPlayer.setId(uuid);
        Key<RankPlayer> key = datastore.save(rankPlayer);
        if (key == null || key.getId() == null)
            return new Success(-1, "no id key was returned");
        if (!key.getId().equals(uuid))
            return new Success(-1, "Wrong id was returned");
        return new Success(true);
    }

    //the inheritance set acts as a circuit breaker if there's a loop
    @Override
    public boolean inheritsFromRank(String uuid, String rank) {
        RankPlayer rankPlayer = getPlayer(uuid);
        if (rankPlayer == null)
            return false;
        if (rankPlayer.getRank() == null)
            return false;
        String inheritedRank = rankPlayer.getRank();
        Set<String> inherited = new HashSet<>();
        do {
            if (inherited.contains(inheritedRank))
                return false;
            if (rankPlayer.getRank().equals(rank))
                return true;
            inherited.add(inheritedRank);
            inheritedRank = getInheritedRank(inheritedRank);
        } while (inheritedRank != null);
        return false;
    }

    private String getInheritedRank(String rankId) {
        Rank rank = datastore.createQuery(Rank.class).field("_id").equal(rankId).project("inheritance", true).get();
        if (rank == null)
            return null;
        return rank.getInheritance();
    }
}
