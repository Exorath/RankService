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

/**
 * Created by Toon on 4/9/2017.
 */
public class MongoService implements Service {
    private MongoCollection<Document> ranksCollection;
    private MongoCollection<Document> rankPlayersCollection;
    public MongoService(MongoClient client, String databaseName) {
        MongoDatabase db = client.getDatabase(databaseName);
        ranksCollection = db.getCollection("ranks");
        rankPlayersCollection = db.getCollection("rankplayers");
    }

    @Override
    public String[] getRanks() {
        return new String[0];
    }

    @Override
    public Rank getRank(String rankId) {
        return null;
    }

    @Override
    public Success updateRank(String rankId, Rank rank) {
        return null;
    }

    @Override
    public RankPlayer getPlayer(String uuid) {
        return null;
    }

    @Override
    public Success setPlayerRank(String uuid, RankPlayer rank) {
        return null;
    }

    @Override
    public boolean inheritsFromRank(String uuid, String rank) {
        return false;
    }
}
