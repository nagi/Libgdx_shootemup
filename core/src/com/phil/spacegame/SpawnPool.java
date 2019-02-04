package com.phil.spacegame;

import java.lang.reflect.Constructor;
import java.util.*;

public class SpawnPool {

    //HashMap containing pools assigned to a SpawnType
    private HashMap<SpawnType, ArrayList<SpawnObject>> pools
            = new HashMap<SpawnType, ArrayList<SpawnObject>>();
    //package name needed for generating new instance of a class
    private String nameOfPackage = "com.phil.spacegame.";

    public SpawnPool(){}

    public void addPool(SpawnType type, ArrayList<SpawnObject> pool) {
        this.pools.put(type, pool);
    }

    public SpawnObject getFromPool(SpawnType type) {
        //Get pool of the given spawntype
        ArrayList<SpawnObject> pool = pools.get(type);
        if (pool == null) {
            System.err.println("Pool of type " + type.name() +
                    " doesn't exist. Maybe forgot to create an" +
                    " ArrayList instance for that pool?");
            return null;
        }
        //try to get a free object to spawn
        SpawnObject found = null;
        for (int i = 0; i<pool.size(); ++i) {
            //find one that is not spawned yet
            if (!pool.get(i).isSpawned()) {
                found = pool.get(i);
                break;
                // else continue for-loop to find another spawn
            }
        }
        if (found != null) {
            found.setSpawned(true);
            return found;
        }

        //No free spawn found, so create a new instance of
        //that spawntype
        //System.out.println("Pool of " + type.name() +
        //        " contains no unspawned object. " +
        //        "Creating new Instance...");
        SpawnObject spawn = createSpawnObject(type);

        if (spawn == null) {
            System.err.println("Instance of Spawn of type "
                    + type.name() + " could not be created");
            return null;
        }

        //Mark this instance as spawned
        spawn.setSpawned(true);

        //And add it to the pool
        pools.get(type).add(spawn);

        //Debug
        //printPoolSize();

        return spawn;
    }

    public void returnToPool(SpawnObject object) {
        object.setSpawned(false);
        //Debug
        //printPoolSize();
    }

private SpawnObject createSpawnObject(SpawnType type) {

    //System.out.println(" - creating instance of " +
    //        "type.name(): " + type.name());

    //Prepare classname
    String className = nameOfPackage + type.name();
    try {
        //Dynamic creation of an instance of a class

        // not working with GWT (for html5 deploy)!
        // Java Reflection api not working with GWT
        // -> Class.getConstructor(), Class.forName(), ...

//        Class c = Class.forName(className);
//        //debug
//        //System.out.println(" - Class: " + c.getName());
//
//        Constructor ctor = c.getConstructor(String.class);
//        //create new instance (there has to be at least
//        //one arg, otherwise it doesn't work...)
//        Object object = ctor.newInstance("");
//        SpawnObject created = (SpawnObject) object;

        //Workaround to compile with GWT for Html5 deploy
        SpawnObject created = null;
        if (type.name() == "MissilePlayer")
            created = new MissilePlayer("");
        else if (type.name() == "MissileEnemy")
            created = new MissileEnemy("");
        else if (type.name() == "Enemy")
            created = new Enemy("");
        else if (type.name() == "Item")
            created = new Item("");
        else if (type.name() == "Obstacle")
            created = new Obstacle("");
        else if (type.name() == "Explosion")
            created = new Explosion("");
        else
            System.err.println("SpawnPool: " + type.name()
                    + " not able to spawn. Maybe forgot to add in createSpawnObject()?");

        return created;

    } catch(Exception e) {
        System.err.println(e);
        System.err.println("Type name: " + type.name());
        System.err.println("Class name: " + className);
    }
    return null;
}

    private void printPoolSize() {
        //Debug pool size
        Iterator it = pools.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            SpawnType type = (SpawnType) pair.getKey();
            ArrayList<SpawnObject> pool = (ArrayList<SpawnObject>) pair.getValue();
            int countNotSpawned = 0;
            int countSpawned = 0;
            for (SpawnObject spawn: pool) {
                if (!spawn.isSpawned())
                    countNotSpawned++;
                else
                    countSpawned++;
            }
            System.out.println("Pool: " + type.name() + " -> Size:  " + pool.size() +  ", Spawned: " + countSpawned);
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
