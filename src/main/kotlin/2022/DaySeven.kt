@file:Suppress("PackageName")

package `2022`

import Project

class DaySeven(file: String) : Project() {
    private val io = getLines(file)
    private val root = Dir("/", null)
    private var pwd = root
    private val allDirs: ArrayList<Dir> = ArrayList()

    init {
        allDirs.add(pwd)
        var mode = IOMode.INPUT

        io.subList(1, io.size).forEach {
            if (it.startsWith("$")) {
                mode = IOMode.INPUT
            }

            val tokens = it.split(" ")

            if (mode == IOMode.INPUT) {
                val command = tokens[1]
                if (command == "ls") {
                    mode = IOMode.OUTPUT
                } else if (command == "cd") {
                    val subdir = tokens[2]

                    pwd = if (subdir == "..") {
                        pwd.parent!!
                    } else {
                        val dir = Dir(subdir, pwd)
                        allDirs.add(dir)
                        pwd.subdirs.add(dir)
                        dir
                    }
                }
            } else {
                if (tokens[0] != "dir") {
                    pwd.files.add(File(tokens[1], tokens[0].toInt()))
                }
            }
        }
    }

    enum class IOMode {
        INPUT,
        OUTPUT
    }

    class Dir(val name: String, val parent: Dir?) {
        val subdirs: ArrayList<Dir> = ArrayList()
        val files: ArrayList<File> = ArrayList()
        var size: Int? = null

        fun size(): Int {
            if (size == null) {
                return files.sumOf { it.size } + subdirs.sumOf { it.size() }
            }
            return size!!
        }
    }

    class File(val name: String, val size: Int)

    override fun part1(): Any {
        return allDirs.filter { it.size() < 100000 }.sumOf { it.size() }
    }

    override fun part2(): Any {
        return allDirs.filter { (70_000_000 - root.size() + it.size()) > 30_000_000 }.minOf { it.size() }
    }

}