package com.badlogic.androidgames.framework

import android.util.Log
import kotlin.math.ceil
import kotlin.math.floor

class SpatialHashGrid(worldWidth: Float, worldHeight: Float, val cellSize: Float) {
    val cellsPerRow: Int = ceil(worldWidth / cellSize).toInt()
    val cellsPerCol: Int = ceil(worldHeight / cellSize).toInt()
    val numCells = cellsPerCol * cellsPerRow

    val dynamicCells: Array<MutableList<GameObject>> =
        Array(numCells, { ArrayList<GameObject>(10) })
    val staticCells: Array<MutableList<GameObject>> =
        Array(numCells, { ArrayList<GameObject>(10) })
    val cellIds = Array<Int>(10, { -1 })
    val foundObjects = ArrayList<GameObject>(10)

    fun insertStaticObject(obj: GameObject) {
        val cellIds = getCellIds(obj)
        var i = 0
        while (i <= 3 && cellIds[i] != -1) {
            staticCells[cellIds[i]].add(obj)
            Log.d("CELLS","$i ${obj.position.x} ${obj.position.y}")
            i++
        }
    }

    fun insertDynamicObject(obj: GameObject) {
        val cellIds = getCellIds(obj)
        var i = 0
        while (i <= 3 && cellIds[i] != -1) {
            dynamicCells[cellIds[i]].add(obj)
            i++
        }
    }

    fun removeObject(obj: GameObject) {
        val cellIds = getCellIds(obj)
        var i = 0
        while (i <= 3 && cellIds[i] != -1) {
            staticCells[cellIds[i]].remove(obj)
            dynamicCells[cellIds[i]].remove(obj)
            i++
        }
    }

    fun getPotentialColliders(obj: GameObject): List<GameObject> {
        foundObjects.clear()
        val cellIds = getCellIds(obj)

        var i = 0
        while (i <= 3 && cellIds[i] != -1) {

            dynamicCells[cellIds[i]].forEach { collider ->
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider)
                }
            }
            staticCells[cellIds[i]].forEach { collider ->
                if (!foundObjects.contains(collider)) {
                    foundObjects.add(collider)
                }
            }
            i++

        }
        return foundObjects
    }

    fun getCellIds(obj: GameObject): Array<Int> {
        cellIds[0] = -1
        cellIds[1] = -1
        cellIds[2] = -1
        cellIds[3] = -1
        val x1 = floor(obj.bounds.lowerLeft.x / cellSize).toInt()
        val y1 = floor(obj.bounds.lowerLeft.y / cellSize).toInt()
        val x2 = floor((obj.bounds.lowerLeft.x + obj.bounds.width) / cellSize).toInt()
        val y2 = floor((obj.bounds.lowerLeft.y + obj.bounds.height) / cellSize).toInt()
        if (x1 == x2 && y1 == y2) {
            if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[0] = x1 + y1 * cellsPerRow
        } else if (x1 == x2) {
            var i = 0
            if (x1 >= 0 && x1 < cellsPerRow) {
                if (y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x1 + y1 * cellsPerRow
                if (y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x1 + y2 * cellsPerRow
            }
        } else if(y1 == y2){
            var i = 0
            if (y1 >= 0 && y1 < cellsPerCol) {
                if (x1 >= 0 && x1 < cellsPerRow)
                    cellIds[i++] = x1 + y1 * cellsPerRow
                if (x2 >= 0 && x2 < cellsPerRow)
                    cellIds[i++] = x2 + y1 * cellsPerRow
            }
        } else {
            var i = 0
            val y1CellsPerRow = y1 * cellsPerRow
            val y2CellsPerRow = y2 * cellsPerRow
            if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[i++] = x1 + y1CellsPerRow
            if(x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                cellIds[i++] = x2 + y1CellsPerRow
            if(x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                cellIds[i++] = x2 + y2CellsPerRow
            if(x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                cellIds[i++] = x1 + y2CellsPerRow
        }
        return  cellIds
    }

}