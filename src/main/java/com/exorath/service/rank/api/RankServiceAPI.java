package com.exorath.service.rank.api;

import com.exorath.service.rank.Service;
import com.exorath.service.rank.res.Rank;
import com.exorath.service.rank.res.RankPlayer;
import com.exorath.service.rank.res.Success;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by toonsev on 4/10/2017.
 */
public class RankServiceAPI implements Service {
    private static final Gson GSON = new Gson();
    private String address;

    public RankServiceAPI(String address) {
        this.address = address;
    }

    @Override
    public String[] getRanks() {
        try {
            JsonObject res = GSON.fromJson(Unirest.get(url("/ranks")).asString().getBody(), JsonObject.class);
            JsonArray jsonRanks = res.getAsJsonArray("ranks");
            if (jsonRanks == null)
                return null;
            String[] ranks = new String[jsonRanks.size()];
            for (int i = 0; i < jsonRanks.size(); i++)
                ranks[i] = jsonRanks.get(i).getAsString();
            return ranks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Rank getRank(String rankId) {
        try {
          return GSON.fromJson(Unirest.get(url("/ranks/{rankId}"))
                   .routeParam("rankId", rankId).asString().getBody(), Rank.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Success updateRank(String rankId, Rank rank) {
        rank.setId(null);
        try {
            return GSON.fromJson(Unirest.put(url("/ranks/{rankId}"))
                    .routeParam("rankId", rankId)
                    .body(GSON.toJson(rank))
                    .asString().getBody(), Success.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RankPlayer getPlayer(String uuid) {
        try {
            return GSON.fromJson(Unirest.get(url("/players/{playerId}"))
                    .routeParam("playerId", uuid).asString().getBody(), RankPlayer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Success setPlayerRank(String uuid, RankPlayer rankPlayer) {
        rankPlayer.setId(null);
        try {
            return GSON.fromJson(Unirest.put(url("/players/{playerId}"))
                    .routeParam("playerId", uuid)
                    .body(GSON.toJson(rankPlayer))
                    .asString().getBody(), Success.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean inheritsFromRank(String uuid, String rankId) {
        try {
            return GSON.fromJson(Unirest.get(url("/players/{playerId}/inherits/{rankId}"))
                    .routeParam("playerId", uuid)
                    .routeParam("rankId", rankId)
                    .asString().getBody(), JsonObject.class).get("inherits").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private String url(String endpoint) {
        return address + endpoint;
    }
}
