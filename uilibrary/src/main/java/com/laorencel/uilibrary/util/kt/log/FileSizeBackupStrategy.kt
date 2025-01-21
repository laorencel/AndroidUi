package com.laorencel.uilibrary.util.kt.log

import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy2

class FileSizeBackupStrategy(maxSize: Long, maxBackupIndex: Int) :
    FileSizeBackupStrategy2(maxSize, maxBackupIndex) {
    override fun getBackupFileName(fileName: String?, backupIndex: Int): String {
//        return super.getBackupFileName(fileName, backupIndex)

        //fileName + ".bak." + backupIndex
//        return super.getBackupFileName(fileName, backupIndex);
        val builder = StringBuilder()
        val name = if (fileName!!.endsWith(".txt")) {
            fileName.replace(".txt", "")
        } else {
            fileName
        }
        builder.append(name).append(".bak.").append(backupIndex).append(".txt")
        return builder.toString()
    }
}