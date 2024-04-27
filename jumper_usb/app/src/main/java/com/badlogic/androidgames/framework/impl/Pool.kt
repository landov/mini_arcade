package com.badlogic.androidgames.framework.impl

class Pool<T> (
    val factory : PoolObjectFactory<T>,
    val maxSize : Int
){
    val freeObjects : MutableList<T> = mutableListOf()

    fun newObject() : T {
        if(freeObjects.isEmpty()) return  factory.createObject()
        else return freeObjects.removeLast()
    }

    fun free(obj : T){
        if(freeObjects.size < maxSize) freeObjects.add(obj)
    }

    interface PoolObjectFactory<T> {
        fun createObject() : T
    }
}