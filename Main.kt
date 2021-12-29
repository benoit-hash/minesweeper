package minesweeper

import java.lang.Math.random
import kotlin.math.min

var tableau = mutableListOf(
    mutableListOf<Int>())
var init: Boolean = true
var mines: Int = 0

fun displayField(cmd: String = "") {
    println(" |123456789|")
    println("-|---------|")
    for ((index, row) in tableau.withIndex()) {
        print("${index + 1}|")
        for (cell in row) {
            when (cell) {
                in 0..8 -> {
                    print('.')
                }
                9 -> {
                    if (cmd == "Ko") {
                        print('X')
                    } else {
                        print('.')
                    }
                }
                19 -> {
                    if (cmd == "Ko") {
                        print('X')
                    } else {
                        print('*')
                    }
                }
                in 10..18 -> {
                    print('*')
                }
                20 -> {
                    print('/')
                }
                else -> {
                    print(cell - 20)
                }
            }
        }
        print("|")
        println()
    }
    println("-|---------|")
}

fun addHint(x: Int, y: Int) {
    for (i in -1..1) {
        for (j in -1..1) {
            try {
                when (tableau[x + i][y + j]) {
                    in 0..9 -> {
                        tableau[x + i][y + j] = min(tableau[x + i][y + j] + 1, 9)
                    }
                    in 10..19 -> {
                        tableau[x + i][y + j] = min(tableau[x + i][y + j] + 1, 19)
                    }
                }
            } catch (e: Exception) {}
        }
    }
}

fun initField(rows: Int, cols: Int, mine: Int, row: Int, col: Int) {
    var mines = mine
    while (mines > 0) {
        val x = (random() * rows).toInt()
        val y = (random() * cols).toInt()
        if (tableau[x][y] < 9 && !(x==row && y ==col)) {
            tableau[x][y] = 9
            addHint(x, y)
            mines --
        }
    }
}

fun checkWin(): Boolean {
    for (row in tableau) {
        for (cell in row) {
            if (cell in 9..10) {
                return false
            }
        }
    }
    return true
}

fun discovery(a: Int, b: Int) {
    if (tableau[a][b] == 20) {
        for (i in -1..1) {
            for (j in -1..1) {
                try {
                    if (tableau[a + i][b + j] < 9) {
                        tableau[a + i][b + j] += 20
                        discovery(a + i, b + j)
                    } else if (tableau[a + i][b + j] in 10..18) {
                        tableau[a + i][b + j] += 10
                        discovery(a + i, b + j)
                    }
                } catch (e: Exception) {}
            }
        }
    }
}

fun checkInput(a: Int, b: Int, c: String){
    if (c == "free") {
        if (init) {
            initField(9, 9, mines, a, b)
            init = false
        }
        when (tableau[a][b]) {
            9 -> {
                displayField("Ko")
                println("You stepped on a mine and failed!")
            }
            0 -> {
                tableau[a][b] = 20
                discovery(a, b)
            }
            else -> {
            tableau[a][b] += 20
            }
        }
    }
    if (c == "mine") {
        when (tableau[a][b]) {
            in 0..9 -> {
                tableau[a][b] += 10
            }
            in 10..19 -> {
                tableau[a][b] -= 10
            }
            else -> {
                println("There is a number here!")
                userInput()
            }
        }
    }
    displayField()
    userInput()
}

fun userInput() {
    if (!init && checkWin()) {
        println("Congratulations! You found all the mines!")
    }
    else {
        if (init) {
            println("Set/unset mines marks or claim a cell as free:")
            val userInput = readLine()!!.split(" ").toTypedArray()
            checkInput(userInput[1].toInt() - 1, userInput[0].toInt() - 1, userInput[2])
        } else {
            try {
                println("Set/unset mines marks or claim a cell as free:")
                val userInput = readLine()!!.split(" ").toTypedArray()
                checkInput(userInput[1].toInt() - 1, userInput[0].toInt() - 1, userInput[2])
            } catch (e: Exception) {
                println(e.message)
                userInput()
            }
        }
    }
}

fun main() {
    val row = MutableList(9){0}
    tableau.removeAt(0)
    for (j in 1..9) {
        tableau.add(row.toMutableList())
    }
    println("How many mines do you want on the field?")
    mines = readLine()!!.toInt()
    displayField()
    userInput()
}
