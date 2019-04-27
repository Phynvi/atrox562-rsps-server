package com.rs.game.area;

import com.rs.game.WorldTile;
import com.rs.game.area.shapes.Polygon;

public final class GESafeZone extends Area {

    @Override
    public Area update() {
        return this;
    }

    @Override
    public String name() {
        return "GESafeZone";
    }

    @Override
    public Shape[] shapes() {
        return new Shape[] { new Polygon(
                new WorldTile[] {
                        new WorldTile(3160, 3472, 0),
                        new WorldTile(3169, 3472, 0),
                        new WorldTile(3176, 3476, 0),
                        new WorldTile(3178, 3478, 0),
                        new WorldTile(3182, 3485, 0),
                        new WorldTile(3182, 3494, 0),
                        new WorldTile(3178, 3501, 0),
                        new WorldTile(3176, 3503, 0),
                        new WorldTile(3169, 3507, 0),
                        new WorldTile(3160, 3507, 0),
                        new WorldTile(3153, 3503, 0),
                        new WorldTile(3151, 3501, 0),
                        new WorldTile(3147, 3494, 0),
                        new WorldTile(3147, 3485, 0)
        })}
                ;
    }

    @Override
    public boolean member() {
        return false;
    }

    @Override
    public Environment environment() {
        return Environment.NORMAL;
    }

}
