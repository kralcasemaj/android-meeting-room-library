package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.util.Log
import java.io.*

class FileUtils {
    companion object {
        fun readFile(absolutePath: String): String {
            FileInputStream(absolutePath).use { inputStream ->
                ByteArrayOutputStream().use { outputStream ->
                    val buf = ByteArray(1024)
                    var len: Int
                    try {
                        while (inputStream.read(buf).also { len = it } != -1) {
                            outputStream.write(buf, 0, len)
                        }
                    } catch (ex: IOException) {
                        Log.e(FileUtils::class.java.simpleName, "Error reading file", ex)
                    }
                    return outputStream.toString()
                }
            }
        }

        fun writeFile(data: String, absolutePath: String) {
            try {
                val file = File(absolutePath)
                if (!file.exists()) {
                    try {
                        if (!file.createNewFile()) {
                            Log.e(FileUtils::class.java.simpleName, "Error creating file")
                        }
                    } catch (ex: IOException) {
                        Log.e(FileUtils::class.java.simpleName, "Error creating file", ex)
                    }
                } else {
                    PrintWriter(file).close()
                }

                BufferedWriter(FileWriter(file, true)).use { bufferedWriter ->
                    bufferedWriter.append(data)
                }
            } catch (ex: Exception) {
                Log.e(FileUtils::class.java.simpleName, "Error writing file", ex)
            }
        }
    }
}