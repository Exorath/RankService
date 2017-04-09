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

package com.exorath.service.rank;

import com.exorath.service.commons.portProvider.PortProvider;
import com.exorath.service.rank.res.Rank;
import com.exorath.service.rank.res.RankPlayer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import spark.Route;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;

/**
 * Created by Toon on 4/9/2017.
 */
public class Transport {
    private static final Gson GSON = new Gson();

    public static void setup(Service service, PortProvider portProvider) {
        port(portProvider.getPort());

        get("/ranks", getGetRanksRoute(service), GSON::toJson);
        get("/ranks/:rankId", getGetRankRoute(service), GSON::toJson);
        put("/ranks/:rankId", getPutRankRoute(service), GSON::toJson);


        get("/players/:uuid", getGetPlayerRankRoute(service), GSON::toJson);
        put("/players/:uuid", getPutPlayerRankRoute(service), GSON::toJson);
        get("/players/:uuid/inherits/:rankId", getInheritsFromRoute(service), GSON::toJson);
    }




    public static Route getGetRankRoute(Service service) {
        return (req, res) -> service.getRank(req.params("rankId"));
    }

    private static Route getPutRankRoute(Service service) {
        return (req, res) -> service.updateRank(req.params("rankId"), GSON.fromJson(req.body(), Rank.class));
    }

    public static Route getGetRanksRoute(Service service) {
        return (req, res) -> {
            JsonObject resObj = new JsonObject();
            JsonArray ranksArray = new JsonArray();
            for (String rank : service.getRanks())
                ranksArray.add(rank);
            resObj.add("ranks", ranksArray);
            return GSON.toJson(resObj);
        };
    }


    private static Route getGetPlayerRankRoute(Service service) {
        return (req, res) -> service.getPlayer(req.params("uuid"));
    }

    private static Route getPutPlayerRankRoute(Service service) {
        return (req, res) -> service.setPlayerRank(req.params("uuid"), GSON.fromJson(req.body(), RankPlayer.class));
    }

    public static Route getInheritsFromRoute(Service service) {
        return (req, res) -> {
            JsonObject resObj = new JsonObject();
            resObj.add("inherits", new JsonPrimitive(service.inheritsFromRank(req.params("uuid"), req.params("rankId"))));
            return GSON.toJson(resObj);
        };
    }
}
