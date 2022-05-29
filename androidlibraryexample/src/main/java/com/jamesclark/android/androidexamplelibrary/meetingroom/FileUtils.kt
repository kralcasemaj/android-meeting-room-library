package com.jamesclark.android.androidexamplelibrary.meetingroom

import android.util.Log
import java.io.*

class FileUtils {
    companion object {
        fun saveToCacheFile(data: String, fileName: String): String? {
            try {
                val cacheFile = File(fileName)
                if (!cacheFile.exists()) {
                    try {
                        if (!cacheFile.createNewFile()) {
                            Log.e(FileUtils::class.java.simpleName, "Error creating cache file")
                            return null
                        }
                    } catch (ex: IOException) {
                        Log.e(FileUtils::class.java.simpleName, "Error creating cache file", ex)
                        return null
                    }
                } else {
                    PrintWriter(cacheFile).close()
                }

                BufferedWriter(FileWriter(cacheFile, true)).use {
                    it.append(data)
                }

                return cacheFile.absolutePath
            } catch (ex: Exception) {
                Log.e(FileUtils::class.java.simpleName, "Error saving data", ex)
            }

            return null
        }

        fun readJsonFile(path: String): String {
            val inputStream = FileInputStream(path)
            val outputStream = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            try {
                while (inputStream.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len)
                }
                outputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                Log.e(
                    FileUtils::class.java.simpleName,
                    "Error reading cache file",
                    e
                )
            }
            return outputStream.toString()
        }
    }
}